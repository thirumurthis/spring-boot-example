package com.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="Student")
public class Student {
	
	public Student() {
		
	}
	public Student(String userName,String subject) {
		this.userName = userName;
		this.subject = subject;
	}

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id")
	 @SequenceGenerator(name = "student_id", sequenceName = "student_id", allocationSize = 1)
	private int id;
	
	@Column(name="user_name")
	public String userName;
	
	@Column(name="subject")
	public String subject;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
