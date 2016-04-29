<%--

    Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
    
    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada
    
--%>
<style>
.spinner {
	padding: 20px;
    position: fixed;
    top: 50%;
    left: 50%;
    margin-left: -50px; /* half width of the spinner gif */
    margin-top: -50px; /* half height of the spinner gif */
    text-align:center;
    z-index:1234;
    overflow: auto;
    width: 130px; /* width of the spinner gif */
}

.screen
{
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            height: 100%;
            width: 100%;
            margin: 0;
            padding: 0;
            background: #ffffff;
            opacity: .8;
            filter: alpha(opacity=80);
            -moz-opacity: .8;
            z-index: 101;
            display: none;
 }
</style>
<script src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>

<script type="text/javascript">

// if locked is ture: can't click away
// if lock is false: can click away from it
function ShowSpin(locked)
{
    $("#screen").show();
    $("#spinner").fadeIn(300);

    if (locked)
    {
        $("#screen").unbind("click");
    }
    else
    {
        $("#screen").click(function (e)
        {
            HideSpin();
        });
    }
}

function HideSpin()
{
    $("#screen").hide();
    $("#spinner").fadeOut(300);
}
</script>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
    <div id="screen" class="screen"></div>
<div id="spinner" class="spinner" style="display:none;">
    <img id="img-spinner" src="<%=request.getContextPath() %>/mcedt/web/img/spinner.gif" alt="Loading"/>
</div>