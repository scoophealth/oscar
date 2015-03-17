alter table DrugDispensing add archived tinyint(1) not null;
update DrugDispensing set archived=0;

