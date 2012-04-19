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


package oscar.oscarResearch.oscarDxResearch.pageUtil;
import org.apache.struts.action.ActionForm;

public final class dxResearchForm extends ActionForm {

    //Using map-backed method to get the value of each field
    //key: the field property
    //value: the value of the associated key
/*    private final Map values = new HashMap();

    public void setValue(String key, Object value) {
        values.put(key, value);
    }

    public Object getValue(String key) {
        return values.get(key);
    }
*/
    private String demographicNo;
    private String providerNo;
    private String xml_research1;
    private String xml_research2;
    private String xml_research3;
    private String xml_research4;
    private String xml_research5;
    private String quickList;
    private String[] quickListItems;
    private String forward;
    private String curCodingSystem;
    
    public String getDemographicNo() {
        return demographicNo;
    }    
    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }
    
    public String getProviderNo() {
        return providerNo;
    }    
    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }
    
    public String getXml_research1() {
        return xml_research1;
    }    
    public void setXml_research1(String xml_research1) {
        this.xml_research1 = xml_research1;
    }
    
    public String getXml_research2() {
        return xml_research2;
    }    
    public void setXml_research2(String xml_research2) {
        this.xml_research2 = xml_research2;
    }
    
    public String getXml_research3() {
        return xml_research3;
    }    
    public void setXml_research3(String xml_research3) {
        this.xml_research3 = xml_research3;
    }
    
    public String getXml_research4() {
        return xml_research4;
    }    
    public void setXml_research4(String xml_research4) {
        this.xml_research4 = xml_research4;
    }
    
    public String getXml_research5() {
        return xml_research5;
    }    
    public void setXml_research5(String xml_research5) {
        this.xml_research5 = xml_research5;
    }
     
    public String getQuickList() {
        return quickList;
    }    
    public void setQuickList(String quickList) {
        this.quickList = quickList;
    }
    
    public String[] getQuickListItems() {
        return quickListItems;
    }    
    public void setQuickListItems(String[] quickListItems) {
        this.quickListItems = quickListItems;
    }
    
    public String getForward() {
        return forward;
    }    
    public void setForward(String forward) {
        this.forward = forward;
    }
    
    public String getSelectedCodingSystem() {
        return curCodingSystem;
    }
    
    public void setSelectedCodingSystem( String cs ) {
        curCodingSystem = cs;
    }
    
}
