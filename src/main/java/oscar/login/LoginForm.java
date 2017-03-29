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


package oscar.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class LoginForm extends ActionForm {
  private String username;
  private String password;
  private String pin;
  private String propname;
  
  private String oldPassword;
  private String newPassword;
  private String confirmPassword;
  
  private String oldPin;
  private String newPin;
  private String confirmPin;
  
  
  //validate the input data, will change based on struts 1.1
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
    ActionErrors err = new ActionErrors();
    if(propname!=null && !propname.endsWith(".properties") ) {
      propname += ".properties";
    }
    if(username==null || username.length()==0 || username.length()>15 ||
       password==null || password.length()==0 || password.length()>10 ||
       (pin!=null && pin.length()>6) ||
       propname==null || propname.length()==0 || propname.length()>50 ) {
      //err.add(ActionErrors.GLOBAL_ERROR, new ActionError("failed", "") );
//      reset();
    }
    return err;
  }

  //reset the login fields
  public void reset (ActionMapping mapping, HttpServletRequest request) {
    reset();
  }

  public String getUsername() {
    return username;
  }
  public String getPassword() {
    return password;
  }
  public String getPin() {
    return pin;
  }
  public String getPropname() {
    return propname;
  }

  public void setUsername(String str) {
    username = str;
  }
  public void setPassword(String str) {
    password = str;
  }
  public void setPin(String str) {
    pin = str;
  }
  public void setPropname(String str) {
    propname = str;
  }

  private void reset() {
    username = "";
    password = "";
    pin = "";
    propname = "";
  }

public String getOldPassword() {
	return oldPassword;
}

public void setOldPassword(String oldPassword) {
	this.oldPassword = oldPassword;
}

public String getNewPassword() {
	return newPassword;
}

public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
}

public String getConfirmPassword() {
	return confirmPassword;
}

public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
}

public String getOldPin() {
	return oldPin;
}

public void setOldPin(String oldPin) {
	this.oldPin = oldPin;
}

public String getNewPin() {
	return newPin;
}

public void setNewPin(String newPin) {
	this.newPin = newPin;
}

public String getConfirmPin() {
	return confirmPin;
}

public void setConfirmPin(String confirmPin) {
	this.confirmPin = confirmPin;
}



}
