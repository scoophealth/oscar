function getLanguageFromLCID(lcid)
{
    switch (lcid)
    {
        case 2052: // Chinese - China	zh-cn
            return "chs"; // Simplified Chinese

        case 1028: // Chinese - Taiwan	zh-tw
        case 3076: // Chinese - Hong Kong SAR	zh-hk
        case 5124: // Chinese - Macau SAR	zh-mo
            return "cht"; // Traditional Chinese
            
        case 1031: // German - Germany	de-de
        case 3079: // German - Austria	de-at
        case 5127: // German - Liechtenstein	de-li
        case 4103: // German - Luxembourg	de-lu
        case 2055: // German - Switzerland	de-ch
            return "de";
            
        case 1034:  // Spanish - Spain	es-es             
        case 11274: // Spanish - Argentina	es-ar         
        case 16394: // Spanish - Bolivia	es-bo           
        case 13322: // Spanish - Chile	es-cl             
        case 9226:  // Spanish - Colombia	es-co          
        case 5130:  // Spanish - Costa Rica	es-cr        
        case 7178:  // Spanish - Dominican Republic	es-do
        case 12298: // Spanish - Ecuador	es-ec           
        case 4106:  // Spanish - Guatemala	es-gt         
        case 18442: // Spanish - Honduras	es-hn          
        case 2058:  // Spanish - Mexico	es-mx            
        case 19466: // Spanish - Nicaragua	es-ni         
        case 6154:  // Spanish - Panama	es-pa            
        case 10250: // Spanish - Peru	es-pe              
        case 20490: // Spanish - Puerto Rico	es-pr       
        case 15370: // Spanish - Paraguay	es-py          
        case 17418: // Spanish - El Salvador	es-sv       
        case 14346: // Spanish - Uruguay	es-uy           
        case 8202:  // Spanish - Venezuela	es-ve          
            return "es";
            
        case 1036: // French - France	fr-fr
        case 2060: // French - Belgium	fr-be
        case 3084: // French - Canada	fr-ca
        case 5132: // French - Luxembourg	fr-lu
        case 4108: // French - Switzerland	fr-ch
            return "fr";
                
        case 1040: // Italian - Italy	it-it
        case 2064: // Italian - Switzerland it-ch
            return "it";         

        case 1041: // Japanese ja
            return "ja";

        case 1042: // Korean ko
            return "ko";
            
        case 1043: // Dutch - The Netherlands nl-nl
        case 2067: // Dutch - Belgium nl-be
            return "nl";

        case 2070: // Portuguese - Portugal pt-pt
        case 1046: // Portuguese - Brazil pt-br
            return "pt";
            
        case 1053: // Swedish - Sweden	sv-se
        case 2077: // Swedish - Finland	sv-fi
            return "sv";
       
         default:   
            return "en"; // English
    }    
}

function writeError(header, message)
{
    document.write(
        '<link rel="stylesheet" type="text/css" href="css/exception.css">' +
        '<table class="crExceptionBorder" width="100%" cellspacing=1 cellpadding=0 border=0>' +
          '<tr><td class="crExceptionHeader">' + header + '</td></tr>' +
          '<tr><td>' +
            '<table width="100%" border=0 cellpadding=5 cellspacing=0>' +
              '<tr><td class="crExceptionElement">' + 
                '<table border=0 cellpadding=5 cellspacing=0>' +
                  '<tr><td><span class="crExceptionText">' + message + '</span></td></tr>' +
                '</table>' +
              '</td></tr>' +
            '</table>' +
          '</td></tr>' +
        '</table>\n' );
}

// Use vbscript to get the locale since jscript doesn't have an equivalent function
document.write('<script language="vbscript">lcid = GetLocale<\/script>');

// Include the appropriate strings file
var scriptPath = 'js/strings_' + getLanguageFromLCID(lcid) + '.js';
document.write('<script language="javascript" src="' + scriptPath + '"></script>');
