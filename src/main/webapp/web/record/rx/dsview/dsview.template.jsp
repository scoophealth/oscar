<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
<div class="modal-header">
	
	<h2>{{$ctrl.alert.heading}}</h2>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row">
		
		
		<blockquote>
  		<p>{{$ctrl.alert.summary}}</p>
  		<footer>Source: {{$ctrl.alert.messageSource}} Author: {{$ctrl.alert.author}}</footer>
		</blockquote>
		<div class="lead"></div>
		<div class="well" ng-bind-html="$ctrl.alert.body"></div>
		<hr>
		<%-- pre>{{$ctrl.alert | json}}</pre> --%>
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box --%>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Hide</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




