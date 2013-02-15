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

package oscar.scratch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.ScratchPadDao;
import org.oscarehr.common.model.ScratchPad;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 create table scratch_pad (
           id int(10) not null auto_increment primary key,
           provider_no varchar(6),
           date_time datetime,
           scratch_text text
       );
 * @author jay
 */
public class ScratchData {

	/** Creates a new instance of ScratchData */
	public ScratchData() {
	}
	
	public List<Object[]>getAllDates(String providerNo) {
		ScratchPadDao dao = SpringUtils.getBean(ScratchPadDao.class);
		return dao.findAllDatesByProviderNo(providerNo);
	}

	public Map<String, String> getLatest(String providerNo) {
		ScratchPadDao dao = SpringUtils.getBean(ScratchPadDao.class);
		ScratchPad scratchPad = dao.findByProviderNo(providerNo);
		if (scratchPad == null) return null;

		Map<String, String> retval = new HashMap<String, String>();
		retval.put("id", scratchPad.getId().toString());
		retval.put("text", scratchPad.getText());
		retval.put("date", ConversionUtils.toDateString(scratchPad.getDateTime()));
		return retval;
	}

	public String insert2(String providerNo, String text) {
		ScratchPad scratchPad = new ScratchPad();
		scratchPad.setProviderNo(providerNo);
		scratchPad.setText(text);
		scratchPad.setDateTime(new Date());

		ScratchPadDao dao = SpringUtils.getBean(ScratchPadDao.class);
		dao.persist(scratchPad);
		return scratchPad.getId().toString();
	}

	public String insert(String providerNo, String text) {
		return insert2(providerNo, text);
	}

}
