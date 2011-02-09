package org.oscarehr.PMmodule.streethealth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class ReportElementTag extends TagSupport {

	private Logger logger = MiscUtils.getLogger();
	private static final long serialVersionUID = -239992499220455498L;
	private String question;
	private String answers;
	private String answerProps;
	private int cohortSize;
	
	private String num;
	private String format;
	

	public int doStartTagCSV() throws JspException {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		cohortSize = (Integer)req.getAttribute("nCohorts");
		Map<StreetHealthReportKey,Integer> results = (Map<StreetHealthReportKey,Integer>) req.getAttribute("results");
		
		
		String answerArray[] = null;
		
		if(getAnswerProps() != null && getAnswerProps().length()>0) {
			Properties p = new Properties();
			try {
				p.load(getClass().getClassLoader().getResourceAsStream("streethealth_report.properties"));
			}catch(IOException e) {logger.warn(e);}
			String temp=null;
			int idx=1;
			List<String> answerValues = new ArrayList<String>();
			while((temp=p.getProperty(getAnswerProps() + idx)) != null) {
				answerValues.add(temp.trim());
				idx++;
			}
			answerArray = answerValues.toArray(new String[answerValues.size()]);
		} else {
			answerArray = answers.split(",");
		}
				
		JspWriter out = pageContext.getOut();
		
		int idx=0;
		for(String answer:answerArray) {
			try {							
				if(idx == 0 && num.length() > 0) {
					out.print("\""+num+"\"");					
				} else {
					out.print("\"\"");
				}
				if(idx==0) {
					out.print(",\""+question+"\"");							
				} else {
					out.print(",\"\"");
				}
				out.print(",\""+answer+"\"");
				int total=0;
				for(int x=0;x<cohortSize;x++) {
					Integer value = results.get(new StreetHealthReportKey(x,question,answer));
					if(value==null) {
						value = new Integer(0);
					}
					total+=value;
				}				
				out.print(",\""+total+"\"");
				
				for(int x=0;x<cohortSize;x++) {
					Integer value = results.get(new StreetHealthReportKey(x,question,answer));
					if(value==null) {
						value = new Integer(0);
					}
					out.print(",\""+value+"\"");
				}							
				
			} catch (IOException e) {
				throw new JspTagException("An IOException occurred.");
			}
			idx++;
		}
		return SKIP_BODY;
	}
	
	@Override
	public int doStartTag() throws JspException {
		if(format != null && format.equals("csv")) {
			return doStartTagCSV();
		}
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		cohortSize = (Integer)req.getAttribute("nCohorts");
		Map<StreetHealthReportKey,Integer> results = (Map<StreetHealthReportKey,Integer>) req.getAttribute("results");
		
		
		String answerArray[] = null;
		
		if(getAnswerProps() != null && getAnswerProps().length()>0) {
			Properties p = new Properties();
			try {
				p.load(getClass().getClassLoader().getResourceAsStream("streethealth_report.properties"));
			}catch(IOException e) {logger.warn(e);}
			String temp=null;
			int idx=1;
			List<String> answerValues = new ArrayList<String>();
			while((temp=p.getProperty(getAnswerProps() + idx)) != null) {
				answerValues.add(temp.trim());
				idx++;
			}
			answerArray = answerValues.toArray(new String[answerValues.size()]);
		} else {
			answerArray = answers.split(",");
		}
				
		JspWriter out = pageContext.getOut();
		
		int idx=0;
		for(String answer:answerArray) {
			try {			
				out.println("<tr>");
				if(idx == 0 && num.length() > 0) {
					out.println("<td>"+num+"</td>");
				} else {
					out.println("<td></td>");
				}
				if(idx==0) {
					out.println("<td>"+question+"</td>");
				} else {
					out.println("<td></td>");
				}
				out.println("<td>"+answer+"</td>");
				int total=0;
				for(int x=0;x<cohortSize;x++) {
					Integer value = results.get(new StreetHealthReportKey(x,question,answer));
					if(value==null) {
						value = new Integer(0);
					}
					total+=value;
				}				
				out.println("<td>"+total+"</td>");
				
				for(int x=0;x<cohortSize;x++) {
					Integer value = results.get(new StreetHealthReportKey(x,question,answer));
					if(value==null) {
						value = new Integer(0);
					}
					out.println("<td>"+value+"</td>");
				}
				out.println("</tr>");
			} catch (IOException e) {
				throw new JspTagException("An IOException occurred.");
			}
			idx++;
		}
		return SKIP_BODY;
	}
	
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public String getAnswers() {
		return answers;
	}


	public void setAnswers(String answers) {
		this.answers = answers;
	}


	public String getAnswerProps() {
		return answerProps;
	}


	public void setAnswerProps(String answerProps) {
		this.answerProps = answerProps;
	}


	public String getNum() {
		return num;
	}


	public void setNum(String num) {
		this.num = num;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}


}
