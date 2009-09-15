package org.oscarehr.cds;

import java.util.Arrays;

import org.oscarehr.util.EnumNameComparator;

public enum CdsLegalStatus {
	
	PreChargeDiversion("01"),
	CourtDiversionProgram("02"),
	AwaitingFitnessAssessment("03"),
	AwaitingTrial("04"),
	AwaitingCriminalResponsibilityAssessment("05"),
	InCommunityOnOwnRecognizance("06"),
	UnfitToStandTrial("07"),
	ChargesWithdrawn("08"),
	StayOfProceedings("09"),
	AwaitingSentence("10"),
	NCR("11"),
	ConditionalDischarge("12"),
	ConditionalSentence("13"),
	RestrainingOrder("14"),
	PeaceBond("15"),
	SuspendedSentence("16"),
	ORBDetainedCommunityAccess("17"),
	ORBConditionalDischarge("18"),
	OnParole("19"),
	OnProbation("20"),
	NoLegalProblems("21"),
	UnknownOrServiceRecipientDeclined("24");
	
	private String dataElementSubCateoryNumber=null;
	
	private CdsLegalStatus(String dataElementSubCateoryNumber)
	{
		this.dataElementSubCateoryNumber=dataElementSubCateoryNumber;
	}
	
	/**
	 * The DataElementSubCateoryNumber is not the entire category number,
	 * as an example the DataElementCateoryNumber for "Court Diversion Program" should be
	 * "013-02" or "014-02" the DataElementSubCateoryNumber is just the "02" portion.
	 * The DataElementSubCateoryNumber is prepadded with 0's to match
	 * the CDS specs, i.e. 02 instead of just 2.
	 */
	public String getDataElementSubCateoryNumber()
	{
		return(dataElementSubCateoryNumber);
	}
	
	public static CdsLegalStatus[] valuesSorted()
	{
		CdsLegalStatus[] results=values();
		Arrays.sort(results, new EnumNameComparator());
		return(results);
	}

}
