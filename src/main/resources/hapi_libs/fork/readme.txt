The jar files are in here because they need to be files "in the class path" not like normal library files.

These jar files are dynamically loaded upon special cases and conflict with other jars in the system so they should be left here.

Specifically this is for MDS lab uploads. When some one fixes / rewrites the MDS Lab Uploads, these can then be deleted.

These jars are referenced from DynamicHapiLoaderUtils.java