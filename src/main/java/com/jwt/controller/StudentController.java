package com.jwt.controller;

import com.jwt.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    List<Student> students = List.of(
            new Student(101, "Adinath", "Pune", 82),
            new Student(102, "Ruturaj", "Pune", 58)
    );

    @GetMapping("/students")
    public List<Student> getAllStudents(){
        return students;
    }

    @PostMapping("/student/add")
    public Student addStudent(@RequestBody Student student){
//        students.add(student);
        return student;
    }
}
