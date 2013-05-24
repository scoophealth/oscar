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
package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.model.PreventionExt;

public class PreventionExtTransfer {
	
	private Integer id;
	private Integer preventionId;
	private String key;
	private String value;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPreventionId() {
		return (preventionId);
	}

	public void setPreventionId(Integer preventionId) {
		this.preventionId = preventionId;
	}

	public String getKey() {
		return (key);
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return (value);
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * We will not support null key entries
	 */
	public static PreventionExtTransfer toTransfer(PreventionExt preventionExt) {
		if (preventionExt==null) return(null);
		
		PreventionExtTransfer transfer = new PreventionExtTransfer();
		transfer.setId(preventionExt.getId());
		transfer.setPreventionId(preventionExt.getPreventionId());
		transfer.setKey(preventionExt.getkeyval());
		transfer.setValue(preventionExt.getVal());

		return (transfer);
	}

	public static PreventionExtTransfer[] toTransfers(List<PreventionExt> preventionExts) {
		ArrayList<PreventionExtTransfer> results = new ArrayList<PreventionExtTransfer>();

		for (PreventionExt preventionExt : preventionExts) {
			results.add(toTransfer(preventionExt));
		}

		return (results.toArray(new PreventionExtTransfer[0]));
	}
}
