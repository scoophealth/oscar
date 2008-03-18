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
package oscar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

class Readdb {
	String strMonth[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	//String strField[] = {"Key", "Last name", "First name", "Given name", "ADDRESS1", "ADDRESS2", "Postal", "City", "Province", "COUNTRY", "PHONE1", "PHONE2", "HNON", "Ver", "SEX", "DOB"};
	int    nEndNum[]  = {   16,          27,           38,           59,         90,        104,      112,    128,        144,       155,      166,      177,    188,   192,   196, 199, 203, 207};

	public String getMySQLDate(String effdate) {
		String temp="";
		if((effdate.equals(""))) return ("\\N");

		if(effdate.substring(7,9).equals("00")) {temp="20";}
		else {temp=effdate.substring(7,9);}
		temp+=effdate.substring(9)+"-";
		for(int im=0; im<strMonth.length; im++) {
			if(strMonth[im].equalsIgnoreCase(effdate.substring(3,6))) {
				temp=temp+(im+1);
				break;
			}
    }
		temp+="-" + effdate.substring(0,2);
		//System.out.println(temp);
		return temp;
	}

	public void go(String [] arg){
		try {
//			File file = new File(""+(arg.length<1?"SFHCDEMO.TXT":arg[0]));
			File file = new File(""+(arg.length<1?"test.TXT":arg[0]));
			//File file = new File(""+(arg.length<1?"cpp.txt":arg[0]));
			if(!file.isFile() || !file.canRead()) {
				throw new IOException();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileWriter inf = new FileWriter("insertdemo.sql");

      //String sqltxt = insert into demographic values(demographic_no ,'last_name ','first_name ','address',  'city','province', 'postal','phone','phone2',
                                                  //'year_of_birth','month_of_birth','date_of_birth',  'hin','ver',  'roster_status','patient_status','date_joined','chart_no','provider_no', 
                                                  //'sex','end_date','eff_date','pcn_indicator','hc_type','hc_renew_date','family_doctor'); 

			String aline, asql, temp="";
			String cppid, lastname,firstname,address,city,province,postal,phone,phone2,ext,yob,mob,dob;
			String hin,ver,roster,patient,datejoined, chart,staff,sex,enddate,effdate,pcn,hctype,hcrenewdate ;
			aline=raf.readLine();
			aline=raf.readLine();
			System.out.println(aline);
			aline=raf.readLine();
			//System.out.println(aline);
			
			while (true) {
				aline = raf.readLine(); 
				if(aline!=null){
					if(aline.substring(0,16).trim().equals("") || aline.substring(0,16).trim().startsWith("PAGE") || aline.substring(0,16).trim().startsWith("Key")) 
					  continue; //skip empty line, etc.

					//cppid=getMySQLnull();
					//datejoined=getMySQLDate(aline.substring(233,245).trim());
					//enddate=getMySQLDate(aline.substring(264,276).trim());
					//effdate=getMySQLDate(aline.substring(276,288).trim());
					//hcrenewdate=getMySQLDate(aline.substring(313).trim());
					dob=getMySQLDate(aline.substring(196).trim());
					
					asql="insert into demographic values(" + aline.substring(0,nEndNum[0]).trim() + ",'" + aline.substring(nEndNum[0],nEndNum[1]).trim() + "','" +
													 aline.substring(nEndNum[1],nEndNum[2]).trim() + "','" + aline.substring(nEndNum[4],nEndNum[5]).trim() +", "+ aline.substring(nEndNum[2],nEndNum[4]).trim()+ "','" +
													 aline.substring(nEndNum[6],nEndNum[7]).trim() + "','" + aline.substring(nEndNum[7],nEndNum[8]).trim() + "','" +
													 aline.substring(nEndNum[5],nEndNum[6]).trim()+ "','" + aline.substring(nEndNum[9],nEndNum[10]).trim() + "','" + aline.substring(nEndNum[10],nEndNum[11]).trim() + "','" +
													 aline.substring(nEndNum[16],nEndNum[17]).trim()+ "','" + aline.substring(nEndNum[15],nEndNum[16]).trim() + "','" + aline.substring(nEndNum[14],nEndNum[15]).trim() + "','" +
													 aline.substring(nEndNum[11],nEndNum[12]).trim() + "','" + aline.substring(nEndNum[12],nEndNum[13]).trim() + "','','','','','','" +
													 aline.substring(nEndNum[13],nEndNum[14]).trim() + "','','','','','',''); \n"	  ;
													 //aline.substring(200,204).trim() + "','" + aline.substring(204,218).trim() + "','" +
													 //aline.substring(218,233).trim() + "','" + datejoined + "','" +
													 //aline.substring(245,254).trim() + "','" + aline.substring(254,260).trim() + "','" +
													 //aline.substring(260,264).trim() + "','" + enddate + "','" +
													 //effdate + "','" + aline.substring(288,302).trim() + "','" +
													 //aline.substring(302,313).trim() + "','" + hcrenewdate  + "'); \n"	  ;
					inf.write(asql);
					effdate="";
					//System.out.println(asql);

				}else {
					break;
				}
			}
			
			inf.close();
			raf.close();
		} catch(IOException e) {}
	}

	public static void main(String[] arg) {
		Readdb aa =new Readdb();
		aa.go(arg);
	}
		
}
