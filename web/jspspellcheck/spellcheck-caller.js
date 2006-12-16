/*

    spellcheck-caller.js - This file needs to be included on the page that
     contains the fields that require spell checking. 
    
    See spellcheck.license.txt.

*/

/*
 * Function to be called by the form that requires spell checking.
 *
 * The caller should pass in an array of form elements that require spell checking.
 */
function startSpellCheck(baseUrl,elements)
{
    var params='?op=1';
    
   
    for( var x = 0; x < elements.length; x++ )
    {
        var form = elements[x].form;
        var formsNumber = getFormsNumber( form );
        params = params + '&element_' + x + '=forms['+formsNumber+'].' + elements[x].name;
    }
    

    openCenteredWindow( baseUrl + 'spellcheck-entry.jsp' + params, 300, 200 );
    
}

/*
 * Finds the index of the form in the document.forms collection.
 * 
 * This is necessary because you can't get the form name from form.name if
 *  there is a field in that form called name.
 */
function getFormsNumber( form )
{
    var forms = document.forms;
    for( var x = 0; x < forms[x].length; x++ )
    {
        if( forms[x] == form )
            return x;
    }
    
    return -1; // Form not found
}


/*
 * Function to open a window in the middle of the current window.
 *
 */
function openCenteredWindow( url, width, height )
{
    var left = 0;
    var top = 0;

    if( document.all )
    {
      top = window.top.screenTop + (window.top.document.body.clientHeight/2) - (height/2);
      left = window.top.screenLeft + (window.top.document.body.clientWidth/2) - (width/2);
    }
    else
    {
      top = window.top.screenY + (window.outerWidth/2) - (height/2);
      left = window.top.screenX + (window.outerHeight/2) - (width/2);
    }

    var newWin = window.open( url,"","height=" + (height) + ",width=" + (width) +",left=" +left+",top="+top+",location=no,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=yes,toolbar=no");
    return newWin;
}

