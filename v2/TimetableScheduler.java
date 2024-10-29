import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Course {
    String name;
    String courseCode;
    int requiredLabs;
    int lecturesPerWeek;
    int tutorialsPerWeek;
    Teacher teacher;

    public Course(String name, String courseCode, int requiredLabs, int lecturesPerWeek, int tutorialsPerWeek, Teacher teacher) {
        this.name = name;
        this.courseCode = courseCode;
        this.requiredLabs = requiredLabs;
        this.lecturesPerWeek = lecturesPerWeek;
        this.tutorialsPerWeek = tutorialsPerWeek;
        this.teacher = teacher;
    }
}

class Teacher {
    String name;
    String department;

    public Teacher(String name, String department) {
        this.name = name;
        this.department = department;
    }
}

class YearBatch {
    String branchName;
    int year;
    List<Course> courses = new ArrayList<>();

    public YearBatch(String branchName, int year) {
        this.branchName = branchName;
        this.year = year;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }
}

class TimeTable {
    String[][] schedule;

    public TimeTable(int days, int slots) {
        schedule = new String[days][slots];
    }

    public void setClass(int day, int slot, String courseName) {
        schedule[day][slot] = courseName;
    }

    public String[][] getSchedule() {
        return schedule;
    }
}

public class TimetableScheduler {
    static String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    static int slotsPerDay = 10;
    static String[] slotTimings = {
            "09:00-10:00", "10:00-11:00", "11:00-12:00", 
            "12:00-12:15 (Break)", "12:15-13:15", "13:15-14:15", 
            "14:15-15:15 (Lunch)", "15:15-16:15", "16:15-17:15", "17:15-18:15"
    };

    public static void main(String[] args) {
        List<Course> courses = loadCoursesFromCSV("courses.csv");
        YearBatch yearBatch = new YearBatch("CSE_B", 2);

        for (Course course : courses) {
            yearBatch.addCourse(course);
        }

        TimeTable timetable = generateTimeTable(yearBatch);
        showTimetableUI(timetable);
        saveTimetableToCSV(timetable, "output_timetable.csv");
    }

    public static List<Course> loadCoursesFromCSV(String fileName) {
        List<Course> courses = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0];
                String courseCode = values[1];
                int requiredLabs = Integer.parseInt(values[2]);
                int lecturesPerWeek = Integer.parseInt(values[3]);
                int tutorialsPerWeek = Integer.parseInt(values[4]);
                String teacherName = values[5];
                String department = values[6];

                Teacher teacher = new Teacher(teacherName, department);
                Course course = new Course(name, courseCode, requiredLabs, lecturesPerWeek, tutorialsPerWeek, teacher);
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static TimeTable generateTimeTable(YearBatch yearBatch) {
        TimeTable timetable = new TimeTable(days.length, slotsPerDay);

        List<String> sessions = new ArrayList<>();

        for (Course course : yearBatch.courses) {
            for (int i = 0; i < course.lecturesPerWeek; i++) sessions.add(course.name + " (Lec, 1.5hr)");
            for (int i = 0; i < course.tutorialsPerWeek; i++) sessions.add(course.name + " (Tut, 1hr)");
            for (int i = 0; i < course.requiredLabs; i++) sessions.add(course.name + " (Lab, 2hr)");
        }

        Collections.shuffle(sessions);

        int day = 0, slot = 0;
        for (String session : sessions) {
            if (day >= days.length) break;
            if (slot == 3 || slot == 6) { // Skip break and lunch slots
                slot++;
                continue;
            }
            if (slot >= slotsPerDay) {
                day++;
                slot = 0;
                if (day >= days.length) break;
                if (slot == 3 || slot == 6) {
                    slot++;
                }
            }

            timetable.setClass(day, slot, session);

            if (session.contains("1.5hr") || session.contains("2hr")) slot += 2;
            else slot += 1;
        }

        return timetable;
    }

    public static void showTimetableUI(TimeTable timetable) {
        JFrame frame = new JFrame("Weekly Timetable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 400);

        String[] columns = new String[slotsPerDay + 2];
        columns[0] = "Days";
        for (int i = 1; i <= slotsPerDay; i++) {
            columns[i] = "Slot " + i + " (" + slotTimings[i - 1] + ")";
        }

        DefaultTableModel model = new DefaultTableModel(columns, days.length);
        JTable table = new JTable(model);

        for (int i = 0; i < days.length; i++) {
            model.setValueAt(days[i], i, 0);
            for (int j = 0; j < slotsPerDay; j++) {
                String cellValue = timetable.getSchedule()[i][j];
                model.setValueAt(cellValue != null ? cellValue : (slotTimings[j].contains("Break") || slotTimings[j].contains("Lunch") ? slotTimings[j] : ""), i, j + 1);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void saveTimetableToCSV(TimeTable timetable, String fileName) {
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            StringBuilder sb = new StringBuilder();

            sb.append("Days");
            for (int i = 1; i <= slotsPerDay; i++) {
                sb.append(",Slot ").append(i).append(" (").append(slotTimings[i - 1]).append(")");
            }
            sb.append("\n");

            for (int i = 0; i < days.length; i++) {
                sb.append(days[i]);
                for (int j = 0; j < slotsPerDay; j++) {
                    sb.append(",");
                    String cellValue = timetable.getSchedule()[i][j];
                    sb.append(cellValue != null ? cellValue : (slotTimings[j].contains("Break") || slotTimings[j].contains("Lunch") ? slotTimings[j] : ""));
                }
                sb.append("\n");
            }

            writer.write(sb.toString());
            System.out.println("Timetable saved to " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error saving timetable to file: " + e.getMessage());
        }
    }
}
