This document outlines how we will handle storing large blobs of data, i.e. files.

The objective is to provide a well defined API to remove the day-to-day programmer 
from the nuances and details of how we will handle large blobs. Specifically we do
not want programmers directly accessing the file system (and perhaps coding bugs
which may then compromise the local file system).

This API will be abstracted from the underlying storage mechanism, so the actual
storage mechanism can change with in reason. Examples of underlying storage 
mechanisms may be the file system itself, or a database blob column, or something
more extensive like hdfs or a network call to another storage mechanism.

This is intended to replace all current file system access along with oscarDocuments.

---
API
---
	public static void setBlobData(String qualifier, String dataId, byte[] data);
	public static byte[] getBlobData(String qualifier, String dataId);
	public static void removeBlobData(String qualifier, String dataId);
   
	(We may need to implement a streaming or chunking option later.)
   
	The qualifier is any qualifier for the data, i.e. the data type. As an example
	you may have qualifiers like "UPLOADED_LABS" or "PATIENT_DOCUMENTS". The qualifier
	should be alphanumeric and dashes/underscores only. Coming from the file system,
	think of this as a directory name. Coming from a database, think of this as 
	the table name.
   
	The dataId is the id of this item, it should be unique with respect to the qualifier.
	The dataId should be alphanumeric and dashes/underscores only. Coming from the 
	file system, think of this as the filename. Coming from the database, think
	of this as the row Id.
   
	The data can be any data in byte form. As an example this can be bytes from
	an image, or bytes of a string xml etc.
	
	Future considerations include adding gz compression before data storage 
	and uncompression during data access. This can be done transparently and
	should ommit known compressed data types like images/videos/pdfs or 
	have it configurable by the getter/setter.
	
--------------
Expected Usage
--------------
	This is a raw data bytes storage mechanism only. This should always be accompanied
	by something else holding the meta-data. As an example a database row.
	
	With a database row, things like the patient, or provider, or lastUpdate or 
	active/archived fields, mimeType, should all be in the database row and are not part of 
	this storage mechanism.
	
	The expectation is that all queries / reports / logic are performed against the database
	table, only the final get/set data is performed against this blob storage.


-----------------------
Internal implementation
-----------------------
	We should start with 2 expected implementations
		- fileSystem
		- database blob table
		
	Fileystem would be the same as most access now but at least we'd have a single point of coding 
	error rather than every programmer. We should have a data root directory, then use the qualifier 
	as a sub directory, then use the dataId as a filename. This access is relatively quick but it doesn't scale.
	The system will slow and suffer after about 20k entries in the same qualifier. This solution will 
	also not cluster well - although an NFS server or similar can help. Data backup 
	would have to include this data root directory (which is no different than currently telling
	people to backup catalina_base/webapps/oscarDocuments right now).
	
	Database blob table, we should probably just create 1 table with 3 columns (qualifier|dataId|blob).
	We may wish to have this configured separately so in theory some one could setup
	a second database to handle this traffic as to not impact the main sql database. 
	This has mediocre access speed. The number of entries how ever scale better than the file system
	with a reasonable limit in the tens of millions. There is a reasonable-usage cap on the data size
	though, roughly 16-32mb. This solution will cluster as well as your database does. Data backups
	will be easier in that no file system backups will be required.
	
	A future consideration maybe something like HDFS, not realistic right now but an honourary
	mention here. HDFS has an small access penalty like databases (when compared to the local file system)
	but scales excessively well with near linear scaling and can be highly redundant. There
	is virtually no limit on the number of files / entries, it can be into the billions and petabytes
	of data.
	
	The "right" storage solution will depend on the usage, if we implement this API then
	it will be up to the system administrator to pick their poison. In the 
	future there can even be upgrade paths so some one could start with a local filesystem
	and migrate to an hdfs solution.	
	
	Some benchmarks taken from another project (myoscar) is listed here for reference, these
	are useful only in showing relative times, not absolute times. The absolute times will 
	vary based on your equipment. (The results are in ms.)
	
	 * machine specs : intel mobile i5-520, 8 gigs of ram, fedora 16 x86_64. Intel-320 SSD 160gb.
	 * 	
	 * /5k_test_image.jpg: db write:14.91
	 * /5k_test_image.jpg: db read:3.75625,
	 * /5k_test_image.jpg: fs write:16.835
	 * /5k_test_image.jpg: fs read:3.72375,
	 * /5k_test_image.jpg: hdfs write:23.84
	 * /5k_test_image.jpg: hdfs read:7.93125,
	 * 
	 * /350k_test_image.jpg: db write:38.245
	 * /350k_test_image.jpg: db read:5.83,
	 * /350k_test_image.jpg: fs write:15.105
	 * /350k_test_image.jpg: fs read:3.97375,
	 * /350k_test_image.jpg: hdfs write:31.71
	 * /350k_test_image.jpg: hdfs read:9.1625,
	 * 
	 * /1mb_test_image.jpg: db write:100.71
	 * /1mb_test_image.jpg: db read:14.00375,
	 * /1mb_test_image.jpg: fs write:27.33
	 * /1mb_test_image.jpg: fs read:5.3675,
	 * /1mb_test_image.jpg: hdfs write:40.035
	 * /1mb_test_image.jpg: hdfs read:14.4225,
	 * 
	 * /5mb_test_image.jpg: db write:445.485
	 * /5mb_test_image.jpg: db read:58.3625,
	 * /5mb_test_image.jpg: fs write:70.01
	 * /5mb_test_image.jpg: fs read:20.3525,
	 * /5mb_test_image.jpg: hdfs write:123.9
	 * /5mb_test_image.jpg: hdfs read:39.20375,
	 * 
	 * /11mb_test_image.jpg: db write:932.265
	 * /11mb_test_image.jpg: db read:142.03876,
	 * /11mb_test_image.jpg: fs write:159.17
	 * /11mb_test_image.jpg: fs read:108.67,
	 * /11mb_test_image.jpg: hdfs write:209.32
	 * /11mb_test_image.jpg: hdfs read:80.76625,
	 * 
	 * /32mb_test_file.txt: fs write:368.36
	 * /32mb_test_file.txt: fs read:298.38,
	 * /32mb_test_file.txt: hdfs write:421.54
	 * /32mb_test_file.txt: hdfs read:342.965,
	
--------------
Migration plan
--------------
Step1 : Write the API and a local file system handler
Step2 : All new code must be routed through the API, no new commits accessing the file system directly will be allowed.
Step3 : Systematically track down all references to oscarDocuments as well as any other file handles and java.io.tmp dir references and convert each one. 
	