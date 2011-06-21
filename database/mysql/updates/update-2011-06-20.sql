alter table drugs add hide_cpp tinyint(1);
update drugs set hide_cpp=0;
