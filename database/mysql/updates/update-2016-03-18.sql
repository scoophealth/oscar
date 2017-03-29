DELIMITER $$

DROP PROCEDURE IF EXISTS `CreateIndex` $$
CREATE PROCEDURE `CreateIndex`
(
    given_database VARCHAR(64),
    given_table    VARCHAR(64),
    given_index    VARCHAR(64),
    given_columns  VARCHAR(64)
)
theStart:BEGIN

    DECLARE TableIsThere INTEGER;
    DECLARE IndexIsThere INTEGER;

    SELECT COUNT(1) INTO TableIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = given_database
    AND   table_name   = given_table;

    IF TableIsThere = 0 THEN
        SELECT CONCAT(given_database,'.',given_table, 
	' does not exist.  Unable to add ', given_index) CreateIndexMessage;
	LEAVE theStart;
    ELSE

	    SELECT COUNT(1) INTO IndexIsThere
	    FROM INFORMATION_SCHEMA.STATISTICS
	    WHERE table_schema = given_database
	    AND   table_name   = given_table
	    AND   index_name   = given_index;

	    IF IndexIsThere = 0 THEN
		SET @sqlstmt = CONCAT('CREATE INDEX ',given_index,' ON ',
		given_database,'.',given_table,' (',given_columns,')');
		PREPARE st FROM @sqlstmt;
		EXECUTE st;
		DEALLOCATE PREPARE st;
	    ELSE
		SELECT CONCAT('Index ',given_index,' Already Exists ON Table ',
		given_database,'.',given_table) CreateIndexMessage;
	    END IF;

	END IF;

END $$

DELIMITER ;

CALL CreateIndex(DATABASE(), 'billing_on_cheader1', 'dem_stat_date_Index', 'demographic_no, status, billing_date');
CALL CreateIndex(DATABASE(), 'casemgmt_tmpsave', 'provider_demoIndex', 'provider_no(4),demographic_no');
CALL CreateIndex(DATABASE(), 'casemgmt_note_link', 'note_idIndex', 'note_id');
CALL CreateIndex(DATABASE(), 'casemgmt_note_ext', 'note_idIndex', 'note_id');
CALL CreateIndex(DATABASE(), 'consultationRequests', 'demographicNoIndex', 'demographicNo');
CALL CreateIndex(DATABASE(), 'demographic', 'demo_last_first_hin_sex_Index', 'demographic_no, last_name, first_name, hin, sex');
CALL CreateIndex(DATABASE(), 'drugs', 'special_instructionIndex', 'special_instruction(5)');
CALL CreateIndex(DATABASE(), 'drugs', 'demo_script_pos_rxdate_Index', 'demographic_no, script_no, position, rx_date, drugid');
CALL CreateIndex(DATABASE(), 'dxresearch', 'demographic_noIndex', 'demographic_no');
CALL CreateIndex(DATABASE(), 'eChart', 'demographicNoIdIndex', 'demographicNo,eChartId');
CALL CreateIndex(DATABASE(), 'eform_data', 'patient_independentIndex', 'patient_independent');
CALL CreateIndex(DATABASE(), 'eform_data', 'dem_inde_stat_date_time_Index', 'demographic_no,patient_independent,status,form_date,form_time');
CALL CreateIndex(DATABASE(), 'eform_values', 'fdid_demo_var_Index', 'fdid, demographic_no, var_name');
CALL CreateIndex(DATABASE(), 'facility_message', 'facility_idIndex', 'facility_id');
CALL CreateIndex(DATABASE(), 'formLabReq10', 'demographic_noIndex', 'demographic_no');

CALL CreateIndex(DATABASE(), 'formONAREnhancedRecord', 'demographic_noIndex', 'demographic_no');
CALL CreateIndex(DATABASE(), 'formONAREnhancedRecordExt1', 'idIndex', 'ID');
CALL CreateIndex(DATABASE(), 'formONAREnhancedRecordExt2', 'idIndex', 'ID');

CALL CreateIndex(DATABASE(), 'icd9','icd9Index','icd9');
CALL CreateIndex(DATABASE(), 'messagetbl','id_by_subject_Index','messageid,sentby,thesubject');
CALL CreateIndex(DATABASE(), 'property', 'provider_noIndex', 'provider_no');
CALL CreateIndex(DATABASE(), 'property', 'nameIndex', 'name(20)');
CALL CreateIndex(DATABASE(), 'tickler', 'statusIndex', 'status');
CALL CreateIndex(DATABASE(), 'tickler', 'demo_status_date_Index', 'demographic_no,status,service_date');
CALL CreateIndex(DATABASE(), 'queue_document_link', 'id_que_doc_status_Index','id, queue_id, document_id, status');
