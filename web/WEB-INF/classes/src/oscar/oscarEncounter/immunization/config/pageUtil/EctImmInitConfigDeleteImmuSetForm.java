package oscar.oscarEncounter.immunization.config.pageUtil;

import org.apache.struts.action.ActionForm;

public final class EctImmInitConfigDeleteImmuSetForm extends ActionForm {
	String action;
	String[] chkSetId;
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		if (action == null) action="";
		return action;
	}

	public String[] getChkSetId() {
		if (chkSetId == null)
			chkSetId = new String[] {
		};
		return chkSetId;
	}

	/**
	 * @param action
	 *            The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	public void setChkSetId(String[] str) {
		chkSetId = str;
	}

}
