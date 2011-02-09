package oscar.appt.status.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.appt.status.dao.AppointmentStatusDAO;
import oscar.appt.status.model.AppointmentStatus;

public class AppointmentStatusDAOHibernate extends HibernateDaoSupport
        implements AppointmentStatusDAO {

    public List getAllStatus() {
        Criteria c = getSession().createCriteria(AppointmentStatus.class);
        c = c.addOrder(Order.asc("id"));
        List list = c.list();
        return list;
    }
    
    public List getAllActiveStatus() {
        Criteria c = getSession().createCriteria(AppointmentStatus.class);
        c.add(Expression.eq("Active",1));
        c = c.addOrder(Order.asc("id"));
        List list = c.list();
        return list;
    }

    public AppointmentStatus getStatus(int ID) {
        return (AppointmentStatus) getHibernateTemplate().get(AppointmentStatus.class, new Integer(ID));
    }

    public void modifyStatus(int ID, String strDesc, String strColor) {
        AppointmentStatus appts = (AppointmentStatus) getHibernateTemplate().get(AppointmentStatus.class, ID);
        appts.setDescription(strDesc);
        appts.setColor(strColor);
        getHibernateTemplate().saveOrUpdate(appts);
    }

    public void changeStatus(int ID, int iActive) {
        AppointmentStatus appts = (AppointmentStatus) getHibernateTemplate().get(AppointmentStatus.class, ID);
        appts.setActive(iActive);
        getHibernateTemplate().saveOrUpdate(appts);
    }
    
    public int checkStatusUsuage(List allStatus){
        int iUsuage = 0;
        SQLQuery query = null;
        AppointmentStatus apptStatus = null;
        String sql= null;
        for(int i=0; i<allStatus.size();i++){
            apptStatus = (AppointmentStatus) allStatus.get(i);
            if (apptStatus.getActive()==1)
                continue;
            sql = "select count(*) as total from appointment where status like"+ "'"+apptStatus.getStatus()+"%' ";
            sql = sql + "collate latin1_general_cs";
            query = getSession().createSQLQuery(sql);
            query.addScalar("total", Hibernate.INTEGER);
            iUsuage = (Integer) query.uniqueResult();
            if(iUsuage > 0){
                iUsuage =i;
                break;
            }
        }
        return iUsuage;
    }
}
