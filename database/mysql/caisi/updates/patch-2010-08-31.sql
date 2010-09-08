update OcanFormOption set ocanDataCategoryValue="410515003" where id=149;
delete from OcanFormOption where ocandatacategory="Symptoms Checklist" and id>788 and id<820;

INSERT INTO `OcanFormOption` VALUES (1209,'1.2','Symptoms Checklist','2073000','Delusions'), (1210,'1.2','Symptoms Checklist','247783009','Grandiosity'),(1211,'1.2','Symptoms Checklist','7011001','Hallucinations');