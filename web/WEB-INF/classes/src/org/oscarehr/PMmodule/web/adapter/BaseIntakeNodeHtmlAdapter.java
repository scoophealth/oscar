package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;

public abstract class BaseIntakeNodeHtmlAdapter implements IntakeNodeHtmlAdapter {

	protected static final String EOL = System.getProperty("line.separator");
	protected static final String TAB = "\t";

	protected int indent;
	protected IntakeInstance instance;
	protected IntakeNode node;
	protected StringBuilder preBuilder;
	protected StringBuilder postBuilder;
	
	protected BaseIntakeNodeHtmlAdapter(int indent) {
		this(indent, null, null);
	}
	
	public BaseIntakeNodeHtmlAdapter(int indent, IntakeInstance instance, IntakeNode node) {
		this.indent = indent;
		this.instance = instance;
		this.node = node;
		
		this.preBuilder = new StringBuilder();
		this.postBuilder = new StringBuilder();
	}
	

	public int getIndent() {
		return indent;
	}
	
	protected boolean isEqual(String value) {
		IntakeAnswer answer = instance.getAnswerMapped(node.getIdStr());
		return answer.isEqual(value);
	}
	
	protected boolean isFirstChild() {
		return node.getIndex() == 0;
	}

	protected boolean isLastChild() {
		return node.getIndex() == (node.getNumSiblings() - 1);
	}

	protected boolean isParentPage() {
		return node.getParent().getType().isPageType();
	}

	protected boolean isParentSection() {
		return node.getParent().getType().isSectionType();
	}

	protected void beginTag() {
		indent += 1;
	}

	protected void endTag() {
		indent -= 1;
	}

	protected StringBuilder indent(StringBuilder builder) {
		for (int i = 0; i < indent; i++) {
			builder.append(TAB);
		}

		return builder;
	}

}
