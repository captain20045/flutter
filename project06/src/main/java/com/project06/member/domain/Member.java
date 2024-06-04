package com.project06.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member") // Specify the table name if different from class name
public class Member{
    @Id
    @Column(name = "userid")
    private String userid; // Assuming the primary key is of type Long

    @Column(name = "userpw")
    private String userpw;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phone_number;


    public Member() {
        // Default constructor for JPA and Jackson
    }

    public Member(String userid, String userpw, String name, String gender, String email, String phone_number) {
        this.userid = userid;
        this.userpw = userpw;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone_number = phone_number;
    }

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserpw() {
		return userpw;
	}

	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	
}
