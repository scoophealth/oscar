package org.caisi.util;

public class EncounterUtil {
	public enum EncounterType
	{
		FACE_TO_FACE_WITH_CLIENT("face to face encounter with client"), 
		TELEPHONE_WITH_CLIENT("telephone encounter with client"),
		ENCOUNTER_WITH_OUT_CLIENT("encounter without client");
		
		private String oldDbValue=null;
		EncounterType(String oldDbValue)
		{
			this.oldDbValue=oldDbValue;
		}
		
		public String getOldDbValue()
		{
			return(oldDbValue);
		}
	}
}
