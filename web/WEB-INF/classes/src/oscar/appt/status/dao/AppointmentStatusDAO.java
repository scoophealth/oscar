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
