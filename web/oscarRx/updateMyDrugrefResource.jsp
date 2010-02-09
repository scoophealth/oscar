<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.RxPrescriptionData" %>
<%@page import="oscar.oscarRx.util.*" %>
<%
         Hashtable hiddenResources = (Hashtable) request.getSession().getAttribute("hideResources");
         if(hiddenResources!=null){
         int numHidden=hiddenResources.size();
         if(numHidden>0){
             %>  <script type="text/javascript">
                    if(showOrHide==1)$('showHideWord').update('hide');
                    else $('showHideWord').update('show');
             </script>
<div id="">
    <a href="javascript:void(0);" style="font-size: 9pt;" onclick="showOrHideRes('<%=hiddenResources%>');"> <span id="showHideWord">show</span> <span id="showHideNumber"><%=numHidden%></span> hidden resources</a>
</div>
            <%                      }
         }
         %>
