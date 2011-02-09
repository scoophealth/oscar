package org.oscarehr.PMmodule.web;

import java.util.TreeMap;

import org.caisi.model.Role;
import org.oscarehr.common.model.IssueGroup;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.AccumulatorMap;
import org.oscarehr.util.EncounterUtil;

/**
 * This bean is a essentially a huge data grid. The purpose of this
 * bean is to facilitate with generating population reports.
 */
public class PopulationReportDataObjects {
	
	/**
	 * This map should hold the number of encounters for a given issueGroup.
	 */
	public static class EncounterTypeDataRow extends AccumulatorMap<IssueGroup>
	{
		public int rowTotalUniqueEncounters=0;
		public int rowTotalUniqueClients=0;
	}

	/**
	 * This object should hold a EncounterTypeDataRow for every encounter type in the system. (about 3 at the time of this writing).
	 */
	public static class EncounterTypeDataGrid extends TreeMap<EncounterUtil.EncounterType, EncounterTypeDataRow>
	{
		public EncounterTypeDataRow subTotal=null;		
	}
	
	/**
	 * This object should hold a EncounterTypeDataGrid for every role in the system.
	 */
	public static class RoleDataGrid extends TreeMap<Role, EncounterTypeDataGrid>
	{
		public EncounterTypeDataRow total=null;		
	}
	
	/**
	 * This object should hold a EncounterTypeDataGrid for every provider.
	 */
	public static class ProviderDataGrid extends TreeMap<Provider, EncounterTypeDataGrid>
	{
		public EncounterTypeDataRow total=null;		
	}
	
}
