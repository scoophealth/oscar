/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 *
 *
 * PGPEncrypt.java
 *
 * Created on July 23, 2007, 11:49 AM
 */

package oscar.oscarDemographic.pageUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import oscar.OscarProperties;

/**
 *
 * @author Ronnie Cheng
 */
public class PGPEncrypt {
    String dir;
    String bin;
    String cmd;
    String key;
    String env;
    String src;

    public PGPEncrypt(String src, String dir) throws Exception {
	OscarProperties op = OscarProperties.getInstance();
	this.dir = dir;
	if (!Util.filled(this.dir)) throw new Exception("Working directory does not exist!");
	this.bin = op.getProperty("PGP_BIN");
	if (!Util.filled(this.bin)) throw new Exception("PGP binary executable (PGP_BIN) not set in oscar.properties!");
	this.cmd = op.getProperty("PGP_CMD");
	if (!Util.filled(this.cmd)) throw new Exception("PGP encryption command (PGP_CMD) not set in oscar.properties!");
	this.key = op.getProperty("PGP_KEY");
	if (!Util.filled(this.key)) throw new Exception("PGP encryption key (PGP_KEY) not set in oscar.properties!");
	this.env = op.getProperty("PGP_ENV");
	if (!Util.filled(this.env)) throw new Exception("PGP environment variable (PGP_ENV) not set in oscar.properties!");
	this.src = src;
	if (!Util.filled(this.src)) throw new Exception("Source file not given. Nothing to encrypt!");
    }
    
    boolean doEncrypt() throws Exception {
	Runtime rt = Runtime.getRuntime();
	String[] cmd = new String[4];
	String[] env = new String[1];
	File dir = new File(this.dir);
	cmd[0] = this.bin;
	cmd[1] = this.cmd;
	cmd[2] = this.src;
	cmd[3] = this.key;
	env[0] = this.env;
	Process proc = rt.exec(cmd, env, dir);
	int ecode = proc.waitFor();
	if (ecode==0) {
	    InputStream in = proc.getInputStream();
	    OutputStream out = new FileOutputStream(dir.getPath()+"/done.tmp");
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len=in.read(buf)) > 0) out.write(buf,0,len);
	    in.close();
	    out.close();	    
	    
	    //Remove source file & "done.tmp" - useless by-product of PGP encryption
	    if (!Util.cleanFile(dir.getPath()+"/done.tmp"))
                throw new Exception("Error! Cannot remove \"done.tmp\".");
	    if (!Util.cleanFile(dir.getPath()+"/"+this.src))
                throw new Exception("Error! Cannot remove source file!");
	    
	    return true;
	} else {	    
	    System.out.println("PGP Encryption Error: " + ecode);
	    InputStream err = proc.getErrorStream();
	    BufferedReader erdr = new BufferedReader(new InputStreamReader(err));
	    String line = null;
	    while ((line = erdr.readLine()) != null) System.out.println(line);
	    erdr.close();
	    err.close();
	    
	    return false;
	}
    }
}
