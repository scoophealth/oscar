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

package org.oscarehr.util;

/**
 * The purpose of this class is to provide a container for 
 * progress information. The expected usage of this class is
 * that you may instantiate this class, then put it in 
 * the HttpSession, then pass it to a long running task which would
 * then periodically update the contents of these variables.
 * It is then expected that something like another web page
 * may periodically refresh, and the contents of this class
 * maybe used to display the contents of that page. The completed
 * boolean should be set by the running task and the dialog can
 * use it to close the window upon completed.
 * 
 * An example maybe as follows 
 * total="148 files"
 * processed="14 files read"
 * percentComplete=10
 * currentItem="foo.txt"
 */
public class ProgressStatus {
	public String total=null;
	public String processed=null;
	public int percentComplete=0;
	public String currentItem=null;
	public boolean completed=false;
}
