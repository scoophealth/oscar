ALTER TABLE document ADD report_status varchar(30);

CREATE TABLE cdx_attachment (
															id                   int  NOT NULL  AUTO_INCREMENT,
															document             int ,
															attachment_type      varchar(30)  NOT NULL  ,
															reference 					 varchar(60),
															content              mediumblob  NOT NULL  ,
															CONSTRAINT pk_cdx_attachment PRIMARY KEY ( id )
);

CREATE INDEX idx_cdx_attachment ON cdx_attachment ( document );

CREATE TABLE cdx_provenance (
																		id                   int  NOT NULL  AUTO_INCREMENT,
																		doc_id               varchar(60)  NOT NULL  ,
																		msg_id							 varchar(60),
																		document_no						int,
																		version              int   ,
																		effective_time       datetime  NOT NULL  ,
																		received_time        datetime NULL,
																		parent_doc           varchar(60)    ,
																		set_id               varchar(60)    ,
																		in_fulfillment_of_id varchar(60)    ,
																		kind                 varchar(50)  NOT NULL  ,
																		action               varchar(10)  NOT NULL  ,
																		log                  bigint    ,
																		payload              text    ,
																		warnings             text,
																		status               varchar(30),
                                                                        distribution_status  varchar(30),
																		CONSTRAINT pk_cdx_provenance_id PRIMARY KEY ( id ),
																		CONSTRAINT cdx_prov_docno_unique UNIQUE ( document_no )
) ;
ALTER TABLE cdx_attachment ADD CONSTRAINT fk_cdx_attachment_prov FOREIGN KEY ( document ) REFERENCES cdx_provenance( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE cdx_provenance ADD CONSTRAINT fk_cdx_prov_doc FOREIGN KEY ( document_no ) REFERENCES document( document_no ) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE cdx_pending_docs (
																			id                   int  NOT NULL  AUTO_INCREMENT,
																			doc_id               varchar(60)  NOT NULL  ,
																			timestamp            datetime  NOT NULL DEFAULT CURRENT_TIMESTAMP ,
																			reason_code          varchar(3)  NOT NULL  ,
																			explanation          text  NOT NULL  ,
																			CONSTRAINT pk_cdx_pending_docs_id PRIMARY KEY ( id )
);

insert into ctl_doctype (module,  doctype, status)
   values ("demographic", "progress note", "A");

insert into ctl_doctype (module,  doctype, status)
values ("demographic", "patient summary", "A");

insert into ctl_doctype (module,  doctype, status)
values ("demographic", "discharge summary", "A");

insert into ctl_doctype (module,  doctype, status)
values ("demographic", "care plan", "A");

insert into ctl_doctype (module,  doctype, status)
values ("demographic", "information request", "A");

insert into ctl_doctype (module,  doctype, status)
values ("demographic", "general purpose notification", "A");

insert into ctl_doctype (module,  doctype, status)
values ("demographic", "note", "A");

insert into ctl_doctype (module,  doctype, status)
values ("demographic", "e-referral note", "A");
create table cdx_clinics
(
    Id            int(10) auto_increment
        primary key,
    clinicId      varchar(36)  not null,
    clinicName    varchar(128) not null,
    clinicAddress varchar(255) null,
    constraint cdx_clinics_clinicId_uindex
        unique (clinicId)
);

create table cdx_ClinicAndProfessionalIds
(
    professionalId varchar(10) null,
    clinicId       varchar(36) null
);



