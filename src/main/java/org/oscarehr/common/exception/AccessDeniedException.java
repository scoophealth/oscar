/**
 * Copyright (c) 2013-2015. Leverage Analytics. All Rights Reserved.
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
 */
package org.oscarehr.common.exception;

public class AccessDeniedException extends RuntimeException {

    private String permission;
    private String action;
    private String subject;

	public AccessDeniedException() {
        super();
        this.permission = null;
        this.action = null;
        this.subject = null;
	}

    public AccessDeniedException(String permission, String action) {
        super();
        this.permission = permission;
        this.action = action;
        this.subject = null;
    }

    public AccessDeniedException(String permission, String action, int subject) {
        super();
        this.permission = permission;
        this.action = action;
        this.subject = Integer.toString(subject);
    }

    public AccessDeniedException(String permission, String action, String subject) {
        super();
        this.permission = permission;
        this.action = action;
        this.subject = subject;
    }

	public AccessDeniedException(String message) {
        super(message);

        if(this.permission != null) message += " permission: "+this.permission;
        if(this.action != null) message += " action: "+this.action;
        if(this.subject != null) message += " subject: "+this.subject;

	}
}
