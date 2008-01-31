<%@page contentType="text/html"%>
<%-- <%@page pageEncoding="UTF-8"%> --%>
<%@page import="oscar.oscarEncounter.pageUtil.NavBarDisplayDAO, oscar.util.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

        <%
        long startTime = System.currentTimeMillis();
        NavBarDisplayDAO dao = (NavBarDisplayDAO)request.getAttribute("DAO");
        String js = dao.getJavaScript();    
        int maxColumnHeight = 40;  //break into columns after maxColumnHeight items reached
        int menuWidth = 125;
        String dateFormat = "dd-MMM-yyyy";
        
        //Is there java script to insert in page?  Then do it
        if( js != null ) {            
        %>
        <%=js%>
        <% }
        //Do we have a '+' command to display on the right of the module header?
        String rh = dao.getRightHeadingID();
        if( !rh.equals("") ) {          
        %>        
        <div id='menuTitle<%=rh%>' style="width:10%; float:right; text-align:center;"><h3 style="padding:0px; <%=getBackgroundColor(dao)%>" ><a href="#" <%=dao.numPopUpMenuItems() > 0 ? "onmouseover" : "onclick"%>="<%=dao.getRightURL()%>">+</a></h3></div>
        <%        
        int num;
        //if there is a pop up menu then grab all of the items and format according to number
        if( (num = dao.numPopUpMenuItems()) > 0 ) {
        boolean columns = false;
        String style;
        String width;
        if( num > maxColumnHeight) {
        columns = true;
        menuWidth *= 2;
        }
        %>
        <div id=menu<%=rh%> class='menu' style='width: <%=menuWidth%>;px' onclick='event.cancelBubble = true;'>
            <h3 style='text-align:center'><%=dao.getMenuHeader()%></h3>
            <%            
            for(int idx = 0; idx < num; ++idx) {
            if( columns )
            style = idx % 2 == 0 ? "menuItemleft" : "menuItemright";
            else
            style = "menuItemleft";
            %>
            <a href="#" class="<%=style%>" onmouseover='this.style.color="black"' onmouseout='this.style.color="white"' onclick="<%=dao.getPopUpUrl(idx)%>; return false;"><%=dao.getPopUpText(idx)%></a>
            <%
            if( columns && idx % 2 == 1) {
            %>
            <br>
            <%
            }
            else if( !columns ){
            %>
            <br>
            <%                    
            }
            } //end for
            %>
        </div>
        <%            
        } //end if menu items
        } //end if there is a right hand header               
        
        
        //left hand module header comes last as it's displayed as a block
        %>
        <div style="clear:left; float:left; width:90%;"><h3 style="width:100%; <%=getBackgroundColor(dao)%>"><a href="#" onclick="<%=dao.getLeftURL()%>; return false;"><%=dao.getLeftHeading()%></a></h3></div>
               
            <ul id="<%=request.getAttribute("navbarName")%>list">
                <%
                //now we display the actual items of the module
                boolean expand = dao.numItems() > 5;
                int numToDisplay = 4; //0 based so 5 items in this case
                int j;
                String stripe,colour,bgColour;        
                boolean showEllipsis;
                for(j=0; j<dao.numItems(); j++) {
                NavBarDisplayDAO.Item item = dao.getItem(j);                       
                colour = item.getColour().equals("") ? "" : "color: " + item.getColour() + ";";
                bgColour = item.getBgColour().equals("") ? "background-color: #f3f3f3;" : "background-color: " + item.getBgColour() + ";";
                if ( (j % 2) == 0){
                stripe = "style=\"clear:both; display:block; white-space:nowrap; " + bgColour + "\"";
                }
                else
                stripe = "style=\"clear:both; display:block; white-space:nowrap; \"";
                
                showEllipsis = false;
                %>
                <li <%=stripe%>>

                        <%--     
                        if( expand && j == 0 ) {
                        %>
                        <a style="width: 2%; margin: 0px; padding-bottom: 0px; padding-right: 10px; vertical-align: bottom; display: inline; float: right;" href="#" onclick="listDisplay('<%=request.getAttribute("navbarName")%>'); return false;"><img id="<%=request.getAttribute("navbarName")%>topimg" src="<c:out value="${ctx}"/>/oscarMessenger/img/collapse.gif"/></a>
                        <%
                        }
                        else if( expand && j == numToDisplay ) {
                        %>
                        <a style="width: 2%; margin: 0px; padding-bottom: 0px; padding-right: 10px; vertical-align: bottom; display: inline; float: right; text-decoration:none;" href="#" onclick="listDisplay('<%=request.getAttribute("navbarName")%>'); return false;" title="<%=dao.numItems()-numToDisplay-1 + " more items"%>">
                            <img id="<%=request.getAttribute("navbarName")%>midimg" src="<c:out value="${ctx}"/>/oscarEncounter/graphics/expand.gif"/>                            
                        </a>
                        <%
                        }
                        
                        else if( j == dao.numItems()-1 && j > numToDisplay ) {
                        %>           
                        <a style="width: 2%; margin: 0px; padding-bottom: 0px; padding-right: 10px; vertical-align: bottom; display: inline; float: right;" href="#" onclick="listDisplay('<%=request.getAttribute("navbarName")%>'); return false;"><img src="<c:out value="${ctx}"/>/oscarMessenger/img/collapse.gif"/></a>
                        <%
                        }    
                        else {
                        --%>
                        <a style="width: 2%; margin: 0px; padding-bottom: 0px; padding-right: 10px; vertical-align: bottom; display: inline; float: right; clear:right;"><img id="img<%=request.getAttribute("navbarName")%><%=j%>" src="<c:out value="${ctx}"/>/images/clear.gif"/>&nbsp;&nbsp;</a>
                        <%--      
                        }
                        --%>
                        <span style="overflow:hidden; width:36%; height:1.2em; white-space:nowrap; float:right; text-align:right;">
                        <% if( item.getDate() != null ) { 
                                showEllipsis = true;
                        %>
                            ...<a class="links" style="<%=colour%>" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href="#" onclick="<%=item.getURL()%>" title="<%=item.getLinkTitle()%>">
                                <%=DateUtils.getDate(item.getDate(), dateFormat)%>
                            </a>
                        <% }
                           else {
                        %>
                            &nbsp;
                        <%   } 
                        %>
                        </span>                        
                        <span style="overflow:hidden; width:55%; height:1.2em; white-space:nowrap; float:right; text-align:left;">
                            <a class="links" style="<%=colour%>" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href="#" onclick="<%=item.getURL()%>" title="<%=item.getLinkTitle()%>">
                                <%=item.getTitle()%>
                            </a>     
                        </span>
                </li>
                <%
                } 
                %>
                
            </ul>   
        <input type="hidden" id="<%=request.getAttribute("navbarName")%>num" value="<%=j%>" />
    <%   
        System.out.println("LeftNavBar " + request.getAttribute("navbarName") + "load time: " + (System.currentTimeMillis()-startTime) + "ms");
    %>
    
    <%!
    public String getBackgroundColor(NavBarDisplayDAO dao){
        if ( dao.hasHeadingColour()){
           return  "background-color: #"+dao.getHeadingColour()+";"; 
        }
        return "";
    }
    
    
    %>
