<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,org.oscarehr.util.OntarioMD,java.util.Hashtable,org.apache.commons.collections.*"%>




<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ontario MD Disease</title>
        <script type="text/javascript">

            function popupDrugOfChoice(vheight,vwidth,varpage) { //open a new popup window
                var page = varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
                var popup=window.open(varpage, "oscarDoc", windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                        popup.opener = self;
                    }
                    popup.focus();
                }
            }

            function goDOC(){
                if (document.RxSearchDrugForm.searchString.value.length == 0){
                    popupDrugOfChoice(720,700,'http://doc.oscartools.org/')
                }else{
                    //var docURL = "http://resource.oscarmcmaster.org/oscarResource/DoC/OSCAR_search/OSCAR_search_results?title="+document.RxSearchDrugForm.searchString.value+"&SUBMIT=GO";
                    var docURL = "http://doc.oscartools.org/search?SearchableText="+document.RxSearchDrugForm.searchString.value;          
                    popupDrugOfChoice(720,700,docURL);                               
                }
            }    
                
                
            function goOMD(params){
                var docURL = "../common/OntarioMDRedirect.jsp?keyword="+params;          
                popupDrugOfChoice(743,817,docURL);                               
            }           
        </script>       
    </head>
    <body>
        <ul>
            <%OrderedMap map = new OntarioMD().getDiseaseList();
            OrderedMapIterator iter = map.orderedMapIterator();

            while (iter.hasNext()) {
                String key = (String) iter.next();
                String val = (String) iter.getValue();
            %>   
             <li>
                 <a href="javascript:void(0)" onclick="goOMD('<%=key%>')"><%=val%></a>
             </li>
            
            <%}%>
        </ul>
    </body>
</html>
