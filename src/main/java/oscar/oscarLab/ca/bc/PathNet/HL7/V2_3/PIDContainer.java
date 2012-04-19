/**
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
 * This software was written for
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */


package oscar.oscarLab.ca.bc.PathNet.HL7.V2_3;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.bc.PathNet.HL7.Node;
/*
 * @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class PIDContainer extends Node {
    private static Logger logger=MiscUtils.getLogger(); 

    private ORC orc;   
   private OBR obr;
   
   public PIDContainer() {
      this.obr = null;
      this.orc = null;
   }
   //If line starts with ORC then the ORC parse object is called
   //If the line starts with OBR then the OBR parse object is called
   //For any other line as long as this.PIDContainer contains an OBR object will be parsed by the OBR parse method
   public Node Parse(String line) {
      if(line.startsWith("ORC")) {
         this.orc = new ORC();
         return this.orc.Parse(line);
      } else if(line.startsWith("OBR")) {
         this.obr = new OBR();
         return this.obr.Parse(line);
      } else if(this.obr != null) {
         return this.obr.Parse(line);
      }
      logger.error("Error During Parsing, Unknown Line - oscar.PathNet.HL7.V2_3.PIDContainer - Message: " + line);
      return null;
   }
   
   
   //This method calls the ORC.ToDatabase if the orc object is not null
   //Then calls the obr.ToDatabase method
   public int ToDatabase(int parent)throws SQLException {
      if(this.orc != null) {
         this.orc.ToDatabase(parent);
      }
      this.obr.ToDatabase(parent);
      return 0;
   }
   
   public boolean HasOBR() {
      return (this.obr != null);
   }
   
   protected String getInsertSql(int parent) {
      return "";
   }
   
   protected String[] getProperties() {
      return new String[0];
   }
   
}
