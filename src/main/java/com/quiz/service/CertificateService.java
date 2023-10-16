package com.quiz.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.Certificate;
import com.quiz.helper.CertificateBean;
import com.quiz.helper.QRCodeGenerator;
import com.quiz.repository.CertificateRepository;
import com.quiz.response.GlobalResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class CertificateService {

	@Autowired
	CertificateRepository certificateRepository;

	@Value("${extern.resoures.path}")
	private String pathOfC;

	@Value("${extern.resourse.ipaddress}")
	private String IpAddress;

	@Value("${extern.resources.Dir}")
	private String path;

	public static List<Certificate> generateUserList() {
		return Arrays.asList(new Certificate());
	}

	// method to generate certificate
	public String generateCertificate(String quizName,String certFileName, String userName, Integer percentage) {

		String certStatus = "notIssued", qrfile = "";

		DateTimeFormatter dft = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String issuedate = LocalDateTime.now().format(dft);
		int certno = 1;

		// name of certificate
		String certname = quizName + "_" + String.format("%06d", certno) + "_" + userName.replaceAll(" ", "") + ".pdf";

		// name of QR
		String QRName = quizName + "_" + String.format("%06d", certno) + "_" + userName.replaceAll(" ", "") + ".png";

		// Website URL- where we want to redirect user after scanning QR code
		String qrtext = "Enter the URL of website";

		// location to save the certificate
		String certPath = pathOfC + certname;

		// location to save QR
		qrfile = pathOfC + QRName;

		CertificateBean bean = new CertificateBean();
		ArrayList<?> dataList = bean.getDataBeanList(quizName, userName, null, null, issuedate, null, percentage);
		Map<String, Object> parameters = new HashMap<>();

		try {

			// get this name from database , pass in quiz table
			// String jasperFile = "Quiz_game.jasper";
			File file = new ClassPathResource("/static/reports").getFile();

			// location of Jasper file and certificate
			String certImgPath = file + File.separator;

			// location of Jasper file and certificate
			String certImgPath1 = file + File.separator + certFileName;
			
			// location of QR logo
			String qrlogo = file + File.separator + "ssoqrlogo.png";

			// To create QR file directory
			File QRoutputFile = new File(qrfile);
			if (!QRoutputFile.getParentFile().exists()) {
				QRoutputFile.getParentFile().mkdirs();
			}

			// To create certPath directory
			File CertOutputFile = new File(certPath);
			if (!CertOutputFile.getParentFile().exists()) {
				CertOutputFile.getParentFile().mkdirs();
			}

			// QRCodeGenerator.generateQRCodeImageWithoutLogo(QRtext, 200, 200, QRfile);
			QRCodeGenerator.generateQRCodeImageWithLogo(qrtext, 200, 200, qrfile, qrlogo);
			File qrfilepath = new File(qrfile);

			JRBeanCollectionDataSource beanCol = new JRBeanCollectionDataSource(dataList);
			parameters.put("imagepath", certImgPath);

			if (qrfilepath.exists()) {
				parameters.put("qrpath", qrfilepath.toString());
			}

			// String printFileName = JasperFillManager.fillReportToFile(certImgPath1,
			// parameters, beanCol);
			JasperPrint printFileName = JasperFillManager.fillReport(certImgPath1, parameters, beanCol);

			// generating certificate and exporting as PDF
			JasperExportManager.exportReportToPdfFile(printFileName, certPath);
			System.out.println("certificate generated");
			certStatus = certPath;
		} 
		
		catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("file not found exception");
			if (e1.toString().contains("FileNotFound")) {
				return "File not found";
			} 
			else {
				return certStatus;
			}
		}
		return certStatus;
	}

	// method to get certificate
	public ResponseEntity<?> getCertificate(String certificateName) {

		// String certImgPath = "/uploads";
		// String filePath = basePath+certImgPath+ "/" + certificateName; // reference code
		String filePath = IpAddress + "/" + path + "/" + certificateName;

		try {
			URL url = new URL(filePath);
			Map<String, String> path = new HashMap<>();
			path.put("Certificate", url.toString());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-Frame-Options", "*");
			return ResponseEntity.ok().headers(headers).body(path);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("File not found", certificateName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Frame-Options", "SAMEORIGIN");

		return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers)
				.body(new GlobalResponse("Error occured due to " + "following reasons ", errorMessages, false));
	}

	// method to get list of all certificate by name
	public ResponseEntity<?> getAllCertificatesByName() {

		List<String> ListOfFileNames = new ArrayList<>();

		try {
			File fileLocation = new ClassPathResource("/static/reports").getFile();

			File[] files = fileLocation.listFiles();

			for (File file : files) {
				if (file.getName().endsWith(".jasper")) {
					ListOfFileNames.add(file.getName());
				}
			}
			return ResponseEntity.ok(ListOfFileNames);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Directory not found");
		} 
		catch (IOException e) {
			System.out.println("IOEception");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Directory not found");
		}
	}

}
