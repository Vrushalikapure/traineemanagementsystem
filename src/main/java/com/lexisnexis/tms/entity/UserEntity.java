package com.lexisnexis.tms.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
@Entity
@Table(name = "user_details")
public class UserEntity {

    @Id
    @Column(name = "userName")
    @NotEmpty(message = "UserName can not be empty!")
    @NotNull
    private String userName;
    @NotEmpty(message = "firstName can not be empty!")
    @NotNull
    @Column(name = "firstName")
    private String firstName;
    @NotEmpty(message = "lastName can not be empty!")
    @NotNull
    @Column(name = "lastName")
    private String lastName;
    @NotEmpty(message = "mobileNo can not be empty!")
    @NotNull
    @Column(name = "mobileNo")
    private String mobileNo;
    @NotEmpty(message = "email can not be empty!")
    @NotNull
    @Email
    @Column(name = "email")
    private String email;
    @NotEmpty(message = "location can not be empty!")
    @NotNull
    @Column(name = "location")
    private String location;
    @NotEmpty(message = "password can not be empty!")
    @NotNull
    @Column(name = "password")
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")
    private String password;
    @Column(name = "createdTimestamp")
    @CreationTimestamp
    @CreatedDate
    private LocalDateTime createdTimestamp;
    @Column(name = "lastUpdatedTimestamp")
    @UpdateTimestamp
    @LastModifiedDate
    private LocalDateTime lastUpdatedTimestamp;

    public UserEntity(String userName, String firstName, String lastName, String mobileNo, String email, String location, String password, LocalDateTime createdTimestamp, LocalDateTime lastUpdatedTimestamp) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.email = email;
        this.location = location;
        this.password = password;
        this.createdTimestamp = createdTimestamp;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public  UserEntity() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public LocalDateTime getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(LocalDateTime lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }
}

