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
import java.util.WeakHashMap;

/**
 * This is a hash map for which every entry is timestamped upon it being added to the hashmap.
 * There is a reaper thread which runs periodically removing entries older than a certain age.
 * <br /><br />
 * Note this does not limit the number of entries in the map so you should use this only 
 * in places which you can for the most part guarantee that the memory usage won't be a problem. 
 */
public class TimeClearedHashMap<K, V> extends HashMap<K, V> {
	private static Timer timer = new Timer(TimeClearedHashMap.class.getName(), true);

	/** E should be the item added and Long should be the time it was added in ms */
	private WeakHashMap<K, Long> data = new WeakHashMap<K, Long>();

	private TimerTask timerTask = new TimerTask() {
		public void run() {
			removeOld();
		}
	};

	private long maxDataAge = -1;

	/**
	 * @param maxDataAge in ms
	 * @param checkPeriod in ms (make sure you don't thrash your jvm)
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
		} catch (ConcurrentModificationException e) {
			// that's okay, we'll try again next time.
		}
	}

	@Override
    public V put(K key, V value) {
		V result = super.put(key, value);
		data.put(key, System.currentTimeMillis());
		return (result);
	}

	@Override
    public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
		for (K key : m.keySet()) {
			data.put(key, System.currentTimeMillis());
		}
	}

}