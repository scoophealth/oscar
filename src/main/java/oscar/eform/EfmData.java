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

package oscar.eform;

import java.util.HashMap;

import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.SpringUtils;

/**
 * 
 * @author Jay Gallagher
+----------------+--------------+------+-----+---------+----------------+
| Field          | Type         | Null | Key | Default | Extra          |
+----------------+--------------+------+-----+---------+----------------+
| fdid           | int(8)       |      | PRI | NULL    | auto_increment |
| fid            | int(8)       |      |     | 0       |                |
| form_name      | varchar(255) | YES  |     | NULL    |                |
| subject        | varchar(255) | YES  |     | NULL    |                |
| demographic_no | int(10)      |      |     | 0       |                |
| status         | tinyint(1)   |      |     | 1       |                |
| form_date      | date         | YES  |     | NULL    |                |
| form_time      | time         | YES  |     | NULL    |                |
| form_provider  | varchar(255) | YES  |     | NULL    |                |
| form_data      | text         | YES  |     | NULL    |                |
+----------------+--------------+------+-----+---------+----------------+

 */

@Deprecated
public class EfmData {

	public HashMap<String, String> getLastEformDate(String formName, String demographicNo) {
		HashMap<String, String> ret = new HashMap<String, String>();
		EFormDataDao dao = SpringUtils.getBean(EFormDataDao.class);
		Integer demographicId = Integer.parseInt(demographicNo);		
		for (EFormData data : dao.findByDemographicIdAndFormName(demographicId, formName)) {
			ret.put("formName", data.getFormName());
			ret.put("subject", data.getSubject());
			ret.put("date", String.valueOf(data.getFormDate()));
			ret.put("time", String.valueOf(data.getFormTime()));
			ret.put("provider", data.getProviderNo());
		}
		return ret;
	}
}
