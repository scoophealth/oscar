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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.PMmodule.model.ProgramProvider;

public final class ProgramProviderTransfer {
	
	private Long id;
	private Integer programId;
	private String providerNo;
	private Long roleId;

	public Long getId() {
		return (id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getProgramId() {
		return (programId);
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Long getRoleId() {
		return (roleId);
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public static ProgramProviderTransfer toTransfer(ProgramProvider programProvider) {
		if (programProvider == null) return (null);

		ProgramProviderTransfer programTransfer = new ProgramProviderTransfer();

		programTransfer.setId(programProvider.getId());
		programTransfer.setProgramId(programProvider.getProgramId().intValue());
		programTransfer.setProviderNo(programProvider.getProviderNo());
		programTransfer.setRoleId(programProvider.getRoleId());

		return (programTransfer);
	}

	public static ProgramProviderTransfer[] toTransfers(List<ProgramProvider> programProviders) {
		ArrayList<ProgramProviderTransfer> results = new ArrayList<ProgramProviderTransfer>();

		for (ProgramProvider programProvider : programProviders) {
			results.add(toTransfer(programProvider));
		}

		return (results.toArray(new ProgramProviderTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
