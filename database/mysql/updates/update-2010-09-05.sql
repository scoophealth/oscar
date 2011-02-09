create table ProviderPreference
(
	providerNo varchar(6) not null primary key,
	startHour tinyint,
	endHour tinyint,
	everyMin tinyint,
	myGroupNo varchar(10),
	colourTemplate varchar(10),
	newTicklerWarningWindow varchar(10),
	defaultServiceType varchar(10),
	defaultCaisiPmm varchar(10),
	defaultNewOscarCme varchar(10),
	printQrCodeOnPrescriptions tinyint not null,
	lastUpdated datetime not null
);

-- replace command because of glitch is oscar where a provider can have more than one preference even though only one is active
replace into ProviderPreference (select provider_no,start_hour,end_hour,every_min,mygroup_no,color_template,new_tickler_warning_window,default_servicetype,default_caisi_pmm,default_new_oscar_cme,0,now() from preference);

update ProviderPreference set myGroupNo=null where myGroupNo='';
update ProviderPreference set colourTemplate=null where colourTemplate='';
update ProviderPreference set newTicklerWarningWindow=null where newTicklerWarningWindow='';
update ProviderPreference set defaultServiceType=null where defaultServiceType='';
update ProviderPreference set defaultCaisiPmm=null where defaultCaisiPmm='';
update ProviderPreference set defaultNewOscarCme=null where defaultNewOscarCme='';

drop table preference;