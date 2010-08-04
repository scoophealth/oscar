package org.oscarehr.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

public class QrCodeUtils {
	public static byte[] toQrCodePng(String s, ErrorCorrectionLevel ec) throws IOException, WriterException
	{
		QRCode qrCode = new QRCode();
		Encoder.encode(s, ec, qrCode);
		BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(qrCode.getMatrix());
		return(toPng(bufferedImage));
	}

	private static byte[] toPng(BufferedImage bufferedImage) throws IOException
	{
		ImageWriter imageWriter = getPngImageWriter();
		try
		{
			// set quality
			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);

			imageWriter.setOutput(imageOutputStream);
			IIOImage iioImage = new IIOImage(bufferedImage, null, null);
			imageWriter.write(null, iioImage, imageWriteParam);

			return(byteArrayOutputStream.toByteArray());
		}
		finally
		{
			imageWriter.dispose();
		}
	}

	/**
	 * Remember to dispose of the ImageWriter when you're finished with it.
	 */
	private static ImageWriter getPngImageWriter()
	{
		Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("png");
		if (writers.hasNext())
		{
			return(writers.next());
		}
		else
		{
			throw(new IllegalStateException("Missing png Image Writer"));
		}
	}

	public static void main(String... argv) throws Exception
	{
		byte[] b = toQrCodePng("this is a test of some text", ErrorCorrectionLevel.H);
		
		FileOutputStream fos = new FileOutputStream("/tmp/test_h.png");
		fos.write(b);
		fos.flush();
		fos.close();

	}
}
