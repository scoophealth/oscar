/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.List;
import org.oscarehr.common.model.QueueProviderLink;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author jackson
 */
public class QueueProviderLinkDao extends HibernateDaoSupport{
    public List getQueueFromProvider(String providerId){
        List queues=this.getHibernateTemplate().find("from QueueProviderLink where providerId=?", new Object[] {providerId});
        return queues;
    }
    public List getProviderFromQueue(Integer queueId){
        List providers=this.getHibernateTemplate().find("from QueueProviderLink where queueId=?", new Object[] {queueId});
        return providers;
    }
    public boolean isLinkExist(Integer queueId,String providerId){
        int count=DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from QueueProviderLink where queueId=? and providerId=?",new Object[]{queueId,providerId}));
        if(count>0)
            return true;
        else
            return false;
    }
    public void addQueueProviderLink(Integer queueId,String providerId){
        try{
            if(!isLinkExist(queueId,providerId)){
                QueueProviderLink qpl=new QueueProviderLink();
                qpl.setProviderId(providerId);
                qpl.setQueueId(queueId);
                this.getHibernateTemplate().save(qpl);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
