package app.audio.Collections;

import app.audio.Files.AudioFile;

import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Album extends AudioCollection {
    private final List<Song> songs;
    private final int releaseYear;
    private final String description;
    @Setter
    private boolean isLoaded;

    public Album(final String name, final int releaseYear, final String description,
                 final List<Song> songs, final String owner) {
        super(name, owner);
        this.songs = songs;
        this.releaseYear = releaseYear;
        this.description = description;
        this.isLoaded = false;
    }

    /**
     * @return
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    /**
     * @param index
     * @return
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     * @return
     */
    public int getNumberOfLikes() {
        return getSongs().stream()
                .mapToInt(Song::getLikes)
                .sum();
    }

}
