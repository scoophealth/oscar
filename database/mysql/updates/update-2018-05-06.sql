alter table Consent add `deleted` tinyint(1);
update Consent set deleted = 0 where deleted is null;
update Consent set deleted = 1, optout = 1 where optout is null;