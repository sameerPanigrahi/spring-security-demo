package app.student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Scope("session")
@RequestMapping("api/v1/students")
public class StudentController {

	private static List<Student> STUDENTS;

	public StudentController() {
		STUDENTS = new ArrayList<>();
		STUDENTS.add(new Student(1, "James Bond"));
		STUDENTS.add(new Student(2, "Maria Jones"));
		STUDENTS.add(new Student(3, "Anna Smith"));
	}

	@GetMapping(path = "{studentId}")
	@PreAuthorize("hasRole('ROLE_' + T(app.security.ApplicationUserRole).STUDENT.name())")
	public Student getStudent(@PathVariable("studentId") Integer studentId) {
		return STUDENTS
				.stream()
					.filter(student -> studentId.equals(student.getStudentId()))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException(
							"Student " + studentId + " does not exists"));
	}
}
