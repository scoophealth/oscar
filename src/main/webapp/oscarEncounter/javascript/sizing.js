/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

// all global javascript variables
    var sht = 75; // height of smaller textareas
    var delta = 15; // amount to adjust height of larger textarea
    var deltaHeight = 50;  // amount boxes change by when + or - is clicked
    var pageht = 500;
    var leftColWidth = 540;

// Temporary:  will be supplied by the database
var dateOfEncounter= ["visit 4", "visit 3", "visit 2", "visit 1"];

// these variables have derived values:
    var lht = 2*sht + delta; // height of larger textareas
    var enht = 2*sht;
    var rightColWidth = leftColWidth/2;
    var encWidth = (1.5*leftColWidth) + 23;
    var leftCol = "width: "+leftColWidth+"; height: "+sht;
    var rightCol = "width: "+rightColWidth+"; height: "+lht;
    var encounterCol = "width: "+encWidth+"; height: "+enht;


function popupTestPage(text) {
    windowprops = "height=700,width=960,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=50,left=50";
    window.open(text, "apptProviderSearch", windowprops);
}

function num(value) {
    var theString = new String(value);
    var st = new String(theString.substring(0, theString.length-2));
    var numberSt = new Number(st);
    //alert('value: '+numberSt.toString());
    return numberSt;
}

function expandTable(textarea) {
    var dheight = deltaHeight;
    var height = num(textarea.style.height) + dheight;
    textarea.style.height = height;

    switch (textarea.name) {
    case document.encForm.shTextarea.name:
        var adjust = num(document.encForm.shTextarea.style.height) + num(document.encForm.fhTextarea.style.height) + delta;
        document.encForm.cmTextarea.style.height = adjust;
        break;
    case document.encForm.cmTextarea.name:
        var adjustsh = num(document.encForm.shTextarea.style.height) + dheight/2;
        document.encForm.shTextarea.style.height = adjustsh;
        var adjustfh = num(document.encForm.fhTextarea.style.height) + dheight/2;
        document.encForm.fhTextarea.style.height = adjustfh;
        break;
    case document.encForm.fhTextarea.name:
        var adjust = num(document.encForm.shTextarea.style.height) + num(document.encForm.fhTextarea.style.height) + delta;
        document.encForm.cmTextarea.style.height = adjust;
        break;
    case document.encForm.mhTextarea.name:
        var adjust = num(document.encForm.mhTextarea.style.height) + num(document.encForm.cpTextarea.style.height) + delta;
        document.encForm.daTextarea.style.height = adjust;
        break;
    case document.encForm.daTextarea.name:
        var adjustmh = num(document.encForm.mhTextarea.style.height) + dheight/2;
        document.encForm.mhTextarea.style.height = adjustmh;
        var adjustcp = num(document.encForm.cpTextarea.style.height) + dheight/2;
        document.encForm.cpTextarea.style.height = adjustcp;
        break;
    case document.encForm.cpTextarea.name:
        var adjust = num(document.encForm.mhTextarea.style.height) + num(document.encForm.cpTextarea.style.height) + delta;
        document.encForm.daTextarea.style.height = adjust;
        break;
    case document.encForm.enTextarea.name:
        break;
    default:
        document.encForm.shTextarea.style.height = sht;
        document.encForm.cmTextarea.style.height = lht;
        document.encForm.fhTextarea.style.height = sht;
        document.encForm.mhTextarea.style.height = sht;
        document.encForm.daTextarea.style.height = lht;
        document.encForm.cpTextarea.style.height = sht;
        document.encForm.enTextarea.style.height = enht;
    }
}

function shrinkTable(textarea) {
    var dheight = deltaHeight;
    var ht = num(textarea.style.height);

    switch (textarea.name) {
    case document.encForm.shTextarea.name:
        var ht = num(textarea.style.height);
        if( (ht - dheight) > sht)
            textarea.style.height = (ht - dheight);
        else
            textarea.style.height = sht;
        var adjust = num(document.encForm.shTextarea.style.height) + num(document.encForm.fhTextarea.style.height) + delta;
        document.encForm.cmTextarea.style.height = adjust;
        break;
    case document.encForm.cmTextarea.name:
        // This changes cmTextarea size
        if( (ht - dheight) > lht)
            textarea.style.height = (ht - dheight);
        else
            textarea.style.height = lht;
        // this changes sh and fh size
        var shHeight = num(document.encForm.shTextarea.style.height);
        var fhHeight = num(document.encForm.fhTextarea.style.height);
        if( ((shHeight - dheight/2) > sht) && ((fhHeight - dheight/2) > sht)) {
            document.encForm.shTextarea.style.height = (shHeight - dheight/2);
            document.encForm.fhTextarea.style.height = (fhHeight - dheight/2);
        }else if( (shHeight - dheight) > sht){
            document.encForm.shTextarea.style.height = (shHeight - dheight);
        }else if( (fhHeight - dheight) > sht) {
            document.encForm.fhTextarea.style.height = (fhHeight - dheight);
        }else {
            var dif = (shHeight - sht);
            document.encForm.shTextarea.style.height = sht;
            if( (fhHeight - dif) > sht)
                document.encForm.fhTextarea.style.height = (fhHeight - (dheight - dif));
            else
                document.encForm.fhTextarea.style.height = sht;
        }
        break;
    case document.encForm.fhTextarea.name:
        if( (ht - dheight) > sht)
            textarea.style.height = (ht - dheight);
        else
            textarea.style.height = sht;
        var adjust = num(document.encForm.shTextarea.style.height) + num(document.encForm.fhTextarea.style.height) + delta;
        document.encForm.cmTextarea.style.height = adjust;
        break;
    case document.encForm.mhTextarea.name:
        if( (ht - dheight) > sht)
            textarea.style.height = (ht - dheight);
        else
            textarea.style.height = sht;
        var adjust = num(document.encForm.mhTextarea.style.height) + num(document.encForm.cpTextarea.style.height) + delta;
        document.encForm.daTextarea.style.height = adjust;
        break;
    case document.encForm.daTextarea.name:
        if( (ht - dheight) > lht)
            textarea.style.height = (ht - dheight);
        else
            textarea.style.height = lht;
        var mhHeight = num(document.encForm.mhTextarea.style.height);
        var cpHeight = num(document.encForm.cpTextarea.style.height);
        if( ((mhHeight - dheight/2) > sht) && ((cpHeight - dheight/2) > sht)) {
            document.encForm.mhTextarea.style.height = (mhHeight - dheight/2);
            document.encForm.cpTextarea.style.height = (cpHeight - dheight/2);
        }else if( (mhHeight - dheight) > sht)
            document.encForm.mhTextarea.style.height = (mhHeight - dheight);
        else if( (cpHeight - dheight) > sht)
            document.encForm.cpTextarea.style.height = (cpHeight - dheight);
        else {
            var dif = (mhHeight - sht);
            document.encForm.mhTextarea.style.height = sht;
            if( (cpHeight - dif) > sht)
                document.encForm.cpTextarea.style.height = (cpHeight - (dheight - dif));
            else
                document.encForm.cpTextarea.style.height = sht;
        }
        break;
    case document.encForm.cpTextarea.name:
        if( (ht - dheight) > sht)
            textarea.style.height = (ht - dheight);
        else
            textarea.style.height = sht;
        var adjust = num(document.encForm.mhTextarea.style.height) + num(document.encForm.cpTextarea.style.height) + delta;
        document.encForm.daTextarea.style.height = adjust;
        break;
    case document.encForm.enTextarea.name:
        if( (ht - dheight) > sht)
            textarea.style.height = (ht - dheight);
        else
            textarea.style.height = sht;
        break;
    default:
        document.encForm.shTextarea.style.height = sht;
        document.encForm.cmTextarea.style.height = lht;
        document.encForm.fhTextarea.style.height = sht;
        document.encForm.mhTextarea.style.height = sht;
        document.encForm.daTextarea.style.height = lht;
        document.encForm.cpTextarea.style.height = sht;
        document.encForm.enTextarea.style.height = enht;
    }
}

function fullPage(textarea) {
    switch (textarea.name) {
    case document.encForm.shTextarea.name:
        textarea.style.height = pageht;
        document.encForm.cmTextarea.style.height = num(document.encForm.shTextarea.style.height) + num(document.encForm.fhTextarea.style.height) + delta;
        break;
    case document.encForm.fhTextarea.name:
        textarea.style.height = pageht;
        document.encForm.cmTextarea.style.height = num(document.encForm.shTextarea.style.height) + num(document.encForm.fhTextarea.style.height) + delta;
        break;
    case document.encForm.cmTextarea.name:
        var trueValue = deltaHeight;
        if(num(textarea.style.height) > pageht){
            deltaHeight = num(textarea.style.height) - pageht;
            shrinkTable(textarea);
        }else if(num(textarea.style.height) < pageht) {
            deltaHeight = pageht - num(textarea.style.height);
            expandTable(textarea);
        }else
            textarea.style.height = pageht;
        deltaHeight = trueValue;
        break;
    case document.encForm.mhTextarea.name:
        textarea.style.height = pageht;
        document.encForm.daTextarea.style.height = num(document.encForm.mhTextarea.style.height) + num(document.encForm.cpTextarea.style.height) + delta;
        break;
    case document.encForm.cpTextarea.name:
        textarea.style.height = pageht;
        document.encForm.daTextarea.style.height = num(document.encForm.mhTextarea.style.height) + num(document.encForm.cpTextarea.style.height) + delta;
        break;
    case document.encForm.daTextarea.name:
        var trueValue = deltaHeight;
        if(num(textarea.style.height) > pageht){
            deltaHeight = num(textarea.style.height) - pageht;
            shrinkTable(textarea);
        }else if(num(textarea.style.height) < pageht) {
            deltaHeight = pageht - num(textarea.style.height);
            expandTable(textarea);
        }else
            textarea.style.height = pageht;
        deltaHeight = trueValue;
        break;
    case document.encForm.enTextarea.name:
        textarea.style.height = pageht;
        break;
    default:
        document.encForm.shTextarea.style.height = sht;
        document.encForm.cmTextarea.style.height = lht;
        document.encForm.fhTextarea.style.height = sht;
        document.encForm.mhTextarea.style.height = sht;
        document.encForm.daTextarea.style.height = lht;
        document.encForm.cpTextarea.style.height = sht;
        document.encForm.enTextarea.style.height = enht;
    }
}

function collapse(textarea) {
    switch (textarea.name) {
    case document.encForm.shTextarea.name:
        textarea.style.height = sht;
        document.encForm.cmTextarea.style.height = num(document.encForm.fhTextarea.style.height) + sht + delta;
        break;
    case document.encForm.cmTextarea.name:
        textarea.style.height = lht;
        document.encForm.shTextarea.style.height = sht;
        document.encForm.fhTextarea.style.height = sht;
        break;
    case document.encForm.fhTextarea.name:
        textarea.style.height = sht;
        document.encForm.cmTextarea.style.height = num(document.encForm.shTextarea.style.height) + sht + delta;
        break;
    case document.encForm.mhTextarea.name:
        textarea.style.height = sht;
        document.encForm.daTextarea.style.height = num(document.encForm.cpTextarea.style.height) + sht + delta;
        break;
    case document.encForm.daTextarea.name:
        textarea.style.height = lht;
        document.encForm.mhTextarea.style.height = sht;
        document.encForm.cpTextarea.style.height = sht;
        break;
    case document.encForm.cpTextarea.name:
        textarea.style.height = sht;
        document.encForm.daTextarea.style.height = num(document.encForm.mhTextarea.style.height) + sht + delta;
        break;
    case document.encForm.enTextarea.name:
        textarea.style.height = enht;
        break;
    default:
        document.encForm.shTextarea.style.height = sht;
        document.encForm.cmTextarea.style.height = lht;
        document.encForm.fhTextarea.style.height = sht;
        document.encForm.mhTextarea.style.height = sht;
        document.encForm.daTextarea.style.height = lht;
        document.encForm.cpTextarea.style.height = sht;
        document.encForm.enTextarea.style.height = enht;
    }
}

function originalSize() {
    document.encForm.shTextarea.style.height = sht;
    document.encForm.cmTextarea.style.height = lht;
    document.encForm.fhTextarea.style.height = sht;
    document.encForm.mhTextarea.style.height = sht;
    document.encForm.daTextarea.style.height = lht;
    document.encForm.cpTextarea.style.height = sht;
    document.encForm.enTextarea.style.height = enht;
}

function hotkeySize(name, evnt) {
    if(evnt.which == 43 | evnt.which == 61)
        expandTable(name);
    if(evnt.which == 45)
        shrinkTable(name);

    /*alert('You have depressed a key');
    alert('event.which: '+evnt.which);
    alert('Event: '+evnt.modifiers);
    //alert('event.C_M: '+event.modifiers);
    if(evnt.modifiers == evnt.CONTROL_MASK)
        alert('you pressed Ctrl');
        */
}
