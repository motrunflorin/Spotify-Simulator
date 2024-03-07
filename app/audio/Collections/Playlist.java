package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Playlist extends AudioCollection {
    private final ArrayList<Song> songs;
    private Enums.Visibility visibility;
    private Integer followers;
    private final int timestamp;

    public Playlist(final String name, final String owner) {
        this(name, owner, 0);
    }

    public Playlist(final String name, final String owner, final int timestamp) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.timestamp = timestamp;
    }

    /**
     * @param song
     * @return
     */
    public boolean containsSong(final Song song) {
        return songs.contains(song);
    }

    /**
     * @param song
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * @param song
     */
    public void removeSong(final Song song) {
        song.setLoaded(false);
        songs.remove(song);
    }


    /**
     *
     */
    public void removeAlbumAndSongs() {
        songs.removeIf(song -> {
            return false;
        });
    }


    /**
     *
     */
    public void switchVisibility() {
        visibility = (visibility == Enums.Visibility.PUBLIC)
                ? Enums.Visibility.PRIVATE : Enums.Visibility.PUBLIC;
    }


    /**
     *
     */
    public void increaseFollowers() {
        followers++;
    }

    /**
     * @return
     */
    public void decreaseFollowers() {
        followers--;
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
     * @param user
     * @return
     */
    @Override
    public boolean isVisibleToUser(final String user) {
        return getVisibility() == Enums.Visibility.PUBLIC
                || (getVisibility() == Enums.Visibility.PRIVATE && getOwner().equals(user));
    }


    /**
     * @param followers
     * @return
     */
    @Override
    public boolean matchesFollowers(final String followers) {
        return filterByFollowersCount(this.getFollowers(), followers);
    }

    /**
     * @param count
     * @param query
     * @return
     */
    private static boolean filterByFollowersCount(final int count, final String query) {
        int targetCount = Integer.parseInt(query.substring(1));

        switch (query.charAt(0)) {
            case '<':
                return count < targetCount;
            case '>':
                return count > targetCount;
            default:
                return count == targetCount;
        }
    }


    /**
     * @return
     */
    public int getTotalLikes() {
        return songs.stream().mapToInt(Song::getLikes).sum();
    }

}
