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

package oscar.appt.status.dao;

import java.util.List;

import oscar.appt.status.model.AppointmentStatus;

public interface AppointmentStatusDAO {
	public List getAllStatus();
        public AppointmentStatus getStatus(int ID);
        public void changeStatus(int ID, int iActive);
	public void modifyStatus(int ID, String strDesc, String strColor);
        public int checkStatusUsuage(List allStatus);	
        public List getAllActiveStatus();
}
