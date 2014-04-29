/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.integration.mcedt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

public class DummyXMLGregorianCalendar extends XMLGregorianCalendar {

    private int year;
    private int month;
    private int day;

    public DummyXMLGregorianCalendar() {
    }

    public DummyXMLGregorianCalendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

    @Override
    public void setYear(BigInteger year) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public void setTimezone(int offset) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setHour(int hour) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setMinute(int minute) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setSecond(int second) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setMillisecond(int millisecond) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setFractionalSecond(BigDecimal fractional) {
        // TODO Auto-generated method stub
    }

    @Override
    public BigInteger getEon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public BigInteger getEonAndYear() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public int getTimezone() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHour() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMinute() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSecond() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public BigDecimal getFractionalSecond() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int compare(XMLGregorianCalendar xmlGregorianCalendar) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public XMLGregorianCalendar normalize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toXMLFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QName getXMLSchemaType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void add(Duration duration) {
        // TODO Auto-generated method stub
    }

    @Override
    public GregorianCalendar toGregorianCalendar() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GregorianCalendar toGregorianCalendar(TimeZone timezone, Locale aLocale, XMLGregorianCalendar defaults) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TimeZone getTimeZone(int defaultZoneoffset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object clone() {
        // TODO Auto-generated method stub
        return null;
    }
}
