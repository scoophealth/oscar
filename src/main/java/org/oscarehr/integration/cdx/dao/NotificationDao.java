package org.oscarehr.integration.cdx.dao;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.integration.cdx.model.Notification;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class NotificationDao extends AbstractDao<Notification> {


    public NotificationDao(){
        super(Notification.class);
    }

    public  List<Notification> findNotifications()
    {
        String sql = "SELECT x FROM Notification x WHERE x.seenAt IS NULL" ;
        Query query = entityManager.createQuery(sql);
        List<Notification> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results;
    }
    public  List<Notification> findNotificationsError()
    {
        String sql = "SELECT x FROM Notification x where x.type='Error' AND x.seenAt IS NULL order by x.generatedAt" ;
        Query query = entityManager.createQuery(sql);
        List<Notification> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results;
    }
    public  List<Notification> findNotificationsWarning()
    {
        String sql = "SELECT x FROM Notification x where x.type='Warning' AND x.seenAt IS NULL order by x.generatedAt" ;
        Query query = entityManager.createQuery(sql);
        List<Notification> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results;
    }
    public Notification findById(String id)
    {
        Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.id='"+id+"'");
        List<Notification> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results.get(0);
    }
}
