package app.user;

import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
public class Event {
    private String name;
    private String description;
    private String date;

    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    /**
     * @param date
     * @return
     */
    public boolean isValidDate(final String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);

        // Additional checks for day, month, and year
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6));

        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2023) {
            return false;
        }

        // Check for February
        return month != 2 || day <= 28;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return String.format("%s - %s:\n\t%s", getName(), getDate(), getDescription());
    }

}
