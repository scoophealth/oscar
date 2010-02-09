package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.IntakeNode;

public class AnswerScalarChoiceBooleanHtmlAdapter extends
AnswerScalarChoiceHtmlAdapter {
	
	public AnswerScalarChoiceBooleanHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}
	
	public StringBuilder getPreBuilder() {			
		StringBuilder preBuilder = startAnswer(super.getPreBuilder());
		
		indent(preBuilder).append(startLabel(false)).append(createCheckBox(IntakeAnswerElement.TRUE)).append(endLabel(true)).append("<br/>").append(EOL);
		String mquest = "mquests";
		if (getNoOfSibling()>1) mquest = "mquestm";
		String pId = "_" + getParent().getId();
		if (getParent().getMandatory()) {
		    indent(preBuilder).append("<input type=\"hidden\" name=\""+mquest+getPos()+pId+"\" value=\"intake.answerMapped("+getId()+").value\">").append(EOL);
		}
	
		
		return endAnswer(preBuilder);
	}
	
	private StringBuilder createCheckBox(String on) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("<input type=\"checkbox\" name=\"intake.answerMapped(%s).value\" value=\"%s\" question_id=\"" +  getQuestionId()  + "\"", new Object[] { getId(), on }));
		
		if (getAnswerValue().equalsIgnoreCase(on)) {
			builder.append(" checked=\"checked\"");
		}
			
		builder.append(" "+this.getValidationsHtml()+"/>");
		
		return builder;
	}

}
