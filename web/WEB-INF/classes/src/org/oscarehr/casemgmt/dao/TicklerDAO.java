package org.oscarehr.casemgmt.dao;

import java.util.List;

import org.caisi.model.CustomFilter;


/**
 * DAO interface for working with Tickler table in OSCAR
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 */
public interface TicklerDAO extends DAO {

	public List getTicklers(CustomFilter filter);
}
