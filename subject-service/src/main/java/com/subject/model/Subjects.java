package com.subject.model;

import java.util.List;

public class Subjects {

	public String studentName;
	public List<String> subjects;
	public Subjects() {
	}
	
	public Subjects(String studentName, List<String> subjects) {
		super();
		this.studentName = studentName;
		this.subjects = subjects;
	}

	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public List<String> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}
	
	
}
