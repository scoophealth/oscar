package org.oscarehr.util;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.xmlrpc.util.DateTool;

public class VelocityUtils
{
	private static Logger logger=MiscUtils.getLogger();
	
	/**
	 * This is only a general purpose engine, for specialised requirements make your own.
	 */
	public static final VelocityEngine velocityEngine=getInitialisedVelocityEngine();
	
	/**
	 * Static instance so you can add it to a velocityContext.
	 */
	public static final EscapeTool escapeTool=new EscapeTool();
	
	/**
	 * Static instance so you can add it to a velocityContext.
	 */
	public static final NumberTool numberTool=new NumberTool();

	/**
	 * Static instance so you can add it to a velocityContext.
	 */
	public static final DateTool dateTool=new DateTool();
	
	private static VelocityEngine getInitialisedVelocityEngine()
	{
        try
        {
    		VelocityEngine velocityEngine=new VelocityEngine();
	        velocityEngine.setProperty(VelocityEngine.PARSER_POOL_SIZE, 10);
	        velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, Log4JLogChute.class.getName());
	        velocityEngine.init();
	        return(velocityEngine);
        }
        catch (Exception e)
        {
        	logger.error("Error", e);
	        return(null);
        }
	}
	
	/**
	 * This method will return a new instance of VelocityContext
	 * with the static instances of the tools in this class added
	 * under the names like their instance names.
	 */
	public static VelocityContext createVelocityContextWithTools()
	{
		VelocityContext velocityContext=new VelocityContext();
		velocityContext.put("escapeTool", escapeTool);
		velocityContext.put("numberTool", numberTool);
		velocityContext.put("dateTool", dateTool);
		return(velocityContext);
	}
	
	/**
	 * This method will do a velocity merge on the template and the velocity contest
	 * and return the merged result as a String. This is just a convenience method 
	 * for dealing with the output writers. This method will return null if the template is null.
	 */
	public static String velocityEvaluate(VelocityContext velocityContext, String template) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException
	{
		if (template==null) return(null);
		
		StringWriter stringWriter=new StringWriter();
		velocityEngine.evaluate(velocityContext, stringWriter, "template", template);
		return(stringWriter.toString());
	}
}
