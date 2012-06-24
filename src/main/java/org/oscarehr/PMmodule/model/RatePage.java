/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the rate_page table.
 * Any customizations belong here.
 */
public class RatePage implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer _id;// fields
    private Integer _visitors;
    private Integer _score;
    private String _pageName;

    // constructors
    public RatePage () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public RatePage (Integer _id) {
        this.setId(_id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public RatePage (
            Integer _id,
            Integer _visitors,
            Integer _score,
            String _pageName) {

        this.setId(_id);
        this.setVisitors(_visitors);
        this.setScore(_score);
        this.setPageName(_pageName);
        initialize();
    }


    protected void initialize () {}

    /**
     * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
     */
    public Integer getId () {
        return _id;
    }

    /**
     * Set the unique identifier of this class
     * @param _id the new ID
     */
    public void setId (Integer _id) {
        this._id = _id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
     * Return the value associated with the column: visitors
     */
    public Integer getVisitors () {
        return _visitors;
    }

    /**
     * Set the value related to the column: visitors
     * @param _visitors the visitors value
     */
    public void setVisitors (Integer _visitors) {
        this._visitors = _visitors;
    }

    /**
     * Return the value associated with the column: score
     */
    public Integer getScore () {
        return _score;
    }

    /**
     * Set the value related to the column: score
     * @param _score the score value
     */
    public void setScore (Integer _score) {
        this._score = _score;
    }

    /**
     * Return the value associated with the column: page_name
     */
    public String getPageName () {
        return _pageName;
    }

    /**
     * Set the value related to the column: page_name
     * @param _pageName the page_name value
     */
    public void setPageName (String _pageName) {
        this._pageName = _pageName;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof RatePage)) return false;
        else {
            RatePage mObj = (RatePage) obj;
            if (null == this.getId() || null == mObj.getId()) return false;
            else return (this.getId().equals(mObj.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}
