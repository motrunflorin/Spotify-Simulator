package app.user;

import lombok.Getter;

@Getter
public class Announcement {
    private final String name;
    private final String description;

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return String.format("%s:\n\t%s\n", name, description);
    }

}
