/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.util;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

public class SpringHibernateLocalSessionFactoryBean extends org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean {

	private static final Logger logger=MiscUtils.getLogger();
	
    public static final Map<Session, StackTraceElement[]> debugMap = Collections.synchronizedMap(new WeakHashMap<Session, StackTraceElement[]>());
    
    // This is a fake weak hash set, the value is actually ignored, put null or what ever in it.
    private static ThreadLocal<WeakHashMap<Session, Object>> sessions = new ThreadLocal<WeakHashMap<Session, Object>>();

	public static Session trackSession(Session session)
	{
        Thread currentThread=Thread.currentThread();
        debugMap.put(session, currentThread.getStackTrace());
        
        WeakHashMap<Session, Object> map=sessions.get();
        if (map==null)
        {
        	map=new WeakHashMap<Session, Object>();
        	sessions.set(map);
        }
        
        map.put(session, null);
        
        return(session);
	}
	
	public static void releaseThreadSessions()
	{
        try {
	        WeakHashMap<Session, Object> map=sessions.get();
	        if (map!=null)
	        {
	        	for (Session session : map.keySet())
	        	{
	        		try
	        		{
	        			if (session.isOpen())
	        			{
	        				session.close();
	        				logger.warn("Found lingering hibernate session. Closing session now.");
	        			}
	        		}
	        		catch (Exception e)
	        		{
	        			logger.error("Error closing hibernate session. (single instance)", e);
	        		}
	        	}
	        	
	        	sessions.remove();
	        }
        } catch (Exception e) {
	        logger.error("Error closing hibernate sessions. (outter loop)", e);
        }
	}
	
	public static class TrackingSessionFactory implements org.hibernate.SessionFactory
	{
		private SessionFactory sessionFactory=null;
		
		public TrackingSessionFactory(SessionFactory sessionFactory)
		{
			this.sessionFactory=sessionFactory;
		}
	
		public void close() throws HibernateException {
	        sessionFactory.close();
        }

		public void evict(Class arg0, Serializable arg1) throws HibernateException {
	        sessionFactory.evict(arg0, arg1);
        }

		public void evict(Class arg0) throws HibernateException {
	        sessionFactory.evict(arg0);
        }

		public void evictCollection(String arg0, Serializable arg1) throws HibernateException {
	        sessionFactory.evictCollection(arg0, arg1);
        }

		public void evictCollection(String arg0) throws HibernateException {
	        sessionFactory.evictCollection(arg0);
        }

		public void evictEntity(String arg0, Serializable arg1) throws HibernateException {
	        sessionFactory.evictEntity(arg0, arg1);
        }

		public void evictEntity(String arg0) throws HibernateException {
	        sessionFactory.evictEntity(arg0);
        }

		public void evictQueries() throws HibernateException {
	        sessionFactory.evictQueries();
        }

		public void evictQueries(String arg0) throws HibernateException {
	        sessionFactory.evictQueries(arg0);
        }

		public Map getAllClassMetadata() throws HibernateException {
	        return sessionFactory.getAllClassMetadata();
        }

		public Map getAllCollectionMetadata() throws HibernateException {
	        return sessionFactory.getAllCollectionMetadata();
        }

		public ClassMetadata getClassMetadata(Class arg0) throws HibernateException {
	        return sessionFactory.getClassMetadata(arg0);
        }

		public ClassMetadata getClassMetadata(String arg0) throws HibernateException {
	        return sessionFactory.getClassMetadata(arg0);
        }

		public CollectionMetadata getCollectionMetadata(String arg0) throws HibernateException {
	        return sessionFactory.getCollectionMetadata(arg0);
        }

		public Session getCurrentSession() throws HibernateException {
            return(trackSession(sessionFactory.getCurrentSession()));
        }

		public Set getDefinedFilterNames() {
	        return sessionFactory.getDefinedFilterNames();
        }

		public FilterDefinition getFilterDefinition(String arg0) throws HibernateException {
	        return sessionFactory.getFilterDefinition(arg0);
        }

		public Reference getReference() throws NamingException {
	        return sessionFactory.getReference();
        }

		public Statistics getStatistics() {
	        return sessionFactory.getStatistics();
        }

		public boolean isClosed() {
	        return sessionFactory.isClosed();
        }

		public Session openSession() throws HibernateException {
	        return(trackSession(sessionFactory.openSession()));
        }

		public Session openSession(Connection arg0, Interceptor arg1) {
	        return(trackSession(sessionFactory.openSession(arg0, arg1)));
        }

		public Session openSession(Connection arg0) {
	        return(trackSession(sessionFactory.openSession(arg0)));
        }

		public Session openSession(Interceptor arg0) throws HibernateException {
			return(trackSession(sessionFactory.openSession(arg0)));
        }

		public StatelessSession openStatelessSession() {
	        return sessionFactory.openStatelessSession();
        }

		public StatelessSession openStatelessSession(Connection arg0) {
	        return sessionFactory.openStatelessSession(arg0);
        }
	}
	
	@Override
	public SessionFactory newSessionFactory(Configuration config)
	{
		SessionFactory sf=super.newSessionFactory(config);
		
		return(new TrackingSessionFactory(sf));
	}
}
