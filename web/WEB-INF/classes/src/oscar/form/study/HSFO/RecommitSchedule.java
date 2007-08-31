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