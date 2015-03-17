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
import org.oscarehr.common.model.DrugProductTemplate;
import org.oscarehr.common.model.ProductLocation;
import org.oscarehr.managers.DrugDispensingManager;
import org.oscarehr.managers.DrugProductManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.DrugProductResponse;
import org.oscarehr.ws.rest.to.DrugProductTemplateResponse;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.ProductLocationResponse;
import org.oscarehr.ws.rest.to.model.DrugProductTemplateTo1;
import org.oscarehr.ws.rest.to.model.DrugProductTo1;
import org.oscarehr.ws.rest.to.model.ProductLocationTo1;
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
		drugProduct.setAmount(Integer.parseInt(params.getFirst("product.amount")));
		
		
		int totalToCreate = 1;
		if(params.getFirst("productBulkTotal") != null) {
			try {
				totalToCreate = Integer.parseInt(params.getFirst("productBulkTotal"));
			}catch(NumberFormatException e) {
				MiscUtils.getLogger().error("invalid number to create, defaulting to 1");
			}
		}
		
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
			for(int x=0;x<totalToCreate;x++) {
				drugProduct.setId(null);
				drugProductManager.saveDrugProduct(getLoggedInInfo(),drugProduct);
				result = drugProduct;
			}
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
	public DrugProductResponse getAllDrugProducts(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,
			@QueryParam("limitByName") String limitByName,@QueryParam("limitByLot") String limitByLot,@QueryParam("limitByLocation") String limitByLocation,
			@QueryParam("availableOnly") boolean availableOnly) {
		List<DrugProduct> results = null;
		
		int count = drugProductManager.getAllDrugProductsByNameAndLotCount(getLoggedInInfo(),limitByName,limitByLot,(limitByLocation!=null && limitByLocation.length()>0)?Integer.valueOf(limitByLocation):null,availableOnly);
		results = drugProductManager.getAllDrugProductsByNameAndLot(getLoggedInInfo(),offset,limit,limitByName, limitByLot, (limitByLocation!=null && limitByLocation.length()>0)?Integer.valueOf(limitByLocation):null,availableOnly);
		
		DrugProductResponse response = new DrugProductResponse();
		for(DrugProduct result:results) {
			DrugProductTo1 to = new DrugProductTo1();
			BeanUtils.copyProperties(result, to);
			response.getContent().add(to);
		}
		response.setTotal(count);
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
	
	@GET
	@Path("/drugProducts/uniqueNames")
	@Produces("application/json")
	public AbstractSearchResponse<String> getUniqueDrugProductNames( ) {
		List<String> results = drugProductManager.findUniqueDrugProductNames(getLoggedInInfo());
		AbstractSearchResponse<String> response = new AbstractSearchResponse<String>();
		for(String result:results) {
			response.getContent().add(result);
		}
		return response;
	}
	
	@GET
	@Path("/drugProducts/uniqueLots")
	@Produces("application/json")
	public AbstractSearchResponse<String> getUniqueDrugProducLotsByName(@QueryParam("name") String name ) {
		
		List<String> results = drugProductManager.findUniqueDrugProductLotsByName(getLoggedInInfo(),name);
		AbstractSearchResponse<String> response = new AbstractSearchResponse<String>();
		for(String result:results) {
			response.getContent().add(result);
		}
		return response;
	}
	
	@GET
	@Path("/deleteDrugProduct/{drugProductId}")
	@Produces("application/json")
	public GenericRESTResponse deleteDrugProduct(@PathParam("drugProductId") Integer drugProductId)  {
		drugProductManager.deleteDrugProduct(getLoggedInInfo(), drugProductId);
		
		GenericRESTResponse response = new GenericRESTResponse();
		response.setMessage("Product deleted");
		
		
		return response;
	}
	
	@GET
	@Path("/productLocations")
	@Produces("application/json")
	public ProductLocationResponse listProductLocations()  {
		List<ProductLocation> productLocations = drugProductManager.getProductLocations();
		
	
		ProductLocationResponse response = new ProductLocationResponse();
		
		for(ProductLocation result:productLocations) {
			ProductLocationTo1 to = new ProductLocationTo1();
			BeanUtils.copyProperties(result, to);
			response.getProductLocations().add(to);
		}
		
		return response;
	}
	
	@GET
	@Path("/status/{drugId}")
	@Produces("application/json")
	public GenericRESTResponse getDispensingStatus(@PathParam("drugId") Integer drugId)  {
		GenericRESTResponse response = new GenericRESTResponse();
		response.setMessage(drugDispensingManager.getStatus(drugId));
		
		return response;
	}
	
	@GET
	@Path("/drugProductTemplates")
	@Produces("application/json")
	public DrugProductTemplateResponse listProductTemplates()  {
		List<DrugProductTemplate> templates = drugProductManager.getDrugProductTemplates();
		
	
		DrugProductTemplateResponse response = new DrugProductTemplateResponse();
		
		for(DrugProductTemplate result:templates) {
			DrugProductTemplateTo1 to = new DrugProductTemplateTo1();
			BeanUtils.copyProperties(result, to);
			response.getTemplates().add(to);
		}
		
		return response;
	}
}
