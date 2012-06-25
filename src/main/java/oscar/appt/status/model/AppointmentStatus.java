/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.appt.status.model;

import oscar.appt.status.model.base.BaseAppointmentStatus;

public class AppointmentStatus extends BaseAppointmentStatus {

	public static final String APPOINTMENT_STATUS_HERE = "H";
	public static final String APPOINTMENT_STATUS_CANCELLED = "C";

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public AppointmentStatus() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AppointmentStatus(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public AppointmentStatus(java.lang.Integer id, java.lang.String status, java.lang.String description, java.lang.String color, java.lang.String icon, java.lang.Integer active) {

		super(id, status, description, color, icon, active);
	}

	/*[CONSTRUCTOR MARKER END]*/

}
