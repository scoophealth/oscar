package oscar.oscarRx.model;

import oscar.oscarRx.model.base.BaseFavorites;



public class Favorites extends BaseFavorites {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Favorites () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Favorites (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Favorites (
		java.lang.Integer id,
		java.lang.String providerNo,
		java.lang.String favoritename,
		java.math.BigDecimal gcnSeqno,
		boolean nosubs,
		boolean prn,
		java.lang.String special) {

		super (
			id,
			providerNo,
			favoritename,
			gcnSeqno,
			nosubs,
			prn,
			special);
	}

/*[CONSTRUCTOR MARKER END]*/


}