package app.student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Scope("session")
@RequestMapping("management/api/v1/students")
public class StudentManagementController {

	private static List<Student> STUDENTS;
	// private static String ROLE_STUDENT, ROLE_ADMIN, ROLE_ADMINTRAINEE;

	public StudentManagementController() {
		STUDENTS = new ArrayList<>();
		STUDENTS.add(new Student(1, "James Bond"));
		STUDENTS.add(new Student(2, "Maria Jones"));
		STUDENTS.add(new Student(3, "Anna Smith"));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_' + T(app.security.ApplicationUserRole).ADMIN.name(),'ROLE_' + T(app.security.ApplicationUserRole).ADMINTRAINEE.name())")
	public List<Student> getAllStudents() {
		return STUDENTS;
	}

	@GetMapping(path = "{studentId}")
	@PreAuthorize("hasAnyRole('ROLE_' + T(app.security.ApplicationUserRole).ADMIN.name(),'ROLE_' + T(app.security.ApplicationUserRole).ADMINTRAINEE.name())")
	public Student getStudent(@PathVariable("studentId") Integer studentId) {
		return STUDENTS
				.stream()
					.filter(student -> studentId.equals(student.getStudentId()))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException(
							"Student " + studentId + " does not exists"));
	}

	@PutMapping(path = "{studentId}")
	@PreAuthorize("hasAuthority(T(app.security.ApplicationUserPermission).STUDENT_WRITE.name())")
	public String putStudent(	@PathVariable("studentId") Integer studentId,
								@RequestBody Student studentBody) {
		STUDENTS
				.stream()
					.filter(student -> studentId.equals(student.getStudentId()))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException(
							"Student " + studentId + " does not exists"));
		System.out.println(String.format("Student ID %s updated", studentId));
		return String.format("StudentId %s is deleted", studentId);
	}

	@PostMapping
	@PreAuthorize("hasAuthority(T(app.security.ApplicationUserPermission).STUDENT_WRITE.name())")
	// @PreAuthorize("hasAuthority('STUDENT_WRITE')")
	public String postStudent(@RequestBody Student studentBody) {

		STUDENTS.add(studentBody);

		System.out
				.println(String
						.format("Student ID %s posted",
								studentBody.getStudentId()));

		return String
				.format("StudentId %s is added", studentBody.getStudentId());
	}

	@DeleteMapping(path = "{studentId}")
	@PreAuthorize("hasAuthority(T(app.security.ApplicationUserPermission).STUDENT_WRITE.name())")
	public Student deleteStudent(@PathVariable("studentId") Integer studentId) {
		System.out.println(String.format("Student ID %s deleted", studentId));
		return STUDENTS
				.stream()
					.filter(student -> studentId.equals(student.getStudentId()))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException(
							"Student " + studentId + " does not exists"));
	}
}
