CREATE TABLE oscar.cdx_provenance ( 
	id                   int  NOT NULL  AUTO_INCREMENT,
	doc_id               varchar(30)  NOT NULL  ,
	version              varchar(2)    ,
	effective_time       datetime  NOT NULL  ,
	parent_doc           varchar(30)    ,
	set_id               varchar(30)    ,
	in_fulfillment_of_id varchar(30)    ,
	kind                 varchar(30)  NOT NULL  ,
	action               varchar(10)  NOT NULL  ,
	log                  bigint    ,
	payload              text    ,
	CONSTRAINT pk_cdx_provenance_id PRIMARY KEY ( id )
 ) ;

ALTER TABLE oscar.cdx_provenance ADD CONSTRAINT fk_cdx_provenance_log FOREIGN KEY ( log ) REFERENCES oscar.log( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;
