CREATE TABLE SnomedCore
(
   id int not null auto_increment,
   SnomedCore varchar(10) NOT NULL,
   description varchar(255) NOT NULL,
   conceptStatus varchar (30) NOT NULL,
   umlsCui varchar (10) NOT NULL,
   occurance int,
   usagePercentage decimal(5,4),
   firstInSubset varchar (20) NOT NULL,
   isRetiredFromSubset varchar (10) NOT NULL,
   lastInSubset varchar (20),
   replacedBySnomedCid varchar (10),
   primary key(id),
   key(SnomedCore),
   key(description),
   key(conceptStatus)
);
