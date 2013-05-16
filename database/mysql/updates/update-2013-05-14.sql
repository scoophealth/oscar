CREATE INDEX scheduledate_sdate on scheduledate(sdate);
CREATE INDEX scheduledate_pno on scheduledate(provider_no);
CREATE INDEX scheduledate_status on scheduledate(status);
CREATE INDEX scheduledate_key1 on scheduledate(sdate,provider_no,hour,status);

