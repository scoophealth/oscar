Howto: Selenium
===============

--------
Overview
--------
This is how we will use selenium in OSCAR to test the web pages and integration between OSCAR and MyOSCAR.

You must have both myoscar_server and myoscar_client up, running and correctly configured (to communicate with each other) in order to do the integration testing.

You must be running firefox and you need to have selenium IDE for firefox installed. For testing with Selenium it is recommended that you install and launch Selenium IDE from another profile other than your profile or the "default" profile.  

To lauch firefox in a profile called selenium, from the command line run "firefox -P selenium" without the quotes.  During the first time of doing this, you will have to edit and save the profile.

You will want to do this because your default profile or the one you use can compete with Selenium IDE by :

* attempting to complete forms you have saved
* attempting to enter in saved user names
* attempting to enter in saved passwords 


You can see the Selenium IDE 2.0 documentation here : http://docs.seleniumhq.org/


We will store our selenium tests in oscar/src/test/selenium, each subdirectory represents different user role use cases. We will have a test_suite.html for all the tests, and each test case should roughly be one feature.  This allows for easy additions and deletions of test cases (test case management).

------------
Running tests
------------
- always start with a new development database setup, i.e. reset_test_data.sh
- Run the tests suites in order
	- setup (creating users - providers and demographics)
	- module/area being tested (i.e. registration, messaging...etc)


- In order to keep an AUA or Terms of Use test case in each suite, there is a test suite titled "OSCAR - MyOSCAR - setup" which should be run before any test suite.  This test suite initializes the admin account in MyOSCAR and the oscardoc account in both OSCAR and MyOSCAR.



The test cases are fairly independent but the admin test suite sets up the provider account and the patient accounts so you MUST run admin before doing anything.

----------------
Making new tests
----------------
In general, start with a fresh database and run the setup test suites, then load the suite appropriate to your test and make your new test case.

The test case should be fairly independent of each other and or be mostly self contained. If you do anything too wierd, try to also undo / clean up before finishing the test case. If it's just not possible, make a new user / patient specific to your test so it doesn't affect everyone else.


----------------
Tips: Base URL:
----------------
The tests have been saved without the base url saved in the html files.  This means when you run the tests, make sure to specify the BASE URL before running.  MyOSCAR has listened on different ports (i.e. 8091/92 and 8095/8096) so it is easier this way.  If the BASE URL was saved in each test case and the MyOSCAR changes in the future, you would have to modify each test case.  Now you just select the URL when executing the test suite for the first test case and each test after assumes the first.

Syntax for the BASE URL code in the HTML files (test files):

<link rel="selenium.base" href="https://www.domain.org:8088/" />


 
