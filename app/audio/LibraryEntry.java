package app.audio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public abstract class LibraryEntry {
    private final String name;
    @Setter
    private boolean loaded;

    /**
     * @param name
     */
    public LibraryEntry(final String name) {
        this.name = name;
        this.loaded = false;
    }



    /**
     * @param name
     * @return
     */
    public boolean matchesName(final String name) {
        return getName().toLowerCase().startsWith(name.toLowerCase());
    }

    /**
     * @param album
     * @return
     */
    public boolean matchesAlbum(final String album) {
        return false;
    }

    /**
     * @param tags
     * @return
     */
    public boolean matchesTags(final ArrayList<String> tags) {
        return false;
    }

    /**
     * @param lyrics
     * @return
     */
    public boolean matchesLyrics(final String lyrics) {
        return false;
    }

    /**
     * @param genre
     * @return
     */
    public boolean matchesGenre(final String genre) {
        return false;
    }

    /**
     * @param artist
     * @return
     */
    public boolean matchesArtist(final String artist) {
        return false;
    }

    /**
     * @param releaseYear
     * @return
     */
    public boolean matchesReleaseYear(final String releaseYear) {
        return false;
    }

    /**
     * @param user
     * @return
     */
    public boolean matchesOwner(final String user) {
        return false;
    }

    /**
     * @param user
     * @return
     */
    public boolean isVisibleToUser(final String user) {
        return false;
    }

    /**
     * @param followers
     * @return
     */
    public boolean matchesFollowers(final String followers) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

}
