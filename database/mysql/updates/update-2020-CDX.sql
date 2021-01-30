create table cdx_messenger
(
    id                  int auto_increment
        primary key,
    doc_id              varchar(60)  null,
    author              varchar(255) not null,
    patient             varchar(255) not null,
    recipients          varchar(255) not null,
    primary_recipient   text         null,
    secondary_recipient text         null,
    document_type       varchar(255) not null,
    category            varchar(255) not null,
    content             text         not null,
    time_stamp          datetime     not null,
    delivery_status     varchar(255) not null,
    draft               char         null
);

create table cdx_messengerAttachments
(
    id          int(10) auto_increment
        primary key,
    requestId   int(10) null,
    document_no int(10) null,
    doctype     char    null,
    deleted     char    null,
    attach_date date    null,
    demo_no     int(10) null
);

alter table consultationRequests add isAdviceRequest char null;
