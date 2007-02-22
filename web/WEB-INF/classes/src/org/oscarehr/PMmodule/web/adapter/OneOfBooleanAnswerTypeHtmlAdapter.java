package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeLabel;
import org.oscarehr.PMmodule.model.IntakeNode;

public class OneOfBooleanAnswerTypeHtmlAdapter extends BaseAnswerTypeHtmlAdapter {
	
	public OneOfBooleanAnswerTypeHtmlAdapter(IntakeInstance instance, IntakeNode node, int index) {
	    super(index, instance, node);
    }

	public StringBuilder getPreBuilder() {
		for (IntakeLabel label : node.getLabels()) {
			startRow(preBuilder);
			startCell(preBuilder);
			
			indent(preBuilder).append("<input type=\"radio\" name=\"instance.getAnswerMapped(").append(node.getIdStr()).append(").value\" value=\"").append(label.getId()).append("\"");
			setChecked(preBuilder).append(" />").append(EOL);
			indent(preBuilder).append(label.getLabel()).append(EOL);
			
			endCell(preBuilder);
			endRow(preBuilder);
		}

	    return preBuilder;
	}
		
}