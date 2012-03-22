package oscar.oscarDemographic.data;

import java.util.Date;

public class EctInformation {

	private oscar.oscarEncounter.data.EctPatientData.Patient patient;
	private oscar.oscarEncounter.data.EctPatientData.Patient.eChart eChart;

	public EctInformation(String demographic_no) {
		init(demographic_no);
	}

	private void init(String demographic_no) {
		oscar.oscarEncounter.data.EctPatientData patientData = new oscar.oscarEncounter.data.EctPatientData();
		this.patient = patientData.getPatient(demographic_no);
		this.eChart = patient.getEChart();
	}

	public Date getEChartTimeStamp() {
		return eChart.getEChartTimeStamp();
	}

	public String getSocialHistory() {
		return eChart.getSocialHistory();
	}

	public String getFamilyHistory() {
		return eChart.getFamilyHistory();
	}

	public String getMedicalHistory() {
		return eChart.getMedicalHistory();
	}

	public String getOngoingConcerns() {
		return eChart.getOngoingConcerns();
	}

	public String getReminders() {
		return eChart.getReminders();
	}

	public String getEncounter() {
		return eChart.getEncounter();
	}

	public String getSubject() {
		return eChart.getSubject();
	}
}