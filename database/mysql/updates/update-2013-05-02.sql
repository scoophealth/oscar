-- Move archive records from demographicExt to demographicExtArchive table

DROP PROCEDURE IF EXISTS MIGRATE_DEMOGRAPHIC_EXT;

DELIMITER //

CREATE PROCEDURE MIGRATE_DEMOGRAPHIC_EXT(keepRecord BOOLEAN)
BEGIN
    DECLARE demographicExtId int;
    DECLARE no_more_result int;

    -- Cursor for archiving
    DECLARE  curArchive CURSOR FOR
        SELECT  ext.id
        FROM    demographicExt ext,
                (SELECT demographic_no,
                        provider_no   ,
                        key_val       ,
                        max(date_time) date_time
                 FROM   demographicExt
                 GROUP  BY demographic_no, key_val) latest
        WHERE    ext.demographic_no = latest.demographic_no
        AND      ext.key_val        = latest.key_val
        AND      ext.date_time      < latest.date_time;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_result = 1;

    -- Archive records (Move from DemographicExt to DemographicExtArchive)
    SET no_more_result = 0;
    OPEN curArchive;
    REPEAT
        FETCH curArchive INTO demographicExtId;
        IF no_more_result = 0 THEN
            INSERT INTO demographicExtArchive (
                    demographic_no,
                    provider_no   ,
                    key_val       ,
                    value         ,
                    date_time     ,
                    hidden        )
            SELECT  demographic_no,
                    provider_no   ,
                    key_val       ,
                    value         ,
                    date_time     ,
                    hidden
            FROM    demographicExt
            WHERE   id = demographicExtId;
        END IF;
    UNTIL no_more_result
    END REPEAT;
    CLOSE curArchive;

    -- Delete archived records
    IF keepRecord = FALSE THEN
	    SET no_more_result = 0;
	    OPEN curArchive;
	    REPEAT
	        FETCH curArchive INTO demographicExtId;
	        IF no_more_result = 0 THEN
	            DELETE  FROM demographicExt
	            WHERE   id = demographicExtId;
	        END IF;
	    UNTIL no_more_result
	    END REPEAT;
	    CLOSE curArchive;
    END IF;

    ALTER TABLE demographicExt ADD CONSTRAINT uk_demo_ext UNIQUE (demographic_no, key_val);
END//

DELIMITER ;

CALL MIGRATE_DEMOGRAPHIC_EXT(FALSE);

DROP PROCEDURE MIGRATE_DEMOGRAPHIC_EXT;

