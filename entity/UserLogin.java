package com.lexisnexis.tms.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "user_login_history")
public class UserLogin {

    @Id
//   @Column(name="userName")
    private String userName;

 

   // @Column(name = "failureAttempts")
    private int failureAttempts;

 

    //@Column(name="loginStatus")
    private String loginStatus;

 

   // @Column(name="isLocked")
    private String isLocked;

 

   // @Column(name="lockTime")
    @UpdateTimestamp
    private LocalDateTime lockTime;

 

   // @Column(name="loginTime")
    @CreationTimestamp
    private LocalDateTime loginTime;

 

    public int getFailureAttempts() {
        return failureAttempts;
    }

 

    public void setFailureAttempts(int failureAttempts) {
        this.failureAttempts = failureAttempts;
    }

 

    public String getLoginStatus() {
        return loginStatus;
    }

 

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

 

    public String getIsLocked() {
        return isLocked;
    }

 

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

 

    public LocalDateTime getLockTime() {
        return lockTime;
    }

 

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

 

    public String getUserName() {
        return userName;
    }

 

    public void setUserName(String userName) {
        this.userName = userName;
    }

 

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

 

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    

}
