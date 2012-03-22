package oscar.oscarDemographic.data;

import org.oscarehr.common.model.Allergy;

import oscar.oscarRx.data.RxPatientData;

public class RxInformation {
	private String currentMedication;
	private String allergies;

	public String getCurrentMedication(String demographic_no) {
		oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
		oscar.oscarRx.data.RxPrescriptionData.Prescription[] arr = {};
		arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demographic_no));
		StringBuilder stringBuffer = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].isCurrent()) {

				stringBuffer.append(arr[i].getFullOutLine().replaceAll(";", " ") + "\n");
				// stringBuffer.append(arr[i].getRxDisplay()+"\n");
			}
		}
		this.currentMedication = stringBuffer.toString();
		return this.currentMedication;
	}

	public String getAllergies(String demographic_no) {
		oscar.oscarRx.data.RxPatientData.Patient patient = RxPatientData.getPatient(Integer.parseInt(demographic_no));
		Allergy[] allergies = {};
		allergies = patient.getAllergies();
		StringBuilder stringBuffer = new StringBuilder();
		for (int i = 0; i < allergies.length; i++) {
			Allergy allerg = allergies[i];
			stringBuffer.append(allerg.getDescription() + "  " + Allergy.getTypeDesc(allerg.getTypeCode()) + " \n");
		}
		this.allergies = stringBuffer.toString();

		return this.allergies;
	}
}




