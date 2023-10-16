package com.quiz.service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.quiz.config.MyServletContextAwareBean;
import com.quiz.entity.CaptchaResponse;
import com.quiz.response.GlobalResponse;
import com.quiz.service.interfaces.CaptchaService;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;

@Service
public class CaptchaServiceImpl implements CaptchaService {
	
	@Value("${spring.secret}")
	private String CAPTCHASECRET;
	
	@Value("${spring.captchaExpireTime}")
	private long CAPTCHA_EXPIRATION_TIME;

//	private static final String CAPTCHASECRET = "cdac@123";
//	private static final long CAPTCHA_EXPIRATION_TIME = 60 * 60 * 1000; // 60 minutes
	
	private Timer timer = new Timer();

	@Autowired
	private MyServletContextAwareBean servletContextAwareBean;

	// Creating Captcha object
	private Captcha createCaptcha(Integer width, Integer height) {
		return new Captcha.Builder(width, height).addBackground(new GradiatedBackgroundProducer())
				.addText(new DefaultTextProducer(), new DefaultWordRenderer()).addNoise(new CurvedLineNoiseProducer())
				.build();
	}

	// Converting captcha object to binary string
	private String encodeCaptcha(Captcha captcha) {
		
		String image = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "jpg", bos);
			byte[] byteArray = Base64.getEncoder().encode(bos.toByteArray());
			image = new String(byteArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	// Generating random string
	private String generateUniqueRandomString() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	@Override
	// Method to generate captcha
	public CaptchaResponse generateCaptch(String IpAddress) {
		
		String randomString = generateUniqueRandomString();

		Captcha captcha = createCaptcha(240, 70);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String captchaHash = encoder.encode(captcha.getAnswer() + CAPTCHASECRET);
		
		IpAddress = IpAddress + randomString;
		long expirationTime = System.currentTimeMillis() + CAPTCHA_EXPIRATION_TIME;

		scheduleExpirationTask(IpAddress, expirationTime);
		servletContextAwareBean.setAttribute(IpAddress, captchaHash);

		// printing Ip Address ,captcha and token to get values while using postman
		System.out.println("IpAddress  : " + IpAddress + "\n" + "captchaHash :" + captcha.getAnswer() + "\n"
				+ "Random String :" + randomString);
		return new CaptchaResponse(captchaHash, encodeCaptcha(captcha), randomString);
	}

	@Override
	// Method to validate captcha
	public ResponseEntity<GlobalResponse> validateCaptchaAPI(String IpAddress, CaptchaResponse validateCaptcha) {
		Map<String, String> errorMessages = new HashMap<>();

		IpAddress = IpAddress + validateCaptcha.getRandomString();

		if (servletContextAwareBean.getAttribute(IpAddress) == null) {
			errorMessages.put("Invalid Request", "Generate Captcha");// captcha expired generate captcha

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new GlobalResponse("Action faild due to following reasons", errorMessages, false));

		}
		else if (servletContextAwareBean.getAttribute(IpAddress).equals(validateCaptcha.getCaptchaHash())) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(validateCaptcha.getCaptchaImage().trim() + CAPTCHASECRET,
					validateCaptcha.getCaptchaHash())) {
				System.out.println("captcha validated");
				servletContextAwareBean.removeAttribute(IpAddress);
				return ResponseEntity.ok(new GlobalResponse(null, "Valid captcha", true));
			} else {
				errorMessages.put("Invalid Request", "Invalid captcha");
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new GlobalResponse("Action faild due to following reasons", errorMessages, false));
			}
		} else {
			errorMessages.put("Invalid Request", "Invalid Token or Data Missmatch");
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new GlobalResponse("Action faild due to following reasons", errorMessages, false));
		}
	}


	private void scheduleExpirationTask(String IpAddress, long expirationTime) {
		TimerTask expirationTask = new TimerTask() {
			@Override
			public void run() {
				servletContextAwareBean.removeAttribute(IpAddress);
			}
		};
		timer.schedule(expirationTask, new Date(expirationTime));
	}

}
