/*
 * Copyright (c) 2007-2008. MB Software Vancouver, Canada. All Rights Reserved.
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
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * MB Software
 * Vancouver, B.C., Canada 
 */

package org.oscarehr.util;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimeClearedHashMap<K, V> extends HashMap<K, V> {
    private static Timer timer = new Timer(TimeClearedHashMap.class.getName(), true);

    /** E should be the item added and Long should be the time it was added in ms */
    private HashMap<K, Long> data = new HashMap<K, Long>();

    private TimerTask timerTask = new TimerTask() {
        public void run() {
            removeOld();
        }
    };

    private long maxDataAge = -1;

    /**
     * @param maxDataAge
     *            in ms
     * @param checkPeriod
     *            in ms
     */
    public TimeClearedHashMap(long maxDataAge, long checkPeriod) {
        this.maxDataAge = maxDataAge;
        timer.schedule(timerTask, checkPeriod, checkPeriod);
    }

    public void removeOld() {
        try {
            long tooOld = System.currentTimeMillis() - maxDataAge;

            Iterator<K> it = data.keySet().iterator();
            while (it.hasNext()) {
                K key = it.next();
                Long value = data.get(key);

                if (value < tooOld) {
                    it.remove();
                    remove(key);
                }
            }
        }
        catch (ConcurrentModificationException e) {
            // that's okay, we'll try again next time.
        }
    }

    public V put(K key, V value) {
        data.put(key, System.currentTimeMillis());
        return (super.put(key, value));
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            data.put(key, System.currentTimeMillis());
        }
        super.putAll(m);
    }

    // @Test
    // public void test() throws InterruptedException {
    public static void main(String... argv) throws Exception { 
        TimeClearedHashMap<String,String> map = new TimeClearedHashMap<String,String>(1000, 1000);
        map.put("foo", "FOO");
        // assertEquals(1, map.size());
        if (map.size()!=1) throw(new IllegalStateException());

        Thread.sleep(600);
        map.put("bar", "BAR");
        // assertEquals(2, map.size());
        if (map.size()!=2) throw(new IllegalStateException());        
        
        Thread.sleep(600);
        // assertEquals(1, map.size());
        if (map.size()!=1) throw(new IllegalStateException());        
        if (map.data.size()!=1) throw(new IllegalStateException());        
        map.put("bar", "BAR");
        map.put("blah", "BLAH");
        // assertEquals(2, map.size());
        if (map.size()!=2) throw(new IllegalStateException());        
        if (map.data.size()!=2) throw(new IllegalStateException());
        
        if (!map.get("bar").equals("BAR")) throw(new IllegalStateException());
        
        System.err.println("all good");
    }
    
}