function popup( height, width, url, windowName)   
{   
  var page = url;  
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";  
  var popup=window.open(url, windowName, windowprops);  
  if (popup != null)   
  {  
    if (popup.opener == null)   
    {  
      popup.opener = self;  
    }  
  }  
  popup.focus();  
  return false;  
}