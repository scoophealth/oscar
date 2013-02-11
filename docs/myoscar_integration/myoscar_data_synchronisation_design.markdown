Overview
=

The purpose of this document is to discuss the design of how data synchronisation between oscar and myosar should take place.

The discussion here presumes an oscar and myoscar system are already setup, and the clinic, provider, and patients accounts are setup in both oscar and myoscar.

Although this document specifically references oscar and myoscar, the same design layout can be used for third parities trying to send data to myoscar, and the same design can be used for oscar sending data to other servers other than myoscar (like the integrator).

Scenarios and Logical Workflow
=
There are 2 basic scenarios as to when synchronisation takes place :
1. A patient has automatic synchronisation disabled, but explicitly asks a provider to send them a piece of data like “the xray picture of my broken arm”.
2. A clinic & provider & patient has automatic synchronisation enabled.

Scenario #1
-
A provider should only be able to click the “send to phr” button if synchronisation is disabled, if synchronisation is enabled it should just disable the button noting that sync is enabled. As a note, the “send to phr” button should only be enabled to begin with if both provider and patient have myoscar accounts and a permissions check on the provider writing medical data to the patient returns that they are allowed to send medical a data, and you need to check the patients verification level in oscar.

When the provider clicks “send to phr”, the system should check if this piece of data has already been sent or not. If it has previously been sent, it should ask the provider “are you sure you want to send again”. The reason we will allow re-sends is because as an example the patient may have deleted their copy (or archived, but they think and feel like it's deleted) and just ask the provider to send it again. Another case is if the oscar side keeps a rolling update on a piece if data, i.e. I think allergies or issues are currently like that where they are modifiable instead of final. In this case the button would check the myoscar server for the item, if it doesn't exist it can create it, or it can update it if it does exist. It can request the item from the sourceId so oscar doesn't need to magically know the myoscar rowId before hand.

When data is sent to myoscar (both create and update), oscar should write an entry into it's own log system which logs exactly what is sent and what date it was sent. This must be logged every time a piece of data is sent, even if it's a duplicate because the state of the object may have changed. It is the responsibility of the oscar system to log and track to whom else it has directly sent data.

Scenario #2
-
A clinic can have auto-synchronisation to myoscar enabled at a system level in oscar. Most likely this should be a “program” setting (as programs in oscar are clinics). There should be a background task running roughly once a day or 4 times a day checking for synchronisation. (i.e. Not once a week, and not once every minute, it should be a reasonable processing overhead with a reasonable delay noting that this is not a real time communications system.)

The clinics synchronisations preference should include the type of data to be synchronised. i.e. There needs to be an option to say enable for Allergies, but not enabled for Prescriptions.

When the synchronisation thread runs, it needs to check all permissions from both the local oscar system as well as the remote myoscar system as everyone has a say in what is synchronised.

From the oscar side :
* The patient must have a myoscar account noted in the oscar system
* The patient verification level must be at least N (where N is configurable)
* the clinic/program must have sync enabled for that particular type of data
* for each piece of patient data, it needs to verify it is from the correct program/clinic, it must not send data from another program/clinic
* for each piece of patient data, it needs to check the provider whom wrote/added this piece of data, the provider themselves has an option to enable/disable data synching

From the myoscar side :
the patient has a preference setting indicating if they want you to send them their data or not (AccountWs.getDataSyncPreferenceForCallingEntity(Long patientMyOscarId)). If the patient does not want their data synced to them you should not send any data. (Note that when a provider clicks the button we do not check this, this preference is for auto-sync only and the presumption is that if the provider is clicking the button, then they were explicitly asked for that piece of data, therefore overriding this general preference.) Although it's probably safe to assume the client has granted permissions if they have asked for syncing, the code itself must fail gracefully if NotAuthorisedException is thrown.

Only when both the oscar and myoscar side checks have been done is the piece of data “eligible” to be sync-ed. You still now need to check that the piece of data has not previously been sent before. For the synching algorithm, if the data is final, then if it has been synced before it should do nothing. If the data is updateable, then it will have to check the last modified date against the previous sync date to determine what to do (create/update/nothing). Upon sending of data it needs to track 2 things.

1. It needs to track the exact piece of data in it's current state, just like when a provider manually clicks the button.

2. It needs to track that this piece of data has been sync-ed. 
Note there is a subtle difference between #1 and #2. The first tracking requires the data to be logged where as the second doesn't. It will be an implementation detail later discussed as to whether this can be one database table or not.

Architecture Details
=
It should be noted that there's no particular reason as to why this thread needs to be run on he same machine as the oscar which is serving providers browsers. To prevent bogging down a user facing server, this can be run on another machine. It should be noted that there will still be database load but at least part of the system load will be alleviated.

WebApp
-
In line with the new modular approach to oscar we will want to implement this as a completely separate webapp. We should use the web services in oscar to work with the data so we're isloated from the DB layer directly. If this is deployed on another machine other than the user facing server, then you will want to deploy another copy of oscar on this machine as well so the web services do not end up loading the user facing server again. (If not then it can reference the user-facing server directly) i.e.

[deployment architecture diagram](myoscar_data_syncrhonisation_design.odg)

There will need to be a couple of new configuration screens.
1. clinic/program sync configuration : basically 1 screen almost identically displaying the database table as outlined below. no real logic required, just write / delete rows from the db.
2. provider sync preference : because I'm lazy and this is the first version, this will also be just a pass through to the database table. Some one else in the future can make pretty search options on the screen.

These 2 web pages can be hosted by this webapp directly, this allows the oscar system to be less invaded by this component. Since oscar doesn't have a module/component registry yet, oscar can just hard code a show/no-show if statement on the configuration pages which link to these pages. When a provider clicks on a link that links to this webapp pages it will need to pass along a security token. How this token works is to-be-determined as it will use what ever is in oscar or we may need to quickly add a security token framework to oscar (maybe copied out of myoscar_sever).


Thread Algorithm
-
The deamon thread will generally need to do thew following pseudo code. Note the pseudo code does not have any check points, i.e. at any point in time the thread can stop and in theory (with the exception of cutting it right between the send&save synced command) the next restart of the system and thread should continue were it left off.

		MainThread.run()
		{
			while (true)
			{
				Thread.sleep(30000); // arbitrary small value less than 1 hour, 5 minutes is reasonable

				checkIfSyncRunsAtThisTime();
				
				for each facility
				{
					for each clinic in that facility
					{
						syncOneClinic();
					}
				}
			}
		}

		checkIfSyncRunsAtThisTime()
		{
			get the current day+hour, i.e. "2013-01-02 15" (15 = 3pm)

			if the hour matches configured time to run
			{
				compare to last sync time (store as static variable or something)
				if it's the same as last sync time, no need to run as already run
				if it's not the same, then run thread and set last sync time to now.
			}
		}

		syncOneClinic()
		{
			for each patient in the clinic
			{
				checkPatientHasMyOscarId();
				checkOscarVerificationLevel();
				checkPatientHasMyOscarSyncPreference=True; (on myoscar server side)
				for each enabled datatype
				{
					Span Multiple threads to (max thread count needs to be configurable)
					{
						syncOneDataTypeForAPatient();
					}
				}
			}
		}

		syncOneDataTypeForAPatient()
		{
			for every piece of medical data (in this facility / clinic) for this patient
			{
				checkRenderingProviderHasSyncEnabled();
				checkIfDataHasPreviouslyBeenSent();

				if data needs to be sent
				{
					sendData();
					logDataSentAndSynced();
				
					SleepThrottle(); (must be configurable sleep time to prevent network saturation, similar to integrator thread throttle)
				}
			}
		}


Database Storage (preference, logging, tracking, etc)
-
Each clinic needs to be able set their own preference on a per-datatype basis.  We will need a new table like :

| Clinic/ProgramId | DataType | Sync | 
| - | - | -|
|222|Null|True|
|222|Allergies|False|

If the dataType is null we will use that as the system wide default. So in this example all data types will sync except allergies. The inverse default can also be modeled with dataType=null and sync=false then explicitly enable dataTypes by setting those to true.


Each provider must also be allowed to set their sync preferences. This should be set both as global and at an individual patient level. As an example

|Clinic/ProgramId|ProviderId|demographicId|Sync|
|-|-|-|-|
|222|333|null|false|
|222|333|444|true|

So in this example, for clinic/program=222, provider=333 set sync to false for the “default” denoted by demographicId=null. So none of his patients data is being synced even though the sync at the clinic level is enabled. The second entry signifies an exception, for demographic 444 sync is explicitly true. So in this example none if his patients except patient 444 will have their data synced. By explicitly having the sync column, it means you can also do the inverse default. You could sync by default for all patients, then specifically exclude certain ones. The check for if a provider has sync enabled or not should check the speicif demographicId for a record first, then check for a null option if no demographic exists, and if neither exists, then it should default to the clinic (which would be implicitly true since the thread ran for the clinic, this implies if the clinic is set to false, no provider can override that setting).

Sync and Data Logging needs to be stored in oscar. I will suggest that these should be 2 different tables because of their different usages and data requirements and data retention requirements. As an example :

**Sync Table**

|Thread/Process|SyncDestination|DataTable|DataId|LastSyncDate|
|-|-|-|-|-|
|MyOscarSync|Maple.myoscar.org:8080|Allergies|23|2013-01-05 12:59:00|
|MyOscarSync|Maple.myoscar.org:8080|Immunisations|664|2013-02-02 11:23:00|
|Integrator|Integrator.example.com|Consent|111|2013-02-02 12:12:12|

**DataLogging Table**

|Sender|Recipient|Data|dateSent|
|-|-|-|-|
|Oscardoc|Maple.myoscar.org|<allergies><name>peanuts</name><onset>2 years old</onset></allergies>|2013-01-01 10:09:00|
|MyOscarSyncAccount|Maple.myoscar.org|<allergies><name>peanuts</name><onset>2 years old</onset></allergies>|2013-01-01 12:59:00|
|MyOscarSyncAccount|Maple.myoscar.org|<immunisation><name>flue shot</name><route>arm</route></allergies>|2013-02-02 11:23:00|
|IntegratorSyncAccount|Integrator.example.com|consent=true, demographic=4, foo=bar|2013-02-02 12:12:12|

So note that there's not much duplicated in each table. 

Sync does not care who sent the data, just that it was sent. It also does not care how many times it's been sent, only the last date it was sent. It also does not care what was actually sent, just the table/row Id reference to the item.

DataLogging on the other hand care about every time data is sent. It must log duplicates if a data is sent twice. It must log who initiated the send command and the exact data which was sent.

Maintenance on these 2 pieces of data are also different. The sync table should never be deleted and the number of entries in there will at max equal the number of medical data rows in oscar. The DataLogging table however can always be deleted in the sense that it is not used by the system. It is only ever used for auditing purposes so as an example you could archive the table every year and then delete all entries out of the production database. The rows in this table are completely unbound, it is determined by the actions of the persons and not the amount of data in oscar.

The Sync table needs to be quickly searchable. The DataLogging table needs no searching ability at all, as a matter of fact it should never be read from code / the application.

As a side note, there is already a table for Datalogging in oscar, it's called RemoteDataLog, this table adds another field called “action”. I believe it's to be “send|receive” because when data is displayed in oscar, oscar also has an obligation to log what it has shown the provider. Note that this table is still write-only because data should always be retrieved fresh from external systems and never read out of the log here as the data is stale. 

My suggestion here is to add a Sync table, and perhaps add an additional column for SyncType where syncType=(Integrator|MyOscar|next_thing). 

In theory, the records should be small enough that when dealing with 1 patient in 1 clinic, you should be able to read all sync entries for that patient/clinic into memory so it's not constantly hitting the database for every item.

As an additional note, there's no requirement as to where these tables reside. i.e. there's no hard requirement for these tables to be in the oscar schema. It maybe beneficial for the data sync and data logging tables to be in oscar as it can be shared by any data import/export logic like the integrator. The configuration however is more independent. A very good argument can be made for using it's own storage and it will be an implementation detail as to whether we put it in the same database/schema or possibly just write out a hashtable to an xml file or something similar.


System configuration Properties
-
We'll probably need a few new oscar_mcmaster.properties
	
		# so it knows to show the links to the configuration pages or not	
		myoscar.sync_component_enabled=true|false
		# so it knows where the configuration pages are located
		myoscar.sync_url_root=http://127.0.0.1:8080/oscar_myoscar_sync 


We'll need at least a few new properties for the sync webapp

		# csv of hour values, so 1,15 would mean at 1am and at 3pm start the sync thread.
		sync_send_thread_start_hours=1 

		sync_minimum_verification_level = 3
	
		# a la integrator send throttle, to prevent network saturation
		sync_data_send_throttle_ms=1 


Servicing Multiple Oscars
-
The main concept is basically ASP-ing this component. The main intent is so that oscar users don't have to actually install this component themselves. If we make this component host-able them oscar only needs to provide the hoster with the ip/user/password and all syncing can be done for them.

The concern is locallity of data though. The audit logs must stay in oscar as they are oscar data. The sync logs can however be kept on the hosted sync machine.

If we go with this model, the logs may need to be done twice. Oscar may need to log that data has left for the sync component, and the sync component would then log that data has left for the myoscar_server.

This model would also imply that the sync component needs to maintain a list of oscar servers and credentials to that server. All data logs and sync logs on this server would then need to be qualified by the oscar instance.