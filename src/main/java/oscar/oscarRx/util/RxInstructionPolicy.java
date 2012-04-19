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


package oscar.oscarRx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oscar.OscarProperties;

public class RxInstructionPolicy {

	public static List<String> checkInstructions(String instr) {
		List<String> errors =  new ArrayList<String>();
				
		String policies  = OscarProperties.getInstance().getProperty("rx.policy");
		if(policies != null) {
			String[] policiesArray = policies.split(",");
			for(String policy:policiesArray) {
				if(policy.equalsIgnoreCase("stjoes")) {
					applyStJoesPolicy(instr,errors);
				}
			}
		}

		return errors;
	}
	
	public static void applyStJoesPolicy(String instr, List<String> errors) {
		//unit
		addPolicy(new String[]{"\\s+(?i)U\\b","\\b\\d+(?i)U\\b"},instr,errors,"U","Unit");
		addPolicy(new String[]{"\\s+(?i)I.U.","\\b\\d+(?i)I.U.\\b"},instr,errors,"I.U.","Unit");
		
		//subcutaneous
		addPolicy(new String[]{"\\s+(?i)S.C.\\b","^(?i)S.C.\\b"},instr,errors,"S.C.","Subcutaneous or subcut");		 
		addPolicy(new String[]{"\\s+(?i)SC\\b","^(?i)SC\\b"},instr,errors,"SC","Subcutaneous or subcut");		 
				
		//CC
		addPolicy(new String[]{"\\s+(?i)CC\\b","\\b\\d+(?i)CC\\b"},instr,errors,"CC","ml or mL");
		addPolicy(new String[]{"\\s+C.C.","\\b\\d+(?i)C.C.\\b"},instr,errors,"C.C.","ml or mL");
		
		//µg
		addPolicy(new String[]{"µg"},instr,errors,"µg","microgram or mcg");		 
		
		//MS
		//addPolicy(new String[]{"\\s+MS\\s+","\\s+MS$","^MS\\s+","^MS$"},instr,errors,"MS","Morphine or morphine sulphate");		 
		
		//MgSO4
		//addPolicy(new String[]{"\\s+MgSO4\\s+","\\s+MgSO4$","^MgSO4\\s+","^MgSO4$"},instr,errors,"MgSO4","Magnesium sulphate");		 
		
		//>
		addPolicy(new String[]{">"},instr,errors,">","greater than");		 
		
		//>
		addPolicy(new String[]{"<"},instr,errors,"<","less than");		 
		
		//@
		addPolicy(new String[]{"@"},instr,errors,"@","at");
		
		//A.S.
		addPolicy(new String[]{"\\s+(?i)A\\.\\.?S.?\\s+","\\s+(?i)A\\.\\.?S.?$","^(?i)A\\.\\.?S.?\\s+","^(?i)A\\.\\.?S.?$"},instr,errors,"A.S.","left ear");				
		
		//A.D.
		addPolicy(new String[]{"\\s+(?i)AD.?\\s+","\\s+(?i)AD.?$","^s+(?i)AD.?\\s+","^(?i)AD.?$","\\s+(?i)A\\.\\.?D.?\\s+","\\s+(?i)A\\.\\.?D.?$","^s+(?i)A\\.\\.?D.?\\s+","^(?i)A\\.\\.?D.?$"},instr,errors,"A.D.","right ear");		
		
		//A.U.
		addPolicy(new String[]{"\\s+(?i)A.?U.?\\s+","\\s+(?i)A.?U.?$","^(?i)A.?U.?\\s+","^(?i)A.?U.?$"},instr,errors,"A.U.","both ears");		
		
		//d.0 should be d
		addPolicy(new String[]{"\\b\\.0\\b"},instr,errors,"10.0","10");		
		
		//.d should be 0.d
		addPolicy(new String[]{"\\s+\\.\\d+","^\\.\\d+"},instr,errors,".1","0.1");		
		
		//O.S.
		addPolicy(new String[]{"\\s+(?i)O.?S.?\\s+","\\s+(?i)O.?S.?$","^(?i)O.?S.?\\s+","^(?i)O.?S.?$"},instr,errors,"O.S.","left eye");		
		
		//O.D.
		addPolicy(new String[]{"\\s+(?i)O.?D.?\\s+","\\s+(?i)O.?D.?$","^(?i)O.?D.?\\s+","^(?i)O.?D.?$"},instr,errors,"O.D.","right eye");		
		
		//O.U.
		addPolicy(new String[]{"\\s+(?i)O.?U.?\\s+","\\s+(?i)O.?U.?$","^(?i)O.?U.?\\s+","^(?i)O.?U.?$"},instr,errors,"O.U.","both eyes");		
		
		//q.o.d
		addPolicy(new String[]{"\\s+(?i)q.?o.?d.?\\s+","\\s+(?i)q.?o.?d.?$","^(?i)q.?o.?d.?\\s+","^(?i)q.?o.?d.?$"},instr,errors,"q.o.d","every other day");		
		
		//q.d
		addPolicy(new String[]{"\\s+(?i)qd.?\\s+","\\s+(?i)qd.?$","^s+(?i)qd.?\\s+","^(?i)qd.?$","\\s+(?i)q\\.\\.?d.?\\s+","\\s+(?i)q\\.\\.?d.?$","^s+(?i)q\\.\\.?d.?\\s+","^(?i)q\\.\\.?d.?$"},instr,errors,"q.d","daily");
		
		//o.d.
		addPolicy(new String[]{"\\s+(?i)o.?d.?\\s+","\\s+(?i)o.?d.?$","^(?i)o.?d.?\\s+","^(?i)o.?d.?$"},instr,errors,"o.d.","daily");		
		
	}
	
	private static void addPolicy(String[] patterns, String instructions, List<String> errors,String violation, String replacement) {		
		for(String p:patterns) {
			Pattern p1 = Pattern.compile(p);
	        Matcher m1 = p1.matcher(instructions);
	        if (m1.find()) {
	        	errors.add("Policy Violation: '"+violation+ ((replacement!=null)?"'\nPlease use: "+replacement+"":""));
	        }
		}
	}
	
}
