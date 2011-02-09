/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Queue;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jackson
 */
@Repository
public class QueueDao extends AbstractDao<Queue>{
    public QueueDao(){
        super(Queue.class);
    }

    public HashMap getHashMapOfQueues(){
        String q="select q from Queue q";
        Query query=entityManager.createQuery(q);
        List<Queue> result=new ArrayList<Queue>();
        result=query.getResultList();
        HashMap<Integer,String> hm=new HashMap<Integer,String>();
        for(Queue que:result){
            hm.put(que.getId(),que.getName());
        }
        return hm;
    }
    public List<Hashtable> getQueues(){
        String q="select q from Queue q";
        Query query=entityManager.createQuery(q);
        List<Queue> result=new ArrayList();
        result=query.getResultList();
        List<Hashtable> r=new ArrayList();
        for(Queue que:result){
            Hashtable ht=new Hashtable();
            ht.put("id", que.getId());
            ht.put("queue", que.getName());
            r.add(ht);
        }
        return r;
    }

    public String getLastId(){
        String r="";
        Query query=entityManager.createQuery("select MAX(q.id) from Queue q");
        Integer ri=(Integer)query.getSingleResult();
        if(ri!=null){
            r=ri.toString();
        }        
        return r;
    }
    public String getQueueName(int id){

        String q="select q from Queue q where q.id="+id;
        Query query=entityManager.createQuery(q);
        Queue result=(Queue)query.getSingleResult();
        if(result!=null){
            return result.getName();
        }else{
            return "";
        }
            
    }
    public String getQueueid(String name){
        String q="select q from Queue q where q.name="+name;
        Query query=entityManager.createQuery(q);
        Queue result=(Queue)query.getSingleResult();
        if(result!=null){
            return result.getId().toString();
        }else{
            return "";
        }
    }
    public boolean addNewQueue(String qn){
       try{
            Queue q=new Queue();
            q.setName(qn);
            entityManager.persist(q);
        }catch(Exception e){
            MiscUtils.getLogger().error(e);
            return false;
        }
       return true;
    }
}
