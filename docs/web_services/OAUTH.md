Web Services via OAuth
----------------------
Access to web services via OAuth is available on at /ws/oauth for token requests and /ws/services for data requests.  For example, if the URL for a web service accessed via an OSCAR sesion token is /ws/rs/status/checkIfAuthed, then the same resource can be requested with OAuth authentication at /ws/services/status/checkIfAuthed.

When creating new web service classes, you must add a new <bean /> tag to src/main/resources/applicationContextREST.xml in both the <jaxrs:server id="restServices" address="/services" /> element (for OAuth access) and the <jaxrs:server address="/rs" id="jaxrServer" /> element (for HTTP cookie access).

All OAuth token requests take place at /ws/oauth/{command}.  See the Admin > Integration > REST Clients screen in the OSCAR EMR 15 user interface for more details.

A special OAuth only endpoint is available to see information about the OAuth request at /ws/services/oauth/info.



OAUTH 1.0a for OSCAR Developers
-------------------------------

OAUTH was implemented in order to provide browser based authentication to the OSCAR web services.
This protocol is an industry standard specification used and maintained by established organizations.
Specifically, We use OAUTH integration to authenticate and authorize 3rd party application (client) requests for resources
(data) on behalf of the resource owner (provider).

The oauth 1.0 protocol - http://tools.ietf.org/html/rfc5849

In order to use OAUTH. Protect your web service resource as described in the REST document. The easiest way to use the resource
is to use an OAUTH client library. The basic flow is for the client to attempt to access the resource. If the user has no
valid token and not the appropriate request headers, they are redirected to an OSCAR login page at which point the application's
callback url is called with the correct token and additional information upon successful authentication by the user.

There's a class called RESTClient included in OSCAR to demonstrate more details about the headers, and the flow as it interacts
with the service programatically.

The ServiceRequestToken class represents the request token..which is what you need to get an access token.

The ServiceAccessToken class represents the token which the client can make authorized web service calls with (within the specified scope).

The ServiceClient class represents an entry regarding a 3rd party application which has been configured by the admin, and has the consumer key and secret.

