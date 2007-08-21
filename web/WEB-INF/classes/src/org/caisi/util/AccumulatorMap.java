package org.caisi.util;

import java.util.Map;

/**
 * This class is meant to be a map of accumulators. Essentially
 * all this is is a TreeMap<K,Integer> where Long is a counter.
 * non-existant entries have a null value adverse to 0. 
 * This class is unsynchronised.
 */
public class AccumulatorMap<K> extends java.util.TreeMap<K,Integer>
{
    /**
     * This method will increment the value associated with 
     * this key by 1. If the key doesn't exist it will create a new
     * entry initialised to a value of 1.
     */
    public void increment(K key)
    {
        increment(key, 1);
    }
    
    /**
     * This method will increment the value associated with 
     * this key by the value passed in. If the key doesn't
     * exist it will create a new entry initialised to the value passed in.
     */
    public void increment(K key, int value)
    {
        Integer previousValue=get(key);
        
        if (previousValue==null) put(key, value);
        else put(key, previousValue+value);
    }
    
    /**
     * This method sums up all the values in this map.
     */
	public int getTotalOfAllValues()
	{
		int total=0;
		for (Integer i :values()) total=total+i;
		return(total);
	}
	
	/**
	 * This method adds the passed in map to this map, i.e. duplicate
	 * keys will have their values added together, non duplicate keys will
	 * just be copied over directly as if the other had 0.
	 */
	public void addAccumulator(AccumulatorMap<K> accumulatorMap)
	{
		for (Map.Entry<K, Integer> entry : accumulatorMap.entrySet())
		{
			increment(entry.getKey(), entry.getValue());
		}
	}
}