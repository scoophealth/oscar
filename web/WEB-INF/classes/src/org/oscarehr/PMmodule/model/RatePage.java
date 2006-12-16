package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseRatePage;

/**
 * This is the object class that relates to the rate_page table.
 * Any customizations belong here.
 */
public class RatePage extends BaseRatePage {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public RatePage () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public RatePage (java.lang.Integer _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public RatePage (
		java.lang.Integer _id,
		java.lang.Integer _visitors,
		java.lang.Integer _score,
		java.lang.String _pageName) {

		super (
			_id,
			_visitors,
			_score,
			_pageName);
	}

/*[CONSTRUCTOR MARKER END]*/
}