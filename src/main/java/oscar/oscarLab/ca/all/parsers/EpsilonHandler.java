package oscar.oscarLab.ca.all.parsers;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class EpsilonHandler extends CMLHandler {
	private static Logger logger = MiscUtils.getLogger();
	
	@Override
	public String getMsgType() {
		return "Epsilon";
	}

	@Override
	public void init(String hl7Body) throws HL7Exception {
		if (hl7Body.startsWith("MSH")) {
			int index = 0;
			for (int i = 0; i < 11; i++)
				index = hl7Body.indexOf('|', index + 1);
			int tmp = index + 1;
			String tmph = hl7Body.substring(0, tmp);
			index = hl7Body.indexOf('|', index + 1);
			if (hl7Body.substring(tmp, index).trim().length() == 0)
				hl7Body = tmph + "2.3" + hl7Body.substring(index);
		}
		Parser p = new PipeParser();
		p.setValidationContext(new NoValidation());
		msg = (ORU_R01) p.parse(hl7Body.replaceAll("\n", "\r\n"));
	}

	@Override
	public String getOBXResultStatus(int i, int j) {
		String status = "";
		try {
			status = getString(msg.getRESPONSE().getORDER_OBSERVATION(i)
					.getOBSERVATION(j).getOBX().getObservResultStatus()
					.getValue());
			if (status.equalsIgnoreCase("I"))
				status = "Pending";
			else if (status.equalsIgnoreCase("F"))
				status = "Final";
		} catch (Exception e) {
			logger.error("Error retrieving obx result status", e);
			return status;
		}
		return status;
	}

	@Override
	public String getOrderStatus() {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(0)
					.getOBR().getResultStatus().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	@Override
	public String getMsgPriority() {
		try {
			return msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR()
					.getPriority().getValue();
		} catch (HL7Exception e) {
			return ("");
		}
	}

	@Override
	public String getServiceDate() {
		try {
			return (formatDateTime(getString(Terser.get(msg.getRESPONSE()
					.getORDER_OBSERVATION(0).getOBR(), 6, 0, 1, 1))));
		} catch (Exception e) {
			return ("");
		}
	}

	@Override
	public boolean isOBXAbnormal(int i, int j) {
		if (("").equals(getOBXAbnormalFlag(i, j).trim())) {
			return (false);
		} else {
			return (true);
		}

	}

	@Override
	public String getAccessionNum() {
		String accessionNum = "";
		try {
			accessionNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(0)
					.getOBR().getPlacerOrderNumber(0).getEntityIdentifier()
					.getValue());
			if (msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR()
					.getFillerOrderNumber().getEntityIdentifier().getValue() != null) {
				accessionNum = accessionNum
						+ ", "
						+ getString(msg.getRESPONSE().getORDER_OBSERVATION(0)
								.getOBR().getFillerOrderNumber()
								.getEntityIdentifier().getValue());
			}
			return (accessionNum);
		} catch (Exception e) {
			logger.error("Could not return accession number", e);
			return ("");
		}
	}

	@Override
	public String getObservationHeader(int i, int j) {
		try {
			return this.getOBXName(i, j).trim();
		} catch (Exception e) {
			return ("");
		}
	}

	@Override
	public String getTimeStamp(int i, int j) {
		try {
			return (formatDateTime(getString(msg.getRESPONSE()
					.getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX()
					.getDateTimeOfTheObservation().getTimeOfAnEvent()
					.getValue())));
		} catch (Exception e) {
			return ("");
		}
	}

	String delimiter = "  ";
	char bl = ' ';

	public String getAuditLine(String procDate, String procTime, String logId,
			String formStatus, String formType, String accession, String hcNum,
			String hcVerCode, String patientName, String orderingClient,
			String messageDate, String messageTime) {
		logger.info("Getting Audit Line");

		return getPaddedString(procDate, 11, bl) + delimiter
				+ getPaddedString(procTime, 8, bl) + delimiter
				+ getPaddedString(logId, 7, bl) + delimiter
				+ getPaddedString(formStatus, 1, bl) + delimiter
				+ getPaddedString(formType, 1, bl) + delimiter
				+ getPaddedString(accession, 9, bl) + delimiter
				+ getPaddedString(hcNum, 10, bl) + delimiter
				+ getPaddedString(hcVerCode, 2, bl) + delimiter
				+ getPaddedString(patientName, 61, bl) + delimiter
				+ getPaddedString(orderingClient, 8, bl) + delimiter
				+ getPaddedString(messageDate, 11, bl) + delimiter
				+ getPaddedString(messageTime, 8, bl) + "\n\r";

	}

	String getPaddedString(String originalString, int length, char paddingChar) {
		StringBuilder str = new StringBuilder(length);
		str.append(originalString);

		for (int i = str.length(); i < length; i++) {
			str.append(paddingChar);
		}

		return str.substring(0, length);
	}

	public String getHealthNumVersion() {
		try {
			return (getString(Terser.get(msg.getRESPONSE().getPATIENT()
					.getPID(), 4, 0, 2, 1)));
		} catch (HL7Exception e) {
			return "";
		}
	}

	public Date getMsgDateAsDate() {
		Date date = null;
		try {
			date = getDateTime(getMsgDate());
		} catch (Exception e) {
			logger.error("Error of parsing message date :", e);
		}
		return date;
	}

	private Date getDateTime(String plain) {
		String dateFormat = "yyyyMMddHHmmss";
		dateFormat = dateFormat.substring(0, plain.length());
		Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
		return date;
	}

	public String getUnescapedName() {
		return getLastName() + "^" + getFirstName() + "^" + getMiddleName();
	}

	private String getMiddleName() {
		return (getString(msg.getRESPONSE().getPATIENT().getPID()
				.getMotherSMaidenName().getMiddleInitialOrName().getValue()));
	}
	
    @Override
	public String getPatientLocation(){
        return(getString(msg.getMSH().getSendingApplication().getNamespaceID().getValue()));
    }


	@Override
	public String audit() {
		return "";		
	}

}
