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

/**
 * Facility class for appending strings.
 */
public class Appender {

	public static final String DEFAULT_SEPARATOR = " ";
	
	private StringBuilder buffer = new StringBuilder();
	private String separator;
	private boolean isAppended = false;

	/**
	 * Creates a new instance and sets space {@link #DEFAULT_SEPARATOR} as the separator
	 */
	public Appender() {
		this(DEFAULT_SEPARATOR);
	}

	/**
	 * Creates a new instance and sets the specified string as the separator
	 * 
	 * @param separator
	 *            Separator to be placed between appended strings
	 */
	public Appender(String separator) {
		setSeparator(separator);
	}

	/**
	 * Creates a new instance and sets the specified string as the separator
	 * and the initial string to be appended
	 * 
	 * @param separator
	 *            Separator to be placed between appended strings
	 * @param initialContent
	 * 			  Content to be appended
	 */
	public Appender(String separator, String initialContent) {
		this(separator);
		append(initialContent);
	}

	protected StringBuilder getBuffer() {
		return buffer;
	}

	protected void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}

	protected boolean isAppended() {
		return isAppended;
	}

	protected void setAppended(boolean isAppended) {
		this.isAppended = isAppended;
	}

	public boolean append(Appender appender) {
		return append(appender.toString());
	}

	/**
	 * Converts the objects provided into a string and appends
	 * this string. Please note that if there is at least one
	 * null object in the parameter list provided, nothing is
	 * appended to this appender.
	 * 
	 * @param objects
	 * 		Non-null objects to append. In case there is at least one
	 * 		null object, the append process is halted.
	 * @return
	 * 		Returns true if content of this appender has been modified 
	 * 		and false otherwise.
	 */
	public boolean appendNonEmpty(Object... objects) {
		StringBuilder buf = new StringBuilder();
		for (Object o : objects) {
			if (o == null) {
				return false;
			} else {
				buf.append(o);
			}
		}
		return append(buf.toString());
	}

	/**
	 * Trims and appends the specified string to this instance, inserting the
	 * separator, if necessary.
	 * 
	 * @param string
	 *            String to be appended
	 * @return Returns true if the specified string was appended and false
	 *         otherwise.
	 */
	public boolean append(String string) {
		if (string == null) {
			return false;
		}
		
		string = string.trim();
		if (isSet(string)) {
			if (isAppended()) {
				getBuffer().append(getSeparator());
			}
			getBuffer().append(string);
			setAppended(true);
			return true;
		}
		return false;
	}

	private boolean isSet(String string) {
		return string != null && string.length() > 0;
	}

	@Override
	public String toString() {
		return getBuffer().toString();
	}

	/**
	 * Gets the separator that this appender uses to separate appended strings
	 * 
	 * @return Returns the separator used
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Sets the separator that this appender uses to separate appended strings
	 * 
	 * @param separator
	 *            separator to set
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Gets length of the currently appended strings for this appender
	 * 
	 * @return Returns the number of characters for the strings appended
	 */
	public int length() {
		return getBuffer().length();
	}

}