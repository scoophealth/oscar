package oscar.caisi;

public class CaisiUtil
{
	public static String removeAttr(String str, String attr)
	{
	    if (str==null) return(null);
	    
		/*delete a parameter from query string*/
		int index,index1;
		String temps;
		index=str.indexOf(attr);
		if (index==-1) return str;
		temps=str.substring(index);
		index1=temps.indexOf("&");
		if (index1!=-1)  return str.substring(0,index)+temps.substring(index1+1);
		else {
			temps=str.substring(0,index);
			if (temps.endsWith("&")) return str.substring(0,index-1);
			else return temps;
		}
		
	}

}
