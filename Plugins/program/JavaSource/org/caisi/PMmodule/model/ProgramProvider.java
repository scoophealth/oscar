package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseProgramProvider;

/**
 * This is the object class that relates to the program_provider table.
 * Any customizations belong here.
 */
public class ProgramProvider extends BaseProgramProvider {

	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public ProgramProvider () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ProgramProvider (java.lang.Long _id) {
		super(_id);
	}
/*[CONSTRUCTOR MARKER END]*/

	private String programName;
	private Program program;
	
	/**
	 * @return Returns the programName.
	 */
	public String getProgramName() {
		return programName;
	}

	/**
	 * @param programName The programName to set.
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
	
}