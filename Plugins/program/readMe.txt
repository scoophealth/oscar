

(I)If the webapp is used as a standalone:

-	go to ‘org.caisi.PMmodule.common.PassUniversalVars.java’ and change  

    public final static boolean isWebAppStandalone = true;


(II)If the wabapp is used as an plugin of  the OscarWAR:

-	You should go to 
	‘org.caisi.PMmodule.common.PassUniversalVars.java’    and   
	make sure that the following statement is set to false.

    public final static boolean isWebAppStandalone = false;


(III)Use  [Admin] ==> [Assign Provider to Role and Program]  to  
     associate  provider to  domain (role is domain or team or facility at this stage)
     and to multiple programs.


(IV)Database tables
(A)	Database schema
		http://www.caisi.ca/wiki/index.php/Program_Management_Module
(B)	Tables used -- make sure those tables are present in your database.
		admission
		admission_history
		demographic       <--  using oscar_mcmaster's table
		demographic_pmm
		formintakea
        formintakeb
		provider          <-- using oscar_mcmaster's table
		provider_pmm
		provider_subgroup
        provider_role_program
		program
		program_association
		program_queue
		program_queue_history
		
-	Only the demographic, provider, and program tables have to have relevant
	data in there. All are tables can be empty at the beginning of usage.

-- You have to add one more important table for the client based security to work:
   
   table  'demographic_pmm'

-- You have to populate the  demographic_pmm  manually at first.

2005/12/29
-- modified the following tables:
-- I added 2 tables:  demographic_pmm  (adding  'sharing'  field)  and  provider_pmm  
-- I also added the  'agency_id'  field  into 3 tables: program, program_queue, program_queue_history.


2006/01/26
-- Added ProviderAgencyProgram  Entity

