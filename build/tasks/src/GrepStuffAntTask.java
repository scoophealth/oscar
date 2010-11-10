import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * The purpose of this class is to have an ant task which does some code checking for us. The initial reason is because as an example we have a class we would like to remove, say "DBHander.java", the problem is there's about 2000 references to this class
 * so refactoring is going to take months. The second problem is that people are making new calls to DBHandler despite the deprecation and notes and comments and emails and documentation saying not to use it. As a result, we need something that will
 * "fail". So, the idea is I grep through the code for DBHandler, I see maybe 2134 references. I now have an ant task which will grep through the code upon each build, if the reference count is greater than the previous count (2134) then I will fail the
 * ant build. So the only real purpose of this class is to help enforce rules we have agreed upon, but is apparently being ignored. This can be extended to other any checking as required. Before extending, check PMD and checkstyle to see if what you want
 * is already covered though. As a note, I will keep more than just the count, I will keep a copy of every file reference. This is because I need to be able to report which file caused the violation or else it'll be useless. For usage, the first time this
 * is run, not previous data will be found. As a result it will assume all errors are new and fail the build. It will then generate and save data file as per your dataFile name. Upon subsequent runs it will assume that data file is the baseline to compare
 * to. When you want to rest the baseline, just delete the file and run the task again and a new data file will be generated.
 */

public class GrepStuffAntTask extends Task {
	private static final String DATA_SEPARATOR="----------------------------------------";
	
	private String dataFile;
	private String srcDir;
	private String showExtensions;
	private String showAllViolations;
	
	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}
	
	public void setShowExtensions(String showExtensions) {
    	this.showExtensions = showExtensions;
    }

	public void setShowAllViolations(String showAllViolations) {
    	this.showAllViolations = showAllViolations;
    }

	@Override
	public void execute() throws BuildException {
		try {
			log(DATA_SEPARATOR);
			log(getClass().getName() + " starting with srcDir=" + srcDir + ", dataFile=" + dataFile);

			// these maps are of type key=String/violation value=String/relativePath
			MultiValueMapSerialisable previousViolations = getPreviousViolations();
			
			GrepStuffDirectoryWalker grepStuffDirectoryWalker=getWalkedDirectory();
			MultiValueMapSerialisable currentViolations = grepStuffDirectoryWalker.getVoliations();

			if (previousViolations == null) writeOut(currentViolations);

			if (hasNewErrors(previousViolations, currentViolations)) {
				throw (new BuildException("Failed, new violations found."));
			}
			
			if ("true".equals(showExtensions)) {
				log(DATA_SEPARATOR);
				for (String s : grepStuffDirectoryWalker.getExtensions())
				{
					log("extension : "+s);
				}
			}
		} catch (BuildException e) {
			throw (e);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new BuildException(e));
		}
	}

	private void writeOut(MultiValueMapSerialisable currentViolations) throws IOException {
		FileOutputStream fos = new FileOutputStream(dataFile);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(currentViolations);
			oos.flush();
		} finally {
			fos.close();
		}
	}

	private boolean hasNewErrors(MultiValueMapSerialisable previousViolations, MultiValueMapSerialisable currentViolations) {

		Set<String> keys = currentViolations.keySet();
		boolean hasNewErrors = false;
		log(DATA_SEPARATOR);
		
		for (String key : keys) {
			Collection<String> previousValues = null;
			if (previousViolations != null) previousValues = previousViolations.getCollection(key);
			if (previousValues == null) previousValues = new ArrayList<String>();

			Collection<String> currentValues = currentViolations.getCollection(key);
			if (currentValues == null) previousValues = new ArrayList<String>();

			log("Violation=" + key + ", previous=" + previousValues.size() + ", current=" + currentValues.size());

			for (String currentTemp : currentValues) {
				if (!previousValues.contains(currentTemp)) {
					hasNewErrors = true;
					log("New violation. Type=" + key + ", file=" + currentTemp);
				}
				else if ("true".equals(showAllViolations))
				{
					log("Existing violation. Type=" + key + ", file=" + currentTemp);
				}
			}
		}

		return (hasNewErrors);
	}

	private GrepStuffDirectoryWalker getWalkedDirectory() throws IOException
	{
		GrepStuffDirectoryWalker grepStuffDirectoryWalker = new GrepStuffDirectoryWalker(this);
		grepStuffDirectoryWalker.walk(srcDir);	
		return(grepStuffDirectoryWalker);
	}
	
	private MultiValueMapSerialisable getPreviousViolations() throws IOException, ClassNotFoundException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(dataFile);
			ObjectInputStream ois = new ObjectInputStream(fis);

			MultiValueMapSerialisable result = (MultiValueMapSerialisable) (ois.readObject());

			ois.close();

			return (result);
		} catch (FileNotFoundException e) {
			return (null);
		} finally {
			if (fis != null) fis.close();
		}
	}
}
