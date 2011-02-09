# PHR MyOSCAR integration tables and migration

# The main documents table:

CREATE TABLE `phr_documents` (
  `id` int(11) NOT NULL auto_increment,
  `phr_index` varchar(70) default NULL,
  `phr_classification` varchar(250) default NULL,
  `datetime_exchanged` datetime default NULL,
  `datetime_sent` datetime default NULL,
  `sender_oscar` varchar(11) default NULL,
  `sender_type` int(1) default NULL,
  `sender_phr` varchar(255) default NULL,
  `receiver_oscar` varchar(11) default NULL,
  `receiver_type` int(1) default NULL,
  `receiver_phr` varchar(255) default NULL,
  `doc_subject` varchar(255) default NULL,
  `doc_content` text,
  `status` int(1) default NULL,
  `sent` tinyint(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM;

# extra table (maps phr/oscar ids)
CREATE TABLE `phr_document_ext` (
  `id` int(11) NOT NULL auto_increment,
  `phr_document_id` varchar(70) default NULL,
  `key` varchar(255) default NULL,
  `value` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM ;

# actions table for exchanges with PHR
CREATE TABLE `phr_actions` (
  `id` int(11) NOT NULL auto_increment,
  `phr_type` varchar(100) default NULL,
  `datetime_queued` datetime default NULL,
  `datetime_sent` datetime default NULL,
  `sender_oscar` varchar(11) default NULL,
  `sender_type` int(1) default NULL,
  `sender_phr` varchar(255) default NULL,
  `receiver_oscar` varchar(11) default NULL,
  `receiver_type` int(1) default NULL,
  `receiver_phr` varchar(255) default NULL,
  `action_type` int(1) default '0',
  `phr_classification` varchar(250) default NULL,
  `oscar_id` varchar(100) default NULL,
  `phr_index` varchar(70) default NULL,
  `doc_content` text,
  `status` tinyint(1) default 0,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM;

# Migration from the old indivo tables:
insert into phr_actions (datetime_queued, datetime_sent, sender_oscar, sender_type, sender_phr, receiver_oscar, receiver_type, receiver_phr, action_type, phr_classification, oscar_id, phr_index, doc_content, status) 
(select i.dateSent as datetime_queued, i.dateSent as datetime_sent, p.provider_no as sender_oscar, '1' as sender_type, p.value as sender_phr, de.demographic_no as receiver_oscar, '2' as receiver_type, de.pin as receiver_phr, '1' as action_type, 'urn:org:indivo:document:classification:medical:medication' as phr_classification, d.drugid as oscar_id, i.indivoDocIdx as phr_index, '' as doc_content, '2' as status FROM indivoDocs i, drugs d, property p, demographic de WHERE i.docType='Rx' AND i.oscarDocNo=d.drugid AND d.demographic_no=de.demographic_no AND p.name='MyOscarId' AND d.provider_no=p.provider_no AND i.update = 'I') UNION DISTINCT (select i.dateSent as datetime_queued, i.dateSent as datetime_sent, p.provider_no as sender_oscar, '1' as sender_type, p.value as sender_phr, de.demographic_no as receiver_oscar, '2' as receiver_type, de.pin as receiver_phr, '2' as action_type, 'urn:org:indivo:document:classification:medical:medication' as phr_classification, d.drugid as oscar_id, i.indivoDocIdx as phr_index, '' as doc_content, '2' as status FROM indivoDocs i, drugs d, property p, demographic de 
WHERE i.docType='Rx' AND i.oscarDocNo=d.drugid AND d.demographic_no=de.demographic_no AND p.name='MyOscarId' AND d.provider_no=p.provider_no AND i.update = 'U') UNION DISTINCT (select i.dateSent as datetime_queued, i.dateSent as datetime_sent, p.provider_no as sender_oscar, '1' as sender_type, p.value as sender_phr, de.demographic_no as receiver_oscar, '2' as receiver_type, de.pin as receiver_phr, '1' as action_type, 'urn:org:indivo:document:classification:medical:binaryData' as phr_classification, d.document_no as oscar_id, i.indivoDocIdx as phr_index, '' as doc_content, '2' as status FROM indivoDocs i, document d, property p, demographic de, ctl_document cd WHERE i.docType='document' AND i.oscarDocNo=d.document_no AND d.document_no=cd.document_no AND cd.module_id=de.demographic_no AND p.name='MyOscarId' AND d.doccreator=p.provider_no AND i.update = 'I') UNION DISTINCT (select i.dateSent as datetime_queued, i.dateSent as datetime_sent, p.provider_no as sender_oscar, '1' as sender_type, p.value as sender_phr, de.demographic_no as receiver_oscar, '2' as receiver_type, de.pin as receiver_phr, '2' as action_type, 'urn:org:indivo:document:classification:medical:binaryData' as phr_classification, d.document_no as oscar_id, i.indivoDocIdx as phr_index, '' as doc_content, '2' as status FROM indivoDocs i, document d, property p, demographic de, ctl_document cd WHERE i.docType='document' AND i.oscarDocNo=d.document_no AND d.document_no=cd.document_no AND cd.module_id=de.demographic_no AND p.name='MyOscarId' AND d.doccreator=p.provider_no AND i.update = 'U');


