package org.mule.custom.providers.file.transformers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.mule.transformers.AbstractTransformer;
import org.mule.umo.transformer.TransformerException;

/**
 * <code>FileToByteArray</code> reads the contents of a files as a byte array
 * 
 * @author <a href="mailto:ross.mason@symphonysoft.com">Ross Mason</a>
 * @version $Revision: 1.1 $
 */
public class FileToByteArray extends AbstractTransformer
{
    public FileToByteArray()
    {
        registerSourceType(File.class);
        registerSourceType(String.class);
        registerSourceType(byte[].class);
        setReturnClass(byte[].class);
    }

    public Object doTransform(Object src, String encoding) throws TransformerException
    {
        if (src instanceof byte[])
            return (byte[]) src;
        if (src instanceof String){
			try{
            	return src.toString().getBytes(encoding);
			}catch(Exception e){
				System.out.println("Encoding not supported: returning with out specifying encoding");
				return src.toString().getBytes();		
			}
		}

        File file = (File) src;
        byte[] buf = new byte[8 * 1024];
        if (file.length() == 0) {
            logger.warn("File is empty: " + ((File) src).getAbsolutePath());
            return new byte[] {};
        }
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream((File) src);
            baos = new ByteArrayOutputStream(new Long(((File) src).length()).intValue());
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new TransformerException(this, e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                logger.debug("Failed to close reader in transformer: " + e.getMessage());
            }
        }
    }
}
