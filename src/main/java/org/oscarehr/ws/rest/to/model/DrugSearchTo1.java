/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.ws.rest.to.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="drug")
public class DrugSearchTo1 implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer regionalId;

    private String atc;

    private String form;

    private Boolean active;

    private String name;

    private Integer category;

    private String genericName;

    private List<DrugComponentTo1> components;

    public DrugSearchTo1(){

        this.components = new ArrayList<DrugComponentTo1>();

    }

    public void addComponent(DrugComponentTo1 c){

        this.components.add(c);
    }

    public List<DrugComponentTo1> getComponents() {
        return components;
    }

    public void setComponents(List<DrugComponentTo1> components) {
        this.components = components;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public Integer getCategory(){

        return category;
    }

    public Integer getRegionalId() {
        return regionalId;
    }

    public void setRegionalId(Integer regional_id) {
        this.regionalId = regional_id;
    }

    public void setCategory(Integer c){
        this.category = c;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getAtc() {
        return atc;
    }

    public void setAtc(String atc) {
        this.atc = atc;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public static class DrugComponentTo1 implements Serializable{

        private String name;
        private Double strength;
        private String unit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getStrength() {
            return strength;
        }

        public void setStrength(Double strength) {
            this.strength = strength;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
