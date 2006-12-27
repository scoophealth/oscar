This is the document for ON billing update in FAQ mode.

1. Can clinics still use the old billing?

Yes, just ignore all the questions below. If it is updated to the new billing system, you should not switch back to the old billing system.

2. Is there a switch to use the new billing?

Just add the following line to the oscar property file.
isNewONbilling=true

3. What do i have to do to upgrade a clinic?

It is suggested to do the upgrade at the beginning of each month. 
a. Set up the property file. (See Q.2)
b. Update oscar.war to the latest version from cvs.
c. Convert the billing data to the new billing system by logging oscar and running the following url:
https://[your oscar path]/report/billingconvert.jsp
d. Create new database tables. (See oscarinit_on.sql and oscardata_on.sql on cvs) 

4. Anything else that you think will help?

The system is tested on MySQL 4.1 (not on MySQL 4.0).