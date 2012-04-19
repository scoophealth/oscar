/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

public class QrCodeUtils {
	
	private static final Logger logger=MiscUtils.getLogger();
	
	public static final int MAX_QR_CODE_DATA_LENGTH=2953;
	public static final float DEFAULT_QR_CODE_GAP=.20f;

	public enum QrCodesOrientation
	{
		VERTICAL, HORIZONTAL
	}
	
	public static byte[] toMultipleQrCodePngs(String s, ErrorCorrectionLevel ec, QrCodesOrientation qrCodesOrientation, int scaleFactor) throws IOException, WriterException
	{
		return(toMultipleQrCodePngs(s, ec, qrCodesOrientation, null, MAX_QR_CODE_DATA_LENGTH, scaleFactor));
	}
	
	/**
	 * This method will break the longInputString into maxQrCodeDataSize size
	 * segments and create separate qr codes for each segment. There is no 
	 * interpretation of the data; therefore, the individual qr codes should not be
	 * assumed to make sense individually but only collectively after all qr codes 
	 * are interpreted and the resulting data concatenated.
	 * 
	 * @param qrCodeImageGap the number of pixels between the QR code images, if this value is null it will calculate it at DEFAULT_QR_CODE_GAP % of the size of the first (and presumably full size) qr code image
	 * @param scaleFactor this scales the resulting image by the provided factor, this value must be an int or it'll ruin the structure of the qr image
	 */
	public static byte[] toMultipleQrCodePngs(String s, ErrorCorrectionLevel ec, QrCodesOrientation qrCodesOrientation, Integer qrCodeImageGap, int maxQrCodeDataSize, int scaleFactor) throws IOException, WriterException
	{
		ArrayList<BufferedImage> results=new ArrayList<BufferedImage>();
		
		int startIndex=0;
		int endIndex=0;
		while (true)
		{
			endIndex=Math.min(startIndex+maxQrCodeDataSize, s.length());
			
			String stringChunk=s.substring(startIndex, endIndex);
			logger.debug("Encoding chunk : "+stringChunk);
			
			results.add(toSingleQrCodeBufferedImage(stringChunk, ec, scaleFactor));

			if (endIndex==s.length()) break;
			else startIndex=endIndex;
		}
	
		if (qrCodeImageGap==null) qrCodeImageGap=(int)(results.get(0).getWidth()*DEFAULT_QR_CODE_GAP);
		
		byte[] mergedResults=mergeImages(results, qrCodesOrientation, qrCodeImageGap);
		
		return(mergedResults);
	}
	
	public static BufferedImage toSingleQrCodeBufferedImage(String s, ErrorCorrectionLevel ec, int scaleFactor) throws WriterException
	{
		QRCode qrCode = new QRCode();
		Encoder.encode(s, ec, qrCode);
		
		BufferedImage bufferedImage=MatrixToImageWriter.toBufferedImage(qrCode.getMatrix());
		
		if (scaleFactor!=1)
		{
			int newWidth=bufferedImage.getWidth()*scaleFactor;
			int newHeight=bufferedImage.getHeight()*scaleFactor;
			Image image=bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
			bufferedImage=ImageIoUtils.toBufferedImage(image);
		}
		
		return(bufferedImage);
	}

	public static byte[] toSingleQrCodePng(String s, ErrorCorrectionLevel ec, int scaleFactor) throws IOException, WriterException
	{
		return(toPng(toSingleQrCodeBufferedImage(s, ec, scaleFactor)));
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

	private static byte[] mergeImages(ArrayList<BufferedImage> results, QrCodesOrientation qrCodesOrientation, int qrCodeImageGap) throws IOException {

		int requiredWidth=getRequiredWidth(results, qrCodesOrientation, qrCodeImageGap);
		int requiredHeight=getRequiredHeight(results, qrCodesOrientation, qrCodeImageGap);
		
		BufferedImage mergedImage=new BufferedImage(requiredWidth, requiredHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D doubleBuffer=(Graphics2D) mergedImage.getGraphics();
		int x=0;
		int y=0;
		for (BufferedImage bi : results)
		{
			doubleBuffer.drawImage(bi, x, y, null);
			
			if (qrCodesOrientation==QrCodesOrientation.VERTICAL) y=y+bi.getHeight()+qrCodeImageGap;
			else x=x+bi.getWidth()+qrCodeImageGap;
		}
			
	    return(toPng(mergedImage));
    }
	
	private static int getRequiredHeight(ArrayList<BufferedImage> results, QrCodesOrientation qrCodesOrientation, int qrCodeImageGap) {
		if (qrCodesOrientation==QrCodesOrientation.VERTICAL) return(sumHeight(results)+(qrCodeImageGap*(results.size()-1)));
		else return(maxHeight(results));
    }

	private static int maxHeight(ArrayList<BufferedImage> results) {
	    int max=0;
	    for (BufferedImage bi : results) max=Math.max(max, bi.getHeight());
	    return(max);
    }

	private static int sumHeight(ArrayList<BufferedImage> results) {
	    int total=0;
	    for (BufferedImage bi : results) total=total+bi.getHeight();
	    return(total);
    }

	private static int getRequiredWidth(ArrayList<BufferedImage> results, QrCodesOrientation qrCodesOrientation, int qrCodeImageGap) {
		if (qrCodesOrientation==QrCodesOrientation.HORIZONTAL) return(sumWidth(results)+(qrCodeImageGap*(results.size()-1)));
		else return(maxWidth(results));
	}

	private static int maxWidth(ArrayList<BufferedImage> results) {
	    int max=0;
	    for (BufferedImage bi : results) max=Math.max(max, bi.getWidth());
	    return(max);
    }

	private static int sumWidth(ArrayList<BufferedImage> results) {
	    int total=0;
	    for (BufferedImage bi : results) total=total+bi.getWidth();
	    return(total);
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
		byte[] b = toSingleQrCodePng("this is a test of some text", ErrorCorrectionLevel.H, 1);
		
		FileOutputStream fos = new FileOutputStream("/tmp/test_h.png");
		fos.write(b);
		fos.flush();
		fos.close();

		//------
		{
			byte[] b1=toMultipleQrCodePngs("1234567890abcdefghijklmnopqrstuvwxyz", ErrorCorrectionLevel.H, QrCodesOrientation.HORIZONTAL, null, 5, 1);
	
			FileOutputStream fos1 = new FileOutputStream("/tmp/test_h1.png");
			fos1.write(b1);
			fos1.flush();
			fos1.close();
		}
		//------
		{
			byte[] b1=toMultipleQrCodePngs("1234567890abcdefghijklmnopqrstuvwxyz", ErrorCorrectionLevel.H, QrCodesOrientation.HORIZONTAL, null, 5, 3);
	
			FileOutputStream fos1 = new FileOutputStream("/tmp/test_h1_x3.png");
			fos1.write(b1);
			fos1.flush();
			fos1.close();
		}
	}
}
