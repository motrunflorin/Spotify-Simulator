package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;

    public AudioCollection(final String name, final String owner) {
        super(name);
        this.owner = owner;
    }

    /**
     * @return
     */
    public abstract int getNumberOfTracks();

    /**
     * @param index
     * @return
     */
    public abstract AudioFile getTrackByIndex(int index);

    /**
     * @param user
     * @return
     */
    @Override
    public boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }

}
