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
package org.oscarehr.ws.rest.to.model;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class PrintRxTo1 {
	private Integer demographic;
	private Integer drugId;
	private Float width;
	private Float height;
	private List<PrintPointTo1> printPoints;
	private boolean autoPrint = false;
	private float[] xPolygonCoords;
	private float[] yPolygonCoords;
	

	public List<PrintPointTo1> getPrintPoints() {
		return printPoints;
	}

	public void setPrintPoints(List<PrintPointTo1> printPoints) {
		this.printPoints = printPoints;
	}

	public int getDemographic() {
		return demographic;
	}

	public void setDemographic(int demographic) {
		this.demographic = demographic;
	}

	public Integer getDrugId() {
		return drugId;
	}

	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public boolean isAutoPrint() {
		return autoPrint;
	}

	public void setAutoPrint(boolean autoPrint) {
		this.autoPrint = autoPrint;
	}

	public float[] getxPolygonCoords() {
		return xPolygonCoords;
	}

	public void setxPolygonCoords(float[] xPolygonCoords) {
		this.xPolygonCoords = xPolygonCoords;
	}

	public float[] getyPolygonCoords() {
		return yPolygonCoords;
	}

	public void setyPolygonCoords(float[] yPolygonCoords) {
		this.yPolygonCoords = yPolygonCoords;
	}
	
	@Override
    public String toString(){
		return(ReflectionToStringBuilder.toString(this));
	}
	
}
