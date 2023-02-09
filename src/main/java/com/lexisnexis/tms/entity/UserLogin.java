package com.lexisnexis.tms.entity;

import com.lexisnexis.tms.bool.BooleanToYNStringConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@Entity
@Table(name = "user_login_history")
public class UserLogin {

    @Id
    @Column(name = "user_Name")
    private String userName;
    @Column(name = "failure_Attempts")
    private int failureAttempts;
    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "login_Status", length = 2)
    private Boolean loginStatus;
    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "is_Locked", length = 2)
    private Boolean isLocked;
    @Column(name = "lock_time")
    private Date lockTime;
    @Column
    @CreationTimestamp
    private LocalDateTime loginTime;

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getFailureAttempts() {
        return failureAttempts;
    }

    public void setFailureAttempts(int failureAttempts) {
        this.failureAttempts = failureAttempts;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @PrePersist
    public void onSave() {
        final LocalDateTime currDateTime = LocalDateTime.now();
        this.loginTime = currDateTime;
    }
}
