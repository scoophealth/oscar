package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.TicklerLink;
import org.springframework.stereotype.Repository;

@Repository
public class TicklerLinkDao extends AbstractDao<TicklerLink>{

	public TicklerLinkDao() {
		super(TicklerLink.class);
	}

	public TicklerLink getTicklerLink(Integer id) {
	    return find(id);
	}

	public List<TicklerLink> getLinkByTableId(Integer tableName, Long tableId) {
		Query query = entityManager.createQuery("SELECT cLink from TicklerLink WHERE cLink.tableName = ? and cLink.tableId = ? order by cLink.id");
		query.setParameter(1, tableName);
		query.setParameter(2,tableId);

		@SuppressWarnings("unchecked")
		List<TicklerLink> results = query.getResultList();

		return results;
	}

	public List<TicklerLink> getLinkByTickler(Integer ticklerNo) {
		Query query = entityManager.createQuery("SELECT cLink from TicklerLink WHERE cLink.ticklerNo = ? order by cLink.id");
		query.setParameter(1, ticklerNo);

		@SuppressWarnings("unchecked")
		List<TicklerLink> results = query.getResultList();
		return results;
	}

	public void save(TicklerLink cLink) {
	    persist(cLink);
	}

	public void update(TicklerLink cLink) {
	    merge(cLink);
	}
}
