package app.audio.Files;

import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioFile extends LibraryEntry {
    private final Integer duration;
    private int listens;


    public AudioFile(final String name, final Integer duration) {
        super(name);
        this.duration = duration;
        this.listens = 1;

    }

    public void incrementListens() {
        listens++;
    }
}
