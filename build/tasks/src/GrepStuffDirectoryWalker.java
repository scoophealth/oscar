import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Task;

public class GrepStuffDirectoryWalker extends DirectoryWalker {

	private String rootPath;
	private Task logger;
	
	/** key=String/violation value=String/relativePath */
	private MultiValueMapSerialisable violations=new MultiValueMapSerialisable();
	
	public GrepStuffDirectoryWalker(Task logger)
	{
		this.logger=logger;
	}
	
	
	@SuppressWarnings("unchecked")
    @Override
    protected void handleFile(File file, int depth, Collection results) throws IOException
	{
		String fullPath=file.getCanonicalPath();
		
		if (isIgnoredFile(fullPath)) return;
		
		String relativePath=fullPath.substring(rootPath.length());
		checkViolations(file, relativePath);
	}
	
	private void checkViolations(File file, String relativePath) throws IOException {
		String fileContents=FileUtils.readFileToString(file);
		
		if (relativePath.endsWith(".hbm.xml")) violations.put(".hbm.xml", relativePath);
		if (relativePath.endsWith(".class")) violations.put(".class", relativePath);
		
		checkContains(relativePath, fileContents, "System.err");
		checkContains(relativePath, fileContents, "System.out");	    
		checkContains(relativePath, fileContents, "printStackTrace");	    
		checkContains(relativePath, fileContents, "ASCII");	    
		checkContains(relativePath, fileContents, "DBHandler.");	    
		checkContains(relativePath, fileContents, "getConnection(");	    
		checkContains(relativePath, fileContents, "OscarSuperManager");	
		checkContains(relativePath, fileContents, "org.apache.commons.logging.Log");
    }


	private void checkContains(String relativePath, String fileContents, String s) {
	    if (fileContents.contains(s))
	    {
	    	violations.put(s, relativePath);
	    }
    }


	private boolean isIgnoredFile(String fullPath) {
		if (fullPath.endsWith("/CVS/Repository")) return(true);
		if (fullPath.endsWith("/CVS/Root")) return(true);
		if (fullPath.endsWith("/CVS/Entries")) return(true);
		
		if (fullPath.endsWith(".jpg")) return(true);
		if (fullPath.endsWith(".png")) return(true);
		if (fullPath.endsWith(".gif")) return(true);
		if (fullPath.endsWith(".pdf")) return(true);
		if (fullPath.endsWith(".swf")) return(true);
		if (fullPath.endsWith(".jar")) return(true);

		return(false);
	}


	public void walk(String srcDir) throws IOException
	{
		File srcDirFile=new File(srcDir);
		rootPath=srcDirFile.getCanonicalPath();
		
		logger.log("canonical srcDir="+rootPath);
		
		super.walk(srcDirFile, null);
	}
	
	public MultiValueMapSerialisable getVoliations()
	{
		return(violations);
	}
}
