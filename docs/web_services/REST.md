REST Web Services
-----------------

OSCAR uses REST based web services to provide platform services to other applications and processes.

The configuration is in spring_ws.xml under the restServices bean. To add new services, add new 
service beans.

The resources can be protected with any or all authentication schemes, whether it's our own
token system, username/password, or OAUTH 1.0a implementation. This is done using the jaxrs:provider
bean definitions.

Services generally return XML streams (Objects to XML from JAXB) or JSON. The extension mappings
have been defined.

Use annotations in your web service implementation class to give details as to the URL, and the return type

@Path("/providerService/")
@Produces("application/xml")
public class ProviderService {
}

You can get an instance of the SecurityContext using

	protected SecurityContext getSecurityContext() {
		Message m = PhaseInterceptorChain.getCurrentMessage();
    	org.apache.cxf.security.SecurityContext sc = m.getContent(org.apache.cxf.security.SecurityContext.class);
    	return sc;
	}

This will give you access to the provider object of the logged in user.


If authenticating through oauth, you can look at the token details using


	protected OAuthContext getOAuthContext() {
		Message m = PhaseInterceptorChain.getCurrentMessage();
		OAuthContext sc = m.getContent(OAuthContext.class);
    	return sc;
	}
	
	
	
Setup simple Transfer objects for return objects. We don't want XML based annotations, or helper methods
within our JPA objects.

