// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.PrintStream;
import java.util.*;
import java.lang.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;

public class EctValidation{

    public String regCharacterExp = "^[0-9a-zA-Z]*$";        

    EctValidation(){
    }
    

    public String getRegCharacterExp(){
        return this.regCharacterExp;
    }
              
            
    public boolean matchRegExp(String regExp, String inputValue){

        boolean validation = true; 
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        System.out.println("matchRegExp function is called.");
        
        if (!gValidator.isBlankOrNull(regExp) && !gValidator.isBlankOrNull(inputValue)){
            System.out.println("both the regExp and inputValue is not blank nor null.");
            if(!inputValue.matches(regExp)){
                System.out.println("Regexp not matched");                                            
                validation=false;
                
            }

        }
        return validation;
     }            
}
