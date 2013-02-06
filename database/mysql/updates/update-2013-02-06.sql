update hl7TextInfo set priority = null where length(priority) = 0;
update hl7TextInfo set result_status = null where length(result_status) = 0;