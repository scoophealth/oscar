This directory is meant to hold random java classes which maybe used by the
code but for some reason isn't part of the code base.

----------------------------
TrackingBasicDataSource.java
----------------------------
With the example of TrackingBasicDataSource.java it's not part of the code
base because it compiles differently under java 1.5 and 1.6 and are not
compatible, it's not possible to write a correct version which compiles
under both because it implements and interface from the jdk which changed
drastically between 1.5 and 1.6. Once we move to java 1.6+ only, we can
move this class back and remove the precompiled jar that contains this class.

-----------
wkhtmltopdf
-----------
This also now contains some binary utilities that are called by the code on
certain platforms. wkhtmltopdf is used to help convert html pages into
pdf files. There's no other good option so for now we're calling binary
utilities for this.

