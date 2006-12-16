package org.oscarehr.PMmodule.web.formbean;

import java.util.List;

import org.oscarehr.PMmodule.model.Program;

public class StaffEditProgramContainer {
	private Program program;
	private List teamList;
	
	public StaffEditProgramContainer(Program program, List teamList) {
		this.program = program;
		this.teamList = teamList;
	}
	
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	public List getTeamList() {
		return teamList;
	}
	public void setTeamList(List teamList) {
		this.teamList = teamList;
	}
}
