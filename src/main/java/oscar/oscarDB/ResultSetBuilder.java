/*
 * Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows
 *    these conditions in the documentation and/or other materials
 *    provided with the distribution.
 *
 * 3. The name "JDOM" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact <request_AT_jdom_DOT_org>.
 *
 * 4. Products derived from this software may not be called "JDOM", nor
 *    may "JDOM" appear in their name, without prior written permission
 *    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 * In addition, we request (but do not require) that you include in the
 * end-user documentation provided with the redistribution and/or in the
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *     JDOM Project (http://www.jdom.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos
 * available at http://www.jdom.org/images/logos.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the JDOM Project and was originally
 * created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 * Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 * on the JDOM Project, please see <http://www.jdom.org/>.
 */

package oscar.oscarDB;

import java.util.Map.Entry;
import java.util.SortedMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.oscarehr.common.model.ProviderLabRoutingModel;

import oscar.util.Textualizer;

/**
 * <p><code>ResultSetBuilder</code> builds a JDOM tree from a 
 * <code>java.sql.ResultSet</code>.  Many good ideas were leveraged from
 * SQLBuilder written from Jon Baer.</p>
 *
 * Notes:
 *   Uses name returned by rsmd.getColumnName(), not getColumnLabel() 
 *   because that is less likely to be a valid XML element title.
 *   Null values are given empty bodies, but you can mark them as empty with
 *   an attribute using the setNullAttribute() method.  Be aware that databases
 *   may change the case of column names.  setAsXXX() methods are case
 *   insensitive on input column name.  Assign each one a proper output name
 *   if you're worried.  Only build() throws JDOMException.  Any exceptions 
 *   encountered in the set methods are thrown during the build().  
 *   The setAsXXX(String columnName, ...) methods do not verify that a column 
 *   with the given name actually exists.
 *
 *   Still needs method-by-method Javadocs.
 * <p>
 * Issues: 
 *   Do attributes have to be added in a namespace?
 *
 * @author Jason Hunter
 * @author Jon Baer
 * @author David Bartle
 * @author Robert J. Munro
 * @version 0.5
 */
public class ResultSetBuilder {

	/** The ResultSet that becomes a <code>Document</code> */
	private ProviderLabRoutingModel rs;

	/** The <code>Namespace</code> to use for each <code>Element</code> */
	private Namespace ns = Namespace.NO_NAMESPACE;

	/** The maximum rows to return from the result set */
	int maxRows = Integer.MAX_VALUE; // default to all

	/** Name for the root <code>Element</code> of the <code>Document</code> */
	private String rootName = "result";

	/** Name for the each immediate child <code>Element</code> of the root */
	private String rowName = "entry";

	/** Name for attribute to mark that a field was null */
	private String nullAttribName = null;

	/** Value for attribute to mark that a field was null  */
	private String nullAttribValue = null;

	/**
	 * <p>
	 *   This sets up a <code>java.sql.ResultSet</code> to be built
	 *   as a <code>Document</code>.
	 * </p>
	 *
	 * @param rs <code>java.sql.ResultSet</code> to build
	 */
	public ResultSetBuilder(ProviderLabRoutingModel rs) {
		this.rs = rs;
	}

	/**
	 * <p>
	 *   This sets up a <code>java.sql.ResultSet</code> to be built
	 *   as a <code>Document</code>.
	 * </p>
	 *
	 * @param rs <code>java.sql.ResultSet</code> to build from
	 * @param rootName <code>String</code> name for the root
	 * <code>Element</code> 
	 * of the <code>Document</code>
	 * @param rowName <code>String</code> name for the each immediate child 
	 * <code>Element</code> of the root
	 */
	public ResultSetBuilder(ProviderLabRoutingModel rs, String rootName, String rowName) {
		this(rs);
		setRootName(rootName);
		setRowName(rowName);
	}

	/**
	 * <p>
	 *   This sets up a <code>java.sql.ResultSet</code> to be built
	 *   as a <code>Document</code>.
	 * </p>
	 *
	 * @param rs <code>java.sql.ResultSet</code> to build from
	 * @param rootName <code>String</code> name for the root
	 * <code>Element</code> 
	 * of the <code>Document</code>
	 * @param rowName <code>String</code> name for the each immediate child 
	 * <code>Element</code> of the root
	 * @param ns <code>Namespace</code> to use for each <code>Element</code>
	 */
	public ResultSetBuilder(ProviderLabRoutingModel rs, String rootName, String rowName, Namespace ns) {
		this(rs, rootName, rowName);
		setNamespace(ns);
	}

	/**
	 * <p>
	 *   This builds a <code>Document</code> from the
	 *   <code>java.sql.ResultSet</code>.
	 * </p>
	 *
	 * @return <code>Document</code> - resultant Document object.
	 * @throws <code>JDOMException</code> when there is a problem
	 *                                    with the build.
	 *
	 */
	public Document build() throws JDOMException {
		try {
			Element root = new Element(rootName, ns);
			Document doc = new Document(root);
			
			// build the org.jdom.Document out of the result set 
			String name;
			String value;
			Element entry;
			Element child;

			Textualizer txt = new Textualizer();

			SortedMap<String, String> map;
			try {
				map = txt.toMap(rs);
			} catch (Exception e1) {
				throw new JDOMException("Unable to convert to map", e1);
			}

			entry = new Element(rowName, ns);
			for (Entry<String, String> e : map.entrySet()) {
				name = e.getKey();
				value = e.getValue();

				child = new Element(name, ns);

				if (value == null || value.isEmpty()) {
					if (nullAttribName != null) {
						child.setAttribute(nullAttribName, nullAttribValue);
					}
				} else {
					child.setText(value);
				}
				entry.addContent(child);
			}
			root.addContent(entry);
		

			return doc;
		} catch (Exception e) {
			throw new JDOMException("Database problem", e);
		}
	}

	/**
	 * Set the name to use as the root element in
	 * the <code>Document</code>.
	 *
	 * @param rootName <code>String</code> the new name.
	 *
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	/**
	 * Set the name to use as the row element in
	 * the <code>Document</code>.
	 *
	 * @param rowName <code>String</code> the new name.
	 *
	 */
	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	/**
	 * <p>
	 *   Set the <code>Namespace</code> to use for
	 *   each <code>Element</code> in the  <code>Document</code>.
	 * </p>
	 *
	 * @param ns <code>String</code> the namespace to use.
	 *
	 */
	public void setNamespace(Namespace ns) {
		this.ns = ns;
	}

	/**
	* <p>
	*   Set the maximum number of rows to add to your
	*   <code>Document</code>.
	* </p>
	*
	* @param maxRows <code>int</code>
	*
	*/
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	/**
	 * <p>
	 *   Set a specific attribute to use to mark that a value in the 
	 *   database was null, not just an empty string.  This is necessary
	 *   because &lt;foo/&gt; semantically represents both null and empty.
	 *   This method lets you have &lt;foo null="true"&gt;.
	 * </p>
	 *
	 * @param nullAttribName <code>String</code> name of attribute to add
	 * @param nullAttribValue <code>String</code> value to set it to.
	 *
	 */
	public void setNullAttribute(String nullAttribName, String nullAttribValue) {
		this.nullAttribName = nullAttribName;
		this.nullAttribValue = nullAttribValue;
	}

}
