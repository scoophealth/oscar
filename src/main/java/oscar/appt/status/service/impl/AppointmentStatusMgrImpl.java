/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oscar.appt.status.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import oscar.appt.status.dao.AppointmentStatusDAO;
import oscar.appt.status.model.AppointmentStatus;
import oscar.appt.status.service.AppointmentStatusMgr;

/**
 *
 * @author toby
 */
@Transactional
public class AppointmentStatusMgrImpl implements AppointmentStatusMgr {

    private AppointmentStatusDAO appointStatusDao = null;

    public AppointmentStatusDAO getAppointStatusDao(){
        return appointStatusDao;
    }
    
    public void setAppointStatusDao(AppointmentStatusDAO dao){
        this.appointStatusDao = dao;
    }
    
    public List getAllStatus(){
        return appointStatusDao.getAllStatus();
    };
    
    public List getAllActiveStatus(){
        return appointStatusDao.getAllActiveStatus();
    };

    public AppointmentStatus getStatus(int ID){
        return appointStatusDao.getStatus(ID);
    };

    public void changeStatus(int ID, int iActive){
        appointStatusDao.changeStatus(ID, iActive);
    };

    public void modifyStatus(int ID, String strDesc, String strColor){
        appointStatusDao.modifyStatus(ID, strDesc, strColor);
    };

    public int checkStatusUsuage(List allStatus){
        return appointStatusDao.checkStatusUsuage(allStatus);
    };
    
    public void reset(){
        appointStatusDao.modifyStatus(1, "To Do", "#FDFEC7");
        appointStatusDao.modifyStatus(2, "Daysheet Printed", "#FDFEC7");
        appointStatusDao.modifyStatus(3, "Here", "#00ee00");
        appointStatusDao.modifyStatus(4, "Picked", "#FFBBFF");
        appointStatusDao.modifyStatus(5, "Empty Room", "#FFFF33");
        appointStatusDao.modifyStatus(6, "Costumized 1", "#897DF8");
        appointStatusDao.modifyStatus(7, "Costumized 2", "#897DF8");
        appointStatusDao.modifyStatus(8, "Costumized 3", "#897DF8");
        appointStatusDao.modifyStatus(9, "Costumized 4", "#897DF8");
        appointStatusDao.modifyStatus(10, "Costumized 5", "#897DF8");
        appointStatusDao.modifyStatus(11, "Costumized 6", "#897DF8");
        appointStatusDao.modifyStatus(12, "No Show", "#cccccc");
        appointStatusDao.modifyStatus(13, "Cancelled", "#999999");
        appointStatusDao.modifyStatus(14, "Billed", "#3ea4e1");
    }
}
