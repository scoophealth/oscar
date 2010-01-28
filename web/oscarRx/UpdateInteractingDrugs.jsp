<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="oscar.oscarRx.data.RxPrescriptionData" %>
<%
    oscar.oscarRx.pageUtil.RxSessionBean bean2 =(oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
    RxPrescriptionData.Prescription [] allRxInStash=bean2.getStash();
    List allRandomIdInStash=new ArrayList() ;
    for(RxPrescriptionData.Prescription rx:allRxInStash){
        allRandomIdInStash.add(rx.getRandomId());
    }
    //System.out.println("allRandomIdInStash="+allRandomIdInStash);
    Vector bean2Vec=bean2.getInteractingDrugList();
    int bean2VecSize=bean2Vec.size();
    //System.out.println("bean2.getInteractingDrugList()="+bean2Vec);

%>

<script type="text/javascript">

                var interactionDrugList='<%=bean2Vec%>';
                var interactionSize='<%=bean2VecSize%>';
                var randomIds='<%=allRandomIdInStash%>';
                //console.log("interactionSize in deletePrescribe  "+interactionSize);
                //console.log("interactionDrugList in deletePrescribe  "+interactionDrugList);
                //console.log("allRandomIdInStash  "+randomIds);
                //clear all warnings
                randomIds=randomIds.replace(/\[/, "");
                randomIds=randomIds.replace(/\]/, "");
                var randomIdArr=randomIds.split(",");
                //console.log("randomIdArr="+randomIdArr);
                for(var h=0;h<randomIdArr.length;h++){
                    var randId=randomIdArr[h];
                    randId=randId.replace(/\s/g,"");//trim
                    //console.log("randId="+randId);
                    $('major_'+randId).hide();
                    $('major_'+randId).update("");
                    $('moderate_'+randId).hide();
                    $('moderate_'+randId).update("");
                    $('minor_'+randId).hide();
                    $('minor_'+randId).update("");
                    $('unknown_'+randId).hide();
                    $('unknown_'+randId).update("");
                }
            ////console.log("done clearing");
            if(interactionSize>0){
                                var patt1=/\}, \{/g;
                                if(patt1.test(interactionDrugList)){//multiple interactions
                                    var intArr=interactionDrugList.split("}, {");
                                    //console.log("in multiple interactions="+intArr);
                                    for(var i=0;i<intArr.length;i++){
                                        var str=intArr[i];
                                        //console.log(str);
                                        str=str.replace(/\{/g, "");
                                        str=str.replace(/\}/g, "");
                                        str=str.replace(/\[/g, "");
                                        str=str.replace(/\]/g, "");
                                        var strArr=str.split("=");
                                        var interacter=strArr[0];
                                        var interactee=strArr[1];
                                        var interacteeArr=interactee.split(",");
                                        var interacteeNameArr=new Array();
                                        var interacteeEffectArr=new Array();
                                        var interacteeSigArr=new Array();
                                        for(var j=0;j<interacteeArr.length;j++){
                                            if(j%3==0){
                                                interacteeNameArr.push(interacteeArr[j]);
                                            }else if((j+2)%3==0){
                                                interacteeEffectArr.push(interacteeArr[j]);
                                            }else{
                                                interacteeSigArr.push(interacteeArr[j]);
                                            }
                                        }
                                        //console.log("interacter="+interacter);
                                        //console.log("interacteeNameArr="+interacteeNameArr);
                                        //console.log("interacteeEffectArr="+interacteeEffectArr);
                                        //console.log("interacteeSigArr="+interacteeSigArr);
                                        //update the html here.
                                        //new Insertion.After("instructions_"+interacter, "<p>"+interacteeNameArr+"<p>");
                                            /*     h.put("1","minor");
                                                   h.put("2","moderate");
                                                   h.put("3","major");
                                                   h.put(" ","unknown");

                                                   h.put("1","yellow");
                                                   h.put("2","orange");
                                                   h.put("3","red");
                                                   h.put(" ","greenyellow");*/
                                       var minor="";
                                        var moderate="";
                                        var major="";
                                        var unknownStr="";
                                        for(var h=0;h<interacteeSigArr.length;h++){
                                            oscarLog("interacteeSigArr[h]="+interacteeSigArr[h]);
                                            if(interacteeSigArr[h]==1){
                                                minor+=" "+interacteeNameArr[h];
                                            }else if(interacteeSigArr[h]==2){
                                                moderate+=" "+interacteeNameArr[h];
                                            }else if(interacteeSigArr[h]==3){
                                                major+=" "+interacteeNameArr[h];
                                            }else{
                                                unknownStr+=" "+interacteeNameArr[h];
                                            }
                                        }
                                     //   console.log("major="+major);
                                     //   console.log("moderate="+moderate);
                                     //   console.log("minor="+minor);
                                     //   console.log("unknownStr="+unknownStr);
                                        if(major.length>0){
                                            var htmlStr="<a title='"+major+"'>&nbsp;&nbsp;</a>";
                                            $('major_'+interacter).show();
                                            $('major_'+interacter).update(htmlStr);
                                        }else{
                                            $('major_'+interacter).hide();
                                            $('major_'+interacter).update("");
                                        }
                                        if(moderate.length>0){
                                            var htmlStr="<a title='"+moderate+"'>&nbsp;&nbsp;</a>";
                                            $('moderate_'+interacter).show();
                                            $('moderate_'+interacter).update(htmlStr);
                                        }else{
                                            $('moderate_'+interacter).hide();
                                            $('moderate_'+interacter).update("");
                                        }
                                        if(minor.length>0){
                                            var htmlStr="<a title='"+minor+"'>&nbsp;&nbsp;</a>";
                                            $('minor_'+interacter).show();
                                            $('minor_'+interacter).update(htmlStr);
                                        }else{
                                            $('minor_'+interacter).hide();
                                            $('minor_'+interacter).update("");
                                        }
                                        if(unknownStr.length>0){
                                            var htmlStr="<a title='"+unknownStr+"'>&nbsp;&nbsp;</a>";
                                            $('unknown_'+interacter).show();
                                            $('unknown_'+interacter).update(htmlStr);
                                        }else{
                                            $('unknown_'+interacter).hide();
                                            $('unknown_'+interacter).update("");
                                        }
                                    }
                                }else{//only one interaction
                                    //console.log("in single interaction");
                                    var str=interactionDrugList.replace(/\{/g, "");
                                        str=str.replace(/\}/g, "");
                                        str=str.replace(/\[/g, "");
                                        str=str.replace(/\]/g, "");
                                        var strArr=str.split("=");
                                        var interacter=strArr[0];
                                        var interactee=strArr[1];
                                        var interacteeArr=interactee.split(",");
                                        var interacteeNameArr=new Array();
                                        var interacteeEffectArr=new Array();
                                        var interacteeSigArr=new Array();
                                        for(var j=0;j<interacteeArr.length;j++){
                                            if(j%3==0){
                                                interacteeNameArr.push(interacteeArr[j]);
                                            }else if((j+2)%3==0){
                                                interacteeEffectArr.push(interacteeArr[j]);
                                            }else{
                                                interacteeSigArr.push(interacteeArr[j]);
                                            }
                                        }
                                        //console.log("interacter="+interacter);
                                      //console.log("interacteeNameArr="+interacteeNameArr);
                                        //console.log("interacteeEffectArr="+interacteeEffectArr);
                                      //console.log("interacteeSigArr="+interacteeSigArr);

                                        //new Insertion.After("instructions_"+interacter, "<p>"+interacteeNameArr+"<p>");
                                        var minor="";
                                        var moderate="";
                                        var major="";
                                        var unknownStr="";
                                        for(var h=0;h<interacteeSigArr.length;h++){
                                            //console.log("interacteeSigArr[h]="+interacteeSigArr[h]);
                                            if(interacteeSigArr[h]==1){
                                                minor+=" "+interacteeNameArr[h];
                                            }else if(interacteeSigArr[h]==2){
                                                moderate+=" "+interacteeNameArr[h];
                                            }else if(interacteeSigArr[h]==3){
                                                major+=" "+interacteeNameArr[h];
                                            }else{
                                                unknownStr+=" "+interacteeNameArr[h];
                                            }
                                        }
                                        //console.log("major="+major);
                                        //console.log("moderate="+moderate);
                                        //console.log("minor="+minor);
                                        //console.log("unknownStr="+unknownStr);
                                        if(major.length>0){
                                            var htmlStr="<a title='"+major+"'>&nbsp;&nbsp;</a>";
                                            $('major_'+interacter).show();
                                            $('major_'+interacter).update(htmlStr);
                                        }else{
                                            $('major_'+interacter).hide();
                                            $('major_'+interacter).update("");
                                        }
                                        if(moderate.length>0){
                                            var htmlStr="<a title='"+moderate+"'>&nbsp;&nbsp;</a>";
                                            $('moderate_'+interacter).show();
                                            $('moderate_'+interacter).update(htmlStr);
                                        }else{
                                            $('moderate_'+interacter).hide();
                                            $('moderate_'+interacter).update("");
                                        }
                                        if(minor.length>0){
                                            var htmlStr="<a title='"+minor+"'>&nbsp;&nbsp;</a>";
                                            $('minor_'+interacter).show();
                                            $('minor_'+interacter).update(htmlStr);
                                        }else{
                                            $('minor_'+interacter).hide();
                                            $('minor_'+interacter).update("");
                                        }
                                        if(unknownStr.length>0){
                                            var htmlStr="<a title='"+unknownStr+"'>&nbsp;&nbsp;</a>";
                                            $('unknown_'+interacter).show();
                                            $('unknown_'+interacter).update(htmlStr);
                                        }else{
                                            $('unknown_'+interacter).hide();
                                            $('unknown_'+interacter).update("");
                                        }

                                }
                 }

</script>
