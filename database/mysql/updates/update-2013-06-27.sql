create table CtlRelationships (
        id int(11) NOT NULL AUTO_INCREMENT,
        value varchar(50) NOT NULL,
        label varchar(255),
        `active` tinyint(1) not null,
	maleInverse varchar(50),
	femaleInverse varchar(50),
        primary key(id)
);

insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Mother','Mother',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Father','Father',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Parent','Parent',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Wife','Wife',true,'Husband','Partner');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Husband','Husband',true,'Partner','Wife');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Partner','Partner',true,'Partner','Partner');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Brother','Brother',true,'Brother','Sister');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Sister','Sister',true,'Brother','Sister');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Son','Son',true,'Father','Mother');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Daughter','Daughter',true,'Father','Mother');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Aunt','Aunt',true,'Nephew','Niece');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Uncle','Uncle',true,'Nephew','Niece');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Nephew','Nephew',true,'Uncle','Aunt');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Niece','Niece',true,'Uncle','Aunt');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('GrandFather','GrandFather',true,'GrandSon','GrandDaughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('GrandMother','GrandMother',true,'GrandSon','GrandDaughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Parent','Foster Parent',true,'Foster Son','Foster Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Son','Foster Son',true,'Foster Parent','Foster Parent');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Daughter','Foster Daughter',true,'Foster Parent','Foster Parent');
insert into CtlRelationships (value,label,active) values ('Guardian','Guardian',true);
insert into CtlRelationships (value,label,active) values ('Next of Kin','Next of kin',true);
insert into CtlRelationships (value,label,active) values ('Administrative Staff','Administrative Staff',true);
insert into CtlRelationships (value,label,active) values ('Care Giver','Care Giver',true);
insert into CtlRelationships (value,label,active) values ('Power of Attorney','Power of Attorney',true);
insert into CtlRelationships (value,label,active) values ('Insurance','Insurance',true);
insert into CtlRelationships (value,label,active) values ('Guarantor','Guarantor',true);
insert into CtlRelationships (value,label,active) values ('Other','Other',true);

