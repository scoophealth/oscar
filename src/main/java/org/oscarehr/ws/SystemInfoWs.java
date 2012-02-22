package org.oscarehr.ws;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

@WebService
@Component
public class SystemInfoWs extends AbstractWs
{
	/**
	 * http://127.0.0.1:8080/myoscar_server/ws/SystemInfoService/helloWorld
	 */
	public String helloWorld()
	{
		return("Hello World! the configuration works! and your client works! :) " + (new java.util.Date()));
	}

	public String isAlive()
	{
		return("alive");
	}
}
