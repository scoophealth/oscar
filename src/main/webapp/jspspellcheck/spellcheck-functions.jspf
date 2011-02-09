
<%/*

    spellcheck-functions.jsp - This file contains all of the javascript
     functions that the spell check dialog requires.

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
<script>
<!--

    /*
     * Handles when the cancelAll button is pressed.
     *
     * Undo's the changes made by this spell check run.
     */
    function cancelAll()
    {
        try
        {
            for( var x = 0 ; x < checkedElements.length; x++ )
            {
                restoreField( checkedElements[x][0], checkedElements[x][2] );
            }
            window.close();
        }
        catch( e )
        {
           alert( '<%=spellcheckBundle.getString("CommunicationsErrorMessage").replaceAll("'","\\\\'")%>' );
        }
    }

    /*
     * Handles the enter key being pressed on the select box.
     */
    function suggestionsKeyPress()
    {
        if (window.event && window.event.keyCode == 13) // Enter Key
        {
            suggestionsDoubleClick();
            return false;
        }       
    }

    /*
     * Handles the single clicking on the suggestions select box.
     */
    function suggestionsSingleClick()
    {
        var selIndex = document.correct.suggestions.selectedIndex;
        if ( selIndex > -1 )
        {
            document.correct.replaceWith.value=document.correct.suggestions.options[selIndex].text;
        }
    }

    /*
     * Handles the double clicking on the suggestions select box.
     */
    function suggestionsDoubleClick()
    {
        var selIndex = document.correct.suggestions.selectedIndex;
        if ( selIndex > -1 )
        {
            document.correct.replaceWith.value=document.correct.suggestions.options[selIndex].text;
            replaceWord(false);    
        }
    }

    /*
     * Handles the pressing of the escape key.
     */
    function miscKeyPress()
    {
        if (window.event && window.event.keyCode == 27) // Escape Key
        {
            window.close();
            return false;
        }
    }

    /*
     * Called when the replaceAll button is pressed.
     *
     * Adds the word to the skip array and calls replaceWord( true ).
     */
    function replaceAllInstances()
    {
        addToSkipArray();
        replaceWord( true );
    }

    /*
     * Called when the ignoreAll button is pressed.
     *
     * Adds the word to the skip array and processes the next mistake.
     */
    function ignoreAllInstances()
    {
        addToSkipArray();
        processNextMistake();
    }
    
    /*
     * Called when the ignore button is pressed.
     * 
     * Increases the count of the word in the singleIgnoredArray.
     */
    function ignoreWord()
    {
        var word = document.correct.original.value;
        
        var instance = 0;
        if ( singleIgnoredArray[ word ] != null )
        {
            instance = singleIgnoredArray[ word ];
        }
        instance++;
        singleIgnoredArray[ word ] = instance;  
        processNextMistake();
    }

    /*
     * Adds a word to the skip array.
     */
    function addToSkipArray()
    {
        skipArray[skipArray.length] = document.correct.original.value;          
    }

    /*
     * Function to replace a word with the word in the replaceWith field.
     *
     * The single parameter is a boolean representing if all of the instances of that
     *  word should be replaced.
     */
    function replaceWord( replaceAll )
    {
        try
        {
            replaceWordInField( document.correct.original.value,
                    checkedElements[currentElement][0], document.correct.replaceWith.value, replaceAll );
            processNextMistake();
        }
        catch( e )
        {
           alert( '<%=spellcheckBundle.getString("CommunicationsErrorMessage").replaceAll("'","\\\\'")%>' );
        }       
    }

    /*
     * Function to process the next spelling mistake.
     */
    function processNextMistake()
    {
        currentEvent++;

        if ( !(currentElement >= checkedElements.length) && currentEvent >= checkedElements[currentElement][1].length )
        {
            // This currentEvent is too high. I need to cycle to the next Element
            currentElement++;
            skipArray=new Array(0); // reset the skipArray - we can only skip easily within one element (a slight limitation for now)
            singleIgnoredArray=new Object(); // reset the singleIgnoredArray - duplicate ignore's on the same word needs to skip the first one
            currentEvent=0;
        }

        if ( currentElement >= checkedElements.length )
        {
            // I am out of elements
            alert( '<%=spellcheckBundle.getString("SpellCheckCompleteMessage").replaceAll("'","\\\\'")%>' );
            window.close();
            return;
        }

        if ( checkedElements[currentElement][1].length == 0 )
        {
            processNextMistake();
            return;
        }

        // Get the original word from the array
        var originalWord = checkedElements[currentElement][1][currentEvent][0];

        // If this word is in the skip array - skip it!
        for ( var x = 0; x < skipArray.length; x++ )
        {
            if ( skipArray[x] == originalWord )
            {
                processNextMistake();
                return;
            }

        }

        // Invalid Word
        document.correct.original.value = originalWord;

        // Clear suggestions box
        document.correct.suggestions.options.length=0;

        // Add suggestion words
        for( var sug = 0; sug < checkedElements[currentElement][1][currentEvent][2].length; sug++ )
        {
            document.correct.suggestions.options[sug] = new Option(checkedElements[currentElement][1][currentEvent][2][sug]);
        }

        // Fill replaceWith box
        if ( document.correct.suggestions.options.length > 0 )
        {
            document.correct.replaceWith.value = document.correct.suggestions.options[0].text;
            document.correct.suggestions.options[0].selected = true;
        }
        else
        {
            document.correct.replaceWith.value = originalWord;
        }

        try
        {
            selectWordInField( originalWord, checkedElements[currentElement][0] )
        }
        catch( e )
        {
            alert( '<%=spellcheckBundle.getString("CommunicationsErrorMessage").replaceAll("'","\\\\'")%>' );
        }
        
     }


    /*
     * Function to highlight a word in a particular field.
     *
     * The formAndField parameter will be appended to a 'document.' to find the field.
     */
    function selectWordInField( word, formAndField )
    {
        var element = eval('opener.document.' + formAndField);
        if (element.setSelectionRange) 
        {
            selectWordInFieldNotIE( word, formAndField );
        }
        else if (element.createTextRange) 
        {
            selectWordInFieldIE( word, formAndField );
        }
        else
        {
            alert( '<%=spellcheckBundle.getString("IncompatibleBrowserErrorMessage").replaceAll("'","\\\\'")%>' );
        }    
    }

    /*
     * Function to replace a word in a particular field.
     *
     * The formAndField parameter will be appended to a 'document.' to find the field.
     */
    function replaceWordInField( word, formAndField, newWord, replaceAll )
    {
        var element = eval('opener.document.' + formAndField);
        
        if (element.setSelectionRange) 
        {
            replaceWordInFieldNotIE( word, formAndField, newWord, replaceAll );
        }
        else if (element.createTextRange) 
        {
            replaceWordInFieldIE( word, formAndField, newWord, replaceAll );
        }
        else
        {
            alert( '<%=spellcheckBundle.getString("IncompatibleBrowserErrorMessage").replaceAll("'","\\\\'")%>' );
        }
    }

    /*
     * Function to highlight a word in a particular field.
     *
     * The formAndField parameter will be appended to a 'document.' to find the field.
     */
    function selectWordInFieldIE( word, formAndField )
    {
        var element = eval('opener.document.' + formAndField);
        var textRange = element.createTextRange();
        findAndSelectIE( textRange, word );
    }
    
    /*
     * Finds and Selects the text in the given textRange.
     *
     * This method looks at the singleIgnoredArray to know if it should find
     *  the n-th instance of a word.
     *
     */
    function findAndSelectIE( textRange, word )
    {
        var instance = 0;
        if ( singleIgnoredArray[ word ] != null )
        {
            instance = singleIgnoredArray[ word ];
        }

        for( var x=0; x<=instance; x++ )
        {
            if( x > 0 )
                textRange.move('character',1);
            var found = textRange.findText( word, 1, 6 );
            if ( ! found )
              textRange.findText( word, 1, 4 );
        }
        textRange.select();
    }


    function replaceWordInFieldIE( word, formAndField, newWord, replaceAll )
    {
        var element = eval('opener.document.' + formAndField);

        var textRange = element.createTextRange();
        findAndSelectIE( textRange, word );
        textRange.text = newWord;
        
        if( replaceAll )
        {
            var keepGoing = true;
            while( keepGoing )
            {
                keepGoing = textRange.findText( word, 1, 6 ) 
                if ( keepGoing /*|| textRange.text == word */ )
                {
                    textRange.select();
                    textRange.text = newWord;
                }
            }
        }   
    }

    /*
     * Function to highlight a word in a particular field.
     *
     * The formAndField parameter will be appended to a 'document.' to find the field.
     */
    function selectWordInFieldNotIE( word, formAndField )
    {
        var element = eval('opener.document.' + formAndField);
        findAndSelectNotIE( element, word );
    }
    
    function findAndSelectNotIE( element, word )
    {
        var instance = 0;
        if ( singleIgnoredArray[ word ] != null )
        {
            instance = singleIgnoredArray[ word ];
        }

        var offSet = 0;
        var match = false;
        var re = new RegExp('\\b'+word+'\\b', 'i');
        var str = element.value;
        var match = re.exec(str);
        
        for( var x = 1; x <= instance; x++ )
        {
            offSet = offSet + match.index + word.length;
            str = str.substring( match.index + word.length );
            match = re.exec( str );
        }

        if (match) 
        {
            var left = match.index + offSet;
            var right = left + word.length;
            element.setSelectionRange ( left, right);
            element.focus();
            return match;
        }
        else
        {
            return false;
        }
        
        
    }  
    
    function replaceWordInFieldNotIE( word, formAndField, newWord, replaceAll )
    {
        var element = eval('opener.document.' + formAndField);

        findAndSelectNotIE( element, word );
        
        var selectionStart = element.selectionStart;
        var selectionEnd = element.selectionEnd;
        element.value = element.value.substring(0, selectionStart)
                      + newWord
                      + element.value.substring(selectionEnd);
        
        if( replaceAll )
        {
            var keepGoing = true;
            while( keepGoing )
            {
                keepGoing = findAndSelectNotIE( element, word );
                if ( keepGoing )
                {
                    selectionStart = element.selectionStart;
                    selectionEnd = element.selectionEnd;
                    element.value = element.value.substring(0, selectionStart)
                                  + newWord
                                  + element.value.substring(selectionEnd);
                }
            }
        }
        
        element.setSelectionRange(0,0);
    }
    

    /*
     * Function to restore a field to it's original value.
     *
     */
    function restoreField( formAndField, originalValue )
    {
        var element = eval('opener.document.' + formAndField);
        element.value = originalValue;
    }

//-->
</script>