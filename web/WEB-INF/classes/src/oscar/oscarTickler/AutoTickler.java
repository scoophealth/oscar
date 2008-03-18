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
 * Ontario, Canada   Creates a new instance of AutoTickler
 *
 *
 * AutoTickler.java
 *
 * Created on July 20, 2005, 1:30 PM
 */

package oscar.oscarTickler;

import java.util.ArrayList;

/**
 *
 * @author Jay Gallagher
 */
      
public class AutoTickler {
 
   static AutoTickler autoTickler = new AutoTickler();
   ArrayList ticklerList = new ArrayList();      
   
   private AutoTickler() {
      //System.out.println("CALL FOR TICKLERS CALLED");
   }
               
   public static AutoTickler getInstance() {         
      //System.out.println("CALL FOR AUTOTICKLERs");
      return autoTickler;
   }
   
   public void addTickler(){
       
   }
   
   public void callForTicklers(String providerNo){
      //This should run through the autoTicklers array and launch threads for each.
      
   //System.out.println("CALL FOR TICKLERS CALLED");
   TicklerWorker tw = new TicklerWorker();
   tw.provider = providerNo;   
   tw.status = TicklerData.ACTIVE;
   tw.priority = TicklerData.NORMAL;
   tw.run();
   }
}
