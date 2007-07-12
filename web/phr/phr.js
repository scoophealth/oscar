function phrActionPopup(url) {
  phrActionPopup(url, Math.round(Math.random()*10000000));
}

function phrActionPopup(url, windowName){   
  var height = 200;
  var width = 350;
  var top = 300;
  var left = 400;
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=" + top + ",left=" + left;
  var popup=window.open(url, windowName, windowprops);  
  if (popup != null){  
    if (popup.opener == null){  
      popup.opener = self;  
    }  
  }  
  popup.focus();  
  //return false;  
}