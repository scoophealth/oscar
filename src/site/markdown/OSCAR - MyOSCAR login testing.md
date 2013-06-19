OSCAR - MyOSCAR login testing
===================================


----------
Purpose
----------
The purpose of this document is to detail the tests that will be executed to validate the functionality of OSCAR -> MyOSCAR integration


--------------
Background 
--------------
Communication between OSCAR and MyOSCAR happens via web services.


### Test cases

ID:	test Case:					Parameters:						Expected Result:
* 1	login/credentials				(good/bad credentials)					Can login (normal flow).  Errors displayed on exceptional flow
* 2	login/account status				(MyOSCAR account disabled/inactive/eula			Can login (normal flow).  Errors displayed on exceptional flow
* 3	login/keep alive				Password (remember)					Keeps session active (MyOSCAR session in OSCAR) 
* 4	login/keep alive				Password (don't remember)				Session is lost after close MyOSCAR portal
* 5	login/MyOSCAR account				Password (remember) - MyOSCAR password changed		Current session retained, leave and re-enter; you are logged out
* 6	login/messages					access sharing setup/not setup				Can send/reply/delete messages / can't select recipient if no access







	


	
