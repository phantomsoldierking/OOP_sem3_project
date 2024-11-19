import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.List;

public class TimetableGUI extends JFrame {
    public TimetableGUI(Timetable timetable) {
        setTitle("Weekly Timetable");
        setSize(800, 600);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Weekly Timetable", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[][] data = generateTableData(timetable);

        JTable table = new JTable(data, columns);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TimetableGenerator generator = new TimetableGenerator();

        while (true) {
            System.out.println("\n--- Timetable Generator ---");
            System.out.print("Enter the batch name (e.g. <branch>_<section(if applicable i.e. for CSE)>_<semester number>) or type 'exit' to quit: ");

            String batchName = scanner.nextLine().trim();

            if (batchName.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program. Goodbye!");
                break;
            }
            String batchFilePath = "./Courses/"  + batchName + ".csv";

            try {
                List<Course> courses = CourseManager.loadCourses(batchFilePath);
                Timetable timetable = new Timetable();
                generator.generateTimetable(courses, timetable);

                System.out.println("Generating timetable for " + batchName + "...");
                SwingUtilities.invokeLater(() -> new TimetableGUI(timetable));
            } catch (Exception e) {
                System.out.println("Error generating timetable for " + batchName + ": " + e.getMessage());
                System.out.println("Please check the batch name or file path.");
            }
        }

        scanner.close();
    }
}
