package com.quiz.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptDecryptHandler {
	
	@Value("${spring.secret}")
	private String jwtSecret;

	private static SecretKeySpec secretKey;

	private static byte[] key;
	private static final String ALGORITHM = "AES";

	// method to encrypt email
	public String encrypt(String originalEmail) {
		try {
			prepareSecreteKey(jwtSecret);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(originalEmail.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	// method to decrypt email
	public String decrypt(String encryptedEmail) {
		try {
			prepareSecreteKey(jwtSecret);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedEmail)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	private void prepareSecreteKey(String secret) {
		MessageDigest sha = null;
		try {
			key = secret.getBytes(StandardCharsets.UTF_8);
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
