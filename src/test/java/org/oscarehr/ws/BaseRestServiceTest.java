/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.ws.rest.to.model.DemographicExtTo1;
import org.oscarehr.ws.rest.to.model.DemographicMergedTo1;
import org.oscarehr.ws.rest.to.model.DemographicTo1;
import org.oscarehr.ws.rest.to.model.PharmacyInfoTo1;
import org.oscarehr.ws.rest.to.model.ProviderTo1;
import org.oscarehr.ws.rest.to.model.Sex1;

/**
 * Base class for restful tests. While running these tests, it's assumed that a running version of OSCAR is available.
 * Since this falls into the category of functional tests now, all subclasses must explicitly verify if the REST WS
 * tests have been enabled.
 * 
 * This may change in the future as we figure out the best way to test the services, and we authenticate 
 * differently (oauth, tokens, etc)
 * 
 * <p/>
 * 
 * This test expects the following properties provided in oscar.properties file
 * 
 * <pre>
 * test.ws.rest.enabled=true
 * test.ws.rest.login.username=oscardoc
 * test.ws.rest.login.password=mac2002
 * test.ws.rest.login.pin=1117
 * test.ws.rest.login.host=http://localhost:8080/oscar
 * </pre>
 * 
 */
public abstract class BaseRestServiceTest {

	/**
	 * Enables or disables WS test if no "enabling" settings can be found
	 * in the test. This should be set to FALSE everywhere except for the 
	 * local dev environment.
	 */
	private static final String ENABLED_BY_DEFAULT = Boolean.FALSE.toString();
	
	public static final String KEY_ENABLED = "test.ws.rest.enabled";
	public static final String KEY_TYPE = "test.ws.rest.type";
	public static final String KEY_USERNAME = "test.ws.rest.login.username";
	public static final String KEY_PASSWORD = "test.ws.rest.login.password";
	public static final String KEY_PIN = "test.ws.rest.login.pin";
	public static final String KEY_HOST = "test.ws.rest.login.host";
	public static final String KEY_PROPNAME = "test.ws.rest.login.propname";

	public static final Integer RESPONSE_STATUS_FOUND = 302;

	private static final Class<?>[] TRANSFER_CLASSES = 
		{DemographicTo1.class, DemographicMergedTo1.class, DemographicExtTo1.class,
				PharmacyInfoTo1.class, ProviderTo1.class, Sex1.class};

	protected static WebClient client;
	protected static Boolean enabled;
	protected static Cookie session;
	protected static String mediaType = MediaType.APPLICATION_XML;

	private static Logger logger = Logger.getLogger(BaseRestServiceTest.class);

	/**
	 * Initializes base restful settings.
	 * 
	 * <p/>
	 * 
	 * <b>Implementation notes</b>
	 * 
	 * <p/>
	 * 
	 * This method works by carrying out HTTP request to the OSCAR login form. In case
	 * login is successful, authenticated session cookie is provided for the inheriting classes. 
	 * This process can most likely be re-implemented using CXF's HTTP Conduit and AuthSupplier
	 * classes, but this implementation appears to be less intrusive and easier to understand.
	 */
	@BeforeClass
	public static void init() {
		logger.info("Initializing REST settings...");
		Properties props = new Properties();

		InputStream stream = null;
		try {
			stream = BaseRestServiceTest.class.getResourceAsStream("/test.properties");
			props.load(stream);
		} catch (Exception e) {
			logger.info("Test properties are not loaded");
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
					logger.warn("Unable to close input stream");
				}
			}
		}

		enabled = Boolean.parseBoolean(props.getProperty(KEY_ENABLED, ENABLED_BY_DEFAULT));
		if (!enabled) {
			logger.info("Restful tests are disabled.");
			return;
		}
		
		BaseRestServiceTest.mediaType = props.getProperty(KEY_TYPE, MediaType.APPLICATION_XML);

		String host = props.getProperty(KEY_HOST, "http://localhost:8080/oscar/");
		String userName = props.getProperty(KEY_USERNAME, "oscardoc");
		String password = props.getProperty(KEY_PASSWORD, "mac2002");
		String pin = props.getProperty(KEY_PIN, "1117");
		String propName = props.getProperty(KEY_PROPNAME, "oscar_mcmaster");

		logger.info("Authenticating against " + host + " as " + userName);

		client = WebClient.create(host, getProviders(), true);
		client.replacePath("/login.do");

		Form form = new Form();
		form.set("username", userName);
		form.set("password", password);
		form.set("pin", pin);
		form.set("propname", propName);
		// login
		Response response = client.post(form);

		String location = null;
		// no location means that auth failed
		List<Object> locations = response.getMetadata().get("Location");
		if (locations != null && !locations.isEmpty()) {
			for (Object o : locations) {
				location = String.valueOf(o);
				break;
			}
		}

		boolean loginFailed = response.getStatus() != RESPONSE_STATUS_FOUND || location == null || location.trim().isEmpty() || (location != null && location.contains("login=failed"));

		if (loginFailed) {
			logger.info("Unable to login: " + response.getMetadata());
			fail("Unable to login");
		}

		logger.info("Auhtentication successful");

		HttpCookie sessionCookie = null;
		List<Object> cookies = response.getMetadata().get("Set-Cookie");
		if (cookies != null) {
			for (Object o : cookies) {
				for (HttpCookie c : HttpCookie.parse(String.valueOf(o))) {
					sessionCookie = c;
					break;
				}

				if (sessionCookie != null) {
					break;
				}
			}
		}

		if (sessionCookie != null) {
			session = new Cookie(sessionCookie.getName(), sessionCookie.getValue(), sessionCookie.getPath(), sessionCookie.getDomain(), sessionCookie.getVersion());
			client.cookie(session);
		}

		logger.info("Complete initialization successfully");
	}

	/**
	 * Marshalls specified object into XML representation
	 * 
	 * @param o
	 * 		Object to generate string for
	 * @return
	 * 		Returns XML representation of the specified object 
	 */
	protected static String toXmlString(Object o) {
		if (o == null) {
			return "";
		}

		try {
			JAXBContext context = JAXBContext.newInstance(o.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			m.marshal(o, os);
			os.flush();
			return os.toString();
		} catch (Exception e) {
			logger.error("Unable to marshal " + o, e);
		}

		return "";
	}
	
	/*
	 * 	<bean id="jaxb" class="org.apache.cxf.jaxrs.provider.JAXBElementProvider">
	      <property name="singleJaxbContext" value="true"/>
	      <property name="extraClass">
	         <list>
	         	<value>org.oscarehr.ws.rest.to.model.AddressTo</value>
				<value>org.oscarehr.ws.rest.to.model.DemographicExtTo</value>
				<value>org.oscarehr.ws.rest.to.model.DemographicMergedTo</value>
				<value>org.oscarehr.ws.rest.to.model.DemographicTo</value>
				<value>org.oscarehr.ws.rest.to.model.PharmacyInfoTo</value>
				<value>org.oscarehr.ws.rest.to.model.ProviderTo</value>
				<value>org.oscarehr.ws.rest.to.model.Sex</value>
	         </list>
	      </property>
	</bean>
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<?> getProviders() {
		List result = new ArrayList();
		JAXBElementProvider provider = new JAXBElementProvider();
		provider.setSingleJaxbContext(true);
		provider.setExtraClass(TRANSFER_CLASSES);
		result.add(provider);
		return result;
	}

	/**
	 * Creates new authenticated service for the specified service class.
	 * 
	 * @param resourceClass
	 * 		Service class
	 * @return
	 * 		Returns the new service instance
	 */
	public static <T> T getResource(Class<T> resourceClass) {
		if (client == null) {
			fail("Null REST client");
		}

		if (session == null) {
			fail("Not authenticated");
		}

		// go to the root of REST services
		client.replacePath("/ws/rs");
		
		
		// create service
		T service = JAXRSClientFactory.fromClient(client, resourceClass);
		Client newClient = WebClient.client(service);
		// propagate session state
		newClient.cookie(session);

		// make sure we only use XML
		newClient.type(mediaType);
		newClient.accept(mediaType);
		
		return service;
	}

	@AfterClass
	public static void close() {
		if (client == null) {
			logger.info("Client is not initialized, exiting...");
			return;
		}

		client.replacePath("/logout.jsp");
		client.get();

		logger.info("Completed logout.");
	}

	/**
	 * Generates test data for the specified instance. If the specified instance can not be populated
	 * the method fails test.
	 * 
	 * @param t
	 * 		Instance to that should be populated with the test data.
	 * @return
	 * 		Returns the populated instance.
	 */
	protected static <T> T populate(T t) {
		try {
			EntityDataGenerator.generateTestDataForModelClass(t);
		} catch (Exception e) {
			fail();
		}
		
		// rest ID field
		try {
	        Method idSetterMethod = t.getClass().getMethod("setId", new Class[] {Integer.class});
	        idSetterMethod.invoke(t, new Object[] {null});
        } catch (Exception e) {
        	logger.info("Unable to reset id on " + t);
        }
		
		return t;
	}
}
