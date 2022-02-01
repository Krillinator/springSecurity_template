package com.krillinator.springSecurityIntro.controller;

import com.krillinator.springSecurityIntro.auth.ApplicationUserService;
import com.krillinator.springSecurityIntro.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // return = STRING != html file
public class StudentController {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public StudentController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    /* TODO - Replace with DB implementation now! */
    @GetMapping("/finalTest")
    public UserDetails getUsername() {
        return applicationUserService.loadUserByUsername("Anton");
    }

    // MAPPINGS (endpoint)
    @GetMapping("/student")
    public Student getStudent(Student student) {
        student.setAge(15);
        student.setName("Benny");
        student.setId(0);
        return student;
    }

    @GetMapping("/admin/get") // Blocked - AntMatchers
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Student getAdminStudent(Student student) {
        student.setAge(25);
        student.setName("Admin");
        student.setId(1);
        return student;
    }

    @GetMapping("/student/get")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')") // @PreAuthorize does NOT override AntMatchers
    public Student getStudentAdmin(Student student) {
        student.setAge(29);
        student.setName("Björn");
        student.setId(2);
        return student;
    }

    // "hasAuthority('student:write')"
    @GetMapping("/admin/write") // Logged in as Admin == works?
    @PreAuthorize("hasRole('ROLE_ADMIN')" + " && hasAuthority('student:write')" )
    public Student getStudentWrite(Student student) {
        student.setAge(190);
        student.setName("Anna-the-destroyer");
        student.setId(3);
        return student;
    }
}
