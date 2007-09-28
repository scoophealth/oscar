package org.oscarehr.PMmodule.web;

import java.util.TreeMap;

import org.caisi.model.IssueGroup;
import org.caisi.model.Role;
import org.oscarehr.util.AccumulatorMap;
import org.oscarehr.util.EncounterUtil;

/**
 * This bean is a essentiallya huge data grid. The purpose of this
 * bean is to facilitate with generating population reports.
 */
public class PopulationReportDataObjects {
	
	/**
	 * This map should hold the number of encounters for a given issueGroup.
	 */
	public static class EncounterTypeDataRow extends AccumulatorMap<IssueGroup>
	{
		// this is just a place holder class so I know what item I'm dealing with
	}

	/**
	 * This object should hold a EncounterTypeDataRow for every encounter type in the system. (about 3 at the time of this writing).
	 */
	public static class EncounterTypeDataGrid extends TreeMap<EncounterUtil.EncounterType, EncounterTypeDataRow>
	{
		private EncounterTypeDataRow total=null;
		
		public EncounterTypeDataRow getIssueGroupTotals()
		{
			if (total==null)
			{
				total=new EncounterTypeDataRow();
				for (EncounterTypeDataRow encounterTypeDataRow : values()) total.addAccumulator(encounterTypeDataRow);
			}
			
			return(total);
		}
	}
	
	/**
	 * This object should hold a EncounterTypeDataGrid for every role in the system.
	 */
	public static class RoleDataGrid extends TreeMap<Role, EncounterTypeDataGrid>
	{
		public EncounterTypeDataRow getIssueGroupTotals()
		{
			EncounterTypeDataRow total=new EncounterTypeDataRow();
			
			for (EncounterTypeDataGrid encounterTypeDataGrid : values()) total.addAccumulator(encounterTypeDataGrid.getIssueGroupTotals());
			
			return(total);
		}
	}
	
}
