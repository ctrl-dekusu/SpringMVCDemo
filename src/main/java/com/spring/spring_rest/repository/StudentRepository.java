package com.spring.spring_rest.repository;

import com.spring.spring_rest.entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository interface for performing CRUD operations on 
 * {@link Student} entities.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

}
