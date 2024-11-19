import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CourseManager {
    public static List<Course> loadCourses(String fileName) {
        List<Course> courses = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            scanner.nextLine(); // Skip header line in CSV

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    String courseTitle = parts[0].trim();
                    String courseCode = parts[1].trim();
                    int[] ltpsc = {
                        Integer.parseInt(parts[2].trim()), // Lecture hours
                        Integer.parseInt(parts[3].trim()), // Tutorial hours
                        Integer.parseInt(parts[4].trim()), // Practical hours
                        Integer.parseInt(parts[5].trim()), // Self Study
                        Integer.parseInt(parts[6].trim())  // Credits
                    };
                    String faculty = parts[7].trim();
                    String labAssistance = parts.length > 8 ? parts[8].trim() : "";

                    courses.add(new Course(courseTitle, courseCode, ltpsc, faculty, labAssistance));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
