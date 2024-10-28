package com.example.scheduler.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String courseCode;
    private int requiredNumberOfLabs;
    private int lectureRequiredPerWeek;
    private int tutorialRequiredPerWeek;

    @ManyToMany(mappedBy = "selectedCoursesForTheSem")
    private List<Student> students;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getRequiredNumberOfLabs() {
        return requiredNumberOfLabs;
    }

    public void setRequiredNumberOfLabs(int requiredNumberOfLabs) {
        this.requiredNumberOfLabs = requiredNumberOfLabs;
    }

    public int getLectureRequiredPerWeek() {
        return lectureRequiredPerWeek;
    }

    public void setLectureRequiredPerWeek(int lectureRequiredPerWeek) {
        this.lectureRequiredPerWeek = lectureRequiredPerWeek;
    }

    public int getTutorialRequiredPerWeek() {
        return tutorialRequiredPerWeek;
    }

    public void setTutorialRequiredPerWeek(int tutorialRequiredPerWeek) {
        this.tutorialRequiredPerWeek = tutorialRequiredPerWeek;
    }
}
