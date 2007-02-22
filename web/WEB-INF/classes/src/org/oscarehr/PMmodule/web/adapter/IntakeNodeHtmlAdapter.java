package org.oscarehr.PMmodule.web.adapter;

/**
 * Adapt intake nodes to html
 */
public interface IntakeNodeHtmlAdapter {

	/**
	 * Get builder for markup before visiting children
	 * 
	 * @return builder with markup
	 */
	public StringBuilder getPreBuilder();

	/**
	 * Get builder for markup after visiting children
	 * 
	 * @return builder with markup
	 */
	public StringBuilder getPostBuilder();

	/**
	 * Get number of tabs to indent
	 * 
	 * @return number of tabs to indent
	 */
	public int getIndent();

}