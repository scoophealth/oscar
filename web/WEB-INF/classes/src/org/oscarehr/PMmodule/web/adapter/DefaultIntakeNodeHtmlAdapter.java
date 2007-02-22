package org.oscarehr.PMmodule.web.adapter;

public class DefaultIntakeNodeHtmlAdapter extends BaseIntakeNodeHtmlAdapter {

	public DefaultIntakeNodeHtmlAdapter(int indent) {
		super(indent);
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		return preBuilder;
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPostBuilder()
	 */
	public StringBuilder getPostBuilder() {
		return postBuilder;
	}

}