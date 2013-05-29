OSCAR Audit Requirements
========================

Current log/audit table 

	+----------------+-------------+------+-----+---------+----------------+
	| Field          | Type        | Null | Key | Default | Extra          |
	+----------------+-------------+------+-----+---------+----------------+
	| id             | bigint(20)  | NO   | PRI | NULL    | auto_increment |
	| dateTime       | datetime    | NO   | MUL | NULL    |                |
	| provider_no    | varchar(10) | YES  |     | NULL    |                |
	| action         | varchar(64) | YES  | MUL | NULL    |                |
	| content        | varchar(80) | YES  | MUL | NULL    |                |
	| contentId      | varchar(80) | YES  | MUL | NULL    |                |
	| ip             | varchar(30) | YES  |     | NULL    |                |
	| demographic_no | int(10)     | YES  | MUL | NULL    |                |
	| data           | text        | YES  |     | NULL    |                |
	| securityId     | int(11)     | YES  |     | NULL    |                |
	+----------------+-------------+------+-----+---------+----------------+

####id 
Incremental id for audit record

####dateTime
Date and Time of audit record

####provider_no
provider_no of user preforming action

####action

Action that was preformed by the user.

EG:

* read
* add
* update
* delete
* acknowledge
* archive
* discontinue
* failed
* log in
* log out
* print
* reprint
* unlock
* verify

####content

Object action was preformed on eg. Demographic Record, Chart Note, Role, User Account, Lab 

####contentId

id of the corresponding content object. There are some special cases eg. Failed logins have the username used 

####ip
IP address performing action

####demographic_no
Demographic Number of patient being acted on.  NULL if no patient is being acted on.

####data

Human readable copy of data that is being acted on.


####securityId

Not sure what this field is for.


===

Requirements
============

List of requirement that OSCAR must meet.


Infoway Audit Requirements
--------------------------

###Security Requirement 43 Minimum Content of Audit Logs

The EHRi audit log and the audit logs of POS systems connecting to the EHRi must contain:

a. the user ID of the accessing user;
b. the role the user is exercising
c. the organization of the accessing user (at least in those cases where an individual accesses information on behalf of more than one organisation);
d. the patient ID of the data subject (patient/person);
e. the function performed by the accessing user;
f. a time stamp;
g. in the case of access override to blocked or masked records or portions of records, a reason for the override, as chosen bythe user making the access; and
h. in the case of changes to consent directives made by a substitute decision-maker, the
identity of the decision-maker.


Ontario MD Requirements
-----------------------

 **The Offering must not allow for the capability to disable the audit trail. This applies to medical and non-medical records within Offering.**


###Appendix A 2.2.2

* There will be a complete audit trail of medical records in accordance with the CPSO requirements.
* Each patient record in the system must have a distinct audit trail.
* All activity (i.e. data viewed, updated, deleted) against medical records maintained by the EMR must be captured in the audit trail.
* Data must not be altered, removed or deleted, just marked as altered, removed or deleted.
* Audit trail includes who accessed the data and when the data was accessed.
* Audit trail must be printable.
* Printed audit trail cannot contain system references that are meaningless outside of the system context.
* Audit Trail functionality is mandatory per CPSO requirements.

See CPSO Medical Records Policy, Appendix A, Section 20: [http://www.cpso.on.ca/uploadedFiles/policies/policies/policyitems/medical_records.pdf](http://www.cpso.on.ca/uploadedFiles/policies/policies/policyitems/medical_records.pdf)


* Each record in the EMR will include a date/time stamp and user ID for the update of that record.
* Can be visible either on the chart or through an audit trail.
* The EMR application must have audit trail for all add/change/delete operations on all EMR system (non-medical record) data, including permission metadata.
* Data must not be altered, removed or deleted, just marked as altered, removed or deleted.
* Updated information retains original data entry as well.


### Requirement 2.5.2

* Audits and logs all logins, successful and failed, at the EMR server.
* The log must include: timestamp, user ID/application ID,originating IP address, port accessed or computer name.
* Both local and remote logins must be auditable.

* Audits and logs traffic that indicates unauthorized activity encountered at the EMR server.
* The log must include: timestamp, user ID/application ID, originating IP address, port accessed or computer name.

* Anonymous access for services installed and running on the server (e.g. FTP, Telnet, Web) is not allowed.

If the EMR does not require any additional services, i.e. the services are disabled, this requirement is then met.

(OPTIONAL) Audits and logs access to components of the medical record from outside the EMR, including:

* external ODBC connections used to execute SQL queries;

* EMR data stored external to the database such as attachments; and

* all data files used to meet other EMR Local requirements (e.g. reporting requirements).

The log must include: timestamp, user ID/application ID and data base operation.

 
 
Ontario Lab Information System (OLIS) Requirements
--------------------------------------------------

The EMR will maintain a log for all OLIS queries The application will maintain a log containing the:

* parameters used in all OLIS related queries 
* date and time the query was executed. 

The log entries will be retained in accordance with regulations governed by the Medicine Act, 1991.
The queries and HL7 messages need to be stored for audit purposes. 

Log overrides of patient consent directive in the EMR, including when the override occurred, the identity of the person performing the override, the practitioner on whose behalf they were acting, and whether it was the patient or the substitute decision maker who authorized the override.

