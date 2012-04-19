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


package oscar.form.study.HSFO;

import java.util.Date;
public class RecommitSchedule{
	Integer id;   
    String status;       
    String memo;                            
    Date schedule_time;  
    String user_no;
    boolean check_flag=true;
    
    public RecommitSchedule(){
    	
    }
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getMemo()
	{
		return memo;
	}
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	public Date getSchedule_time()
	{
		return schedule_time;
	}
	public void setSchedule_time(Date schedule_time)
	{
		this.schedule_time = schedule_time;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getUser_no()
	{
		return user_no;
	}
	public void setUser_no(String user_no)
	{
		this.user_no = user_no;
	}
	public boolean isCheck_flag()
	{
		return check_flag;
	}
	public void setCheck_flag(boolean check_flag)
	{
		this.check_flag = check_flag;
	}
    
    
}
