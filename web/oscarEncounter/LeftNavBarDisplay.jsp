<%@page contentType="text/html"%>
<%-- <%@page pageEncoding="UTF-8"%> --%>
<%@page import="oscar.oscarEncounter.pageUtil.NavBarDisplayDAO, oscar.util.*"%>

<%
    long startTime = System.currentTimeMillis();
    NavBarDisplayDAO dao = (NavBarDisplayDAO)request.getAttribute("DAO");
    String js = dao.getJavaScript();
    
    if( js != null ) {            
%>
    <%=js%>
<% }
    
%>
<%=dao.getRightHeading()%>   
<%=dao.getLeftHeading()%>

<ul id="<%=request.getAttribute("navbarName")%>list" style="clear:both;">
    <%
        int j;
        String stripe,colour,bgColour;        
        for(j=0; j<dao.numItems(); j++) {
            NavBarDisplayDAO.Item item = dao.getItem(j);                       
            colour = item.getColour().equals("") ? "" : "color: " + item.getColour() + ";";
            bgColour = item.getBgColour().equals("") ? "background-color: #f3f3f3;" : "background-color: " + item.getBgColour() + ";";
            if ( (j % 2) == 0){
                stripe = "style=\"display:block; " + bgColour + "\"";
            }
            else
                stripe = "style=\"display:block;\"";
                       
      %>
      <li <%=stripe%>>
     <%
            if( j == 4 && dao.numItems() > 5 ) {
     %>    
                <a id="<%=request.getAttribute("navbarName")%>down" style="width: 2%; margin: 0px; padding: 0px; display: inline; float: right;" href="#" onclick="listDisplay('<%=request.getAttribute("navbarName")%>'); return false;" title="<%=dao.numItems()-5 + " more items"%>"><img src="graphics/triangle_blue_down.gif"/></a>
     <%
            }
            else if( j == dao.numItems()-1 && j > 4 ) {
     %>           
                <a id="<%=request.getAttribute("navbarName")%>up" style="width: 2%; margin: 0px; padding: 0px; display: inline; float: right;" href="#" onclick="listDisplay('<%=request.getAttribute("navbarName")%>'); return false;"><img src="graphics/triangle_blue_up.gif"/></a>
     <%
            }
     %>
        <a class="links" style="<%=colour%>" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href="#" onclick="<%=item.getURL()%>" title="<%=item.getTitle()%>">
                               <%=StringUtils.maxLenString(item.getTitle(), 28, 25, "...")%>
        </a>     
                        
      </li>
      <%
        } 
      %>
                        
    </ul>
    <input type="hidden" id="<%=request.getAttribute("navbarName")%>num" value="<%=j%>" />
    <%   
        System.out.println("LeftNavBar " + request.getAttribute("navbarName") + "load time: " + (System.currentTimeMillis()-startTime) + "ms");
    %>