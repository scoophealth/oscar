alter table BornTransmissionLog add demographicNo int;
alter table BornTransmissionLog add type varchar(20);
alter table BornTransmissionLog add httpCode varchar(20);
alter table BornTransmissionLog add httpResult mediumtext;
alter table BornTransmissionLog add httpHeaders text;
alter table BornTransmissionLog add hialTransactionId varchar(255);
alter table BornTransmissionLog add contentLocation varchar(255);
alter table BornTransmissionLog modify filename varchar(100);

