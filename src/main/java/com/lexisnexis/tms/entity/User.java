package com.lexisnexis.tms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@Data
@Entity
@Table(name = "USER_TBL")
public class User {

	@Id
	private String userName;
    @NotEmpty(message = "firstName can not be empty!")
    @NotNull
    @Column(name = "firstName")
    private String firstName;
    @NotEmpty(message = "lastName can not be empty!")
    @NotNull
    @Column(name="lastName")
    private String lastName;
    @NotEmpty(message = "mobileNo can not be empty!")
    @NotNull
    @Column(name="mobileNo")
    private String mobileNo;
    @NotEmpty(message = "email can not be empty!")
    @NotNull
    @Email
    @Column(name="email")
    private String email;
    @NotEmpty(message = "location can not be empty!")
    @NotNull
    @Column(name="location")
    private String location;
    @Column(name="password")
    private String password;
    }