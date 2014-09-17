##Patient Search Module
--------------

Developed using Bootstrap 3 and Angular 1.2 by Marc Dumontier (marc@mdumontier.com)

Initial Gerrit commit https://source.oscartools.org:8080/10951


Angular modules
--

* ng-table
* securityService
* $resource
* $q
* $modal (ui.bootstrap)
* $http
* $timeout


Other noteworthy dependencies
--
* typeahead 0.9.3 (older version - major rewrite in 0.10) Already had the code working with this version, so wasn't willing to make the change yet)


Security Objects
--
* _demographic (r)


REST calls
--
* /demographics/search
* /demographics/quickSearch
* /demogaphics/searchIntegrator
* /persona/rights


TODO
--
* Il18n
* quicksearch - should out of domain  be false? integrator results not in this


Lost Functionality
--
* anything to do with specialist interface
* removed create demographic button since we have it in the global navigation bar now
