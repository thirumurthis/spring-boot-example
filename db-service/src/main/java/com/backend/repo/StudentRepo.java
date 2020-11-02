package com.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.Student;


public interface StudentRepo extends JpaRepository<Student, Integer>{

	public List<Student> findByUserName(String name);
}
