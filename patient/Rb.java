import java.io.*;
// input the read file name 2001/12/30

class Rb {
//	String strMonth[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	//String strField[] = {"Key", "Last name", "First name", "Given name", "ADDRESS1", "ADDRESS2", "Postal", "City", "Province", "COUNTRY", "PHONE1", "PHONE2", "HNON", "Ver", "SEX", "DOB"        , "Patient Status", "PCN","ROSTER STATUS", "family_doctor"};
//	int    nEndNum[]  = {   10,          41,           72,           103,         144,        185,      193,    214,        235,       246,      267,      288,    304,   308,   312, 315, 319, 324,             339,   343,            357,    377}; //col-1

	public void go(String [] arg){
		try {
			File file = new File(""+(arg.length<1?"test.txt":arg[0]));
//			File file = new File(""+(arg.length<1?"test.TXT":arg[0]));
			if(!file.isFile() || !file.canRead()) {
				throw new IOException();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileWriter inf = new FileWriter("sc.jsp");

			String aline, asql, temp="", address2="";
			
			while (true) {
				aline = raf.readLine(); 
				if(aline!=null){
					asql= replaceString(aline, "\"datafld='", "'\"", "\"value=\\\"\"+SxmlMisc.getReadableString(SxmlMisc.getXmlContent(content,\"", "\"))+\"\\\"\" " )+"\n";
					  //"value=\  " "+SxmlMisc.getReadableString(SxmlMisc.getXmlContent(content,"xml_baobs"))   +" \ " "
					inf.write(asql);

				}else {
					break;
				}
			}
			
			inf.close();
			raf.close();
		} catch(IOException e) {}
	}
	public String replaceString(String a, String oldS1, String oldS2, String newS1, String newS2) {
		int i = a.indexOf(oldS1);  
		
		if(i==-1) return a;
		int ie= a.substring(i).indexOf(oldS2); 
		String temp="", temp1="", temp2="", oldS="";
    temp1 = a.substring(0,i);
    temp2 = a.substring(i).substring(ie+oldS2.length()); 
		oldS = a.substring(i).substring(0+oldS1.length(),ie);

		temp+=temp1+ newS1+ oldS +newS2 +temp2;
    System.out.println(oldS);
		return temp;
	}

	public String rreplaceString(String a, String oldS1, String oldS2, String newS) {
		int i = a.indexOf(oldS1);  
		
		if(i==-1) return a;
		int ie= a.substring(i).indexOf(oldS2); 
		String temp="", temp1="", temp2="";
    temp1 = a.substring(0,i);
    temp2 = a.substring(i).substring(ie+oldS2.length()); 

		temp+=temp1+ newS +temp2;
    System.out.println(temp);
		return temp;
	}

	public static void main(String[] arg) {
		Rb aa =new Rb();
		aa.go(arg);
	}
		
}
