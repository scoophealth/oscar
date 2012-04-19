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


package org.oscarehr.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;

/**
 * This is a hash map which limits the number of items with in. The intended use is for a LRU cache.
 * Implementation will be hashmaps behind the scene, when one fills the next is used, when all fills 
 * an entire pool is emptied.  
 * 
 * There are reasonable limits, i.e. there's no point in having 10 pools and max items of 10....
 * like wise even 10 pools and 100 items is fairly silly. In general the pool sizes should be large, 
 * and the number of pools should be small. Something like 1000 items or more in 4 pools is good. 
 * you should never really want more than 10 pools and no less than 3.
 * 
 * General times for lookup and insert combination (as you would do to search cache then populate it)
 * is roughly 1000 *nano* seconds overhead (on a core i5). This was tested between 5 to 10 pools on random integers 
 * over flowing the pool size, like random.nextInt(2000) where pool size is 1000. This is roughly 2x slower than
 * a regular hashMap (i.e. 500ns) but .5x faster then a WeakHashMap i.e. (1500ns). This is a replacement for the
 * TimeClearedHashMap and this is generally much more efficient although timing does not always reflect this as time
 * cleared hashmap uses a background thread to clear based on time only and does not support size.  
 */
public final class QueueCache<K, V>
{
	private static Logger logger = MiscUtils.getLogger();
	private static Timer timer = null;

	private class ShiftTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			try
			{
				shiftPools();
			}
			catch (Exception e)
			{
				logger.error("Error", e);
			}
		}
	}

	private HashMap<K, V>[] data;

	private int maxPoolSize;

	public QueueCache(int pools, int objectsToCache, long maxTimeToCache)
	{
		this(pools, objectsToCache);

		synchronized (QueueCache.class)
		{
			if (timer == null) timer = new Timer(QueueCache.class.getName(), true);
		}
		timer.schedule(new ShiftTimerTask(), maxTimeToCache / pools, maxTimeToCache / pools);
	}

	@SuppressWarnings("unchecked")
	public QueueCache(int pools, int objectsToCache)
	{
		data = new HashMap[pools];

		maxPoolSize = Math.max(10, objectsToCache / pools);

		for (int i = 0; i < pools; i++)
		{
			data[i] = new HashMap<K, V>((int) (maxPoolSize * 1.5));
		}
	}

	public synchronized void put(K key, V value)
	{
		Map<K, V> pool = data[0];

		pool.put(key, value);

		// shuffle pools if required.
		if (pool.size() > maxPoolSize) shiftPools();
	}

	private synchronized void shiftPools()
	{
		for (int i = data.length - 1; i > 0; i--)
		{
			data[i] = data[i - 1];
		}

		data[0] = new HashMap<K, V>((int) (maxPoolSize * 1.5));
	}

	public synchronized void remove(K key)
	{
		for (int i = 0; i < data.length; i++)
		{
			data[i].remove(key);
		}
	}

	public synchronized V get(K key)
	{
		V result;

		for (int i = 0; i < data.length; i++)
		{
			result = data[i].get(key);
			if (result != null) return(result);
		}

		return(null);
	}

	public synchronized int size()
	{
		int count = 0;

		for (int i = 0; i < data.length; i++)
		{
			count = count + data[i].size();
		}

		return(count);
	}

	public static void main(String... argv) throws InterruptedException
	{
		WeakHashMap<Integer, Integer> test1 = new WeakHashMap<Integer, Integer>();
		HashMap<Integer, Integer> test2 = new HashMap<Integer, Integer>();
		QueueCache<Integer, Integer> test3 = new QueueCache<Integer, Integer>(5, 1000, 10000);
//		TimeClearedHashMap<Integer, Integer> test4 = new TimeClearedHashMap<Integer, Integer>(10000, 1000);
//		TimeClearedHashMap2<Integer, Integer> test5 = new TimeClearedHashMap2<Integer, Integer>(1000, 10000);

		for (int i = 0; i < 100; i++)
		{
			test1.put(i, i);
			test2.put(i, i);
			test3.put(i, i);
//			test4.put(i, i);
//			test5.put(i, i);
		}

		long time1 = 0;
		long time2 = 0;
		long time3 = 0;
		long time4 = 0;
		long time5 = 0;
		Random rand = new Random();

		for (int i = 0; i < 10000; i++)
		{
			Integer x = rand.nextInt(2000);

			long start = System.nanoTime();
			Integer y = test1.get(x);
			if (y == null) test1.put(x, x);
			time1 = time1 + (System.nanoTime() - start);

			start = System.nanoTime();
			y = test2.get(x);
			if (y == null) test2.put(x, x);
			time2 = time2 + (System.nanoTime() - start);

			start = System.nanoTime();
			y = test3.get(x);
			if (y == null) test3.put(x, x);
			time3 = time3 + (System.nanoTime() - start);

//			start = System.nanoTime();
//			y = test4.get(x);
//			if (y == null) test4.put(x, x);
//			time4 = time4 + (System.nanoTime() - start);
//
//			start = System.nanoTime();
//			y = test5.get(x);
//			if (y == null) test5.put(x, x);
//			time5 = time5 + (System.nanoTime() - start);
		}

		logger.info("test1=" + (time1 / 10000f));
		logger.info("test2=" + (time2 / 10000f));
		logger.info("test3=" + (time3 / 10000f));
		logger.info("test4=" + (time4 / 10000f));
		logger.info("test5=" + (time5 / 10000f));

		for (int i = 0; i < 21; i++)
		{
			logger.info("test3.size=" + test3.size());
			Thread.sleep(500);
		}
	}
}
