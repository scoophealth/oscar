package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;

public class StringAnswerTypeHtmlAdapter extends BaseAnswerTypeHtmlAdapter {

	public StringAnswerTypeHtmlAdapter(IntakeInstance instance, IntakeNode node, int index) {
	    super(index, instance, node);
    }

	public StringBuilder getPreBuilder() {
		startRow(preBuilder);
		startCell(preBuilder);

		indent(preBuilder).append("<input type=\"text\" name=\"instance.getAnswerMapped(").append(node.getIdStr()).append(").value\" />").append(EOL);
		indent(preBuilder).append(node.getLabel(0)).append(EOL);
		
		endCell(preBuilder);
		endRow(preBuilder);
		
	    return preBuilder;
	}	
}
