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
package org.oscarehr.ws.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.impl.cookie.DateUtils;
import org.oscarehr.common.model.DrugProduct;
import org.oscarehr.managers.DrugDispensingManager;
import org.oscarehr.managers.DrugProductManager;
import org.oscarehr.ws.rest.to.DrugProductResponse;
import org.oscarehr.ws.rest.to.model.DrugProductTo1;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/productDispensing")
@Component("productDispensingService")
public class ProductDispensingService extends AbstractServiceImpl{

	@Autowired
	private DrugDispensingManager drugDispensingManager;
	
	@Autowired 
	private DrugProductManager drugProductManager;
	
	@GET
	public String helloWorld() {
		return "Hello World " + drugDispensingManager;
	}
	
	@POST
	@Path("/saveDrugProduct")
	@Produces("application/json")
	@Consumes("application/x-www-form-urlencoded")
	public DrugProductResponse saveDrugProduct(MultivaluedMap<String, String> params) throws Exception {
		
		DrugProduct drugProduct = new DrugProduct();
		drugProduct.setId(Integer.parseInt(params.getFirst("product.id")));
		drugProduct.setName(params.getFirst("product.name"));
		drugProduct.setCode(params.getFirst("product.code"));
		drugProduct.setLocation(Integer.parseInt(params.getFirst("product.location")));
		drugProduct.setLotNumber(params.getFirst("product.lotNumber"));
		drugProduct.setExpiryDate(DateUtils.parseDate(params.getFirst("product.expiryDate"),new String[]{"yyyy-MM-dd"}));
		
		DrugProduct result = null;
		
		if(drugProduct.getId()>0) {
			//edit
			DrugProduct savedDrugProduct = drugProductManager.getDrugProduct(getLoggedInInfo(),drugProduct.getId());
			savedDrugProduct.setName(drugProduct.getName());
			savedDrugProduct.setCode(drugProduct.getCode());
			savedDrugProduct.setLocation(drugProduct.getLocation());
			savedDrugProduct.setLotNumber(drugProduct.getLotNumber());
			savedDrugProduct.setExpiryDate(drugProduct.getExpiryDate());
			
			drugProductManager.updateDrugProduct(getLoggedInInfo(),savedDrugProduct);
			result = savedDrugProduct;
		} else  {
			drugProduct.setId(null);
			drugProductManager.saveDrugProduct(getLoggedInInfo(),drugProduct);
			result = drugProduct;
		}
		
		
		return getDrugProduct(result.getId());
	}
	
	@GET
	@Path("/drugProduct/{drugProductId}")
	@Produces("application/json")
	public DrugProductResponse getDrugProduct(@PathParam("drugProductId") Integer drugProductId) {
		DrugProduct result = drugProductManager.getDrugProduct(getLoggedInInfo(),drugProductId);
		DrugProductResponse response = new DrugProductResponse();
		DrugProductTo1 to = new DrugProductTo1();
		BeanUtils.copyProperties(result, to);
		response.getContent().add(to);
		return response;
	}
	
	@GET
	@Path("/drugProducts")
	@Produces("application/json")
	public DrugProductResponse getAllDrugProducts(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit ) {
		List<DrugProduct> results = drugProductManager.getAllDrugProducts(getLoggedInInfo(),offset,limit);
		DrugProductResponse response = new DrugProductResponse();
		for(DrugProduct result:results) {
			DrugProductTo1 to = new DrugProductTo1();
			BeanUtils.copyProperties(result, to);
			response.getContent().add(to);
		}
		return response;
	}
	
	@GET
	@Path("/drugProducts/byCode")
	@Produces("application/json")
	public DrugProductResponse getAllDrugProductsGroupedByCode(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit ) {
		List<DrugProduct> results = drugProductManager.getAllDrugProductsGroupedByCode(getLoggedInInfo(),offset,limit);
		DrugProductResponse response = new DrugProductResponse();
		for(DrugProduct result:results) {
			DrugProductTo1 to = new DrugProductTo1();
			BeanUtils.copyProperties(result, to);
			response.getContent().add(to);
		}
		return response;
	}
	
}
