create table demographic_ptbr (
	demographic_no numeric(10),
	cpf varchar(13) default '',
	rg varchar(13)  default '',
	chart_address varchar(15) default '',
	marriage_certificate varchar(50) default '', --certidão de casamento
	birth_certificate varchar(50)  default '', --certidão de nascimento
	marital_state varchar(1)  default '', --estado civil
	partner_name varchar(100)  default '',
	father_name varchar(100)  default '',
	mother_name varchar(100)  default '', 
	district varchar(100)  default '', --bairro
	address_no numeric(8),
	complementary_address varchar(100)  default '', --complemento
	constraint fk_demographic_ptbr_demographic_no foreign key (demographic_no) references demographic(demographic_no), 
	constraint pk_demographic_ptbr primary key (demographic_no)
);
