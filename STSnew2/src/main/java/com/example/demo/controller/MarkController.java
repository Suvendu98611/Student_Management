package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Mark;
import com.example.demo.entity.Student;
import com.example.demo.service.MarkService;
import com.example.demo.service.StudentService;

@Controller
public class MarkController {

    private final StudentService studentService;
    private final MarkService markService;

    public MarkController(StudentService studentService, MarkService markService) {
        this.studentService = studentService;
        this.markService = markService;
    }

    @GetMapping("/students/{id}/marks/new")
    public String showMarkForm(@PathVariable Long id, Model model) {
        model.addAttribute("studentId", id);
        return "add_mark";
    }

    
    @PostMapping("/students/{id}/marks/saveAll")
    public String saveAllMarks(@PathVariable Long id, @RequestParam Map<String, String> marks) {
        Student student = studentService.getStudentById(id);

        int total = 0;
        int subjectCount = 0;
        boolean hasFailedSubject = false; // 👈 track if any subject is failed

        for (Map.Entry<String, String> entry : marks.entrySet()) {
            String subject = entry.getKey();
            String scoreStr = entry.getValue();

            try {
                int score = Integer.parseInt(scoreStr);
                subjectCount++;
                total += score;

                Mark mark = new Mark();
                mark.setStudent(student);
                mark.setSubject(subject);
                mark.setScore(score);

                String status = (score >= 30) ? "Pass" : "Fail";
                mark.setStatus(status);

                if (score < 30) {
                    hasFailedSubject = true; // 👈 mark overall result as fail
                }

                markService.saveMark(mark);

            } catch (NumberFormatException e) {
                System.out.println("Invalid score for subject: " + subject);
            }
        }

        if (subjectCount > 0) {
        	double percentage = Math.round(((double) total / subjectCount) * 100.0) / 100.0;
            String overallResult = hasFailedSubject ? "Fail" : "Pass";

            System.out.println("✅ Total Marks: " + total);
            System.out.println("✅ Percentage: " + percentage + "%");
            System.out.println("🎓 Overall Result: " + overallResult);
        }

        return "redirect:/students";
    }
    
    @GetMapping("/students/{id}/report")
    public String showStudentReport(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        List<Mark> marks = markService.getMarksByStudentId(id);

        int total = 0;
        int maxTotal = 0; // Variable to track total of max marks for all subjects

        // Hardcode maximum marks for each subject
        Map<String, Integer> maxMarks = Map.of(
            "Math", 100,
            "Science", 100,
            "English", 100,
            "Hindi", 100,
            "Social Science", 100,
            "Computer", 100
        );

        // Calculate total marks and maxTotal marks
        for (Mark mark : marks) {
            total += mark.getScore();
            maxTotal += maxMarks.get(mark.getSubject());  // Accumulate the max marks for each subject
        }

        // Calculate percentage based on total marks and maximum possible marks
        double percentage = (maxTotal > 0) ? ((double) total / maxTotal) * 100.0 : 0;

        // Format percentage to 2 decimal places without rounding
        String percentageFormatted = String.format("%.2f", percentage);

        // Compute overall result
        boolean hasFailed = marks.stream().anyMatch(mark -> "Fail".equalsIgnoreCase(mark.getStatus()));
        String overallResult = hasFailed ? "Fail" : "Pass";

        model.addAttribute("student", student);
        model.addAttribute("marks", marks);
        model.addAttribute("total", total);
        model.addAttribute("maxTotal", maxTotal); // Pass max total
        model.addAttribute("percentageFormatted", percentageFormatted); // Add formatted percentage
        model.addAttribute("overallResult", overallResult);
        model.addAttribute("maxMarks", maxMarks); // Add maxMarks to the model

        return "student_report";
    }









    
}

