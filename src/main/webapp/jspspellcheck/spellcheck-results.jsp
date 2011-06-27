
<%/*

    spellcheck-results.jsp - Performs the actual spell check and displays the 
     results in a dialog. This file contains the majority of the spellchecking
     logic. You probably should not change this file unless you are certain you 
     know what you are doing. If you are interested in changing the look and feel
     see spellcheck.css and spellcheck-form.jsp.

    Copyright (C) 2005 Balanced Insight, Inc.

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/%>

<%@ page
	import="com.swabunga.spell.engine.*
    ,com.swabunga.spell.event.SpellCheckListener
    ,com.swabunga.spell.event.SpellCheckEvent
    ,com.swabunga.spell.event.SpellChecker
    ,com.swabunga.spell.event.StringWordTokenizer
    ,com.swabunga.spell.event.XMLWordFinder
    ,java.util.*
    "%>

<%!

private class SpellingHelper
    implements SpellCheckListener
{
    /** List of SpellCheckEvents. */
    private List spellCheckEvents = new LinkedList();

    /** Called by the Spell Checker. */
    public void spellingError(SpellCheckEvent spellCheckEvent)
    {
        spellCheckEvents.add( spellCheckEvent );
    }

    /** An easy way to get the events. */
    public List getSpellCheckEvents()
    {
        return spellCheckEvents;
    }

    /** Reset the list of events between calls to SpellChecker.checkSpelling(...). */
    public void reset()
    {
        spellCheckEvents = new LinkedList();
    }
}


%>

<%
ResourceBundle spellcheckBundle = ResourceBundle.getBundle( "spellcheckBundle", request.getLocale() );
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><%=spellcheckBundle.getString("WindowTitle")%></title>
<link rel="stylesheet" href="spellcheck.css" type="text/css" />
</head>
<body onload="processNextMistake();" onkeypress="miscKeyPress();">

<%@ include file="spellcheck-form.jspf"%>
<%@ include file="spellcheck-functions.jspf"%>
<%@ include file="spellcheck-dictionary.jspf"%>

<script>
<!--

    // The master Array
    var checkedElements=new Array(0);
    var currentElement = 0;
    var currentEvent = -1;

    // The skip Array
    var skipArray=new Array(0);

    // The processed Objects Associative Array
    var singleIgnoredArray=new Object();

    <%

    SpellChecker spellChecker = new SpellChecker(dictionary);

    SpellingHelper spellingHelper = new SpellingHelper();
    spellChecker.addSpellCheckListener( spellingHelper );
    
    Configuration configuration = spellChecker.getConfiguration();
    
    %>
    <%@ include file="spellcheck-config.jsp" %>
    <%
    
    boolean keepGoing = true;
    int element = -1;

    while ( keepGoing )
    {
        element++;
        String value = request.getParameter( "element_" + element + "_value");
        String formElement = request.getParameter( "element_" + element + "_name" );
        if( value == null || formElement == null )
        {
            keepGoing = false;
            continue;
        }

        spellChecker.checkSpelling(new StringWordTokenizer( new XMLWordFinder( value ) ) );
        List spellCheckEvents = spellingHelper.getSpellCheckEvents();
        spellingHelper.reset();
        %>


            //Results for a new element:
            checkedElements[<%=element%>] = new Array(3);

            //Element Name (opener.document.xxx):
            checkedElements[<%=element%>][0] = '<%=formElement%>';

            // Number of spelling errors:
            checkedElements[<%=element%>][1] = new Array(<%=spellCheckEvents.size()%>);

            // Original Value:
            checkedElements[<%=element%>][2] = '<%=value.replaceAll("'","\\\\'").replaceAll("\r\n","\\\\r\\\\n")%>';


            <%
            for ( int x = 0; x < spellCheckEvents.size(); x++ )
            {

                SpellCheckEvent spellCheckEvent = (SpellCheckEvent)spellCheckEvents.get(x);

                String invalidWord = spellCheckEvent.getInvalidWord();
                String firstChar = invalidWord.substring(0,1);
                String lastChar = invalidWord.substring( invalidWord.length() -1 );
                boolean firstLetterCaps = firstChar.equals( firstChar.toUpperCase() );
                boolean allCaps = firstLetterCaps && lastChar.equals( lastChar.toUpperCase() ); // Assume that all in the middle are too

                %>
                // Holder for the information about the event:
                checkedElements[<%=element%>][1][<%=x%>]=new Array(3);

                //Invalid Word:
                checkedElements[<%=element%>][1][<%=x%>][0]='<%=invalidWord.replaceAll("'","\\\\'")%>';

                //Word Context Position:
                checkedElements[<%=element%>][1][<%=x%>][1]=<%=spellCheckEvent.getWordContextPosition()%>;

                //Number of Suggestions (size of next array):
                <%
                List suggestions = spellCheckEvent.getSuggestions();
                %>
                checkedElements[<%=element%>][1][<%=x%>][2]=new Array(<%=suggestions.size()%>);

                // Suggestions:
                <%
                for( int y = 0; y < suggestions.size(); y++ )
                {
                    Word word = (Word)suggestions.get( y );
                    String suggestedWord = word.toString().replaceAll("'","\\\\'");
                    if ( allCaps )
                    {
                        suggestedWord =  suggestedWord.toUpperCase();
                    }
                    else if( firstLetterCaps )
                    {
                        suggestedWord =  suggestedWord.substring(0,1).toUpperCase() + suggestedWord.substring(1);                   
                    }
                    %>
                    checkedElements[<%=element%>][1][<%=x%>][2][<%=y%>] = '<%=suggestedWord%>';
                    <%
                }

                %>
                <%
            }
            %>
        <%
    }

    %>

    // Disable the right click context menu
    //document.oncontextmenu = new Function("return false");

    // Focus the suggestions box
    document.correct.suggestions.focus();
    
//-->
</script>



</body>
</html>

