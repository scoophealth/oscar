/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarRx.pageUtil;

import oscar.oscarRx.data.*;

import java.util.*;

public class RxSessionBean {
    private String providerNo = null;
    private int demographicNo = 0;
    
    private ArrayList stash = new ArrayList();
    private int stashIndex = -1;
    
    
    
    //--------------------------------------------------------------------------
    
    public String getProviderNo() {
        return this.providerNo;
    }
    public void setProviderNo(String RHS) {
        this.providerNo = RHS;
    }
    
    public int getDemographicNo() {
        return this.demographicNo;
    }
    public void setDemographicNo(int RHS) {
        this.demographicNo = RHS;
    }
    
    //--------------------------------------------------------------------------
    
    public int getStashIndex() {
        return this.stashIndex;
    }
    public void setStashIndex(int RHS) {
        if(RHS < this.getStashSize()) {
            this.stashIndex = RHS;
        }
    }
    
    public int getStashSize() {
        return this.stash.size();
    }
    
    public RxPrescriptionData.Prescription[] getStash() {
        RxPrescriptionData.Prescription[] arr = {};
        
        arr = (RxPrescriptionData.Prescription[])stash.toArray(arr);
        
        return arr;
    }
    
    public RxPrescriptionData.Prescription getStashItem(int index) {
        return (RxPrescriptionData.Prescription)stash.get(index);
    }
    
    public void setStashItem(int index, RxPrescriptionData.Prescription item) {
        //this.clearDAM();
        //this.clearDDI();
        stash.set(index, item);
    }
    
    public int addStashItem(RxPrescriptionData.Prescription item) {
        //this.clearDDI();
        //this.clearDAM();
        int ret = -1;
        
        int i;
        RxPrescriptionData.Prescription rx;
        
        //check to see if the item already exists
        //by checking for duplicate brandname and gcn seq no
        //if it exists, return it, else add it.
        for(i=0;i<this.getStashSize(); i++) {
            rx = this.getStashItem(i);
            
            if(item.isCustom()) {
                if(rx.isCustom() && rx.getCustomName() !=null && item.getCustomName() != null) {
                    if(rx.getCustomName().equals(item.getCustomName())) {
                        ret = i;
                        break;
                    }
                }
            }
            else {
                if(rx.getBrandName()!=null && item.getBrandName() !=null) {
                    if(rx.getBrandName().equals(item.getBrandName())
                    && rx.getGCN_SEQNO()==item.getGCN_SEQNO()) {
                        ret = i;
                        break;
                    }
                }
            }
        }
        
        if(ret>-1) {
            return ret;
        }
        else {
            stash.add(item);
            return this.getStashSize()-1;
        }
    }
    
    public void removeStashItem(int index) {
    //    this.clearDDI();
    //    this.clearDAM();
        stash.remove(index);
    }
    
    public void clearStash() {
    //    this.clearDDI();
    //    this.clearDAM();
        stash = new ArrayList();
    }
    
    
    //--------------------------------------------------------------------------
    
    public boolean isValid() {
        if(this.demographicNo > 0
        && this.providerNo != null
        && this.providerNo.length() > 0) {
            return true;
        }
        return false;
    }
}