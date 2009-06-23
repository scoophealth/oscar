/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.struts.upload.FormFile;
import org.hibernate.Hibernate;

public class AttachmentText implements java.io.Serializable {
	private byte[] attData;
	private String fileName;
	private Integer docId;

	private Calendar revDate;
	private FormFile imagefile;

	public FormFile getImagefile() {		
		return imagefile;
	}

	public void setImagefile(FormFile imagefile) {
		this.imagefile = imagefile;
	}
	public byte[] getAttData() {
		return attData;
	}

	public void setAttData(byte[] attData) {
		this.attData = attData;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Calendar getRevDate() {
		return revDate;
	}

	public void setRevDate(Calendar revDate) {
		this.revDate = revDate;
	}
	public void setAttDataBlob(Blob dataBlob) {
		  this.attData = this.toByteArray(dataBlob);
	  } 

	  // Don't invoke this.  Used by Hibernate only. 
	  public Blob getAttDataBlob() {
		  return Hibernate.createBlob(this.attData); 
	  }

	private byte[] toByteArray(Blob fromBlob) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			return toByteArrayImpl(fromBlob, baos);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {

				}
			}
		}
	}

	private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos)
			throws SQLException, IOException {
		byte[] buf = new byte[4000];
		InputStream is = fromBlob.getBinaryStream();
		try {
			for (;;) {
				int dataSize = is.read(buf);
				if (dataSize == -1)
					break;
				baos.write(buf, 0, dataSize);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
				}
			}
		}

		return baos.toByteArray();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
