/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.NoResultException;
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
        List<Queue> result=new ArrayList<Queue>();
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
        try {
	        Query query=entityManager.createQuery("select MAX(q.id) from Queue q");
	        Integer ri=(Integer)query.getSingleResult();
	        r = ri.toString();
        }catch(NoResultException e) {
        	//ignore
        }
       
        return r;
    }
    public String getQueueName(int id){

        String q="select q from Queue q where q.id="+id;
        Query query=entityManager.createQuery(q);
        try {
        	Queue result=(Queue)query.getSingleResult();
        	return result.getName();
        }catch(NoResultException e) {
        	//ignore
        }
        return "";
    }
    public String getQueueid(String name){
        String q="select q from Queue q where q.name="+name;
        Query query=entityManager.createQuery(q);
        try {
        	Queue result=(Queue)query.getSingleResult();
        	return result.getId().toString();
        }catch(NoResultException e) {
        	//ignore
        }
       return "";
    }
    public boolean addNewQueue(String qn){
       try{
            Queue q=new Queue();
            q.setName(qn);
            entityManager.persist(q);
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
       return true;
    }
}
