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


package oscar.oscarDemographic.pageUtil;

import java.io.File;
import java.io.IOException;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.util.StringUtils;

/**
 *
 * @author Ronnie Cheng
 */
public class PGPEncrypt {
    String bin;
    String cmd;
    String key;
    String env;

    public PGPEncrypt() {
	OscarProperties op = OscarProperties.getInstance();
	this.bin = StringUtils.noNull(op.getProperty("PGP_BIN"));
	if (StringUtils.empty(this.bin)) MiscUtils.getLogger().debug("Warning: PGP binary executable (PGP_BIN) not set!");
	this.cmd = StringUtils.noNull(op.getProperty("PGP_CMD"));
	if (StringUtils.empty(this.cmd)) MiscUtils.getLogger().debug("Warning: PGP encryption command (PGP_CMD) not set!");
	this.key = StringUtils.noNull(op.getProperty("PGP_KEY"));
	if (StringUtils.empty(this.key)) MiscUtils.getLogger().debug("Warning: PGP encryption key (PGP_KEY) not set!");
	this.env = StringUtils.noNull(op.getProperty("PGP_ENV"));
	if (StringUtils.empty(this.env)) MiscUtils.getLogger().debug("Warning: PGP environment variable (PGP_ENV) not set!");
    }

    public boolean check(String dirName) throws Exception {
        if (!Util.checkDir(dirName)) {
            MiscUtils.getLogger().debug("Error! Cannot write to directory ["+dirName+"]");
            return false;
        }
        Runtime rt = Runtime.getRuntime();
        String[] env = {""};
        File dir = new File(dirName);

        boolean rtrn = false;
        try {
            Process proc = rt.exec("touch null.tmp", env, dir);
            int ecode = proc.waitFor();
            if (ecode==0) {
                String[] cmd = {this.bin, this.cmd, "null.tmp", this.key};
                env[0] = this.env;
                proc = rt.exec(cmd, env, dir);
                ecode = proc.waitFor();
                if (ecode==0) {
                    Util.cleanFile("null.tmp.pgp", dirName);
                    rtrn = true;
                }
                Util.cleanFile("null.tmp", dirName);
            }
        } catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
        }
        return rtrn;
    }

    boolean encrypt(String srcFile, String workDir) throws Exception {
        if (!Util.checkDir(workDir)) {
            MiscUtils.getLogger().debug("Error! Cannot write to directory ["+workDir+"]");
            return false;
        }
        if (StringUtils.empty(srcFile)) {
            MiscUtils.getLogger().debug("Error! Source file not given; nothing to encrypt!");
            return false;
        }
	Runtime rt = Runtime.getRuntime();
	String[] env = {this.env};
	String[] cmd = new String[4];
	cmd[0] = this.bin;
	cmd[1] = this.cmd;
	cmd[2] = srcFile;
	cmd[3] = this.key;
        File dir = new File(workDir);

        try {
            Process proc = rt.exec(cmd, env, dir);
            int ecode = proc.waitFor();
            if (ecode==0) return true;

        } catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
        }
        return false;
    }
}
