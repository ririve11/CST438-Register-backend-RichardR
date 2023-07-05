package com.cst438.controller;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.StudentDTO;

@RestController
@CrossOrigin(origins = {"http://localhost:30000"})
public class StudentController {

	@Autowired
	StudentRepository studentrepos;
	
	@GetMapping("/student")
	public StudentDTO getStudent(@RequestParam("student_id") int student_id) {
		System.out.println("/student");
		Student find = studentrepos.findById(student_id).orElse(null);
		
		if (find != null) {
			System.out.println("/schedule student " + find.getName() + " " + find.getStudent_id());
			StudentDTO stud = createStudentDTO(find);
			return stud;
		} else {
			System.out.println("/schedule student not found. " + student_id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found. ");
		}
	}
	
	@PostMapping("/student")
	@Transactional
	public StudentDTO addNewStudent(@RequestBody StudentDTO student) {
		
		Student check = studentrepos.findByEmail(student.student_email);
		
		if (check == null) {
			Student register = new Student();
			register.setStudent_id(student.id);
			register.setName(student.student_name);
			register.setEmail(student.student_email);
			register.setStatusCode(student.status_code);
			studentrepos.save(register);
			StudentDTO result = createStudentDTO(register);
			return result;
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered. " + student.student_email);
		}
	}
	
	@PutMapping("/student")
	public void updateStudentStatus(@RequestBody StudentDTO student, @PathVariable int student_id) {
		Student checkIfAvailable = studentrepos.findById(student_id).orElse(null);
		
		if (checkIfAvailable != null) {
			checkIfAvailable.setStatus(student.status);
			checkIfAvailable.setStatusCode(student.status_code);
			studentrepos.save(checkIfAvailable);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student ID." + student_id);
		}
	}
	
	private StudentDTO createStudentDTO(Student stud) {
		StudentDTO studDTO = new StudentDTO();
		studDTO.id = stud.getStudent_id();
		studDTO.student_name = stud.getName();
		studDTO.student_email = stud.getEmail();
		studDTO.status_code = stud.getStatusCode();
		return studDTO;
	}
}
