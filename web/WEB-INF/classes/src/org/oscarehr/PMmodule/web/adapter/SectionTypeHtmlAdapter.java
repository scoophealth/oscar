package org.oscarehr.PMmodule.web.adapter;


import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;

public class SectionTypeHtmlAdapter extends BaseIntakeNodeHtmlAdapter {
	
	public SectionTypeHtmlAdapter(int indent, IntakeInstance instance, IntakeNode node) {
		super(indent, instance, node);
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		if (isFirstChild()) {
			if (isParentPage()) {
				indent(preBuilder);
			}

			// Section Container
			preBuilder.append("<div dojoType=\"AccordionContainer\" class=\"intakeSectionContainer\" >").append(EOL);
			beginTag();
		}

		// Section
		indent(preBuilder).append("<div dojoType=\"ContentPane\" label=\"").append(node.getLabel(0)).append("\" class=\"intakeSection\" >").append(EOL);
		beginTag();

		// Question Table
		indent(preBuilder).append("<table class=\"intakeTable\">").append(EOL);
		beginTag();

		return preBuilder;
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPostBuilder()
	 */
	public StringBuilder getPostBuilder() {
		// Question Table
		endTag();
		indent(postBuilder).append("</table> <!-- End Question Table -->").append(EOL);
			
		// Section
		endTag();
		indent(postBuilder).append("</div> <!-- End Section -->").append(EOL);

		// Section Container
		if (isLastChild()) {
			endTag();
			indent(postBuilder).append("</div> <!-- End Section Container -->").append(EOL);
		}

		return postBuilder;
	}

}