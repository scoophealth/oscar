package org.oscarehr.PMmodule.common;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;

public class IntakeInstanceTag extends SimpleTagSupport {

	private int base;
	private IntakeInstance instance;

	public void setBase(int base) {
		this.base = base;
	}
		
	public void setInstance(IntakeInstance instance) {
		this.instance = instance;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print(toHtml());
	}
	
	String toHtml() {
		StringBuilder html = new StringBuilder();
		toHtml(html, base, instance.getNode());
		
		return html.toString();
	}

	void toHtml(StringBuilder builder, int indent, IntakeNode node) {
		IntakeNodeHtmlAdapter htmlAdapter = node.getType().getHtmlAdapter(indent, instance, node);

		builder.append(htmlAdapter.getPreBuilder());

		for (IntakeNode child : node.getChildren()) {
			toHtml(builder, htmlAdapter.getIndent(), child);
		}

		builder.append(htmlAdapter.getPostBuilder());
	}

}
