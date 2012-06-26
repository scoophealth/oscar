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

package oscar.oscarBilling.ca.bc.pageUtil;

import java.util.Map;

import org.displaytag.decorator.TableDecorator;
import org.oscarehr.common.model.BillingService;

/**
A helper class for the displayTag Tag Library used to List of java beans for augmented tabular display
**/
public class BillCodesTableWrapper
    extends TableDecorator {
  public BillCodesTableWrapper() {
  }

  public String getBillingserviceNo() {
    BillingService bcd = (BillingService) this.getCurrentRowObject();
    String links = "<a href=\"billingEditCode.jsp?codeId=" + bcd.getBillingserviceNo() +
    "&code=" + bcd.getServiceCode()+"&desc=" + bcd.getDescription() + "&value=" + bcd.getValue() + "&whereTo=private\">Edit</a> <br>" +
    "<a href=\"deletePrivateCode.jsp?code=" + bcd.getBillingserviceNo() + "\">Delete</a>";
    return links;
  }

  public String getAssociationStatus(){
    String ret = "";
    Map map = (Map)this.getCurrentRowObject();
    ret = "<a href=\"#\" onClick=\"editAssociation('" + map.get("billingServiceNo") + "','" + map.get("billingServiceTrayNo") + "'); return false;\">Edit</a>";
    ret+="<br />";
    ret +="<a href=\"#\" onClick=\"deleteAssociation('" + map.get("id") + "'); return false;\">Delete</a>";
    return ret;
  }
}
