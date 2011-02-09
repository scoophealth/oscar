OSCAR McMaster cvs

This folder contains the necessary sql scripts to build the MySQL OSCAR database.

Generally speaking for new installation you only need to run the init scripts, and 
not the update scripts. The update scripts are there for updating existing installations
to the more recent schema.

It is highly advised that installations use the InnoDB engine. As of this writing
it is the default engine under windows installations but is not for linux/unix 
installations. You may encounter foreign key errors if you're not using innodb.