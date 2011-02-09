/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.web.adapter;

/**
 * Adapt intake nodes to html
 */
public interface IntakeNodeHtmlAdapter {

	/**
	 * Get builder for markup before visiting children
	 * 
	 * @return builder with markup
	 */
	public StringBuilder getPreBuilder();

	/**
	 * Get builder for markup after visiting children
	 * 
	 * @return builder with markup
	 */
	public StringBuilder getPostBuilder();

	/**
	 * Get number of tabs to indent
	 * 
	 * @return number of tabs to indent
	 */
	public int getIndent();

}