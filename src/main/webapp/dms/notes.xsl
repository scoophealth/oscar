<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml">
 
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
 
  <xsl:template match="/notes">
    <html>
      <head> 
        <title>Patient Name: <xsl:value-of select="@name"/> Chart #: <xsl:value-of select="@chart"/></title> 
      	<style type="text/css">
      	  body { font-family:sans-serif; }
      	  span.code {  font-weight: bold; }
      	  div.episode{ border: 1px dotted grey;margin-bottom:2px;}
      	  div.episode h3 {margin-top: 2px;margin-bottom: 2px; color:blue; }
      	  
      	</style>
        
        <script type="text/javascript">
          function showAll(){
            var dd = document.getElementsByClassName('eachnote');

             for ( var j in dd ){
                dd[j].style.display = '';
             }
          }



      function getElementsByClassName(classname, node) {
      if(!node) node = document.getElementsByTagName("body")[0];
      var a = [];
      var re = new RegExp('\\b' + classname + '\\b');
      var els = node.getElementsByTagName("*");
      for ( i in els){
         if(re.test(els[i].className))a.push(els[i]);
      }
      return a;

      }

          function runShowHide(){
             var showCodes = [];
             var form = document.getElementById('myform');   
             var ee = form.codes;
              
             for( var i in ee){
                if( ee[i].checked == true ){
                   showCodes[showCodes.length] = ee[i].value;
                }
             }

             var dd = getElementsByClassName('eachnote');//,// document.getElementsByClassName('eachnote');
             
             var el = document.getElementById('PickIssues');
             el.style.display = 'none';
             for ( var j in dd ){
                var found = false;
                for ( var k in showCodes){
                   if(dd[j].className){
                      if(dd[j].className.indexOf(showCodes[k]) != -1){
                         //alert("found "+dd[j]);
                         found = true;
                      }
                   }
                }
                
                if(found == true){
                   if(dd[j].style)
                   dd[j].style.display = '';
                   window.resizeTo(1000,660);
                }else{
                   if(dd[j].style)
                   dd[j].style.display = 'none';
                }
                    
             } 
          }
        
          function load(){
            window.resizeTo(1000,660);
          }

          function showHideChecks(){
             var el = document.getElementById('PickIssues');
             if(el.style){
                 if ( el.style.display != 'none' ) {
                    el.style.display = 'none';
                 }else{
                    el.style.display = '';
                 }
             }
          }

        </script>
      </head>
      <body onload="load();">
        <h1>Patient: <xsl:value-of select="@name"/> Chart #: <xsl:value-of select="@chart"/>  </h1>
        <div>
            <a href="javascript:void();" onclick="showHideChecks()">Select Issues Show/Hide</a>
               
            <a style="margin-left:5px;" href="javascript:void();" onclick="showAll()">Show All</a>
            <div style="display:none;" id="PickIssues">
               <form id="myform">
               <input type="button" value="Update" onclick="runShowHide();"/>
                  <xsl:for-each select="codes/code">
                       <xsl:sort select="@code" />
                       <li>
                          <input type="checkbox" name="codes" >
                             <xsl:attribute name="value">
                                 <xsl:value-of select="@bcode" />
                             </xsl:attribute>
                          </input>
                          <xsl:value-of select="@code"/>
                      </li>
                  </xsl:for-each>
               </form>
            </div>
        </div>

        <div>
          <xsl:apply-templates select="episode">
            <xsl:sort select="family-name" />
          </xsl:apply-templates>
        </div>
      </body>
    </html>
  </xsl:template>
 
  <xsl:template match="episode">
    <div class="episode">
      <h3><xsl:value-of select="@date"/></h3>
      <xsl:for-each select="element">
        <div>
            <xsl:attribute name="class">
                <xsl:text>eachnote </xsl:text>
                <xsl:value-of select="@bcode" />
            </xsl:attribute>
        
           <span class="code"><xsl:value-of select="@code"/>:</span>
           <span><xsl:value-of select="text()"/></span>
        </div>   
      </xsl:for-each>
    </div>
  </xsl:template>
 
</xsl:stylesheet>