package org.oscarehr.common.dao;

import org.oscarehr.common.model.Favorite;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteDao extends AbstractDao<Favorite>{

	public FavoriteDao() {
		super(Favorite.class);
	}
}
