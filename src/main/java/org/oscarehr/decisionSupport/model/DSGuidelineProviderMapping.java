/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

/**
 *
 * @author apavel
 */
public class DSGuidelineProviderMapping {
    private int id;
    private String providerNo;
    private String guidelineUUID;

    public DSGuidelineProviderMapping() {
        
    }

    public DSGuidelineProviderMapping(String guidelineUUID, String providerNo) {
        this.guidelineUUID = guidelineUUID;
        this.providerNo = providerNo;
    }

    @Override  //must have same hashcode, but oh well
    public boolean equals(Object object2) {
        DSGuidelineProviderMapping mapping2 = (DSGuidelineProviderMapping) object2;
        if (mapping2.getProviderNo().equals(this.getProviderNo()) && mapping2.getGuidelineUUID().equals(this.getGuidelineUUID())) {
            return true;
        }
        return false;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the providerNo
     */
    public String getProviderNo() {
        return providerNo;
    }

    /**
     * @param providerNo the providerNo to set
     */
    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    /**
     * @return the guidelineUUID
     */
    public String getGuidelineUUID() {
        return guidelineUUID;
    }

    /**
     * @param guidelineUUID the guidelineUUID to set
     */
    public void setGuidelineUUID(String guidelineUUID) {
        this.guidelineUUID = guidelineUUID;
    }

}
