package org.oscarehr.integration.cdx;

import org.oscarehr.integration.cdx.dao.NotificationDao;
import org.oscarehr.integration.cdx.model.Notification;
import org.oscarehr.util.SpringUtils;

import java.util.List;

public class NotificationController {


    public NotificationController()
    {

    }



    public void insertNotifications(String type, String message, String category, String generatedDuring)
    {
        NotificationDao notificationDao= SpringUtils.getBean(NotificationDao.class);
        Notification notification=new Notification();
        notification.setType(type);
        notification.setMessage(message);
        notification.setCategory(category);
        notification.setGeneratedDuring(generatedDuring);
        notificationDao.persist(notification);

    }


    public void deleteNotifications(String category)
    {
        NotificationDao notificationDao= SpringUtils.getBean(NotificationDao.class);
        List<Notification> nList=notificationDao.findByCategory(category);
        if(nList!=null && nList.size()!=0)
        {
            notificationDao.removeByCategory(category);
        }

    }


    public void checkOBIB()
    {
        NotificationDao notificationDao= SpringUtils.getBean(NotificationDao.class);
        Boolean connection= CDXConfiguration.obibIsConnected();
        if(connection)
        {
            deleteNotifications("OBIB");
        }
        else
        {
            List<Notification> nList=notificationDao.findByCategory("OBIB");
            if(nList!=null && nList.size()!=0)
            {
            }
            else
            {
                insertNotifications("Error","OBIB is not Connected: Error checking connectivity, Please check your connection.","OBIB","Constant checking of OBIB Connection.");
            }
        }
    }
}
