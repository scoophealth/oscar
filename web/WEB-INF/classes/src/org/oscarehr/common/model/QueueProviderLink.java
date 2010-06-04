/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.model;

/**
 *
 * @author jackson
 */
public class QueueProviderLink {
    private int id;
    private String providerId;
    private int queueId;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getProviderId(){
        return providerId;
    }
    public void setProviderId(String pId){
        this.providerId=pId;
    }

    public int getQueueId(){
        return queueId;
    }
    public void setQueueId(int qId){
        this.queueId=qId;
    }
}
