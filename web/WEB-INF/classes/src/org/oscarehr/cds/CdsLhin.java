package org.oscarehr.cds;

public enum CdsLhin {
	
	Central("01"),
	CentralEast("02"),
	CentralWest("03"),
	Champlain("04"),
	ErieStClair("05"),
	HamiltonNiagaraHaldimandBrant("06"),
	MississaugaHalton("07"),
	NorthEast("08"),
	NorthSimcoeMuskoka("09"),
	NorthWest("10"),
	SouthEast("11"),
	SouthWest("12"),
	TorontoCentral("13"),
	WaterlooWellington("14"),
	OutOfProvince("15"),
	OtherUnknown("16");
	
	private String dataElementSubCateoryNumber=null;
	
	private CdsLhin(String dataElementSubCateoryNumber)
	{
		this.dataElementSubCateoryNumber=dataElementSubCateoryNumber;
	}
	
	/**
	 * The DataElementSubCateoryNumber is not the entire cateogry number,
	 * as an example the DataElementCateoryNumber for "Champlain" should be
	 * "10a-04" or "10b-04" the DataElementSubCateoryNumber is just the "04" portion.
	 * The DataElementSubCateoryNumber is prepadded with 0's to match
	 * the CDS specs, i.e. 04 instead of just 4. This allows the LHIN list
	 * to be used for multiple sections like 10a and 10b
	 */
	public String getDataElementSubCateoryNumber()
	{
		return(dataElementSubCateoryNumber);
	}
}
