package com.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.Student;
import com.backend.model.Subjects;
import com.backend.repo.StudentRepo;

@RestController
@RequestMapping("/api")
public class StudentController {

	@Autowired
	private StudentRepo studentRepo;
	
	@GetMapping("/student/{name}")
	public List<String> getStudent(@PathVariable("name") String name) {
		
		return studentRepo.findByUserName(name).stream().map(Student::getSubject).collect(Collectors.toList());
	}
	
	@PostMapping("/student/add")
	public List<String> addStudent(@RequestBody Subjects subjects){
		if(null == subjects)
			return null;
		subjects.getSubjects()
		//.map(subject -> (new Student(subjects.getStudentName(),subject) )
		.forEach(subject -> {
			studentRepo.save(new Student(subjects.getStudentName(),subject));			
		});
         return studentRepo.findByUserName(subjects.getStudentName()).stream().map(Student::getSubject).collect(Collectors.toList());
	}
	
	@PostMapping("/student/delete/{name}")
	public List<String> delete(@PathVariable("name") final String name){
		return deleteStudent(name);
	}
	
	@DeleteMapping("/student/{name}")
	public List<String> deleteStudent(@PathVariable("name") final String name){

		List<Student> students = studentRepo.findByUserName(name);
		studentRepo.deleteAll(students);
		return studentRepo.findByUserName(name).stream().map(Student::getSubject).collect(Collectors.toList());
	}
	
	
	@GetMapping("/students")
	public List<Student> getStudents(){
		return studentRepo.findAll();
	}
	
	@GetMapping("/subjects")
	public List<Subjects> getSubjects(){
		List<Subjects> subjects = new ArrayList<>();
		Map<String,List<String>> mapStudentSubject= new HashMap<String,List<String>>();
		studentRepo.findAll()
		.stream()
		.forEach(student -> {
			if(mapStudentSubject.containsKey(student.getUserName())) {
				mapStudentSubject.get(student.getUserName()).add(student.getSubject());
			}else {
				List<String> sub = new ArrayList<>();
				sub.add(student.getSubject());
				mapStudentSubject.put(student.getUserName(),sub);
			}
		});
		mapStudentSubject
		.forEach((k,v) -> {
			Subjects subject = new Subjects(k,v);
			subjects.add(subject);
		});
		return subjects;
	}
}
