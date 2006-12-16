package org.oscarehr.PMmodule.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedType;

public class BedType extends BaseBedType {

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BedType() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedType(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedType(java.lang.Integer id, java.lang.String name, boolean m_default) {
		super(id, name, m_default);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}