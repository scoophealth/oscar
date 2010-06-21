package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

import oscar.util.BuildInfo;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.datatype.FT;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.NTE;
import ca.uhn.hl7v2.model.v26.segment.OBR;

public final class OruR01 {
	
	private static final Logger logger=MiscUtils.getLogger();
	
	public static class ObservationData
	{
		public String name;
		public String dataType;
		public String data;
		
		/**
		 * @param name i.e. "wcb_form"
		 * @param dataType i.e. "pdf"
		 * @param data must be utf-8 data, use base64 encoding for binary data (see the other constructor)
		 */
		public ObservationData(String name, String dataType, String data) {
	        this.name = name;
	        this.dataType = dataType;
	        this.data = data;
        }

		/**
		 * See the other constructor
		 * @param data binary data which will be base64 encoded.
		 */
		public ObservationData(String name, String dataType, byte[] data) throws UnsupportedEncodingException {
			this(name, dataType, OscarToOscarUtils.encodeBase64ToString(data));
        }
	}
	
	/**
	 * This method is essentially used to make an ORU_R01 containing pretty much any random data.
	 */
	public static ORU_R01 makeOruR01(String facilityName, Demographic demographic, List<ObservationData> observationDataList, Provider sendingProvider, Provider receivingProvider) throws HL7Exception
	{
		ORU_R01 observationMsg=new ORU_R01();

		DataTypeUtils.fillMsh(observationMsg.getMSH(), new Date(), facilityName, "ORU", "R01", "ORU_R01", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(observationMsg.getSFT(), BuildInfo.getBuildTag(), BuildInfo.getBuildDate());

		ORU_R01_PATIENT_RESULT patientResult=observationMsg.getPATIENT_RESULT(0);
		DataTypeUtils.fillPid(patientResult.getPATIENT().getPID(), 1, demographic);

		ORU_R01_ORDER_OBSERVATION orderObservation=patientResult.getORDER_OBSERVATION(0);
		fillBlankOBR(orderObservation.getOBR());
		fillNtesWithObservationData(orderObservation, observationDataList);

		// use ROL for the sending and receiving provider
		DataTypeUtils.fillRol(orderObservation.getROL(0), sendingProvider, DataTypeUtils.ACTION_ROLE_SENDER);
		DataTypeUtils.fillRol(orderObservation.getROL(1), receivingProvider, DataTypeUtils.ACTION_ROLE_RECEIVER);
		
		return(observationMsg);
	}
	
	private static void fillNtesWithObservationData(ORU_R01_ORDER_OBSERVATION orderObservation, List<ObservationData> observationDataList) throws HL7Exception {
		// Use NTE's to send random data, each nte represents one piece of data. 
		// Each comment text is only 64k so we'll break large data into comment repetitions.
		// Example : nte.commenttype.Text="WCB Form", nte.commenttype.NameOfCodingSystem="pdf", nte.comment=<base64 encoded contents of the pdf file> 

		int nteCounter=0;
		for (ObservationData observationData : observationDataList)
		{
			NTE nte=orderObservation.getNTE(nteCounter);
			fillOneNte(nte, observationData);
			nteCounter++;
		}
    }

	private static void fillOneNte(NTE nte, ObservationData observationData) throws HL7Exception
	{
		nte.getCommentType().getText().setValue(observationData.name);
		nte.getCommentType().getNameOfCodingSystem().setValue(observationData.dataType);

		int dataLength=observationData.data.length();
		int chunks=dataLength/DataTypeUtils.NTE_COMMENT_MAX_SIZE;
		if (dataLength%DataTypeUtils.NTE_COMMENT_MAX_SIZE!=0) chunks++;
		logger.debug("Breaking Observation Data ("+dataLength+") into chunks:"+chunks);
		
		for (int i=0; i<chunks; i++)
		{
			FT commentPortion=nte.getComment(i);
			
			int startIndex=i*DataTypeUtils.NTE_COMMENT_MAX_SIZE;
			int endIndex=Math.min(dataLength, startIndex+DataTypeUtils.NTE_COMMENT_MAX_SIZE);
			
			commentPortion.setValue(observationData.data.substring(startIndex, endIndex));
		}		
	}
	
	public static String getNteCommentsAsSingleString(NTE nte)
	{
		FT[] fts=nte.getComment();
		
		StringBuilder sb=new StringBuilder();
		for (int i=0; i<fts.length; i++) sb.append(fts[i].getValue());
		
		return(sb.toString());
	}
	
	public static byte[] getNteCommentsAsDecodedBytes(NTE nte) throws UnsupportedEncodingException
	{
		String temp=getNteCommentsAsSingleString(nte);
		return(OscarToOscarUtils.decodeBase64(temp));
	}
	
	/**
	 * An OBR segment is required even though none of the fields are relevant. This will create essentially a blank / useless OBR. It will fill in required fields with valid but essentially useless data.
	 * @throws DataTypeException 
	 */
	public static void fillBlankOBR(OBR obr) throws DataTypeException {
		obr.getUniversalServiceIdentifier().getIdentifier().setValue(String.valueOf(System.nanoTime()));
	}

	public static void main(String... argv) throws Exception
	{
		Demographic demographic=new Demographic();
		demographic.setLastName("test LN");
		demographic.setLastName("test FN");
		demographic.setBirthDay(new GregorianCalendar(1960, 2, 3));
		
		ArrayList<ObservationData> observationDataList=new ArrayList<ObservationData>();
		byte[] b=FileUtils.readFileToByteArray(new File("/tmp/r6.jpg"));
		observationDataList.add(new ObservationData("test","jpg", b));
		
		Provider sender=new Provider();
		sender.setProviderNo("111");
		sender.setLastName("sender ln");
		sender.setFirstName("sender fn");
		
		Provider receiver=new Provider();
		receiver.setProviderNo("222");
		receiver.setLastName("receiver ln");
		receiver.setFirstName("receiver fn");
		
		ORU_R01 observationMsg=makeOruR01("facility name", demographic, observationDataList, sender, receiver);
		
		String messageString=OscarToOscarUtils.pipeParser.encode(observationMsg);
		logger.info(messageString);
		
		ORU_R01 newObservationMsg=(ORU_R01)OscarToOscarUtils.pipeParser.parse(messageString);
		byte[] decoded=getNteCommentsAsDecodedBytes(newObservationMsg.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getNTE(0));
		logger.info("equal data:"+Arrays.equals(b, decoded));
		FileUtils.writeByteArrayToFile(new File("/tmp/out.jpg"), decoded);
	}
}
