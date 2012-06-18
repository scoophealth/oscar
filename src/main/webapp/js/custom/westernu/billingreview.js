jQuery(document).ready(function(){
     var url = ctx+"/BillingONReview.do";
     jQuery.ajax({
         url: url,
         dataType: 'json',
         data: {method: "getDemographic",demographicNo:demographicNo},
         success:function(data){
             jQuery("textarea#billTo").val(data.firstName+" "+data.lastName+"\n"+data.address+"\n"+data.city+", "+data.province+"\n"+data.postal+"\nEmail: "+data.email+"\nTel: "+ data.phone+"\n\nStudent ID: " + data.chartNo);
         }

     });
     jQuery.ajax({
         url: url,
         dataType: 'json',
         data: {method: "getClinic"},
         success:function(data){
             jQuery("textarea#remitTo").val(data.clinicName+"\n"+data.clinicAddress+"\n"+data.clinicCity+", "+data.clinicProvince+"\n"+data.clinicPostal+"\nTel: 519-661-2111 x11111");
         }

     });
     jQuery("#settlePrintBtn").css("display","none");

});
