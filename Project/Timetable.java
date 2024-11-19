import java.util.ArrayList;

public class Timetable {
    private ArrayList<ArrayList<String>> slots;

    public Timetable() {
        slots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ArrayList<String> daySlots = new ArrayList<>();
            for (int j = 0; j < 18; j++) {
                daySlots.add("");
            }
            slots.add(daySlots);
        }
    }

    public void assignCourseToSlot(int day, int startSlot, int duration, String courseCode) {
        for (int i = 0; i < duration; i++) {
            slots.get(day).set(startSlot + i, courseCode);
        }
    }

    public ArrayList<ArrayList<String>> getSlots() {
        return slots;
    }
}
