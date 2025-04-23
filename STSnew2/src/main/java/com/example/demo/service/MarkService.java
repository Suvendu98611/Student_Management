package com.example.demo.service;



import java.util.List;
import com.example.demo.entity.Mark;

public interface MarkService {
    void saveMark(Mark mark);
    List<Mark> getMarksByStudentId(Long studentId);

}
