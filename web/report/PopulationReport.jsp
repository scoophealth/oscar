<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
	Copyright (c) 2001-2002.
	
	Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
	This software is published under the GPL GNU General Public License.
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
	See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
	
	OSCAR TEAM
	
	This software was written for Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada
-->
<%@ include file="/taglibs.jsp"%>
<html xmlns="http:‎//www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Population Report</title>
    <style type="text/css">
        <!--
        body {
            margin: 2.5% 5%;
            font-family: sans-serif;
        }
        
        h2 {
            text-align: center;
            text-transform: uppercase;
        }
        
        table {
            border: 1px solid black;
            border-collapse: collapse;
            margin: 0%;
            padding: 0%;
            width: 100%;
        }
        
        td,th {
            border: 1px solid black;
            font-weight: normal;
            margin: 0%;
            padding: 0.2em 0.5em;
            vertical-align: top;
            text-align: center;
        }
        
        thead th {
            background: #666666 none repeat scroll 0%;
            color: #FFFFFF;
            text-transform: uppercase;
        }
        
        tbody td {
            background: #CCCCCC none repeat scroll 0%;
        }
        
        tbody th {
            background: #999999 none repeat scroll 0%;
            width: 30%;
        }
        
        tbody tr.odd td {
            background: #EEEEEE none repeat scroll 0%;
        }
        
        tbody tr.odd th {
            background: #CCCCCC none repeat scroll 0%;
        }
        
        caption {
            font-size: 105%;
            text-align: left;
            text-transform: uppercase;
            letter-spacing: -1px;
        }
        -->
    </style>
</head>
<body>
    <table>
        <tr>
            <td>Health of the Homeless: CAISI Population Health Report</td>
            <td><c:out value="${date}"></c:out></td>
        </tr>
        <tr>
            <td>Number of Individuals with Specific Acute and Chronic Conditions who currently reside in Toronto Shelters which are part of the CAISI Project</td>
            <td><c:out value="${time}"></c:out></td>
        </tr>
    </table>
    <br />
    <!-- Shelter Population -->
    <table>
        <caption>Homeless Shelter Population</caption>
        <tbody>
            <tr>
                <th scope="row">Clients who used a CAISI shelter in the past year</th>
                <td><c:out value="${shelterPopulation.pastYear}"></c:out></td>
            </tr>
            <tr class="odd">
                <th scope="row">Current number of individuals in CAISI shelters</th>
                <td><c:out value="${shelterPopulation.current}"></c:out></td>
            </tr>
        </tbody>
    </table>
    <br />
    <!-- Shelter Usage -->
    <table>
        <caption>Intensity of Shelter Use</caption>
        <tbody>
            <tr>
                <th scope="row">Low Use: (1 - 10 days / 4 yr)</th>
                <td><c:out value="${shelterUsage.low}"></c:out></td>
            </tr>
            <tr class="odd">
                <th scope="row">Moderate Use: (11 - 179 days / 4 yr)</th>
                <td><c:out value="${shelterUsage.medium}"></c:out></td>
            </tr>
            <tr>
                <th scope="row">High Use: (180 - 730 days / 4 yr)</th>
                <td><c:out value="${shelterUsage.high}"></c:out></td>
            </tr>
        </tbody>
    </table>
    <br />
    <!-- Mortality -->
    <table>
        <caption>Mortality In Shelters</caption>
        <tbody>
            <tr>
                <th scope="row">Death Count (# of deaths in past year)</th>
                <td><c:out value="${mortalities.count}"></c:out></td>
            </tr>
            <tr class="odd">
                <th scope="row">Death Rate (# of deaths in past year / population)</th>
                <td><c:out value="${mortalities.percent}"></c:out></td>
            </tr>
        </tbody>
    </table>
    <br />
    <!-- Major Medical Condition -->
    <table>
        <caption>Major Medical Condition</caption>
        <thead>
            <tr>
                <th scope="col">Condition</th>
                <th scope="col">Number of Cases in CAISI Shelters</th>
                <th scope="col">Prevalence of Condition in CAISI Shelters</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach varStatus="status" var="condition" items="${majorMedicalConditions}">
            <c:choose><c:when test="${status.index % 2 == 0}"><tr></c:when><c:otherwise><tr class="odd"></c:otherwise></c:choose>
                <th scope="row"><c:out value="${condition.key}"></c:out></th>
                <td><c:out value="${condition.value.count}"></c:out></td>
                <td><c:out value="${condition.value.percent}"></c:out></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <br />
    <!-- Major Mental Illness -->
    <table>
        <caption>Major Mental Illness</caption>
        <thead>
            <tr>
                <th scope="col">Condition</th>
                <th scope="col">Number of Cases in CAISI Shelters</th>
                <th scope="col">Prevalence of Condition in CAISI Shelters</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach varStatus="status" var="condition" items="${majorMentalIllnesses}">
            <c:choose><c:when test="${status.index % 2 == 0}"><tr></c:when><c:otherwise><tr class="odd"></c:otherwise></c:choose>
                <th scope="row"><c:out value="${condition.key}"></c:out></th>
                <td><c:out value="${condition.value.count}"></c:out></td>
                <td><c:out value="${condition.value.percent}"></c:out></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <br />
    <!-- Serious Medical Conditions -->
    <table>
        <caption>Incidence in the past year of Serious Medical Conditions</caption>
        <thead>
            <tr>
                <th scope="col">Condition</th>
                <th scope="col">Number of Cases in CAISI Shelters</th>
                <th scope="col">Prevalence of Condition in CAISI Shelters</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach varStatus="status" var="condition" items="${seriousMedicalConditions}">
            <c:choose><c:when test="${status.index % 2 == 0}"><tr></c:when><c:otherwise><tr class="odd"></c:otherwise></c:choose>
                <th scope="row"><c:out value="${condition.key}"></c:out></th>
                <td><c:out value="${condition.value.count}"></c:out></td>
                <td><c:out value="${condition.value.percent}"></c:out></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>