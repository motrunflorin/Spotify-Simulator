package fileio.input;

import java.util.ArrayList;

public class FiltersInput {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear; // pentru search song/episode -> releaseYear
    @lombok.Setter
    private String artist;
    private String owner; // pentru search playlist si podcast
    private String followers; // pentru search playlist -> followers

    public FiltersInput() {
    }

    /**
     * @return
     */

    public String getName() {
        return name;
    }

    /**
     * @param name
     */

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return
     */

    public String getAlbum() {
        return album;
    }

    /**
     * @param album
     */

    public void setAlbum(final String album) {
        this.album = album;
    }

    /**
     * @return
     */

    public ArrayList<String> getTags() {
        return tags;
    }

    /**.
     * @param tags
     */

    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     * Get the lyrics.
     * @return
     */

    public String getLyrics() {
        return lyrics;
    }

    /**
     * @param lyrics
     */

    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    /**.
     * @return
     */

    public String getGenre() {
        return genre;
    }

    /**
     * @param genre
     */

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    /**
     * @return
     */

    public String getReleaseYear() {
        return releaseYear;
    }

    /**
     * @param releaseYear
     */

    public void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }


    /**
     * @return
     */
    public String getArtist() {
        return artist;
    }


    /**
     * @return
     */

    public String getOwner() {
        return owner;
    }


    /**
     * @param owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }


    /**
     * @return
     */
    public String getFollowers() {
        return followers;
    }

    /**
     * Set the followers.
     * @param followers
     */

    public void setFollowers(final String followers) {
        this.followers = followers;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "FilterInput{"
                + ", name='" + name + '\''
                + ", album='" + album + '\''
                + ", tags=" + tags
                + ", lyrics='" + lyrics + '\''
                + ", genre='" + genre + '\''
                + ", releaseYear='" + releaseYear + '\''
                + ", artist='" + artist + '\''
                + ", owner='" + owner + '\''
                + ", followers='" + followers + '\''
                + '}';
    }
}
