package app.user;

import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class UserFile extends LibraryEntry {
    public UserFile(final String name) {
        super(name);
    }
}
