import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Task;

public class GrepStuffDirectoryWalker extends DirectoryWalker {

	private String rootPath;
	private Task logger;
	private HashSet<String> extensions = new HashSet<String>();

	/** key=String/violation value=String/relativePath */
	private MultiValueMapSerialisable violations = new MultiValueMapSerialisable();

	public GrepStuffDirectoryWalker(Task logger) {
		this.logger = logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		String fullPath = file.getCanonicalPath();

		if (isIgnoredFile(fullPath)) return;

		int indexOfLastDot = fullPath.lastIndexOf('.');
		if (indexOfLastDot > 0) {
			String ext = fullPath.substring(indexOfLastDot);
			extensions.add(ext);
		}

		String relativePath = fullPath.substring(rootPath.length());
		checkViolations(file, relativePath);
	}

	private void checkViolations(File file, String relativePath) throws IOException {
		String fileContents = FileUtils.readFileToString(file);
		String fileContentsLowered = fileContents.toLowerCase();

		// stuff to check if it's the oscar code base
		if (file.getCanonicalPath().contains("/web/")) {
			// --- filename checking ---
			if (relativePath.endsWith(".hbm.xml")) violations.put(".hbm.xml, stop using hibernate, use jpa & annotations instead", relativePath);
			if (relativePath.endsWith(".class")) violations.put(".class, don't commit compiled class files", relativePath);
			if (relativePath.endsWith(".PNG")) violations.put(".PNG, png should be lower case", relativePath);
			if (relativePath.endsWith(".JPG")) violations.put(".JPG, jpg should be lower case", relativePath);
			if (relativePath.endsWith(".GIF")) violations.put(".GIF, gif should be lower case", relativePath);

			// --- case sensitive comparison ---
			
			checkContains(relativePath, fileContents, "System.err", "use log4j logger");
			checkContains(relativePath, fileContents, "System.out", "use log4j logger");
			checkContains(relativePath, fileContents, "getConnection(", "use jpa");
			
			if (fileContents.contains("printStackTrace") && !relativePath.endsWith(".js")) {
				violations.put("printStackTrace" + ", " + "use log4j logger", relativePath);
			}
			
			checkContains(relativePath, fileContents, "DBHandler.", "use jpa");
			checkContains(relativePath, fileContents, "OscarSuperManager", "use jpa");
			checkContains(relativePath, fileContents, "org.apache.commons.logging.Log", "use log4j logger");
			checkContains(relativePath, fileContents, "com.sun.", "classes are not always available, usually org.apache will have standard libraries to replace these");
			checkContains(relativePath, fileContents, "LogManager.getLogger(", "use MiscUtils.getLogger() instead");
			checkContains(relativePath, fileContents, "HibernateDaoSupport", "use jpa");
			checkContains(relativePath, fileContents, "ArrayList()", "use generics");
			checkContains(relativePath, fileContents, "HashSet()", "use generics");
			checkContains(relativePath, fileContents, "HashMap()", "use generics");
			checkContains(relativePath, fileContents, "TreeSet()", "use generics");
			checkContains(relativePath, fileContents, "TreeMap()", "use generics");
			checkContains(relativePath, fileContents, "WeakHashMap()", "use generics");
			checkContains(relativePath, fileContents, "StringBuffer", "very very rare that this is required, usually StringBuilder is the better choice");
			checkContains(relativePath, fileContents, "com.Ostermiller", "use org.apache libraries instead, duplicated libs and apache is... more standard (if that's a word)");

			checkContains(relativePath, fileContents, "Vector", "excessively rare that this is required, usually ArrayList is the better choice");

			if (fileContents.contains("Hashtable") && !fileContents.contains("initHashtableWithList")) {
				violations.put("Hashtable" + ", " + "excessively rare that this is required, usually HashMap is the better choice", relativePath);
			}

			// --- hack for ignore case comparison ---
			if (fileContentsLowered.contains("latin-1") && !relativePath.endsWith("calendar-sv.js")) {
				violations.put("latin-1" + ", " + "use utf-8", relativePath);
			}

			checkContains(relativePath, fileContentsLowered, "ascii", "use utf-8");
			checkContains(relativePath, fileContentsLowered, "8859-1", "use utf-8");
			checkContains(relativePath, fileContentsLowered, "http-equiv=\"content-type\"", "http headers instead");
			checkContains(relativePath, fileContentsLowered, "http-equiv=\"Cache-Control\"", "http headers instead");
		}

		// --- check for database sql problems ---
		if (file.getCanonicalPath().contains("/database/")) {
			if (relativePath.endsWith(".sql")) {
				if (fileContentsLowered.contains(" default ")) {
					violations.put("database 'default' should not be used, set the default in java in the jpa objects", relativePath);
				}
			}
		}

	}

	private void checkContains(String relativePath, String fileContents, String searchString, String violationReason) {
		if (fileContents.contains(searchString)) {
			violations.put(searchString + ", " + violationReason, relativePath);
		}
	}

	private boolean isIgnoredFile(String fullPath) {
		if (fullPath.endsWith("/CVS/Repository")) return (true);
		if (fullPath.endsWith("/CVS/Root")) return (true);
		if (fullPath.endsWith("/CVS/Entries")) return (true);

		if (fullPath.endsWith(".jpg")) return (true);
		if (fullPath.endsWith(".png")) return (true);
		if (fullPath.endsWith(".gif")) return (true);
		if (fullPath.endsWith(".pdf")) return (true);
		if (fullPath.endsWith(".swf")) return (true);
		if (fullPath.endsWith(".jar")) return (true);

		return (false);
	}

	public void walk(String srcDir) throws IOException {
		File srcDirFile = new File(srcDir);
		rootPath = srcDirFile.getCanonicalPath();

		logger.log("canonical srcDir=" + rootPath);

		super.walk(srcDirFile, null);
	}

	public MultiValueMapSerialisable getVoliations() {
		return (violations);
	}

	public HashSet<String> getExtensions() {
		return extensions;
	}
}
