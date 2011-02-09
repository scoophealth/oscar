package org.oscarehr.util;

/**
 * The purpose of this class is to provide a container for 
 * progress information. The expected usage of this class is
 * that you may instantiate this class, then put it in 
 * the HttpSession, then pass it to a long running task which would
 * then periodically update the contents of these variables.
 * It is then expected that something like another web page
 * may periodically refresh, and the contents of this class
 * maybe used to display the contents of that page. The completed
 * boolean should be set by the running task and the dialog can
 * use it to close the window upon completed.
 * 
 * An example maybe as follows 
 * total="148 files"
 * processed="14 files read"
 * percentComplete=10
 * currentItem="foo.txt"
 */
public class ProgressStatus {
	public String total=null;
	public String processed=null;
	public int percentComplete=0;
	public String currentItem=null;
	public boolean completed=false;
}
