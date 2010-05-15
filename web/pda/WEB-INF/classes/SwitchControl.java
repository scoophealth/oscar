package bean;

/*
 * $RCSfile: AbstractApplication.java,v1.0 $
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * Tom Zhu
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

// this class is for setting all the switch variables 

public class SwitchControl {
     private int main_switch = 0 ;
     private int AppointmentToday_switch = 0;
     
  public SwitchControl() {
  }

  public void set_main_switch(int i){
       main_switch = i ;
  }
  public int get_main_switch(){
       return main_switch ;
  }

  public void set_AppointmentToday_switch(int i){
       AppointmentToday_switch = i ;
  }
  public int get_AppointmentToday_switch(){
       return AppointmentToday_switch ;
  }

}