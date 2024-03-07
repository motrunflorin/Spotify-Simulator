package app.audio.Files;

import app.Admin;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;
    private final boolean loaded;

    public Song(final String name, final Integer duration, final  String album,
                final ArrayList<String> tags, final String lyrics,
                final String genre, final Integer releaseYear, final String artist) {
        super(name, duration);
        super.getListens();
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
        this.loaded = false;
    }

    @Override
    public boolean matchesAlbum(final String album) {
        return this.getAlbum().equalsIgnoreCase(album);
    }

    @Override
    public boolean matchesTags(final ArrayList<String> tags) {
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : tags) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean matchesLyrics(final String lyrics) {
        return this.getLyrics().toLowerCase().contains(lyrics.toLowerCase());
    }

    @Override
    public boolean matchesGenre(final String genre) {
        return this.getGenre().equalsIgnoreCase(genre);
    }

    @Override
    public boolean matchesArtist(final String artist) {
        return this.getArtist().equalsIgnoreCase(artist);
    }

    @Override
    public boolean matchesReleaseYear(final String releaseYear) {
        return filterByYear(this.getReleaseYear(), releaseYear);
    }

    private static boolean filterByYear(final int year, final String query) {
        int targetYear = Integer.parseInt(query.substring(1));
        return switch (query.charAt(0)) {
            case '<' -> year < targetYear;
            case '>' -> year > targetYear;
            default -> year == targetYear;
        };
    }


    /**
     *
     */
    public void like() {
        likes++;
    }

    /**
     *
     */
    public void dislike() {
        likes--;
    }

    /**
     * @return
     */
    public boolean isPlaying() {
        return Admin.getUsers().stream()
                .map(User::getPlayer)
                .anyMatch(player -> player.getCurrentAudioFile() != null
                        && Admin.isSong(player.getCurrentAudioFile().getName())
                        && player.getCurrentAudioFile().getDuration() > 0
                        && !player.getPaused());
    }


}
