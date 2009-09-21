/*
 * Copyright (c) 2007-2008. MB Software Vancouver, Canada. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * This software was written for 
 * MB Software
 * Vancouver, B.C., Canada 
 */

package org.oscarehr.util;

import java.util.Map;

/**
 * This class is meant to be a map of accumulators. Essentially
 * all this is is a TreeMap<K,Integer> where Integer is a counter.
 * non-existant entries have a null value adverse to 0. 
 * This class is unsynchronised.
 */
public class AccumulatorMap<K> extends java.util.TreeMap<K, Integer> {
    /**
     * This method will increment the value associated with 
     * this key by 1. If the key doesn't exist it will create a new
     * entry initialised to a value of 1.
     */
    public void increment(K key) {
        increment(key, 1);
    }

    /**
     * This method will increment the value associated with 
     * this key by the value passed in. If the key doesn't
     * exist it will create a new entry initialised to the value passed in.
     */
    public void increment(K key, int value) {
        Integer previousValue = get(key);

        if (previousValue == null) put(key, value);
        else put(key, previousValue + value);
    }

    /**
     * This method sums up all the values in this map.
     */
    public int getTotalOfAllValues() {
        int total = 0;
        for (Integer i : values()) total = total + i;
        return(total);
    }

    /**
     * This method adds the passed in map to this map, i.e. duplicate
     * keys will have their values added together, non duplicate keys will
     * just be copied over directly as if the other had 0.
     */
    public void addAccumulator(AccumulatorMap<K> accumulatorMap) {
        for (Map.Entry<K, Integer> entry : accumulatorMap.entrySet()) {
            increment(entry.getKey(), entry.getValue());
        }
    }
}
