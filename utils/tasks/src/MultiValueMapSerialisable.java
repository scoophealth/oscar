import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * GAH, the apache MultiValueMap is not serialisable, I need my own.
 */
public class MultiValueMapSerialisable implements Serializable {
	
	private HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
	
    public void put(String key, String value)
	{
		ArrayList<String> al=map.get(key);
		if (al==null)
		{
			al=new ArrayList<String>();
			map.put(key, al);
		}
		
		al.add(value);
	}

    public Set<String> keySet()
    {
    	return(map.keySet());
    }
    
    public ArrayList<String> getCollection(String key)
    {
    	return(map.get(key));
    }
}
