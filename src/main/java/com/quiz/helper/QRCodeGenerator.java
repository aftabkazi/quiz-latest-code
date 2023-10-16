package com.quiz.helper;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {

	// method to generate QR code without image logo
	public static void generateQRCodeImageWithoutLogo(String text, int width, int height, String filePath) {

		BitMatrix bitMatrix = null;
		QRCodeWriter writer = new QRCodeWriter();

		// String text = "qrtext";
		// int width = 200, height = 200;
		// String filePath="C:\\Users\\cdac\\Desktop\\Aftab\\Quiz Web application-Backend\\quiz\\quiz\\src\\barcode.png";
		try {
			bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);

			Path path = FileSystems.getDefault().getPath(filePath);
			
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to generate QR code with image logo
	public static void generateQRCodeImageWithLogo(String text, int width, int height, String filePath, String qrlogo) {

		Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
		
		/* QR code can tolerate upto 30 % of damage
		 * Highest level of error damage 
		 * used to scan QR code more easily
		 */
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix bitMatrix = null;

		try {
			// BitMatrix contains the binary data representing the QR code pattern
			bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
			
			// blank image and background white
			MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
			
			/* Load QR image
			 * converts the bitmax image to bufferImage
			 * bufferImage can easily be saved as image file
			 */
			BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
			
			// Load logo image on the QR code
			File file = new File(qrlogo);
			BufferedImage logoImage = ImageIO.read(file);
			
			// Calculate the delta height and width between QR code and logo
			int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
			int deltaWidth = qrImage.getWidth() - logoImage.getWidth();
			
			// The combined object stores the final image 
			BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();
			
			/*  Write QR code to new image at position 0/0
			 *  by calling this method QR code image is drawn on the combined image
			 */
			g.drawImage(qrImage, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			g.drawImage(logoImage, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
			
			// Write combined image as PNG to OutputStream
			ImageIO.write(combined, "png", new File(filePath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
