package com.example.demo.controller;

import com.example.demo.entity.Mark;
import com.example.demo.entity.Student;
import com.example.demo.service.MarkService;
import com.example.demo.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final MarkService markService;

    public StudentController(StudentService studentService, MarkService markService) {
        this.studentService = studentService;
        this.markService = markService;
    }

    // List all students
    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    // Create student form
    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "create_student";
    }

    // Save student
//    @PostMapping("/students")
//    public String saveStudent(@ModelAttribute("student") Student student) {
//        studentService.saveStudent(student);
//        return "redirect:/students";
//    }

    // Edit student form
    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "edit_student";
    }

    // Update student
    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") Student student, Model model) {
        Student existingStudent = studentService.getStudentById(id);
        Student duplicateRollNumber = studentService.getStudentByRollNumber(student.getRollNumber());

        // Check for duplicate roll number but exclude the current student
        if (duplicateRollNumber != null && !duplicateRollNumber.getId().equals(id)) {
            model.addAttribute("student", student);
            model.addAttribute("error", "Roll number already exists!");
            return "edit_student";
        }

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setRollNumber(student.getRollNumber());

        studentService.updateStudent(existingStudent);
        return "redirect:/students";
    }


 // Delete student (proper POST request from form)
    @PostMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }


    // Show form to add marks
    @GetMapping("/students/{id}/marks/add")
    public String showAddMarkForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("mark", new Mark());
        return "add_mark";
    }

    // Save mark
    @PostMapping("/students/{id}/marks")
    public String saveMark(@PathVariable Long id, @ModelAttribute("mark") Mark mark) {
        Student student = studentService.getStudentById(id);
        mark.setStudent(student);
        markService.saveMark(mark);
        return "redirect:/students/" + id + "/report";
    }

    // Student report view
//    @GetMapping("/students/{id}/report")
//    public String showStudentReport(@PathVariable Long id, Model model) {
//        Student student = studentService.getStudentById(id);
//        List<Mark> marks = markService.getMarksByStudentId(id);
//        model.addAttribute("student", student);
//        model.addAttribute("marks", marks);
//        return "student_report";
//        
//        }
    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") Student student, Model model) {
        // Check if a student with the same roll number already exists
        Student existing = studentService.getStudentByRollNumber(student.getRollNumber());
        
        if (existing != null) {
            model.addAttribute("student", student);
            model.addAttribute("error", "Roll number already exists!");
            return "create_student";
        }

        studentService.saveStudent(student);
        return "redirect:/students";
    }

    }


    

