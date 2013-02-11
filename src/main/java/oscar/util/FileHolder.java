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


package oscar.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 */
public class FileHolder implements Serializable {
	private boolean toMemory;

  // "file system" name of the file
  private String fileName;

  // content type of the file
  private String contentType;

	// buffer to hold data till its use (used if memory buffering choosen)
	private byte[] memoryBuffer;

	// stores the length of the file
	private int fileLength;

  /**
   *
   * Constructor
   * - takes descriptive data (fileName, contentType)
   * - inputstream, coming from servletinputstream - we must read it out _now_
   * - toMemory: true->write it to memory (implemented), false->write it to tempfile (not implemented yet)
   */

  FileHolder(String fileName, String contentType, InputStream is, boolean toMemory, int maxSize) throws IOException {
	this.toMemory = toMemory;
	this.fileName = fileName;
	this.contentType = contentType;

		if(toMemory) {
			bufferInMemory(maxSize, is);
		} else {
			throw new IllegalArgumentException("tmpFile-feature not implemented yet");
		}

  }  

  /**
   * Returns the name that the file was stored with on the remote system,
   * or <code>null</code> if the user didn't enter a file to be uploaded.
   * Note: this is not the same as the name of the form parameter used to
   * transmit the file; that is available from the <code>getName</code>
   * method.
   *
   * @return name of file uploaded or <code>null</code>.
   *
   */
  public String getFileName() {
	return fileName;
  }  

  public void setFileName(String fileName) {
		this.fileName = fileName;
	}

  /**
   * Returns the content type of the file data contained within.
   *
   * @return content type of the file data.
   */
  public String getContentType() {
	return contentType;
  }  


  /**
   * <p>Returns the length of the file representated by this FileHolder</p>
   * <p>fileLength gets determinated either during "bufferInMemory" or "bufferInFile" (not impl. yet)</p>
   *
   * @return content type of the file data.
   */
	public int getFileLength() {
		return fileLength;
	}

  private void bufferInMemory(int maxSize, InputStream partInput) throws IOException {
		byte[] tmpMemoryBuffer = new byte[maxSize]; // this could lead to memory problems. if it does used filebuffering instead
		fileLength = partInput.read(tmpMemoryBuffer);
		memoryBuffer = new byte[fileLength];
		System.arraycopy(tmpMemoryBuffer, 0, memoryBuffer, 0, fileLength);
		tmpMemoryBuffer = null;
	}

	public InputStream getInputStreamFromBuffer() {
		if(toMemory)
		  return new ByteArrayInputStream(memoryBuffer);
		 else
		  throw new IllegalArgumentException("tmpFile-feature not implemented yet");
	}

	public byte[] getMemoryBuffer() {
		return memoryBuffer;
	}

  public long writeBufferToFile(File fileOrDirectory) throws IOException {

		if(!toMemory)
		  throw new IllegalArgumentException("tmpFile-feature not implemented yet");

	long written = 0;

	OutputStream fileOut = null;
	try {
	  // Only do something if this part contains a file
	  if (fileName != null) {
		// Check if user supplied directory
		File file;
		if (fileOrDirectory.isDirectory()) {
		  // Write it to that dir the user supplied,
		  // with the filename it arrived with
		  file = new File(fileOrDirectory, fileName);
		}
		else {
		  // Write it to the file the user supplied,
		  // ignoring the filename it arrived with
		  file = fileOrDirectory;
		}
		fileOut = new BufferedOutputStream(new FileOutputStream(file));
		fileOut.write(memoryBuffer, 0, memoryBuffer.length);
	  }
	}
	finally {
	  if (fileOut != null) fileOut.close();
	}
	return written;
  }  

}
