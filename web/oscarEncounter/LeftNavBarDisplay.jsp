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
        String stripe,colour;        
        for(j=0; j<dao.numItems(); j++) {
            NavBarDisplayDAO.Item item = dao.getItem(j);                       
            colour = item.getColour().equals("") ? "" : "color: " + item.getColour();
            if ( (j % 2) == 0){
                stripe = "style=\"display:block; " + colour + "\"";
            }
            else
                stripe = "style=\"display:block; " + colour + " background-color: #f3f3f3; \" ";
                       
      %>
      <li <%=stripe%>>
        <a class="links" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href="#" onclick="<%=item.getURL()%>" title="<%=item.getTitle()%>">
                               <%=StringUtils.maxLenString(item.getTitle(), 25, 22, "...")%>
        </a>
                        
      </li>
      <%
        } 
      %>
                        
    </ul>
    <input type="hidden" id="<%=request.getAttribute("navbarName")%>num" value="<%=j%>" />
    <%-- 
        if( j > 5 ) {
    %>
        <div><a href="#" onclick="listDisplay('<%=request.getAttribute("navbarName")%>'); return false;">Show</a></div>
    <%
        }
                
    --%>

    <% System.out.println("LeftNavBar " + request.getAttribute("navbarName") + "load time: " + (System.currentTimeMillis()-startTime) + "ms"); %>