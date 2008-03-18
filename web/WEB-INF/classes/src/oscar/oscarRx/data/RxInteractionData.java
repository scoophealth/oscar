/**
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
 * Jason Gallagher
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of RxInteractionData
 *
 *
 * RxInteractionData.java
 *
 * Created on May 5, 2005, 6:30 PM
 */

package oscar.oscarRx.data;

import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Jay Gallagher
 */
public class RxInteractionData {
   static RxInteractionData rxInteractionData= new RxInteractionData();
   static Hashtable  htable = new Hashtable();
   public static RxInteractionData getInstance() {
      return rxInteractionData;
   }
	
   static Hashtable working = new Hashtable();
	
   
   private RxInteractionData() {
   }
   
   public void preloadInteraction(Vector atccodes){
      //launch thread that searches database for them
      System.out.println("PRELOADING"+atccodes.hashCode());
      if (!htable.containsKey(new Integer(atccodes.hashCode())) ){
         RxInteractionWorker worker = new RxInteractionWorker(rxInteractionData,atccodes);
         worker.start();
         addToWorking(atccodes,worker);
      }            
   }
   
   public void addToHash(Vector atccodes,RxDrugData.Interaction[] interact ){
      htable.put(new Integer(atccodes.hashCode()), interact);
   }
   
   public void addToWorking(Vector atccodes,RxInteractionWorker worker){
      working.put(new Integer(atccodes.hashCode()), worker);
   }
   public void removeFromWorking(Vector atccodes){
      working.remove(new Integer(atccodes.hashCode()));
   }
   
   public RxDrugData.Interaction[] getInteractions(Vector atccodes){
      RxDrugData.Interaction[] interact = null;
      System.out.println("h table size "+htable.size()+"RxInteractionData.getInteraction atc code val  "+atccodes.hashCode());
      Integer i = new Integer(atccodes.hashCode());
      if (htable.containsKey(i) ){
         System.out.println("Already been searched!");
         interact = (RxDrugData.Interaction[]) htable.get(i);
      }else if(working.contains(i) ){
         System.out.println("Already been searched but not finished !");
         RxInteractionWorker worker = (RxInteractionWorker) working.get(i);
         if (worker != null){
             try {
                worker.join();
                System.out.println("Already been searched now finished!");
                // Finished
             } catch (InterruptedException e) {
                // Thread was interrupted
                System.out.println("Already been searched PROBLEM!");
                e.printStackTrace();
             }
            
             
         }
         interact = (RxDrugData.Interaction[]) htable.get(i);
      
      }else{
         System.out.println("NEW ATC CODES");
         try{        
            RxDrugData drugData = new RxDrugData();
            interact = drugData.getInteractions(atccodes);
            if (interact != null){
               addToHash(atccodes,interact);
            }
         }catch(Exception e){
             e.printStackTrace();
         }         
      }
      return interact;
   }
   
   
}
