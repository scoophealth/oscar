package org.oscarehr.cds;

import java.util.Arrays;

import org.oscarehr.util.EnumNameComparator;

public enum CdsMunicipality {
	
	AlgomaDistrict("01"),
	Brant("02"),
	Bruce("03"),
	CochraneDistrict("04"),
	Dufferin("05"),
	Durham("06"),
	Elgin("07"),
	Essex("08"),
	Frontenac("09"),
	Grey("10"),
	HaldimandNorfolk("11"),
	Haliburton("12"),
	Halton("13"),
	Hamilton("14"),
	Hastings("15"),
	Huron("16"),
	KenoraKenoraPP("17"),
	ChathamKent("18"),
	Lambton("19"),
	Lanark("20"),
	LeedsGrenville("21"),
	LennoxAddington("22"),
	ManitoulinDistrict("23"),
	Middlesex("24"),
	MuskokaDistrictMun("25"),
	Niagara("26"),
	NipissingDistrict("27"),
	Northumberland("28"),
	Ottawa("29"),
	OutOfProvince("30"),
	Oxford("31"),
	ParrySoundDistrict("32"),
	Peel("33"),
	Perth("34"),
	Peterborough("35"),
	PrescottRussell("36"),
	PrinceEdward("37"),
	RainyRiverDistrict("38"),
	Renfrew("39"),
	Simcoe("40"),
	StormontDundasGlengarry("41"),
	SudburyDistrict("42"),
	SudburyRegion("43"),
	ThunderBayDistrict("44"),
	TimiskamingDistrict("45"),
	Toronto("46"),
	Unknown("47"),
	KawarthaLakes("48"),
	Waterloo("49"),
	Wellington("50"),
	York("51"),
	OutOfCountry("52");

	private String dataElementSubCateoryNumber=null;
	
	private CdsMunicipality(String dataElementSubCateoryNumber)
	{
		this.dataElementSubCateoryNumber=dataElementSubCateoryNumber;
	}
	
	/**
	 * The DataElementSubCateoryNumber is not the entire category number,
	 * as an example the DataElementCateoryNumber for "Durham" should be
	 * "010-06" the DataElementSubCateoryNumber is just the "06" portion.
	 * The DataElementSubCateoryNumber is prepadded with 0's to match
	 * the CDS specs, i.e. 06 instead of just 6.
	 */
	public String getDataElementSubCateoryNumber()
	{
		return(dataElementSubCateoryNumber);
	}
	
	public static CdsMunicipality[] valuesSorted()
	{
		CdsMunicipality[] results=values();
		Arrays.sort(results, new EnumNameComparator());
		return(results);
	}
}
