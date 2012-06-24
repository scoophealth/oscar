/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.caisi.model.BaseObject;
import org.hibernate.Hibernate;
import org.oscarehr.util.MiscUtils;

public class ClientImage extends BaseObject {
	public static final String imageMissingPlaceholderUrl="/images/defaultR_img.jpg";
	public static final String imagePresentPlaceholderUrl="/images/default_img.jpg";
	
	private Long id;
	private int demographic_no;
	private String image_type;
	private byte[] image_data;
	private Date update_date;
	
	public ClientImage() {
		update_date = new Date();
	}

	public int getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(int demographic_no) {
		this.demographic_no = demographic_no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getImage_data() {
		return image_data;
	}

	public void setImage_data(byte[] image_data) {
		this.image_data = image_data;
	}

	public String getImage_type() {
		return image_type;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Blob getImage_contents() {
		if(image_data == null) {
			return null;
		}
		return Hibernate.createBlob(Base64.encodeBase64(getImage_data()));
	}

	public void setImage_contents(Blob image_contents) {
		if(image_contents != null) {
			try {
				this.image_data = Base64.decodeBase64(this.blobToByteArray(image_contents));
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
				this.image_data = null;
			}
		}
	}

	private byte[] blobToByteArray(Blob image_contents) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[4000];
		InputStream is = null;
		try {
			is = image_contents.getBinaryStream();
			for (;;) {
				int dataSize = is.read(buf);
				if (dataSize == -1)
					break;
				baos.write(buf, 0, dataSize);
			}
		} catch(Exception e) {
			throw new Exception(e);
		} finally {
			if(is != null)
				is.close();
		}
		
		return baos.toByteArray();
	}

}
