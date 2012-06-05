/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.caisi.wl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.caisi.wl package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _ListVacanciesForWaitListProgramResponse_QNAME = new QName(
			"http://caisi.org/wl/", "listVacanciesForWaitListProgramResponse");
	private final static QName _ListTopMatchesResponse_QNAME = new QName(
			"http://caisi.org/wl/", "listTopMatchesResponse");
	private final static QName _RecordContactAttemptResponse_QNAME = new QName(
			"http://caisi.org/wl/", "recordContactAttemptResponse");
	private final static QName _RecordMatchWasForwarded_QNAME = new QName(
			"http://caisi.org/wl/", "recordMatchWasForwarded");
	private final static QName _ListVacanciesForAgencyProgramResponse_QNAME = new QName(
			"http://caisi.org/wl/", "listVacanciesForAgencyProgramResponse");
	private final static QName _ListActiveContactAttempts_QNAME = new QName(
			"http://caisi.org/wl/", "listActiveContactAttempts");
	private final static QName _GetVacancy_QNAME = new QName(
			"http://caisi.org/wl/", "getVacancy");
	private final static QName _RecordReferralOutcome_QNAME = new QName(
			"http://caisi.org/wl/", "recordReferralOutcome");
	private final static QName _ListActiveContactAttemptsResponse_QNAME = new QName(
			"http://caisi.org/wl/", "listActiveContactAttemptsResponse");
	private final static QName _ListTopMatches_QNAME = new QName(
			"http://caisi.org/wl/", "listTopMatches");
	private final static QName _RecordReferralOutcomeResponse_QNAME = new QName(
			"http://caisi.org/wl/", "recordReferralOutcomeResponse");
	private final static QName _RecordContactAttempt_QNAME = new QName(
			"http://caisi.org/wl/", "recordContactAttempt");
	private final static QName _ListVacanciesForAgencyProgram_QNAME = new QName(
			"http://caisi.org/wl/", "listVacanciesForAgencyProgram");
	private final static QName _RecordClientContactResponse_QNAME = new QName(
			"http://caisi.org/wl/", "recordClientContactResponse");
	private final static QName _GetVacancyResponse_QNAME = new QName(
			"http://caisi.org/wl/", "getVacancyResponse");
	private final static QName _RecordMatchWasForwardedResponse_QNAME = new QName(
			"http://caisi.org/wl/", "recordMatchWasForwardedResponse");
	private final static QName _ListVacanciesForWaitListProgram_QNAME = new QName(
			"http://caisi.org/wl/", "listVacanciesForWaitListProgram");
	private final static QName _RecordClientContact_QNAME = new QName(
			"http://caisi.org/wl/", "recordClientContact");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: org.caisi.wl
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link RecordMatchWasForwarded }
	 * 
	 */
	public RecordMatchWasForwarded createRecordMatchWasForwarded() {
		return new RecordMatchWasForwarded();
	}

	/**
	 * Create an instance of {@link ListActiveContactAttempts }
	 * 
	 */
	public ListActiveContactAttempts createListActiveContactAttempts() {
		return new ListActiveContactAttempts();
	}

	/**
	 * Create an instance of {@link ListTopMatchesResponse }
	 * 
	 */
	public ListTopMatchesResponse createListTopMatchesResponse() {
		return new ListTopMatchesResponse();
	}

	/**
	 * Create an instance of {@link MatchParam }
	 * 
	 */
	public MatchParam createMatchParam() {
		return new MatchParam();
	}

	/**
	 * Create an instance of {@link RecordClientContact }
	 * 
	 */
	public RecordClientContact createRecordClientContact() {
		return new RecordClientContact();
	}

	/**
	 * Create an instance of {@link GetVacancy }
	 * 
	 */
	public GetVacancy createGetVacancy() {
		return new GetVacancy();
	}

	/**
	 * Create an instance of {@link ListVacanciesForAgencyProgramResponse }
	 * 
	 */
	public ListVacanciesForAgencyProgramResponse createListVacanciesForAgencyProgramResponse() {
		return new ListVacanciesForAgencyProgramResponse();
	}

	/**
	 * Create an instance of {@link ClientQuery }
	 * 
	 */
	public ClientQuery createClientQuery() {
		return new ClientQuery();
	}

	/**
	 * Create an instance of {@link VacancyQuery }
	 * 
	 */
	public VacancyQuery createVacancyQuery() {
		return new VacancyQuery();
	}

	/**
	 * Create an instance of {@link ReferralOutcomeBO }
	 * 
	 */
	public ReferralOutcomeBO createReferralOutcomeBO() {
		return new ReferralOutcomeBO();
	}

	/**
	 * Create an instance of {@link ListVacanciesForWaitListProgramResponse }
	 * 
	 */
	public ListVacanciesForWaitListProgramResponse createListVacanciesForWaitListProgramResponse() {
		return new ListVacanciesForWaitListProgramResponse();
	}

	/**
	 * Create an instance of {@link VacancyDisplayBO }
	 * 
	 */
	public VacancyDisplayBO createVacancyDisplayBO() {
		return new VacancyDisplayBO();
	}

	/**
	 * Create an instance of {@link TopMatchesQuery }
	 * 
	 */
	public TopMatchesQuery createTopMatchesQuery() {
		return new TopMatchesQuery();
	}

	/**
	 * Create an instance of {@link ClientWLEntryBO }
	 * 
	 */
	public ClientWLEntryBO createClientWLEntryBO() {
		return new ClientWLEntryBO();
	}

	/**
	 * Create an instance of {@link RecordMatchWasForwardedResponse }
	 * 
	 */
	public RecordMatchWasForwardedResponse createRecordMatchWasForwardedResponse() {
		return new RecordMatchWasForwardedResponse();
	}

	/**
	 * Create an instance of {@link RecordContactAttempt }
	 * 
	 */
	public RecordContactAttempt createRecordContactAttempt() {
		return new RecordContactAttempt();
	}

	/**
	 * Create an instance of {@link RecordContactAttemptResponse }
	 * 
	 */
	public RecordContactAttemptResponse createRecordContactAttemptResponse() {
		return new RecordContactAttemptResponse();
	}

	/**
	 * Create an instance of {@link ListVacanciesForWaitListProgram }
	 * 
	 */
	public ListVacanciesForWaitListProgram createListVacanciesForWaitListProgram() {
		return new ListVacanciesForWaitListProgram();
	}

	/**
	 * Create an instance of {@link RecordClientContactResponse }
	 * 
	 */
	public RecordClientContactResponse createRecordClientContactResponse() {
		return new RecordClientContactResponse();
	}

	/**
	 * Create an instance of {@link RecordReferralOutcome }
	 * 
	 */
	public RecordReferralOutcome createRecordReferralOutcome() {
		return new RecordReferralOutcome();
	}

	/**
	 * Create an instance of {@link ListTopMatches }
	 * 
	 */
	public ListTopMatches createListTopMatches() {
		return new ListTopMatches();
	}

	/**
	 * Create an instance of {@link RecordReferralOutcomeResponse }
	 * 
	 */
	public RecordReferralOutcomeResponse createRecordReferralOutcomeResponse() {
		return new RecordReferralOutcomeResponse();
	}

	/**
	 * Create an instance of {@link GetVacancyResponse }
	 * 
	 */
	public GetVacancyResponse createGetVacancyResponse() {
		return new GetVacancyResponse();
	}

	/**
	 * Create an instance of {@link ProgramQuery }
	 * 
	 */
	public ProgramQuery createProgramQuery() {
		return new ProgramQuery();
	}

	/**
	 * Create an instance of {@link ListActiveContactAttemptsResponse }
	 * 
	 */
	public ListActiveContactAttemptsResponse createListActiveContactAttemptsResponse() {
		return new ListActiveContactAttemptsResponse();
	}

	/**
	 * Create an instance of {@link MatchBO }
	 * 
	 */
	public MatchBO createMatchBO() {
		return new MatchBO();
	}

	/**
	 * Create an instance of {@link ListVacanciesForAgencyProgram }
	 * 
	 */
	public ListVacanciesForAgencyProgram createListVacanciesForAgencyProgram() {
		return new ListVacanciesForAgencyProgram();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ListVacanciesForWaitListProgramResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listVacanciesForWaitListProgramResponse")
	public JAXBElement<ListVacanciesForWaitListProgramResponse> createListVacanciesForWaitListProgramResponse(
			ListVacanciesForWaitListProgramResponse value) {
		return new JAXBElement<ListVacanciesForWaitListProgramResponse>(
				_ListVacanciesForWaitListProgramResponse_QNAME,
				ListVacanciesForWaitListProgramResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ListTopMatchesResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listTopMatchesResponse")
	public JAXBElement<ListTopMatchesResponse> createListTopMatchesResponse(
			ListTopMatchesResponse value) {
		return new JAXBElement<ListTopMatchesResponse>(
				_ListTopMatchesResponse_QNAME, ListTopMatchesResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordContactAttemptResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordContactAttemptResponse")
	public JAXBElement<RecordContactAttemptResponse> createRecordContactAttemptResponse(
			RecordContactAttemptResponse value) {
		return new JAXBElement<RecordContactAttemptResponse>(
				_RecordContactAttemptResponse_QNAME,
				RecordContactAttemptResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordMatchWasForwarded }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordMatchWasForwarded")
	public JAXBElement<RecordMatchWasForwarded> createRecordMatchWasForwarded(
			RecordMatchWasForwarded value) {
		return new JAXBElement<RecordMatchWasForwarded>(
				_RecordMatchWasForwarded_QNAME, RecordMatchWasForwarded.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ListVacanciesForAgencyProgramResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listVacanciesForAgencyProgramResponse")
	public JAXBElement<ListVacanciesForAgencyProgramResponse> createListVacanciesForAgencyProgramResponse(
			ListVacanciesForAgencyProgramResponse value) {
		return new JAXBElement<ListVacanciesForAgencyProgramResponse>(
				_ListVacanciesForAgencyProgramResponse_QNAME,
				ListVacanciesForAgencyProgramResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ListActiveContactAttempts }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listActiveContactAttempts")
	public JAXBElement<ListActiveContactAttempts> createListActiveContactAttempts(
			ListActiveContactAttempts value) {
		return new JAXBElement<ListActiveContactAttempts>(
				_ListActiveContactAttempts_QNAME,
				ListActiveContactAttempts.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetVacancy }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "getVacancy")
	public JAXBElement<GetVacancy> createGetVacancy(GetVacancy value) {
		return new JAXBElement<GetVacancy>(_GetVacancy_QNAME, GetVacancy.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordReferralOutcome }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordReferralOutcome")
	public JAXBElement<RecordReferralOutcome> createRecordReferralOutcome(
			RecordReferralOutcome value) {
		return new JAXBElement<RecordReferralOutcome>(
				_RecordReferralOutcome_QNAME, RecordReferralOutcome.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ListActiveContactAttemptsResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listActiveContactAttemptsResponse")
	public JAXBElement<ListActiveContactAttemptsResponse> createListActiveContactAttemptsResponse(
			ListActiveContactAttemptsResponse value) {
		return new JAXBElement<ListActiveContactAttemptsResponse>(
				_ListActiveContactAttemptsResponse_QNAME,
				ListActiveContactAttemptsResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link ListTopMatches }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listTopMatches")
	public JAXBElement<ListTopMatches> createListTopMatches(ListTopMatches value) {
		return new JAXBElement<ListTopMatches>(_ListTopMatches_QNAME,
				ListTopMatches.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordReferralOutcomeResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordReferralOutcomeResponse")
	public JAXBElement<RecordReferralOutcomeResponse> createRecordReferralOutcomeResponse(
			RecordReferralOutcomeResponse value) {
		return new JAXBElement<RecordReferralOutcomeResponse>(
				_RecordReferralOutcomeResponse_QNAME,
				RecordReferralOutcomeResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordContactAttempt }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordContactAttempt")
	public JAXBElement<RecordContactAttempt> createRecordContactAttempt(
			RecordContactAttempt value) {
		return new JAXBElement<RecordContactAttempt>(
				_RecordContactAttempt_QNAME, RecordContactAttempt.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ListVacanciesForAgencyProgram }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listVacanciesForAgencyProgram")
	public JAXBElement<ListVacanciesForAgencyProgram> createListVacanciesForAgencyProgram(
			ListVacanciesForAgencyProgram value) {
		return new JAXBElement<ListVacanciesForAgencyProgram>(
				_ListVacanciesForAgencyProgram_QNAME,
				ListVacanciesForAgencyProgram.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordClientContactResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordClientContactResponse")
	public JAXBElement<RecordClientContactResponse> createRecordClientContactResponse(
			RecordClientContactResponse value) {
		return new JAXBElement<RecordClientContactResponse>(
				_RecordClientContactResponse_QNAME,
				RecordClientContactResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetVacancyResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "getVacancyResponse")
	public JAXBElement<GetVacancyResponse> createGetVacancyResponse(
			GetVacancyResponse value) {
		return new JAXBElement<GetVacancyResponse>(_GetVacancyResponse_QNAME,
				GetVacancyResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordMatchWasForwardedResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordMatchWasForwardedResponse")
	public JAXBElement<RecordMatchWasForwardedResponse> createRecordMatchWasForwardedResponse(
			RecordMatchWasForwardedResponse value) {
		return new JAXBElement<RecordMatchWasForwardedResponse>(
				_RecordMatchWasForwardedResponse_QNAME,
				RecordMatchWasForwardedResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ListVacanciesForWaitListProgram }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "listVacanciesForWaitListProgram")
	public JAXBElement<ListVacanciesForWaitListProgram> createListVacanciesForWaitListProgram(
			ListVacanciesForWaitListProgram value) {
		return new JAXBElement<ListVacanciesForWaitListProgram>(
				_ListVacanciesForWaitListProgram_QNAME,
				ListVacanciesForWaitListProgram.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RecordClientContact }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://caisi.org/wl/", name = "recordClientContact")
	public JAXBElement<RecordClientContact> createRecordClientContact(
			RecordClientContact value) {
		return new JAXBElement<RecordClientContact>(_RecordClientContact_QNAME,
				RecordClientContact.class, null, value);
	}

}
