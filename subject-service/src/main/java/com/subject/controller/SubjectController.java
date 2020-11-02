package com.subject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.subject.model.Subjects;

@RestController
@RequestMapping("/api")
public class SubjectController {

	@Autowired
	private RestTemplate restTemplate;
	
	//private final String URL = "http://localhost:8090/api/subjects";
	private final String URL = "http://db-service/api/subjects";
	
	@GetMapping("/students")
	public List<Subjects> getSubjects(){
		
		ParameterizedTypeReference<List<Subjects>> subjects =
			     new ParameterizedTypeReference<List<Subjects>>() {};

		ResponseEntity<List<Subjects>> response =  restTemplate.exchange(URL, HttpMethod.GET, null, subjects);
		return response.getBody();
	}
}
