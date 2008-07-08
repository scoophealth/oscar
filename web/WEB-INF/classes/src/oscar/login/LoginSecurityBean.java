/*
 * Created on 2005-5-19
 *
 */
package oscar.login;

import java.util.Date;

/**
 * @author yilee18
 */
public class LoginSecurityBean {
    Integer security_no;
    String user_name;
    String password;
    String provider_no;
    String pin;
    Integer b_ExpireSet;
    Date date_ExpireDate;
    Integer b_LocalLockSet;
    Integer b_RemoteLockSet;

    public void setNull() {
        setB_ExpireSet(null);
        setB_LocalLockSet(null);
        setB_RemoteLockSet(null);
        setDate_ExpireDate(null);
        setSecurity_no(null);
        setPassword(null);
        setPin(null);
        setProviderNo(null);
        setUser_name(null);
    }

    public Integer getB_ExpireSet() {
        return b_ExpireSet;
    }

    public void setB_ExpireSet(Integer expireSet) {
        b_ExpireSet = expireSet;
    }

    public Integer getB_LocalLockSet() {
        return b_LocalLockSet;
    }

    public void setB_LocalLockSet(Integer localLockSet) {
        b_LocalLockSet = localLockSet;
    }

    public Integer getB_RemoteLockSet() {
        return b_RemoteLockSet;
    }

    public void setB_RemoteLockSet(Integer remoteLockSet) {
        b_RemoteLockSet = remoteLockSet;
    }

    public Date getDate_ExpireDate() {
        return date_ExpireDate;
    }

    public void setDate_ExpireDate(Date date_ExpireDate) {
        this.date_ExpireDate = date_ExpireDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getProviderNo() {
        return provider_no;
    }

    public void setProviderNo(String provider_no) {
        this.provider_no = provider_no;
    }

    public Integer getSecurity_no() {
        return security_no;
    }

    public void setSecurity_no(Integer security_no) {
        this.security_no = security_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
