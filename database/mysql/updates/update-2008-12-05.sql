-- remove initial comma from dates_printed
 update prescription set dates_reprinted = substr(dates_reprinted,2) where dates_reprinted is not null and substr(dates_reprinted,1,1) = ',';
