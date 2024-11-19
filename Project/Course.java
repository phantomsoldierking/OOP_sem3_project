public class Course {
    private String courseTitle;
    private String courseCode;
    private int[] ltpsc; // Lecture, Tutorial, Practical, Self Study, Credits
    private String faculty;
    private String labAssistance;

    public Course(String courseTitle, String courseCode, int[] ltpsc, String faculty, String labAssistance) {
        this.courseTitle = courseTitle;
        this.courseCode = courseCode;
        this.ltpsc = ltpsc;
        this.faculty = faculty;
        this.labAssistance = labAssistance;
    }

    public String getCourseTitle() { return courseTitle; }
    public String getCourseCode() { return courseCode; }
    public int[] getLtpsc() { return ltpsc; }
    public String getDetails() { return (ltpsc[0] + "-" + ltpsc[1] + "-" + ltpsc[2] + "-" + ltpsc[3] + "-" + ltpsc[4]); }
    public String getFaculty() { return faculty; }
    public String getLabAssistance() { return labAssistance; }

    public String toString() {
        return "\nCourse Code: " + courseCode + "\nCourse Title: " + courseTitle +
                "\nL-T-P-S-C: " +  ltpsc[0] + "-" + ltpsc[1] + "-" + ltpsc[2] + "-" + ltpsc[3] + "-" + ltpsc[4] +
                "\nFaculty: " + faculty + "\nLab Assistance: " + labAssistance;
    }
}
