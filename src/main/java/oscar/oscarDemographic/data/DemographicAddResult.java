package oscar.oscarDemographic.data;

import java.util.ArrayList;

public class DemographicAddResult {
	ArrayList<String> warnings = null;
	boolean added = false;
	String id = null;

	public void addWarning(String str) {

		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		warnings.add(str);
	}

	public String[] getWarnings() {
		String[] s = {};
		if (warnings != null) {
			s = warnings.toArray(s);
		}
		return s;
	}

	public ArrayList<String> getWarningsCollection() {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		return warnings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean wasAdded() {
		return added;
	}

	public void setAdded(boolean b) {
		added = b;
	}
}
