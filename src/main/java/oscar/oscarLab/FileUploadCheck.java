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


package oscar.oscarLab;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 * @author Jay Gallagher
 */
public final class FileUploadCheck {

	private FileUploadCheck() {
		// no instantiation allowed
	}

	private static boolean hasFileBeenUploaded(String md5sum) {
		boolean hasFileBeenUploaded = false;
		try {

			String sql = "select * from fileUploadCheck where md5sum = '" + md5sum + "' ";
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				hasFileBeenUploaded = true;
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return hasFileBeenUploaded;
	}
	
	public static Map<String,String> getFileInfo(Integer id) {
		Map<String, String> fileInfo = new HashMap<String, String>();
		try {
			String sql = "select * from fileUploadCheck where id = " + id.toString();
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				fileInfo.put("providerNo", oscar.Misc.getString(rs, "provider_no"));
				fileInfo.put("filename", oscar.Misc.getString(rs, "filename"));
				fileInfo.put("md5sum", oscar.Misc.getString(rs, "md5sum"));
				fileInfo.put("dateTime", oscar.Misc.getString(rs, "date_time"));
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		
		return fileInfo;
		
	}

	public static Hashtable<String, String> getFileInfo(String md5sum) {
		Hashtable<String, String> fileInfo = new Hashtable<String, String>();
		try {

			String sql = "select * from fileUploadCheck where md5sum = '" + md5sum + "' ";
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				fileInfo.put("providerNo", oscar.Misc.getString(rs, "provider_no"));
				fileInfo.put("filename", oscar.Misc.getString(rs, "filename"));
				fileInfo.put("md5sum", oscar.Misc.getString(rs, "md5sum"));
				fileInfo.put("dateTime", oscar.Misc.getString(rs, "date_time"));
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return fileInfo;
	}

	public static final int UNSUCCESSFUL_SAVE = -1;

	/**
	 *Used to add a new file to the database, checks to see if it already has been added
	 */
	public static synchronized int addFile(String name, InputStream is, String provider) throws Exception {
		int fileUploaded = UNSUCCESSFUL_SAVE;
		try {
			String md5sum = DigestUtils.md5Hex(IOUtils.toByteArray(is));
			if (!hasFileBeenUploaded(md5sum)) {

				String sql = "insert into fileUploadCheck (provider_no,filename,md5sum,date_time) values ('" + provider + "','" + StringEscapeUtils.escapeSql(name) + "','" + md5sum + "',now())";
				MiscUtils.getLogger().debug(sql);
				DBHandler.RunSQL(sql);
				ResultSet rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID() ");
				if (rs.next()) {
					fileUploaded = rs.getInt(1);
				}
			}
		} catch (SQLException conE) {
			MiscUtils.getLogger().error("Error", conE);
			throw new Exception("Database Is not Running");
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		MiscUtils.getLogger().debug("returning " + fileUploaded);
		return fileUploaded;
	}

}
