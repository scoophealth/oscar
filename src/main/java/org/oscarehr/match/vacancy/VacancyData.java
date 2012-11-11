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
package org.oscarehr.match.vacancy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author AnooshTech
 *
 */
public class VacancyData {
	private int vacancy_id;
	private int program_id;
	private Map<String, VacancyTemplateData> vacancyData = new HashMap<String, VacancyTemplateData>();
	
	public int getVacancy_id() {
		return vacancy_id;
	}
	public void setVacancy_id(int vacancy_id) {
		this.vacancy_id = vacancy_id;
	}
	public int getProgram_id() {
		return program_id;
	}
	public void setProgram_id(int program_id) {
		this.program_id = program_id;
	}
	public Map<String, VacancyTemplateData> getVacancyData() {
		return vacancyData;
	}
	public void setVacancyData(Map<String, VacancyTemplateData> vacancyData) {
		this.vacancyData = vacancyData;
	}
	@Override
	public String toString() {
		final int maxLen = 16;
		return "VacancyData [vacancy_id="
				+ vacancy_id
				+ ", program_id="
				+ program_id
				+ ", vacancyData="
				+ (vacancyData != null ? toString(vacancyData.entrySet(),
						maxLen) : null) + "]";
	}
	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	
}
