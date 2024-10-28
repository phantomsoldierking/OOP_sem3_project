package com.example.scheduler.entity;

import javax.persistence.*;

@Entity
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String breakSession;
    private String classStartTime;
    private String classEndTime;

    @ManyToOne
    private Department department;

    @ManyToOne
    private YearBatch yearBatch;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreakSession() {
        return breakSession;
    }

    public void setBreakSession(String breakSession) {
        this.breakSession = breakSession;
    }

    public String getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(String classStartTime) {
        this.classStartTime = classStartTime;
    }

    public String getClassEndTime() {
        return classEndTime;
    }

    public void setClassEndTime(String classEndTime) {
        this.classEndTime = classEndTime;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public YearBatch getYearBatch() {
        return yearBatch;
    }

    public void setYearBatch(YearBatch yearBatch) {
        this.yearBatch = yearBatch;
    }
}
