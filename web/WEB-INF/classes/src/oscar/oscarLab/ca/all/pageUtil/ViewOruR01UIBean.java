package oscar.oscarLab.ca.all.pageUtil;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;

import oscar.oscarLab.ca.all.parsers.Factory;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;

public final class ViewOruR01UIBean {
	private ORU_R01 oruR01;
	private Demographic demographic;
	private ObservationData observationData;
	
	public ViewOruR01UIBean(String segmentId) throws EncodingNotSupportedException, HL7Exception, UnsupportedEncodingException
	{
		String hl7Message=Factory.getHL7Body(segmentId);
		oruR01=(ORU_R01) OscarToOscarUtils.pipeParserParse(hl7Message);
		
		PID pid=oruR01.getPATIENT_RESULT(0).getPATIENT().getPID();
		demographic=DataTypeUtils.parsePid(pid);
		
		observationData=OruR01.getObservationData(oruR01);
	}
	
	public String getFromProviderDisplayString() throws HL7Exception
	{
		return(getProviderDisplayString(DataTypeUtils.ACTION_ROLE_SENDER));
	}
	
	public String getToProviderDisplayString() throws HL7Exception
	{
		return(getProviderDisplayString(DataTypeUtils.ACTION_ROLE_RECEIVER));
	}
	
	private String getProviderDisplayString(String actionRole) throws HL7Exception
	{
		Provider provider=OruR01.getProviderByActionRole(oruR01, actionRole);
		
		StringBuilder sb = new StringBuilder();

		sb.append(provider.getLastName());
		sb.append(", ");
		sb.append(provider.getFirstName());

		if (provider.getProviderNo() != null) {
			sb.append(" (");
			sb.append(provider.getProviderNo());
			sb.append(')');
		}

		if (provider.getPhone() != null) {
			sb.append(", ");
			sb.append(provider.getPhone());
		}

		if (provider.getEmail() != null) {
			sb.append(", ");
			sb.append(provider.getEmail());
		}

		if (provider.getAddress() != null) {
			sb.append(", ");
			sb.append(provider.getAddress());
		}

		return (StringEscapeUtils.escapeHtml(sb.toString()));
	}
	
	public String getClientDisplayName()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append(demographic.getLastName());
		sb.append(", ");
		sb.append(demographic.getFirstName());
		
		if (demographic.getSex()!=null){
			sb.append('(');
			sb.append(demographic.getSex());
			sb.append(')');
		}
		
		return(StringEscapeUtils.escapeHtml(sb.toString()));
	}
	
	public String getHinForDisplay()
	{
		if (demographic.getHin()==null) return("");
		return(StringEscapeUtils.escapeHtml(demographic.getHin()));
	}
	
	public String getBirthDayForDisplay()
	{
		if (demographic.getBirthDay()==null) return("");
		return(DateFormatUtils.ISO_DATE_FORMAT.format(demographic.getBirthDay()));
	}
	
	public String getDataNameForDisplay()
	{
		return(StringEscapeUtils.escapeHtml(observationData.dataName));
	}
	
	public String getTextDataForDisplay()
	{
		if (observationData.textData==null) return("");
		return(StringEscapeUtils.escapeHtml(observationData.textData));
	}
}
