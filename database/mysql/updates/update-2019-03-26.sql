CREATE TABLE cdx_document (
														document_no          int  NOT NULL  ,
														template_id          varchar(20)  NOT NULL  ,
														template_name        varchar(30)  NOT NULL  ,
														document_oid         varchar(30)  NOT NULL  ,
														loinc_code           varchar(30)  NOT NULL  ,
														loinc_name           varchar(60)  NOT NULL  ,
														title                varchar(40)  NOT NULL  ,
														authoring_time       datetime    ,
														device               varchar(30)  NOT NULL  ,
														effective_time       datetime    ,
														custodian            varchar(30)    ,
														order_id             varchar(30)    ,
														status_code          varchar(30)    ,
														observation_date     datetime    ,
														procedure_name       varchar(100)    ,
														parent_doc_id        varchar(60)    ,
														patient_encounter_id varchar(60)    ,
														admission_date       date    ,
														discharge_date       date    ,
														disposition          varchar(60)    ,
														contents             text  NOT NULL  ,
														CONSTRAINT pk_cdx_document PRIMARY KEY ( document_no ),
														CONSTRAINT id_unique UNIQUE ( document_oid )
);

ALTER TABLE cdx_document ADD CONSTRAINT fk_cdx_document FOREIGN KEY ( document_no ) REFERENCES document( document_no ) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE cdx_attachment (
															id                   int  NOT NULL  AUTO_INCREMENT,
															document             int  NOT NULL  ,
															attachment_type      varchar(10)  NOT NULL  ,
															reference 					 varchar(60) NOT NULL ,
															content              blob  NOT NULL  ,
															CONSTRAINT pk_cdx_attachment PRIMARY KEY ( id )
);

CREATE INDEX idx_cdx_attachment ON cdx_attachment ( document );

ALTER TABLE cdx_attachment ADD CONSTRAINT fk_cdx_attachment FOREIGN KEY ( document ) REFERENCES cdx_document( document_no ) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE cdx_person (
													id                   int  NOT NULL  AUTO_INCREMENT,
													first_name           varchar(100)    ,
													last_name            varchar(100)    ,
													gender               varchar(1)    ,
													birthdate            date    ,
													street_address       varchar(100)    ,
													city                 varchar(30)    ,
													province             varchar(2)    ,
													country              char(2)    ,
													prefix               varchar(3)    ,
													clinic_id            varchar(30)    ,
													clinic_name          varchar(100)    ,
													document             int  NOT NULL  ,
													role_in_doc          char(3)  NOT NULL  ,
													postal_code          varchar(10)    ,
													CONSTRAINT pk_cdx_person_id PRIMARY KEY ( id )
);


ALTER TABLE cdx_person ADD CONSTRAINT fk_cdx_person FOREIGN KEY ( document ) REFERENCES cdx_document( document_no ) ON DELETE NO ACTION ON UPDATE NO ACTION;



CREATE TABLE cdx_telco (
												 id                   int  NOT NULL  AUTO_INCREMENT,
												 kind                 char(1)  NOT NULL  ,
												 `type`               char(1)  NOT NULL  ,
												 address              varchar(100)  NOT NULL  ,
												 person               int  NOT NULL  ,
												 CONSTRAINT pk_cdx_telco_id PRIMARY KEY ( id )
);

ALTER TABLE cdx_telco ADD CONSTRAINT fk_cdx_telco FOREIGN KEY ( person ) REFERENCES cdx_person( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE cdx_person_id (
														 id                   int  NOT NULL  AUTO_INCREMENT,
														 id_type              varchar(10)  NOT NULL  ,
														 id_code              varchar(30)  NOT NULL  ,
														 person               int  NOT NULL  ,
														 CONSTRAINT pk_cdx_person_id_id PRIMARY KEY ( id )
);


ALTER TABLE cdx_person_id ADD CONSTRAINT fk_cdx_person_id FOREIGN KEY ( person ) REFERENCES cdx_person( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE cdx_config (
  id									 int NOT NULL AUTO_INCREMENT,
	default_provider     varchar(6)  NOT NULL
)

ALTER TABLE cdx_config ADD CONSTRAINT fk_cdx_config FOREIGN KEY ( default_provider ) REFERENCES provider( provider_no ) ON DELETE NO ACTION ON UPDATE NO ACTION;
