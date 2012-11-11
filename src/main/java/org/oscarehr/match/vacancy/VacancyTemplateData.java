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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class VacancyTemplateData {
	private static final Logger logger = MiscUtils.getLogger();
	private static final int MAX_WEIGHT = 100;
	private static final int MIN_WEIGHT = 0;
	private static final String GENDER = "gender";
	private int weight = 1;
	private String param;
	private List<String> values = new ArrayList<String>();
	private List<Range> ranges = new ArrayList<Range>();
	private boolean option;
	private boolean range;
	//TODO: Probably want to use the Gender object here. I'm not sure where these values are coming from at this time
	private List<String> transaGender = Arrays.asList("male","female","m","f","transgender");

	public int matches(String value) {
		if (this.weight == 0) {
			this.weight = 1;
		}
		if(GENDER.equalsIgnoreCase(param)){
			if(value != null){
				for (String gender : transaGender) {
					if(value.toLowerCase().contains(gender)){
						return 100;
					}
				}
			}
		}
		if (this.range) {
			if (ranges.isEmpty()) {
				return 100;
			}
			if(!StringUtils.isNumeric(value)){
				for(Range range:ranges){
					for(String rangeString:range.rangeString){
						if(value.contains(rangeString) || rangeString.contains(value)){
							return 100;
						}
					}
				}
			}
			Integer val = null;
			try {
				val = Integer.valueOf(value);
			} catch (Exception e) {
				logger.error("Error", e);
			}
			if (val != null) {
				for (Range rangeVal : ranges) {
					if (rangeVal.isInRange(val)) {
						return 100;
					}
				}
			} else {
				return 0;
			}
		}
		String valueToMatch = value;
		if (valueToMatch == null) {
			return 0;
		}
		if (values.isEmpty()) {
			return 100;
		}
		TreeSet<Integer> weights = new TreeSet<Integer>();
		for (String val : values) {
			if (val == null) {
				if (values.size() == 1) {
					return 100;
				}
				weights.add(MAX_WEIGHT);
			} else {
				int distance = StringUtils.getLevenshteinDistance(val.toLowerCase(),
						valueToMatch.toLowerCase());
				int calculatedWeight = 100 / (distance == 0 ? 1 : distance);
				weights.add(calculatedWeight);
			}
		}
		return weights.last();
	}

	public boolean isOption() {
		return option;
	}

	public void setOption(boolean option) {
		this.option = option;
	}

	public void setRange(boolean range) {
		this.range = range;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public void addRange(Integer min, Integer max) {
		if (min == null) {
			min = Integer.MIN_VALUE;
		}
		if (max == null) {
			max = Integer.MAX_VALUE;
		}
		ranges.add(new Range(min.longValue(), max.longValue()));
	}

	@Override
	public String toString() {
		final int maxLen = 20;
		return "VacancyTemlateData [weight="
				+ weight
				+ ", param="
				+ param
				+ ", values="
				+ (values != null ? values.subList(0,
						Math.min(values.size(), maxLen)) : null)
				+ ", ranges="
				+ (ranges != null ? ranges.subList(0,
						Math.min(ranges.size(), maxLen)) : null) + ", option="
				+ option + ", range=" + range + "]";
	}

	private static class Range {
		private long min;
		private long max;
		
		List<String> rangeString = Collections.EMPTY_LIST;

		public Range(long min, long max) {
			this.min = min;
			this.max = max;
			rangeString = new ArrayList<String>(Arrays.asList(""+ min + "-" + max, "" + min + " to " + max));
		}

		boolean isInRange(long number) {
			return number >= min && number <= max;
		}
	}

}
