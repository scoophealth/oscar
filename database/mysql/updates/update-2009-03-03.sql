update demographic d set spoken_lang =
(select value from demographicExt e where key_val='language' and d.demographic_no=e.demographic_no);

