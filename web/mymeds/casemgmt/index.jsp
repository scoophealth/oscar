<!--
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
-->


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title  >MyMeds Case Management</title>
        <script language="javascript" type="text/javascript">
            var Dom = {
                 get: function(e) {
                     if (typeof e === 'string') {
                        return document.getElementById(e);
                     } else {
                        return e;
                     }
                 },
                 add: function(e, d) {
                     var e = this.get(e);
                     var d = this.get(d);
                     d.appendChild(e);
                     e.focus();
                 },
                 remove: function(e) {
                     var e = this.get(e);
                     e.parentNode.removeChild(e);
                 },
                 expand: function(e){
                     document.getElementById('contentdisplay').innerHTML='';
                     var e=this.get(e);
                     document.getElementById('contentdisplay').appendChild(e);
                     e.setAttribute('height','100%');

                 }

            };
            var Event = {
                 add: function() {
                     if (window.addEventListener) {
                         return function(el, type, fn) {
                            Dom.get(el).addEventListener(type, fn, false);
                         };
                     } else if (window.attachEvent) {
                         return function(el, type, fn) {
                             var f = function() {
                                fn.call(Dom.get(el), window.event);
                             };
                                Dom.get(el).attachEvent('on' + type, f);
                         };
                     }
                 }()
            };

            ///////////////////////////////////////////////////////////////////////////////////////////
            Event.add(window, 'load', function() {
                //PERSONAL INFORMATION
                 Event.add('personalinfo', 'click', function() {
                     var existing=document.getElementById('pi_display');
                     if(existing){}
                     else{
                             var el = document.createElement('div');
                             el.setAttribute('id','pi_display');
                             el.innerHTML = "<a id='pi_close' href='javascript:void(0);'>Hide Personal Info</a>\n\
         <a id='pi_expand' href='javascript:void(0);'>Expand Personal Info</a><iframe src='./casemgmt/test.jsp' width='100%' height='300'>\n\
             <p>Your browser doesn't support iframe.</p></iframe>"
                             Dom.add(el, 'contentdisplay');
                             var close=document.getElementById('pi_close');
                             Event.add(close, 'click', function(e) {
                                Dom.remove(el);
                             });
                             var expand=document.getElementById('pi_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                    }
                 });
                 //MESSAGES
                 Event.add('msg', 'click', function() {
                     var existing=document.getElementById('msg_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id', 'msg_display');
                     el.innerHTML = "<a id='msg_close' href='javascript:void(0);'>Hide Messages</a>\n\
<a id='msg_expand' href='javascript:void(0);'>Expand Message</a> <iframe src='./casemgmt/test.jsp' width='100%' height='300'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('msg_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                             var expand=document.getElementById('msg_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                     }
                 });
                 //DOCUMENTS
                 Event.add('documents', 'click', function() {
                     var existing=document.getElementById('documents_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id','documents_display');
                     el.innerHTML = "<a id='documents_close' href='javascript:void(0);'>Hide Documents</a>\n\
<a id='documents_expand' href='javascript:void(0);'>Expand Documents</a>  <iframe src='./casemgmt/test.jsp' width='100%' height='300'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('documents_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                             var expand=document.getElementById('documents_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                     }
                 });
                 //MEDICATION
                 Event.add('med', 'click', function() {
                     var existing=document.getElementById('med_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id', 'med_display');
                     el.innerHTML = "<a id='med_close' href='javascript:void(0);'>Hide Medication</a>\n\
<a id='med_expand' href='javascript:void(0);'>Expand Medication</a>   <iframe src='./casemgmt/test.jsp' width='100%' height='300'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('med_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                             var expand=document.getElementById('med_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                     }
                 });
                 //MEASUREMENT
                 Event.add('meas', 'click', function() {
                     var existing=document.getElementById('meas_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id', 'meas_display');
                     el.innerHTML = "<a id='meas_close' href='javascript:void(0);'>Hide Measurements</a>\n\
<a id='meas_expand' href='javascript:void(0);'>Expand Measurements</a>   <iframe src='./casemgmt/test.jsp' width='100%' height='300'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('meas_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                             var expand=document.getElementById('meas_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                     }});
                 //BLOOD PRESSURE
                 Event.add('bp', 'click', function() {
                     var existing=document.getElementById('bp_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id', 'bp_display');
                     el.innerHTML = "<a id='bp_close' href='javascript:void(0);'>Hide Blood Pressure</a>\n\
<a id='bp_expand' href='javascript:void(0);'>Expand Blood Pressure</a>   <iframe src='./casemgmt/test.jsp' width='100%' height='300'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('bp_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                             var expand=document.getElementById('bp_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                     }});
                 //SURVEYS
                 Event.add('surveys', 'click', function() {
                     var existing=document.getElementById('surveys_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id', 'surveys_display');
                     el.innerHTML = "<a id='surveys_close' href='javascript:void(0);'>Hide Surveys</a>\n\
<a id='surveys_expand' href='javascript:void(0);'>Expand Surveys</a>   <iframe src='./casemgmt/test.jsp' width='100%' height='300'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('surveys_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                             var expand=document.getElementById('surveys_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                    }
                 });
            });

        </script>
    </head>
    <body>
        <h2 align="center" >MyMeds Case Management</h2>
        <table width="100%">
            <tr>
                <td width="30%" >
                    <table>
                        <tr><td><a id="personalinfo" href="javascript:void(0);" onclick="">Personal Info</a></td></tr>
                        <tr><td><a id="msg" href="javascript:void(0);" onclick="">Messages</a></td></tr>
                        <tr><td><a id="documents" href="javascript:void(0);" onclick="">Documents</a></td></tr>
                        <tr><td><a id="med" href="javascript:void(0);" onclick="">Medication</a></td></tr>
                        <tr><td><a id="meas" href="javascript:void(0);" onclick="">Measurements</a></td></tr>
                        <tr><td><a id="bp" href="javascript:void(0);" onclick="">Blood Pressure</a></td></tr>
                        <tr><td><a id="surveys" href="javascript:void(0);" onclick="">Surveys</a></td></tr>
                    </table>
                </td>
                <td width="70%">
                    <table id="contentdisplay" width="100%"></table>
                </td>
            </tr>
        </table>
    </body>
</html>
