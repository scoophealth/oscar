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


package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public final class DynamicHapiLoaderUtils {

	private static final Logger logger = MiscUtils.getLogger();

	private static URLClassLoader hackedHapiClassLoader;
	private static Class<?> hackedPipedParserClass;
	private static Method hackedParseMethod;
	private static Object hackedPipedParserInstance;
	private static Class<?> hackedMessageClass;
	private static Class<?> hackedSegmentClass;
	private static Method hackedEncodeMethod;
	private static Class<?> hackedTerserClass;
	private static Constructor<?> hackedTerserConstructor;
	private static Class<?> hackedTerserUtilsClass;
	private static Method hackedTerserGetMethod;
	private static Method hackedTerserGetBySegmentMethod;
	private static Method hackedTerserUtilsterser_getFinder_getCurrentGroup_getNamesMethod;
	private static Method hackedTerserUtilsterser_getFinder_getRoot_getAllMethod;

	static{
		try
		{
			initialise();
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
		}
	}
	
	private static void initialise() throws ClassNotFoundException, SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		hackedHapiClassLoader=getHackedUrlClassLoader();

		hackedPipedParserClass=hackedHapiClassLoader.loadClass("ca.uhn.hl7v2.parser.PipeParser");		
		hackedMessageClass=hackedHapiClassLoader.loadClass("ca.uhn.hl7v2.model.Message");
		hackedSegmentClass=hackedHapiClassLoader.loadClass("ca.uhn.hl7v2.model.Segment");
		hackedTerserClass=hackedHapiClassLoader.loadClass("ca.uhn.hl7v2.util.Terser");
		hackedTerserUtilsClass=hackedHapiClassLoader.loadClass("TerserUtils");

		hackedParseMethod=hackedPipedParserClass.getMethod("parse", String.class);
		hackedEncodeMethod=hackedPipedParserClass.getMethod("encode", hackedMessageClass);
		
		hackedPipedParserInstance=hackedPipedParserClass.newInstance();
		setNoValidate(hackedHapiClassLoader,hackedPipedParserClass,hackedPipedParserInstance);
		
		hackedTerserConstructor=hackedTerserClass.getConstructor(hackedMessageClass);
		hackedTerserGetMethod=hackedTerserClass.getMethod("get", String.class);
		hackedTerserGetBySegmentMethod=hackedTerserClass.getMethod("get", hackedSegmentClass, int.class, int.class, int.class, int.class);

		hackedTerserUtilsterser_getFinder_getCurrentGroup_getNamesMethod=hackedTerserUtilsClass.getMethod("terser_getFinder_getCurrentGroup_getNames", hackedTerserClass);
		hackedTerserUtilsterser_getFinder_getRoot_getAllMethod=hackedTerserUtilsClass.getMethod("terser_getFinder_getRoot_getAll", hackedTerserClass, String.class);
	}
	
	private static URLClassLoader getHackedUrlClassLoader()
	{
		// HACKED_HAPI_51_JAR = "/hapi_libs/fork/hapi_51_fork.jar";

		URL[] urls = new URL[2];
		urls[0] = DynamicHapiLoaderUtils.class.getResource("/hapi_libs/fork/hacked_patched_hapi-0.5.1.jar");
		urls[1] = DynamicHapiLoaderUtils.class.getResource("/hapi_libs/fork/commons-logging-1.1.1.jar");
		
		if (logger.isDebugEnabled()) {
			try {
				logger.debug("jar:hacked hapi, size: "+urls[0].openStream().available());
			} catch (IOException e) {
				logger.error("Unexpected Error", e);
			}
		}
		
		URLClassLoader classLoader = new URLClassLoader(urls, null);
		return(classLoader);
	}
	
	
	private static void setNoValidate(URLClassLoader classLoader, Class<?> pipedParserClass, Object pipedParserInstance) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException
	{
		Class<?> noValidateClass=classLoader.loadClass("ca.uhn.hl7v2.validation.impl.NoValidation");
		Object noValidateInstance=noValidateClass.newInstance();
		
		Class<?> validationContextClass=classLoader.loadClass("ca.uhn.hl7v2.validation.ValidationContext");
		
		Method noValidateMethod=pipedParserClass.getMethod("setValidationContext", validationContextClass);
		noValidateMethod.invoke(pipedParserInstance, new Object[]{noValidateInstance});
	}
	
	public static Object parseMdsMsg(String hl7Text) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Object result=hackedParseMethod.invoke(hackedPipedParserInstance, hl7Text);
		return(result);
	}

	
	public static String encodeMdsMsg(Object mdsMessage) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		String result=(String) hackedEncodeMethod.invoke(hackedPipedParserInstance, mdsMessage);
		return(result);
	}
	
	public static Object getMdsTerser(Object mdsMsg) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Object terser=hackedTerserConstructor.newInstance(mdsMsg);
		return(terser);
	}
	
	public static String terserGet(Object terser, String s) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		String result=(String) hackedTerserGetMethod.invoke(terser, s);
		return(result);
	}

	public static String terserGet(Object terser, Object segment, int a, int b, int c, int d) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		String result=(String) hackedTerserGetBySegmentMethod.invoke(terser, segment, a, b, c, d);
		return(result);
	}

	public static String[] terser_getFinder_getCurrentGroup_getNames(Object terser) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		String[] result=(String[]) hackedTerserUtilsterser_getFinder_getCurrentGroup_getNamesMethod.invoke(null, terser);
		return(result);
	}

	public static Object[] terser_getFinder_getRoot_getAll(Object terser, String segment) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Object[] result=(Object[]) hackedTerserUtilsterser_getFinder_getRoot_getAllMethod.invoke(null, terser, segment);
		return(result);		
	}
	
	public static void main(String... argv) throws Exception {
		String hl7msg = FileUtils.readFileToString(new File("/data/git/oscar_utils/tmp/51_hl7body.txt")).trim();

		Object msg = parseMdsMsg(hl7msg);
		String result=encodeMdsMsg(msg);
		
		logger.error("--------------------");
		logger.error(result.replace("\r", "\n"));

		Object terser=getMdsTerser(msg);
		logger.error("--------------------");		
	
	}
}
