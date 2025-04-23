package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Mark;
import com.example.demo.repository.MarkRepository;
import com.example.demo.service.MarkService;

@Service
public class MarkServiceImpl implements MarkService {

    private final MarkRepository markRepository;

    public MarkServiceImpl(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    @Override
    public void saveMark(Mark mark) {
        if (mark.getId() != null) {
            // Existing mark — fetch it first
            Mark existingMark = markRepository.findById(mark.getId())
                .orElseThrow(() -> new RuntimeException("Mark not found with ID: " + mark.getId()));

            // Update only the fields you allow users to change
            existingMark.setSubject(mark.getSubject());
            existingMark.setScore(mark.getScore());
            // Add more setters if needed

            markRepository.save(existingMark);
        } else {
            // New mark — safe to save directly
            markRepository.save(mark);
        }
    }


    @Override
    public List<Mark> getMarksByStudentId(Long studentId) {
        return markRepository.findByStudentId(studentId);
    }
}
