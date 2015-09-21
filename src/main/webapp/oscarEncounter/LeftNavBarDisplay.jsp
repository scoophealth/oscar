<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page
	import="oscar.oscarEncounter.pageUtil.NavBarDisplayDAO, oscar.util.*, java.util.ArrayList, java.util.Date, java.util.Calendar, java.io.IOException"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="com.quatro.dao.security.SecobjprivilegeDao" %>
<%@ page import="com.quatro.model.security.Secobjprivilege" %>
<%@ page import="java.util.List, java.util.regex.Pattern, java.util.regex.Matcher" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%
        long startTime = System.currentTimeMillis();
        NavBarDisplayDAO dao = (NavBarDisplayDAO)request.getAttribute("DAO");
        String js = dao.getJavaScript();
        int maxColumnHeight = 40;  //break into columns after maxColumnHeight items reached
        int menuWidth = 125;


        //Is there java script to insert in page?  Then do it
        if( js != null ) {
        %>
<%=js%>
<% } %>
<input type=hidden name="reloadUrl" value="<%=dao.getReloadUrl()%>"/>
<%
        //Do we have a '+' command to display on the right of the module header?
        String rh = dao.getRightHeadingID();
		String rhid = dao.getRightHeadingID();
		com.quatro.service.security.SecurityManager securityMgr = new com.quatro.service.security.SecurityManager();

		if( !rh.equals("") && securityMgr.hasWriteAccess("_" + ((String)request.getAttribute("cmd")).toLowerCase(),roleName$)) {
        %>
<div id='menuTitle<%=rh%>'
	style="width: 10%; float: right; text-align: center;">
<h3 style="padding:0px; <%=getBackgroundColor(dao)%>"><a href="javascript:void(0);"
	<%=dao.numPopUpMenuItems() > 0 ? "onmouseover" : "onclick"%>="<%=dao.getRightURL()%>">+</a></h3>
</div>
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
<div id='menu<%=rh%>' class='menu' style='width: <%=menuWidth%>;px'
	onclick='event.cancelBubble = true;'>
<h3 style='text-align: center'><%=dao.getMenuHeader()%></h3>
<%
            for(int idx = 0; idx < num; ++idx) {
            if( columns )
            style = idx % 2 == 0 ? "menuItemleft" : "menuItemright";
            else
            style = "menuItemleft";
            %> <a href="#" class="<%=style%>"
	onmouseover='this.style.color="black"'
	onmouseout='this.style.color="white"'
	onclick="<%=dao.getPopUpUrl(idx)%>; return false;"><%=dao.getPopUpText(idx)%></a>
<%
            if( columns && idx % 2 == 1) {
            %> <br>
<%
            }
            else if( !columns ){
            %> <br>
<%
            }
            } //end for
            %>
</div>
<%
        } //end if menu items

        } //end if there is a right hand header
        else {
        	if(!rh.equals("")) {
        	 %>
             <div id='menuTitle<%=rh%>' style="width: 10%; float: right; text-align: center;">
      			<h3 style="padding:0px; <%=getBackgroundColor(dao)%>">&nbsp;</h3>
      	   </div>
             <%
        } }

        //left hand module header comes last as it's displayed as a block
        %>
<div style="clear: left; float: left; width: 90%;">
<h3 style="width:100%; <%=getBackgroundColor(dao)%>"><a href="#"
	onclick="<%=dao.getLeftURL()%>; return false;"><%=dao.getLeftHeading()%></a></h3>
</div>

<ul id="<%=request.getAttribute("navbarName")%>list">
	<%
            //now we display the actual items of the module
            String manageItems = "";
            String div = (String)request.getAttribute("navbarName");
            div = div.trim();
            int numItems = dao.numItems();
            StringBuilder reloadURL = new StringBuilder(request.getParameter("reloadURL") + "&reloadURL=" + request.getParameter("reloadURL"));
            String strToDisplay = request.getParameter("numToDisplay");
            int numToDisplay;
            boolean xpanded = false;
            int displayThreshold = 6;

            if( strToDisplay != null ) {
                numToDisplay = Integer.parseInt(strToDisplay);
				reloadURL.append("&numToDisplay=" + strToDisplay);
                if( numItems > numToDisplay ) {
                    String xpandUrl = request.getParameter("reloadURL") + "&reloadURL=" + request.getParameter("reloadURL") + "&cmd=" + div;
                    manageItems = xpandUrl;
                }
            }
            else {
                numToDisplay = numItems;
                if( numToDisplay > displayThreshold ) {
                    xpanded = true;
                }
            }
			reloadURL.append("&cmd=" + div);
            int numDisplayed = 0;

            ArrayList<NavBarDisplayDAO.Item> current = new ArrayList<NavBarDisplayDAO.Item>();
            ArrayList<NavBarDisplayDAO.Item> pastDates = new ArrayList<NavBarDisplayDAO.Item>();
            ArrayList<NavBarDisplayDAO.Item> noDates = new ArrayList<NavBarDisplayDAO.Item>();
            Calendar threshold = Calendar.getInstance();
            threshold.add(Calendar.MONTH, -3);
            Date threeMths = threshold.getTime();
            int j;

            for(j=0; j<numItems; j++) {
                NavBarDisplayDAO.Item item = dao.getItem(j);
                Date d = item.getDate();
                if( d == null )
                    noDates.add(item);
                else if( d.compareTo(threeMths) < 0 )
                    pastDates.add(item);
                else
                    current.add(item);
            }

            StringBuilder jscode = new StringBuilder();
			
            numDisplayed = display(noDates, numToDisplay, numDisplayed, manageItems, xpanded, numItems, jscode, displayThreshold, reloadURL.toString(), dao.getDivId(), request, out);

            if( numDisplayed < numToDisplay ){
               numDisplayed += display(current, numToDisplay, numDisplayed, manageItems, xpanded, numItems, jscode, displayThreshold, reloadURL.toString(), dao.getDivId(), request, out);
            }

            if( numDisplayed < numToDisplay ){
                numDisplayed += display(pastDates, numToDisplay, numDisplayed, manageItems, xpanded, numItems, jscode, displayThreshold, reloadURL.toString(), dao.getDivId(),request, out);
            }

            if( numDisplayed == 0 ) {
                out.println("<li>&nbsp;</li>");
            }
            %>
</ul>
<input type="hidden" id="<%=request.getAttribute("navbarName")%>num"
	value="<%=numDisplayed%>" />
<%
        out.println("<script type=\"text/javascript\">" + jscode.toString() + "</script>");
    %>

<%!
    public String getBackgroundColor(NavBarDisplayDAO dao){
        if ( dao.hasHeadingColour()){
           return  "background-color: #"+dao.getHeadingColour()+";";
        }
        return "";
    }

    public int display(ArrayList<NavBarDisplayDAO.Item>items, int numToDisplay, int numDisplayed, String reloadUrl, boolean xpanded, int numItems, StringBuilder js, int displayThreshold, String divReloadUrl, String cmd, javax.servlet.http.HttpServletRequest request, javax.servlet.jsp.JspWriter out ) throws IOException {
        String stripe,colour,bgColour;
        String imgName;
        String dateFormat = "dd-MMM-yyyy";
        Pattern pattern = Pattern.compile("'([^']*)'");        
        
        
        String divReloadInfo;
        numToDisplay -= numDisplayed;

        int total = items.size() < numToDisplay ? items.size() : numToDisplay;
        int j;
        int curNum = numDisplayed;
        for(j = 0 ; j< total; ++j ) {
                NavBarDisplayDAO.Item item = items.get(j);
                colour = item.getColour().equals("") ? "" : "color: " + item.getColour() + ";";
                bgColour = item.getBgColour().equals("") ? "background-color: #f3f3f3;" : "background-color: " + item.getBgColour() + ";";
                String dateColour = "background-color: white;";
                if ( (j % 2) == 0){
                   stripe = "style=\"overflow: hidden; clear:both; position:relative; display:block; white-space:nowrap; " + bgColour + "\"";
                   dateColour = bgColour;
                }else{
                   stripe = "style=\"overflow: hidden; clear:both; position:relative; display:block; white-space:nowrap; \"";
                }
                out.println("<li " + stripe + ">");

                if( curNum == 0 && xpanded ) {
                    imgName = "img" + request.getAttribute("navbarName") + curNum;
                    out.println("<a href='#' onclick=\"return false;\" style='text-decoration:none; width:7px; z-index: 100; "+dateColour+" position:relative; margin: 0px; padding-bottom: 0px;  vertical-align: bottom; display: inline; float: right; clear:both;'><img id='" + imgName + "' src='" + request.getContextPath() + "/oscarMessenger/img/collapse.gif'/>&nbsp;&nbsp;</a>");
                    js.append("imgfunc['" + imgName + "'] = clickListDisplay.bindAsEventListener(obj,'" + request.getAttribute("navbarName") + "', '" + displayThreshold + "');" );
                    js.append("Element.observe($('" + imgName + "'), 'click', imgfunc['" + imgName + "']);");
                }else if( j == (numToDisplay-1) && xpanded ) {
                    imgName = "img" + request.getAttribute("navbarName") + curNum;
                    out.println("<a href='#' onclick=\"return false;\" style='text-decoration:none; width:7px; z-index: 100; "+dateColour+" position:relative; margin: 0px; padding-bottom: 0px;  vertical-align: bottom; display: inline; float: right; clear:both;'><img id='" + imgName + "' src='" + request.getContextPath() + "/oscarMessenger/img/collapse.gif'/>&nbsp;&nbsp;</a>");
                    js.append("imgfunc['" + imgName + "'] = clickListDisplay.bindAsEventListener(obj,'" + request.getAttribute("navbarName") + "', '" + displayThreshold + "');" );
                    js.append("Element.observe($('" + imgName + "'), 'click', imgfunc['" + imgName + "']);");
                }else if( j == (numToDisplay-1) && numItems > (curNum+1) ) {
                    imgName = "img" + request.getAttribute("navbarName") + curNum;
                    out.println("<a href='#' onclick=\"return false;\" title='" + String.valueOf(numItems - j - 1) + " more items' style=' text-decoration:none; width:7px; z-index: 100; "+dateColour+" position:relative; margin: 0px; padding-bottom: 0px;  vertical-align: bottom; display: inline; float: right; clear:both;'><img id='" + imgName +  "' src='" + request.getContextPath() + "/oscarEncounter/graphics/expand.gif'/>&nbsp;&nbsp;</a>");
                    js.append("imgfunc['" + imgName + "'] = clickLoadDiv.bindAsEventListener(obj,'" + request.getAttribute("navbarName") + "','" + reloadUrl + "');" );
                    js.append("Element.observe($('" + imgName + "'), 'click', imgfunc['" + imgName + "']);");
                }else{
                    out.println("<a border=0 style='text-decoration:none; width:7px; z-index: 100; "+dateColour+" position:relative; margin: 0px; padding-bottom: 0px;  vertical-align: bottom; display: inline; float: right; clear:both;'><img  id='img" + request.getAttribute("navbarName") + curNum + "' src='" + request.getContextPath() + "/images/clear.gif'/>&nbsp;&nbsp;</a>");
                }
                ++curNum;

                out.println("<span style=\" z-index: 1; position:absolute; margin-right:10px; width:90%; overflow:hidden;  height:1.2em; white-space:nowrap; float:left; text-align:left; \">");
				String url = item.getURL();
				//This should be done in the display classes but I'll keep it here for future reference
				//url = StringUtils.replaceEach(url, new String[] {"'","\\\""}, new String[] {"\'","\\\""});
                if( item.isURLJavaScript() ) {
                    divReloadInfo = trackWindowString(url, divReloadUrl, cmd, pattern);
                	out.println("<a class='links' style='" + colour + "' onmouseover=\"this.className='linkhover'\" onmouseout=\"this.className='links'\" href='#' onclick=\"" + divReloadInfo + url + "\" title='" + item.getLinkTitle() + "'>");
                }
                else {
                	out.println("<a class='links' style='" + colour + "' onmouseover=\"this.className='linkhover'\" onmouseout=\"this.className='links'\" href=\"" + url + "\" title='" + item.getLinkTitle() + "' target=\"_blank\">");
                }
                out.println(item.getTitle());
                out.println("</a>");
                out.println("</span>");

                if( item.getDate() != null ) {
                    out.println("<span style=\"z-index: 100; "+dateColour+" overflow:hidden;   position:relative; height:1.2em; white-space:nowrap; float:right; text-align:right;\">");
										
                    if( item.isURLJavaScript() ) {
                		divReloadInfo = trackWindowString(url, divReloadUrl, cmd, pattern);
                    	out.println("...<a class='links' style='margin-right: 2px;" + colour + "' onmouseover=\"this.className='linkhover'\" onmouseout=\"this.className='links'\" href='#' onclick=\"" + divReloadInfo + url + "\" title='" + item.getLinkTitle() + "'>");
                    }
                    else {
                    	out.println("...<a class='links' style='margin-right: 2px;" + colour + "' onmouseover=\"this.className='linkhover'\" onmouseout=\"this.className='links'\" href=\"" + url + "\" title='" + item.getLinkTitle() + "' target=\"_blank\">");
                    }

                    if(item.getValue() != null && !item.getValue().trim().equals("")){
                        out.println(item.getValue());
                    }
                    out.println(DateUtils.getDate(item.getDate(), dateFormat, request.getLocale()));
                    out.println("</a>");
                    out.println("</span>");
                }
                out.println("</li>");
         }

         return j;
    }
    
    public String trackWindowString(String url, String reloadUrl, String cmd, Pattern pattern) {
		String windowName, divReloadInfo = "";
		if( url.startsWith("popupPage") ) {                		    
	    	Matcher matcher = pattern.matcher(url);
	    	if( matcher.find() ) {                				
	    		windowName = matcher.group(1);
	    		reloadUrl += "&numToDisplay=6&cmd=" + cmd;
	    		divReloadInfo = "reloadWindows['" + windowName + "'] = '" + reloadUrl + "';reloadWindows['"+ windowName + "div'] = '" + cmd + "';";                		    	
	    	}
	   
		}
		
		return divReloadInfo;
    }

    %>
