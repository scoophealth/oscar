This is the database script, and helper files for working with UMLSs SnomedCT Core dataset.

It is available @ https://uts.nlm.nih.gov/home.html

Just register for a license, then you will get instructions to downloads SnomedCt files

These files were used against http://download.nlm.nih.gov/umls/kss/IHTSDO20120131/SnomedCT_Release_INT_20120131.zip


snomedinit.sql - Create table
setup_snomedct_core_file.sh - simple script to remove the (finding)/(procedure)/(disorder) suffixes from descriptions
load_snomed_core.sql - insert data into table
populate_issue_SnomedCore.sql - loads snomedct core data into issue table for use in CME "assign issue" section

