load data local infile 'SNOMEDCT_CORE_SUBSET_201202.txt.mysql' into table SnomedCore fields terminated by '|' ignore 1 lines (SnomedCore,description,conceptStatus,umlsCui,occurance,usagePercentage,firstInSubset,isRetiredFromSubset,lastInSubset,replacedBySnomedCid);

