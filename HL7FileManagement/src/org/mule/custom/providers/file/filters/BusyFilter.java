package org.mule.custom.providers.file.filters;

import org.mule.providers.file.filters.FilenameWildcardFilter;
import org.mule.umo.UMOMessage;
import org.mule.providers.file.FileConnector;
import java.io.File;

public class BusyFilter extends FilenameWildcardFilter {

    public BusyFilter()
    {
        super();
    }

    public BusyFilter(String pattern)
    {
        super(pattern);
    }

    /**
     * UMOFilter condition decider method. <p/> Returns
     * <code>boolean</code> <code>TRUE</code> if the file conforms to an
     * acceptable pattern and no file busy.txt exists in the
	 * inbound directory or <code>FALSE</code> otherwise.
     * 
     * @param dir The directory to apply the filter to.
     * @param name The name of the file to apply the filter to.
     * @return indication of acceptance as boolean.
     */
    public boolean accept(File dir, String name)
    {
        if (name == null) {
            logger.warn("The filename and or directory was null");
            return false;
		} else {
			File test = new File(dir.getPath()+"/busy.txt");
			if (test.exists())
				return false;
			else
	        	return accept(name);
        }
    }
   
    public boolean accept(UMOMessage message)
    {
        return accept(message.getProperty(FileConnector.PROPERTY_ORIGINAL_FILENAME));
    }

}
