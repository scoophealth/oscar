CREATE TABLE cdx_attachment (
															id                   int  NOT NULL  AUTO_INCREMENT,
															document             int  NOT NULL  ,
															attachment_type      varchar(10)  NOT NULL  ,
															reference 					 varchar(60) NOT NULL ,
															content              blob  NOT NULL  ,
															CONSTRAINT pk_cdx_attachment PRIMARY KEY ( id )
);

CREATE INDEX idx_cdx_attachment ON cdx_attachment ( document );

ALTER TABLE cdx_attachment ADD CONSTRAINT fk_cdx_attachment FOREIGN KEY ( document ) REFERENCES document( document_no ) ON DELETE NO ACTION ON UPDATE NO ACTION;
