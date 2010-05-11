<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
<%@page import="java.util.*" %>
<%@page import="oscar.oscarRx.data.RxPrescriptionData" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%
    oscar.oscarRx.pageUtil.RxSessionBean bean2 =(oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
    RxPrescriptionData.Prescription [] allRxInStash=bean2.getStash();
    List allRandomIdInStash=new ArrayList() ;
    for(RxPrescriptionData.Prescription rx:allRxInStash){
        allRandomIdInStash.add(rx.getRandomId());
    }
    String interactingDrugList=bean2.getInteractingDrugList();


%>

<script type="text/javascript">

    var errorMsg="<bean:message key='oscarRx.MyDrugref.InteractingDrugs.error.msgFailed' />" ;
    //oscarLog("errorMsg="+errorMsg);
            var interactStr='<%=interactingDrugList%>';
            var randomIds='<%=allRandomIdInStash%>';
                //clear all warnings
                randomIds=randomIds.replace(/\[/, "");
                randomIds=randomIds.replace(/\]/, "");
                if(randomIds.length>0){
                var randomIdArr=randomIds.split(",");
                for(var h=0;h<randomIdArr.length;h++){
                    var randId=randomIdArr[h];
                    randId=randId.replace(/\s/g,"");//trim
                    $('major_'+randId).hide();
                    $('major_'+randId).update("");
                    $('moderate_'+randId).hide();
                    $('moderate_'+randId).update("");
                    $('minor_'+randId).hide();
                    $('minor_'+randId).update("");
                    $('unknown_'+randId).hide();
                    $('unknown_'+randId).update("");
                }
                }


            if(interactStr.length>0){
                if(interactStr==errorMsg){
                    $("interactingDrugErrorMsg").show();
                    $("interactingDrugErrorMsg").update("<span style='color:red'>"+errorMsg+"</span>");
                }
                else{
                    $("interactingDrugErrorMsg").hide();
                    $("interactingDrugErrorMsg").update("");
                var arr1=interactStr.split(",");
                for(var i=0;i<arr1.length;i++){
                    var str=arr1[i];
                    var arr2=str.split("=");
                    var id=arr2[0];
                    var title=arr2[1];
                    var htmlStr="<a title='"+title+"'>&nbsp;&nbsp;</a>";
                    id=id.replace(/\s/g,"");
                    $(id).show();
                    $(id).update(htmlStr);
                }
            }
            }else{
                $("interactingDrugErrorMsg").hide();
                $("interactingDrugErrorMsg").update("");
            }

</script>
