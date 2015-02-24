/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

public class BillingReviewCodeItem {
	String codeName;
	String codeUnit;
	String codeFee;
	String codeTotal;
	String codeAt;
	String msg;
	String codeDescription;
	
	public String getCodeAt() {
		return codeAt;
	}
	public void setCodeAt(String codeAt) {
		this.codeAt = codeAt;
	}
	public String getCodeFee() {
		return codeFee;
	}
	public void setCodeFee(String codeFee) {
		this.codeFee = codeFee;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeUnit() {
		return codeUnit;
	}
	public void setCodeUnit(String codeUnit) {
		this.codeUnit = codeUnit;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCodeTotal() {
		return codeTotal;
	}
	public void setCodeTotal(String codeTotal) {
		this.codeTotal = codeTotal;
	}
	public String getCodeDescription() {
		return codeDescription;
	}
	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}
	
}
