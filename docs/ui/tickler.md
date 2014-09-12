##Tickler Module
--------------

Developed using Bootstrap 3 and Angular 1.2 by Marc Dumontier (marc@mdumontier.com)

Initial Gerrit commit https://source.oscartools.org:8080/10937


Angular modules
--

* ng-table
* oscarFilters
* securityService
* $resource
* $q
* $modal (ui.bootstrap)
* $http
* $timeout


Security Objects
--
* _tickler (r/w/u/x)
* _eChart (for the note edit feature) (r/w)


REST calls
--
* /notes/ticklerGetNote/{ticklerNote}
* /notes/ticklerSaveNote
* /persona/rights
* /providerService/search
* /providerService/providers2
* /tickler/ticklers
* /tickler/complete
* /tickler/delete

TODO
--
* Il18n
* multisite functionality (provider filters limited to site, and I guess ALL to all of that provider's site.
* sort/filtering (I think we need to rewrite the query so that it's a big join. that way we can do sort/filter across the whole dataset)
* re-integrate tickler_warn_period property once I figure out a better way then OscarProperties
* tickler add/edit templates and modal controllers.

Lost Functionality
--
* TICKLERSAVEVIEW property
