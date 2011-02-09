/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.appt.status.service;

import java.util.List;

import oscar.appt.status.model.AppointmentStatus;

/**
 *
 * @author toby
 */
public interface AppointmentStatusMgr {
	public List getAllStatus();
        public List getAllActiveStatus();
        public AppointmentStatus getStatus(int ID);
        public void changeStatus(int ID, int iActive);
	public void modifyStatus(int ID, String strDesc, String strColor);
        public int checkStatusUsuage(List allStatus);
        public void reset();
}
