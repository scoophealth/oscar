package org.oscarehr.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

public class ImageIoUtils
{
	private static final Logger logger=MiscUtils.getLogger();
	
	static
	{
		ImageIO.setUseCache(false);
	}

	public static byte[] scaleJpgSmallerProportionally(byte[] inputImage, int maxWidth, int maxHeight, float quality)
	{
		try
		{
			ByteArrayInputStream bais=new ByteArrayInputStream(inputImage);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			
			scaleJpgSmallerProportionally(bais, baos, maxWidth, maxHeight, quality);
			
			return(baos.toByteArray());
		}
		catch (Exception e)
		{
			logger.error("Error scaling image.", e);
			return(null);
		}
	}
		
	
	/**
	 * This method will strip exif data
	 * @param quality is only applicable for compression algorithms like jpg and should be 0-100
	 * @param shrinkOnly if true it will only shrink the image, never enlarge it
	 * @throws IOException 
	 */
	public static void scaleJpgSmallerProportionally(InputStream inputStream, OutputStream outputStream, int maxWidth, int maxHeight, float quality) throws IOException
	{
		BufferedImage image = ImageIO.read(inputStream);
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// scale if necessary
		if (maxWidth < imageWidth || maxHeight < imageHeight)
		{
			float shrinkRatio = Math.min((float)maxHeight / imageHeight, (float)maxWidth / imageWidth);
			int newWidth = (int)(imageWidth * shrinkRatio);
			int newHeight = (int)(imageHeight * shrinkRatio);

			Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			image = toBufferedImage(scaledImage);
		}

		// write image 
		ImageWriter jpgImageWriter=getJpgImageWriter();
		try
		{
			// set quality
			ImageWriteParam imageWriteParam = jpgImageWriter.getDefaultWriteParam();
			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality(quality);

			ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
			try
			{
				jpgImageWriter.setOutput(imageOutputStream);
				IIOImage iioImage = new IIOImage(image, null, null);
				jpgImageWriter.write(null, iioImage, imageWriteParam);
			}
			finally
			{
				imageOutputStream.close();
			}
		}
		finally
		{
			jpgImageWriter.dispose();
		}
	}

	/**
	 * You better remember to dispose of this image writer...
	 * @return
	 */
	public static ImageWriter getJpgImageWriter()
	{
		Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpg");
		if (writers.hasNext())
		{
			return(writers.next());
		}
		else
		{
			throw(new IllegalStateException("Missing jpg Image Writer"));
		}
	}

	public static BufferedImage toBufferedImage(Image image)
	{
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		try
		{
			g2d.drawImage(image, 0, 0, null);
		}
		finally
		{
			g2d.dispose();
		}

		return(bufferedImage);
	}
}
