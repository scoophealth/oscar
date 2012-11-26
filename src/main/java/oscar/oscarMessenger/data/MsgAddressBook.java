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


package oscar.oscarMessenger.data;

import java.util.List;

import javax.servlet.jsp.JspWriter;

import org.oscarehr.common.dao.OscarCommLocationsDao;
import org.oscarehr.common.model.OscarCommLocations;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */



public class MsgAddressBook {
   static int num;
   public java.util.Vector providerList;
   public java.util.Vector remoteaddrBook;
   public java.util.Vector remoteLocationDesc;
   public java.util.Vector remoteLocationId;
   public String CurrentLocationName;

   /**
    * Constructor for the address data class
    */
  public MsgAddressBook(){
      providerList = new java.util.Vector();
      remoteLocationDesc = new java.util.Vector();
      remoteLocationId = new java.util.Vector();
  }//---------------------------------------------------------------------------

    /**
     * This function gets the xml document from the database.
     * It also sets the current location data member string for this location.
     * @return String xml string for this location
     */
   public String myAddressBook(){
      CurrentLocationName = "";
      
      OscarCommLocationsDao dao = SpringUtils.getBean(OscarCommLocationsDao.class); 
      List<OscarCommLocations> comms = dao.findByCurrent1(1);
      
      for(OscarCommLocations comm : comms) {
    	  CurrentLocationName = comm.getLocationDesc();
          return comm.getAddressBook();
      }
      
      return "";
   }
   
   //---------------------------------------------------------------------------

   /**
    * This funtion create 3 vectors with the xml address books, location ids and descriptions of the ids
    * @return Vector the address book vector is returned.
    */
   public java.util.Vector remoteAddressBooks(){
      java.util.Vector vector = new java.util.Vector();
      remoteLocationDesc      = new java.util.Vector();
      remoteLocationId        = new java.util.Vector();

      OscarCommLocationsDao dao = SpringUtils.getBean(OscarCommLocationsDao.class); 
      List<OscarCommLocations> comms = dao.findByCurrent1(1);
      
      for(OscarCommLocations comm : comms) {
    	  vector.add(comm.getAddressBook());
          remoteLocationDesc.add(comm.getLocationDesc());
          remoteLocationId.add("" + comm.getId());
      }
      return vector;
   }
   //---------------------------------------------------------------------------


   /**
    *
    * @param node
    * @param out
    * @param depth
    * @param thePros
    */
   public void displayNodes(Node node,JspWriter out,int depth,String[] thePros){
      depth++;

      Element element = (Element) node;

      try{
         if (depth > 2){
            if ((element.getTagName()).equals("group")){
               out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" border=0 >\n");
            }
            else{
               if (node.getPreviousSibling() == null){
                  out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" cellspacing=0  >\n");
               }
            }
         }else{
            if (depth == 1){
            out.print("<table id=\"tblDFR"+depth+"\" cellpadding=\"0\" border=0>\n");
            }else{
            out.print("<table id=\"tblDFR"+depth+"\" class=\"groupIndent\" cellpadding=\"0\" border=0>\n");
            }
         }
         out.print("   <tr> \n");
         out.print("      <td> \n");

         if ((element.getTagName()).equals("group")){
            out.print("<span class=\"treeNode\" onclick=\"javascript:showTbl('tblDFR"+(depth+1)+"');\">");

            if (depth < 2){
               out.print("<img class=\"treeNode\" src=\"img/minusblue.gif\" border=\"0\" />");
            }else{
               out.print("<img class=\"treeNode\" src=\"img/plusblue.gif\" border=\"0\" />");
            }
            out.print("</span>");
            if (depth == 1){
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"');\"><font color=#0c7bd6><b>"+CurrentLocationName+"</b></font><br>");
            }else{
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"');\"><font color=#0c7bd6><b>"+element.getAttribute("desc")+"</b></font><br>");
            }

         }else{
               if ( java.util.Arrays.binarySearch(thePros,element.getAttribute("id")) < 0 ){
                  out.print("<input type=\"checkbox\" name=providerNos value="+element.getAttribute("id")+"  > <font color=#0e8ef7>"+element.getAttribute("desc")+"</font>\n");
               }else{
                  out.print("<input type=\"checkbox\" name=providerNos value="+element.getAttribute("id")+" checked > "+element.getAttribute("desc")+"\n");
               }
         }
         if (node.hasChildNodes()){
            NodeList nlst = node.getChildNodes();
            for (int i = 0; i < nlst.getLength(); i++){
               displayNodes(nlst.item(i), out,depth,thePros);
            }
         }
         out.print("</td>\n");
         out.print("</tr>\n");
         if ((element.getTagName()).equals("group") && !node.hasChildNodes()){
            out.print("</table id="+depth+">\n");
         }else{
            if (node.getNextSibling() == null){
               out.print("</table id="+depth+">\n");
               if (depth == 2)
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"   onclick=\"javascript:showTbl('tblDFR"+(depth)+"');\" src=\"img/collapse.gif\" border=\"0\" />");
               else
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"  style=\"display:none\" onclick=\"javascript:showTbl('tblDFR"+(depth)+"');\" src=\"img/collapse.gif\" border=\"0\" />");

            }
         }

       }catch(Exception e){MiscUtils.getLogger().error("Error", e); }
   }
//------------------------------------------------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////////////
   public void displayRemoteNodes(Node node,JspWriter out,int depth,String[] thePros,int remoId, java.util.Vector locationVector){
      depth++;

      Element element = (Element) node;

      try{
         if (depth > 2){
            if ((element.getTagName()).equals("group")){
               out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" border=0 >\n");
            }
            else{
               if (node.getPreviousSibling() == null){
                  out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" cellspacing=0  >\n");
               }
            }
         }else{
            out.print("<table id=\"tblDFR"+depth+"\"  cellpadding=\"0\" border=0>\n");
         }
         out.print("   <tr> \n");
         out.print("      <td> \n");

         if ((element.getTagName()).equals("group")){
            out.print("<span class=\"treeNode\" onclick=\"javascript:showTbl('tblDFR"+(depth+1)+"');\">");

            if (depth < 2){
               out.print("<img class=\"treeNode\" src=\"img/minusblue.gif\" border=\"0\" />");
            }else{
               out.print("<img class=\"treeNode\" src=\"img/plusblue.gif\" border=\"0\" />");
            }
            out.print("</span>");
            if (depth == 1){
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"');\"><font color=#ff9452><b>"+((String)remoteLocationDesc.elementAt(remoId))+"</b></font><br>");
            }else{
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"');\"><font color=#ff9452><b>"+element.getAttribute("desc")+"</b></font><br>");
            }

         }else{
               MiscUtils.getLogger().debug("Im here");
               int binSearch = java.util.Arrays.binarySearch(thePros,element.getAttribute("id")) ;
               MiscUtils.getLogger().debug("the binsearch returned "+binSearch+" there are "+locationVector.size()+" in the locationVector ");
               if ( ( binSearch > 0 ) && ( ( (String) locationVector.elementAt(binSearch) ).equals( remoteLocationId.elementAt(remoId)  ) )){
               MiscUtils.getLogger().debug("i found it at = "+locationVector.elementAt(binSearch));
                  out.print("<input type=\"checkbox\" name=provider value="+element.getAttribute("id")+"@"+((String)remoteLocationId.elementAt(remoId))+" checked > "+element.getAttribute("desc")+"\n");
               }else{
                  out.print("<input type=\"checkbox\" name=provider value="+element.getAttribute("id")+"@"+((String)remoteLocationId.elementAt(remoId))+"  ><font color=#ff5900>"+element.getAttribute("desc")+"</font>\n");
               }
         }
         if (node.hasChildNodes()){
            NodeList nlst = node.getChildNodes();
            for (int i = 0; i < nlst.getLength(); i++){
               displayRemoteNodes(nlst.item(i), out,depth,thePros,remoId,locationVector);
            }
         }
         out.print("</td>\n");
         out.print("</tr>\n");
         if ((element.getTagName()).equals("group") && !node.hasChildNodes()){
            out.print("</table id="+depth+">\n");
         }else{
            if (node.getNextSibling() == null){
               out.print("</table id="+depth+">\n");
               if (depth == 2)
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"   onclick=\"javascript:showTbl('tblDFR"+(depth)+"');\" src=\"img/collapse.gif\" border=\"0\" />");
               else
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"  style=\"display:none\" onclick=\"javascript:showTbl('tblDFR"+(depth)+"');\" src=\"img/collapse.gif\" border=\"0\" />");

            }
         }

       }catch(Exception e){MiscUtils.getLogger().error("Error", e); }
   }
//------------------------------------------------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////////////
   public void displayRemoteNodes2(Node node,JspWriter out,int depth,oscar.oscarMessenger.data.MsgReplyMessageData reData,int remoId, java.util.Vector locationVector){
      depth++;

      Element element = (Element) node;

      try{
         if (depth > 2){
            if ((element.getTagName()).equals("group")){
               out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" border=0 >\n");
            }
            else{
               if (node.getPreviousSibling() == null){
                  out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" cellspacing=0  >\n");
               }
            }
         }else{
            out.print("<table id=\"tblDFR"+depth+"\"  cellpadding=\"0\" border=0>\n");
         }
         out.print("   <tr> \n");
         out.print("      <td> \n");

         if ((element.getTagName()).equals("group")){
            out.print("<span class=\"treeNode\" onclick=\"javascript:showTbl('tblDFR"+(depth+1)+"',event);\">");

            if (depth < 2){
               out.print("<img class=\"treeNode\" src=\"img/minusblue.gif\" border=\"0\" />");
            }else{
               out.print("<img class=\"treeNode\" src=\"img/plusblue.gif\" border=\"0\" />");
            }
            out.print("</span>");
            if (depth == 1){
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"',event);\"><font color=#ff9452><b>"+((String)remoteLocationDesc.elementAt(remoId))+"</b></font><br>");
            }else{
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"',event);\"><font color=#ff9452><b>"+element.getAttribute("desc")+"</b></font><br>");
            }

         }else{

               //int binSearch = java.util.Arrays.binarySearch(thePros,element.getAttribute("id")) ;

               //if ( ( binSearch > 0 ) && ( ( (String) locationVector.elementAt(binSearch) ).equals( (String) remoteLocationId.elementAt(remoId)  ) )){

               if (reData.remoContains(element.getAttribute("id"), (String) remoteLocationId.elementAt(remoId)) ){
                  out.print("<input type=\"checkbox\" name=provider value="+element.getAttribute("id")+"@"+((String)remoteLocationId.elementAt(remoId))+" checked > "+element.getAttribute("desc")+"\n");
               }else{
                  out.print("<input type=\"checkbox\" name=provider value="+element.getAttribute("id")+"@"+((String)remoteLocationId.elementAt(remoId))+"  ><font color=#ff5900>"+element.getAttribute("desc")+"</font>\n");
               }
         }
         if (node.hasChildNodes()){
            NodeList nlst = node.getChildNodes();
            for (int i = 0; i < nlst.getLength(); i++){
               displayRemoteNodes2(nlst.item(i), out,depth,reData,remoId,locationVector);
            }
         }
         out.print("</td>\n");
         out.print("</tr>\n");
         if ((element.getTagName()).equals("group") && !node.hasChildNodes()){
            out.print("</table id="+depth+">\n");
         }else{
            if (node.getNextSibling() == null){
               out.print("</table id="+depth+">\n");
               if (depth == 2)
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"   onclick=\"javascript:showTbl('tblDFR"+(depth)+"',event);\" src=\"img/collapse.gif\" border=\"0\" />");
               else
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"  style=\"display:none\" onclick=\"javascript:showTbl('tblDFR"+(depth)+"',event);\" src=\"img/collapse.gif\" border=\"0\" />");

            }
         }

       }catch(Exception e){MiscUtils.getLogger().error("Error", e); }
   }
//------------------------------------------------------------------------------------------------------------


}
