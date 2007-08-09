package org.caisi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import oscar.util.SqlUtils;

public class RedirectLinkTrackingDao {

	private SessionFactory sessionFactory = null;

	public SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	public void addInstanceOfRedirect(Date date, int providerId, int redirectLinkId) {

		Session session = sessionFactory.openSession();
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = session.connection();
			ps = c.prepareStatement("insert into RedirectLinkTracking values (?,?,?)");
			ps.setTimestamp(1, new Timestamp(date.getTime()));
			ps.setInt(2, providerId);
			ps.setInt(3, redirectLinkId);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw (new HibernateException(e));
		}
		finally {
			// don't close hibernate connections.
			SqlUtils.closeResources(null, ps, null);
			session.close();
		}
	}
}
