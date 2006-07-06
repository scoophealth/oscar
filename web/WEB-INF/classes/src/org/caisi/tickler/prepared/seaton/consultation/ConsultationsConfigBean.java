package org.caisi.tickler.prepared.seaton.consultation;

public class ConsultationsConfigBean {
	private  RecipientContainerBean processrequest;
	private  RecipientContainerBean notifyconsultation;
	public RecipientContainerBean getNotifyconsultation() {
		return notifyconsultation;
	}
	public void setNotifyconsultation(RecipientContainerBean notifyconsultation) {
		this.notifyconsultation = notifyconsultation;
	}
	public RecipientContainerBean getProcessrequest() {
		return processrequest;
	}
	public void setProcessrequest(RecipientContainerBean processrequest) {
		this.processrequest = processrequest;
	}
	
}
