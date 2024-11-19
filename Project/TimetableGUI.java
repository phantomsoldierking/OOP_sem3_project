import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.List;

import java.io.*;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


public class TimetableGUI extends JFrame {
    public TimetableGUI(Map<String, Timetable> timetableMap, Map<String, List<Course>> courseMap) {
        setTitle("Timetables");
        setSize(800, 800);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Create a tab for each batch
        for (String batchName : timetableMap.keySet()) {
            Timetable timetable = timetableMap.get(batchName);
            List<Course> courses = courseMap.get(batchName);

            JPanel panel = createBatchTab(timetable, courses);
            tabbedPane.addTab(batchName, panel);
        }

        add(tabbedPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createBatchTab(Timetable timetable, List<Course> courses) {
        JPanel panel = new JPanel(new BorderLayout());

        // Create timetable table
        String[] timetableColumns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[][] timetableData = generateTableData(timetable);
        JTable timetableTable = new JTable(timetableData, timetableColumns) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    if (timetableData[row][column].equals("Lunch Break")) {
                        c.setBackground(new Color(255, 223, 186)); // Light orange for lunch break
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternating rows
                    }
                }
                return c;
            }
        };
        timetableTable.setRowHeight(30);
        JScrollPane timetableScrollPane = new JScrollPane(timetableTable);

        // Create course details table
        String[] courseColumns = {"Course Code", "Course Name", "Type", "Credits"};
        String[][] courseData = generateCourseTableData(courses);
        JTable courseTable = new JTable(courseData, courseColumns) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternating rows
                }
                return c;
            }
        };
        courseTable.setRowHeight(30);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);

        // Split timetable and courses into two sections
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, timetableScrollPane, courseScrollPane);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(5);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }


    private String[][] generateTableData(Timetable timetable) {
        int totalSlots = 18;
        String[][] data = new String[totalSlots + 1][6];
        String[] timeSlots = {
            "09:00 - 09:30", "09:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00", "11:00 - 11:30", "11:30 - 12:00",
            "12:00 - 12:30", "12:30 - 13:00", "13:00 - 13:30 ", "13:30 - 14:00", "14:00 - 14:30", "14:30 - 15:00",
            "15:00 - 15:30", "15:30 - 16:00", "16:00 - 16:30", "16:30 - 17:30", "17:00 - 17: 30", "17:30 - 18:30"
        };

        ArrayList<ArrayList<String>> slots = timetable.getSlots();
        int rowIndex = 0;

        for (int i = 0; i < timeSlots.length; i++) {
            if (i == 8 || i == 9) {
                data[rowIndex][0] = timeSlots[i];
                for (int day = 1; day <= 5; day++) {
                    data[rowIndex][day] = "Lunch Break";
                }
                rowIndex++;
            } else {
                data[rowIndex][0] = timeSlots[i];
                for (int day = 0; day < 5; day++) {
                    data[rowIndex][day + 1] = (slots.get(day).get(rowIndex) == null) ? "" : slots.get(day).get(rowIndex);
                }
                rowIndex++;
            }
        }
        return data;
    }

    private String[][] generateCourseTableData(List<Course> courses) {
        String[][] data = new String[courses.size()][4];
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            data[i][0] = course.getCourseCode();
            data[i][1] = course.getCourseTitle();
            data[i][2] = course.getFaculty();
            data[i][3] = course.getDetails();
        }
        return data;
    }

    private static void saveTimetableToCSV(Timetable timetable, String batchName) throws IOException {
        String[] timeSlots = {
            "09:00 - 09:30", "09:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00", "11:00 - 11:30", "11:30 - 12:00",
            "12:00 - 12:30", "12:30 - 13:00", "13:00 - 13:30 ", "13:30 - 14:00", "14:00 - 14:30", "14:30 - 15:00",
            "15:00 - 15:30", "15:30 - 16:00", "16:00 - 16:30", "16:30 - 17:30", "17:00 - 17: 30", "17:30 - 18:30"
        };
        ArrayList<ArrayList<String>> slots = timetable.getSlots();

        File batchFolder = new File("./GeneratedTimetables/" + batchName);
        if (!batchFolder.exists()) {
            batchFolder.mkdirs();
        }

        File csvFile = new File(batchFolder, "timetable.csv");
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("Time,Monday,Tuesday,Wednesday,Thursday,Friday\n");
            for (int i = 0; i < timeSlots.length; i++) {
                StringBuilder row = new StringBuilder(timeSlots[i] + ",");
                for (int day = 0; day < 5; day++) {
                    if (i == 8 || i == 9) {
                        row.append("Lunch Break,");
                    } else {
                        row.append((slots.get(day).get(i) == null ? "" : slots.get(day).get(i)) + ",");
                    }
                }
                writer.write(row.deleteCharAt(row.length() - 1).toString() + "\n");
            }
        }
    }


    public static void main(String[] args) {
        File coursesFolder = new File("./Courses");
        if (!coursesFolder.exists() || !coursesFolder.isDirectory()) {
            System.out.println("Courses folder not found!");
            return;
        }

        File[] csvFiles = coursesFolder.listFiles((dir, name) -> name.endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No CSV files found in the Courses folder.");
            return;
        }

        TimetableGenerator generator = new TimetableGenerator();
        Map<String, Timetable> timetableMap = new HashMap<>();
        Map<String, List<Course>> courseMap = new HashMap<>();

        for (File csvFile : csvFiles) {
            String batchName = csvFile.getName().replace(".csv", "");
            try {
                List<Course> courses = CourseManager.loadCourses(csvFile.getPath());
                Timetable timetable = new Timetable();
                generator.generateTimetable(courses, timetable);

                timetableMap.put(batchName, timetable);
                courseMap.put(batchName, courses);

                // Save timetable to CSV
                saveTimetableToCSV(timetable, batchName);
                System.out.println("Timetable saved for batch: " + batchName);
            } catch (Exception e) {
                System.out.println("Error generating timetable for batch " + batchName + ": " + e.getMessage());
            }
        }

        SwingUtilities.invokeLater(() -> new TimetableGUI(timetableMap, courseMap));
    }

}