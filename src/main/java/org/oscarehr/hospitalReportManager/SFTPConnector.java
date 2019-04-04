/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.common.dao.HrmLogDao;
import org.oscarehr.common.dao.HrmLogEntryDao;
import org.oscarehr.common.dao.SecObjPrivilegeDao;
import org.oscarehr.common.model.HrmLog;
import org.oscarehr.common.model.HrmLogEntry;
import org.oscarehr.common.model.OscarMsgType;
import org.oscarehr.common.model.SecObjPrivilege;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import oscar.OscarProperties;
import oscar.oscarMessenger.data.MsgProviderData;

/**
 * SFTP Connector to interact with servers and return the server's reply/file data.
 */
public class SFTPConnector {

	private static org.apache.log4j.Logger logger=MiscUtils.getLogger();
	
	private JSch jsch;
	private ChannelSftp cmd;
	private Session sess;
	private Logger fLogger; //file logger

	private static final String TEST_DIRECTORY = "Test";

	private static final String OMD_HRM_USER = OscarProperties.getInstance().getProperty("OMD_HRM_USER");
	private static final String OMD_HRM_IP = OscarProperties.getInstance().getProperty("OMD_HRM_IP");
	private static final int OMD_HRM_PORT = Integer.parseInt(OscarProperties.getInstance().getProperty("OMD_HRM_PORT"));

	//this file needs chmod 444 permissions for the connection to go through
	public static  String OMD_directory = OscarProperties.getInstance().getProperty("OMD_directory");
	private static  String OMD_keyLocation = OMD_directory + OscarProperties.getInstance().getProperty("OMD_HRM_AUTH_KEY_FILENAME");
	public static final String XSD_ontariomd = OMD_directory + "ontariomd_cds_dt.xsd";
	public static final String XSD_reportmanager = OMD_directory + "report_manager_cds.xsd";

	//where all the daily logs will be saved
	public final String logDirectory = OscarProperties.getInstance().getProperty("OMD_log_directory");

	//root folder for daily downloads
	public static String downloadsDirectory= OscarProperties.getInstance().getProperty("OMD_downloads");

	public final String fileDirectory = OscarProperties.getInstance().getProperty("OMD_stored");

	//set when initialized, to change keys, manually do it in the main constructor
	public static String decryptionKey=null;
	
	private HrmLogDao hrmLogDao = SpringUtils.getBean(HrmLogDao.class);
	private HrmLogEntryDao hrmLogEntryDao = SpringUtils.getBean(HrmLogEntryDao.class);
	
	/**
	 * String is the providerNo of people who don't want to see anymore messages.
	 * This is cleared each time the run succeeds as it would be a new outtage after one success. 
	 */
	private static HashSet<String> doNotSentMsgForOuttage=new HashSet<String>();

	private HrmLog hrmLog;
	
	/**
	 * This is called for MANUAL download
	 * 
	 * @throws Exception
	 */
	public SFTPConnector(LoggedInInfo loggedInInfo) throws Exception {
		this(loggedInInfo, OMD_HRM_IP, OMD_HRM_PORT, OMD_HRM_USER, getOMD_keyLocation(),"Manual");
	}

	public SFTPConnector(LoggedInInfo loggedInInfo, String triggerType) throws Exception {
		this(loggedInInfo,OMD_HRM_IP, OMD_HRM_PORT, OMD_HRM_USER, getOMD_keyLocation(),triggerType);
	}

	/**
	 * Main constructor. To change keys, manually set the references below.
	 */
	public SFTPConnector(LoggedInInfo loggedInInfo, String host, int port, String user, String keyLocation, String triggerType) throws Exception {

		logger.debug("Host "+host+" port "+port+" user "+user+" keyLocation "+keyLocation);	

		hrmLog = new HrmLog();
		hrmLog.setTransactionType(triggerType);
		hrmLog.setStarted(new Date());
		hrmLog.setInitiatingProviderNo(loggedInInfo.getLoggedInProviderNo());
		hrmLogDao.persist(hrmLog);
		
		String logName = SFTPConnector.getDayMonthYearTimestamp() + ".log";
		String fullLogPath = this.logDirectory + logName;
		FileHandler handler = new FileHandler(fullLogPath, true); 
		fLogger = Logger.getLogger("SFTPConnector");
		fLogger.addHandler(handler);

		try {
			jsch = new JSch();
			jsch.addIdentity(keyLocation);
			sess = jsch.getSession(user, host, port);
		} catch(JSchException je) {
			hrmLog.setError(je.getMessage());
			hrmLogDao.merge(hrmLog);
			throw je;
		}

		java.util.Properties confProp = new java.util.Properties();
		confProp.put("StrictHostKeyChecking", "no");
		sess.setConfig(confProp);

		try {
			sess.connect();
			Channel channel = sess.openChannel("sftp");
			channel.connect();
			cmd = (ChannelSftp) channel;
			fLogger.info("SFTP connection established with " + host + ":" + port + ". Current path on server is: " + cmd.pwd());
		}catch(JSchException je) {
			hrmLog.setError(je.getMessage());
			hrmLogDao.merge(hrmLog);
			throw je;
		}
		
		hrmLog.setConnected(true);
		hrmLogDao.merge(hrmLog);
	}

	public static String getOMD_keyLocation() {
		return OMD_keyLocation;

	}

	/**
	 * Ensure the specified folder exists within the SFTP download folder. If folder is null, then ensure that the
	 * download folder exists.
	 * 
	 * @throws Exception
	 */
	private static String prepareForDownload(String folder) throws Exception {

		//ensure the downloads directory exists
		String path = checkFolder(downloadsDirectory);

		//if it's a simple "do i have my downloads folder" check, then we're done!
		//no other folder is specified
		if (folder == null)
			return path;

		//if code gets to here then we're ensuring that specified folder exists within SFTP download folder.
		//-also fixes the beginning if the specified folder already begins with a '/' slash it ignores the slash
		String dir = downloadsDirectory
		+ (folder == null ? "" : (folder.charAt(0) == '/' ? folder.substring(1, folder.length() - 1) : folder));

		//return the full path of the existing folder
		return checkFolder(dir);
	}

	/**
	 * ls print - issue an 'ls' command and simply print the results to System out (rather than returning a String array
	 * of elements listed from command)
	 * 
	 * @param folder
	 * @throws SftpException
	 */
	public void lsP(String folder) throws SftpException {
		ls(folder, true);
	}

	/**
	 * Issue an 'ls' command and return the objects in an array
	 * 
	 * @param folder
	 * @return
	 * @throws SftpException
	 */
	public String[] ls(String folder) throws SftpException {
		return ls(folder, false);
	}

	/**
	 * Issue an 'ls' command on remote server and exclussively print the values or return them in a String array.
	 * 
	 * @param folder
	 *            to issue the 'ls' command on
	 * @param printInfo
	 * @return
	 * @throws SftpException
	 */
	public String[] ls(String folder, boolean printInfo) throws SftpException {
		List fileList = cmd.ls(folder);
		String[] filenames = null;
		List<String> files = new ArrayList<String>();

		if (fileList != null) {

			//only instantiate array to hold ls results if user is not printing info
			if (!printInfo){
				filenames = new String[fileList.size()];
			}

			logger.debug("ls " + folder);
			int i = 0;
			for (Object obj : fileList) {

				if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
					LsEntry lsEntry = (LsEntry) obj;

					//either print or store each element
					if (printInfo) {
						logger.debug(lsEntry.getFilename());
					} else {
						String fn = lsEntry.getFilename(); //filename
						if (fn != null && !fn.equals(".") && !fn.equals("..")) {
							filenames[i++] = fn;
							files.add(fn);
						}
					}
				}
			}
		}

		return files.toArray(new String[files.size()]);
	}

	/**
	 * Download the contents of the specified directory on the server side. The presumption is made for OMD where the
	 * user has to fetch all contents from the user's home directory. Thus a "./" is prepended to each folder requested
	 * on the server. If you don't prepend the "./" before the folder directory, JSch will use the "/" directory (root
	 * dir).
	 * 
	 * @param serverDirectory
	 *            directory on server side to fetch contents
	 * @param localDownloadFolder
	 *            name of folder to place downloaded files. This folder is placed under the tmp folder specified at
	 * @throws Exception
	 *             custom error messages if Java is unable to create a folder in /tmp/oscar-sftp and parent dirs
	 * @return array of full path filenames
	 */
	public String[] downloadDirectoryContents(String serverDirectory) throws Exception {
		//get the filenames of all files in the directory
		String[] files = ls(serverDirectory);
		String[] fullPathFilenames = new String[files.length];

		//go into the server directory
		cmd.cd("./" + serverDirectory);
		//and fetch each file into the source folder

		String todaysFolderName = SFTPConnector.getDayMonthYearTimestamp();

		//ensure today's folder exists
		String fullPath = prepareForDownload(todaysFolderName);

		fLogger.info("About to download all contents of directory: " + serverDirectory);
		if (files.length == 0) {
			fLogger.info("No files to download from server folder: " + serverDirectory);
			return null;
		}

		int i = 0;
		//not too sure whether multiple connections are handled by the JSch library
		//such that multiple calls to "get" has a sync limit until one or more other
		//files have finished downloading.
		for (String file : files) {
			if (file != null) {
				String fullFilePath = fullPath + file;
				cmd.get(file, fullFilePath);
				fullPathFilenames[i++] = fullFilePath;
				fLogger.info("Downloaded File: " + fullFilePath);
				logger.debug("SFTP::Downloaded file: " + fullFilePath);
			}
		}

		return fullPathFilenames;
	}

	/**
	 * Given a server-side directory, go in and delete all files
	 * 
	 * @param serverDirectory
	 * @throws SftpException
	 */
	public void deleteDirectory(String serverDirectory) throws SftpException {
		String[] files = ls(serverDirectory);
		deleteDirectoryContents(serverDirectory, files);
	}

	/**
	 * Given a directory and the filenames of the directories (already pre-determined) go in the directory and delete
	 * each file.
	 * 
	 * @param serverDirectory
	 *            the directory onto which to remove contents
	 * @param filenames
	 *            a String array of filenames of the directory, pre-fetched specifically for the directory.
	 * @throws SftpException
	 */
	public void deleteDirectoryContents(String serverDirectory, String[] filenames) throws SftpException {
		cmd.cd("/");

		fLogger.info("About to delete all contents from server directory: " + serverDirectory);
		logger.debug("Deleting contents from directory: " + serverDirectory);
		cmd.cd(serverDirectory);
		for (String file : filenames) {
			if (file != null) {
				cmd.rm(file);

				fLogger.info("Deleted file " + file + " from server");
				logger.debug("Deleted server file " + file);
			}
		}

	}

	public String[] decryptFiles(String[] fullPaths) throws Exception {
		return decryptFiles(fullPaths,getDecryptionKey());
	}
	/**
	 * Given a String array of absolute filenames of encrypted files, proceed to decrypt them in today's folder under
	 * the specified folder below.
	 * 
	 * @param fullPaths
	 * @throws Exception
	 */
	public String[] decryptFiles(String[] fullPaths, String decryptionKey) throws Exception {
		if (fullPaths.length == 0)
			return null;
					
		String[] decryptedFilePaths = new String[fullPaths.length];
		

		//placed under each daily's folder for all files needing decryption to store the decrypted version
		String decryptedFolderName = "decrypted";
		//ensure that the given folder exists (by trying to create it if it dne)
		//return the full path with last slash always there
		String saveToDirectoryFullPath = prepareForDownload(getDayMonthYearTimestamp() + "/" + decryptedFolderName);

		//we'll get each file's contents in a string then dump that onto a file
		String decryptedContent = null;
		String filename = "";

		FileWriter handler = null;
		BufferedWriter out = null;
		for (int x=0;x<fullPaths.length;x++) {
			String sfile = fullPaths[x];
			if(sfile == null)
				continue;
			
			try {
	            decryptedContent = decryptFile(sfile,decryptionKey);
	            filename = sfile.substring(sfile.lastIndexOf("/"));
	            String newFullPath = saveToDirectoryFullPath + filename;
	            handler = new FileWriter(newFullPath);
	            out = new BufferedWriter(handler);
	            out.write(decryptedContent);
	            decryptedFilePaths[x] = newFullPath;
			} catch(Exception e) {
				//Don't want this to fail on all other files in the directory just because one doesn't decrypt;
				logger.error("Error decrypting file - " + sfile);
				decryptedFilePaths[x] = null;
            } finally {
	            if (out!=null) out.close();
	            if (handler!=null) handler.close();
            }
		}

		return decryptedFilePaths;
	}

	public String decryptFile(String fullPath) throws Exception {
		return decryptFile(fullPath,null);
	}
	/**
	 * Given the absolute path of an encrypted file, decrypt the file using the specified AES key at the top. Return the
	 * string value of the decrypted file.
	 * 
	 * @param fullPath
	 * @return
	 * @throws Exception
	 */
	public String decryptFile(String fullPath, String decryptionKey) throws Exception {
		
		if(decryptionKey == null) {
			decryptionKey = OscarProperties.getInstance().getProperty("OMD_HRM_DECRYPTION_KEY");
		}
		logger.info("About to decrypt: " + fullPath);
		File encryptedFile = new File(fullPath);
		if (!encryptedFile.exists()) {
			throw new Exception("Could not find file '" + fullPath + "' to decrypt.");
		}

		int fileLength = (int) encryptedFile.length();
		byte[] fileInBytes = new byte[fileLength];
		FileInputStream fin = new FileInputStream(encryptedFile);
		try {
	        fin.read(fileInBytes);
        } finally {
    		fin.close();
        }

		byte keyBytes[] = toHex( decryptionKey);
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decode = cipher.doFinal(fileInBytes);

		return new String(decode);
	}

	/**
	 * Close channels, disconnect sessions, release/close file handlers.
	 */
	public void close() {
		cmd.exit();
		sess.disconnect();
		fLogger.getHandlers()[0].close();
	}

	/********************************************************/
	/////////////////// HELPERS / STATIC /////////////////////
	/********************************************************/

	public static String getDayMonthYearTimestamp() {
		Calendar cal = Calendar.getInstance();

		String day = cal.get(Calendar.DAY_OF_MONTH) + "";
		if (day.length() == 1)
			day = "0" + day;

		String month = (cal.get(Calendar.MONTH) + 1) + "";
		if (month.length() == 1)
			month = "0" + month;

		String year = cal.get(Calendar.YEAR) + "";

		return day + month + year;
	}

	/**
	 * Check that the given folder exists, if it doesn't exist, create it. Object method for convenience.
	 * 
	 * @param fullPath
	 * @throws Exception
	 */
	private static String checkFolder(String fullPath) throws Exception {
		return SFTPConnector.ensureFolderExists(fullPath);
	}

	/**
	 * Ensure that the given folder exists by creating it if it isn't present.
	 * 
	 * Static method so other external Classes may use this feature.
	 * 
	 * @param fullPath
	 * @throws Exception
	 */
	public static String ensureFolderExists(String fullPath) throws Exception {
		File tmpFolder = new File(fullPath);
		if (!tmpFolder.exists()) {
			boolean res = tmpFolder.mkdir();
			if (!res)
				throw new Exception("Unable to create folder " + tmpFolder.getAbsolutePath()
						+ " required for SFTP operations. Please check permissions.");
		}

		return tmpFolder.getAbsolutePath() + "/";
	}

	public static byte[] toHex(String encoded) {
		if ((encoded.length() % 2) != 0)
			throw new IllegalArgumentException("Input string must contain an even number of characters");

		final byte result[] = new byte[encoded.length() / 2];
		final char enc[] = encoded.toCharArray();
		for (int i = 0; i < enc.length; i += 2) {
			StringBuilder curr = new StringBuilder(2);
			curr.append(enc[i]).append(enc[i + 1]);
			result[i / 2] = (byte) Integer.parseInt(curr.toString(), 16);
		}
		return result;

	}

	/********************************************************/
	//////////////////Auto Fetcher////////////////////////
	/**
	 * @throws Exception
	 ******************************************************/

	private static boolean isAutoFetchRunning = false;

	public static boolean isFetchRunning() {
		return SFTPConnector.isAutoFetchRunning;
	}
	

	public synchronized void startAutoFetch(LoggedInInfo loggedInInfo) {
		startAutoFetch(loggedInInfo,OscarProperties.getInstance().getProperty("OMD_HRM_REMOTE_DIR"));
	}
	/*
	 * This gets called by "SchedulerJob"
	 */
	public synchronized boolean startAutoFetch(LoggedInInfo loggedInInfo, String remoteDir) {
		String[] localFilePaths =null;
		String[] paths = null;
		
		boolean seenError = false;
		
		if (!isAutoFetchRunning) {
			SFTPConnector.isAutoFetchRunning = true;
			logger.info("HRM: starting auto fetch");
			
			if (remoteDir == null || remoteDir.isEmpty()) {
				remoteDir = TEST_DIRECTORY;
			} 
			
			logger.info("HRM: remoteDir:"+remoteDir);
			
			try {

				String[] files = ls(remoteDir);		
				localFilePaths=downloadDirectoryContents(remoteDir);		
				
				hrmLog.setDownloadedFiles(true);
				hrmLog.setNumFilesDownloaded(files.length);
				hrmLogDao.merge(hrmLog);

				if(files == null || files.length==0) {
					return true;
				}
				deleteDirectoryContents(remoteDir, files);

				hrmLog.setDeleted(true);
				hrmLogDao.merge(hrmLog);
				
				if(localFilePaths == null || localFilePaths.length == 0) {
					return true;
				}
				
				for(String encryptedFile : localFilePaths) {
					
					HrmLogEntry hrmLogEntry = new HrmLogEntry();
					hrmLogEntry.setHrmLogId(hrmLog.getId());
					hrmLogEntry.setEncryptedFileName(encryptedFile);
					hrmLogEntryDao.persist(hrmLogEntry);
					
					String decryptedContent = null;
					try {
						decryptedContent = decryptFile(encryptedFile,decryptionKey);
						hrmLogEntry.setDecrypted(true);
						hrmLogEntryDao.merge(hrmLogEntry);
						
						String decryptedFileName = saveDecryptedData(encryptedFile, decryptedContent);
						hrmLogEntry.setDecryptedFileName(decryptedFileName);
						hrmLogEntryDao.merge(hrmLogEntry);
						
						String filename = copyFileToDocumentDir(loggedInInfo,decryptedFileName);
						hrmLogEntry.setFilename(filename);
						hrmLogEntryDao.merge(hrmLogEntry);
						
						if(filename != null) {
						
							List<Throwable> errors = new ArrayList<Throwable>();
							HRMReport report = HRMReportParser.parseReport(loggedInInfo, filename,errors);
							if (report != null) {
								hrmLogEntry.setParsed(true);
								hrmLogEntry.setRecipientId(report.getDeliverToUserId());
								hrmLogEntry.setRecipientName(report.getDeliveryToUserIdFormattedName());
								hrmLogEntryDao.merge(hrmLogEntry);
								
								HRMReportParser.addReportToInbox(loggedInInfo, report);
								
								hrmLogEntry.setDistributed(true);
								hrmLogEntryDao.merge(hrmLogEntry);
								
							} else {
								for(Throwable e:errors) {
									hrmLogEntry.setError(e.getMessage());
									seenError=true;
								}
								hrmLogEntryDao.merge(hrmLogEntry);
								
							}
							
						}
						
					}catch(Exception e) {
						hrmLogEntry.setError(e.getMessage());
						hrmLogEntryDao.merge(hrmLogEntry);
						notifyHrmError(loggedInInfo, encryptedFile.substring(encryptedFile.lastIndexOf("/")+1) + ": " + e.getMessage());
					//	throw e;
					}
					
					
				}

				logger.debug("Closed SFTP connection");
				logger.debug("Clearing doNotSend list");
				doNotSentMsgForOuttage.clear();
			} catch (Exception e) {
				logger.error("Couldn't perform SFTP fetch for HRM - notifying user of failure", e);
				notifyHrmError(loggedInInfo, e.getMessage());
			} finally {
				close();
				SFTPConnector.isAutoFetchRunning = false;
			}

		} else {
			logger.warn("There is currently an HRM fetch running -- will not run another until it has completed or timed out.");
		}
		return !seenError;
	}
	
	protected String saveDecryptedData(String encryptedFilePath, String decryptedFileContents) throws IOException {
		String decryptedFolderName = "decrypted";
		String path = encryptedFilePath.substring(0, encryptedFilePath.lastIndexOf("/") );
		String filename = encryptedFilePath.substring(encryptedFilePath.lastIndexOf("/")+1);
        
		String saveToDirectoryFullPath = path + File.separator +  decryptedFolderName + File.separator + filename.replaceAll("_encrypted", "");
		
		FileUtils.write(new File(saveToDirectoryFullPath), decryptedFileContents);
		
		
		return saveToDirectoryFullPath;
	}
	
	protected String copyFileToDocumentDir(LoggedInInfo loggedInInfo, String f) throws IOException {
		String destDir = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		String result = null;
		
			if(f != null) {
				FileUtils.copyFileToDirectory(new File(f), new File(destDir));
				result = new File(destDir,new File(f).getName()).getAbsolutePath();
			}
		
		return result;
	}
	
	protected static String[] copyFilesToDocumentDir(LoggedInInfo loggedInInfo, String[] paths) {
		String destDir = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		List<String> results = new ArrayList<String>();
		
		for(int x=0;x<paths.length;x++) {
			String f = paths[x];
			if(f != null) {
				try {
					FileUtils.copyFileToDirectory(new File(f), new File(destDir));
					results.add(new File(destDir,new File(f).getName()).getAbsolutePath());
				}catch(IOException e) {
					logger.error("Error copying HRM file. Will not be viewable from Inbox!",e);
					notifyHrmError(loggedInInfo, "Failed to copy HRM file to DOCUMENT_DIR. Please contact admin ("+ f +")");
				}
			}
		}
		
		return results.toArray(new String[results.size()]);
	}

	protected static void notifyHrmError(LoggedInInfo loggedInInfo, String errorMsg) {
	    HashSet<String> sendToProviderList = new HashSet<String>();
    	
	    if (loggedInInfo != null && loggedInInfo.getLoggedInProvider() != null)
	    {
	    	String providerNoTemp=loggedInInfo.getLoggedInProviderNo();
		    if (!doNotSentMsgForOuttage.contains(providerNoTemp)) sendToProviderList.add(providerNoTemp);
	    }
	    
	    //load all _hrm.administrators
	    SecObjPrivilegeDao secObjPrivilegeDao = SpringUtils.getBean(SecObjPrivilegeDao.class);
	    SecUserRoleDao secUserRoleDao = SpringUtils.getBean(SecUserRoleDao.class);
	    
	    for(SecObjPrivilege sop : secObjPrivilegeDao.findByObjectName("_hrm.administrator")) {
	    	if("x".equals(sop.getPrivilege()) || "w".equals(sop.getPrivilege()) || "r".equals(sop.getPrivilege())) {
	    		for(SecUserRole sur : secUserRoleDao.getSecUserRolesByRoleName(sop.getId().getRoleUserGroup())) {
	    			if(sur.getActive()) {
	    				
	    				if (!doNotSentMsgForOuttage.contains(sur.getProviderNo())) {
	    					sendToProviderList.add(sur.getProviderNo());
	    				}
	    			}
	    		}
	    		
	    	}
	    }
	    
	    if (sendToProviderList.size()==0) {
	    	String providerNoTemp="999998";
		    if (!doNotSentMsgForOuttage.contains(providerNoTemp)) {
		    	sendToProviderList.add(providerNoTemp);
		    }
	    }

	    // no one wants to hear about the problem
	    if (sendToProviderList.size()==0) {
	    	return;
	    }
	    
	    
	    String message = "OSCAR attempted to perform a fetch of HRM data at " + new Date() + " but there was an error during the task.\n\nSee below and HRM log for further details:\n" + errorMsg;

	    oscar.oscarMessenger.data.MsgMessageData messageData = new oscar.oscarMessenger.data.MsgMessageData();

	    ArrayList<MsgProviderData> sendToProviderListData = new ArrayList<MsgProviderData>();
	    for (String providerNo : sendToProviderList) {
	    	MsgProviderData mpd = new MsgProviderData();
	    	mpd.providerNo = providerNo;
	    	mpd.locationId = "145";
	    	sendToProviderListData.add(mpd);
	    	logger.info("HRM retrieval error: notifying "  + providerNo);
	    }

    	String sentToString = messageData.createSentToString(sendToProviderListData);
    	messageData.sendMessage2(message, "HRM Retrieval Error", "System", sentToString, "-1", sendToProviderListData, null, null, OscarMsgType.GENERAL_TYPE);
    }

	/**
	 * adds the currently logged in user to the do not send anymore messages for this outtage list 
	 */
	public static void addMeToDoNotSendList(LoggedInInfo loggedInInfo)
	{
	    if (loggedInInfo != null && loggedInInfo.getLoggedInProvider() != null)
	    {
	    	doNotSentMsgForOuttage.add(loggedInInfo.getLoggedInProviderNo());
	    }
	}
	
	public static void setOMD_keyLocation(String oMD_keyLocation) {
		OMD_keyLocation = oMD_keyLocation;
	}

	public static String getDownloadsDirectory() {
		String dd = downloadsDirectory;
		if (dd == null || dd.equals("")){
			dd = "webapps/OscarDocument/hrm/sftp_downloads/";
			return dd;

		} else {
			return downloadsDirectory;
		}

	}

	public static void setDownloadsDirectory(String downloadsDir) {
		downloadsDirectory = downloadsDir;
	}

	public static String getDecryptionKey() {
		return decryptionKey;
	}

	public static  void setDecryptionKey(String decryptKey) {
		decryptionKey = decryptKey;
	}
	
}
/*
class MyUserInfo implements UserInfo {

	@Override
	public String getPassphrase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return "password";
	}

	@Override
	public boolean promptPassword(String message) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean promptPassphrase(String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean promptYesNo(String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		
	}
	
}
*/
