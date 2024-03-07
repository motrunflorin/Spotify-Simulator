package app.searchBar;

import app.audio.LibraryEntry;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class FilterUtils {

    private FilterUtils() {

    }


    /**
     * @param entries
     * @param name
     * @return
     */
    public static List<LibraryEntry> filterByName(final List<LibraryEntry> entries,
                                                  final String name) {
        return entries.stream()
                .filter(entry -> entry.matchesName(name))
                .collect(Collectors.toList());
    }

    /**
     * @param entries
     * @param album
     * @return
     */
    public static List<LibraryEntry> filterByAlbum(final List<LibraryEntry> entries,
                                                   final String album) {
        return filter(entries, entry -> entry.matchesAlbum(album));
    }


    /**
     * @param entries
     * @param tags
     * @return
     */
    public static List<LibraryEntry> filterByTags(final List<LibraryEntry> entries,
                                                  final ArrayList<String> tags) {
        return filter(entries, entry -> entry.matchesTags(tags));
    }

    /**
     * @param entries
     * @param lyrics
     * @return
     */
    public static List<LibraryEntry> filterByLyrics(final List<LibraryEntry> entries,
                                                    final String lyrics) {
        return filter(entries, entry -> entry.matchesLyrics(lyrics));
    }


    /**
     * @param entries
     * @param genre
     * @return
     */
    public static List<LibraryEntry> filterByGenre(final List<LibraryEntry> entries,
                                                   final String genre) {
        return filter(entries, entry -> entry.matchesGenre(genre));
    }

    /**
     * @param entries
     * @param artist
     * @return
     */
    public static List<LibraryEntry> filterByArtist(final List<LibraryEntry> entries,
                                                    final String artist) {
        return filter(entries, entry -> entry.matchesArtist(artist));
    }

    /**
     * @param entries
     * @param releaseYear
     * @return
     */
    public static List<LibraryEntry> filterByReleaseYear(final List<LibraryEntry> entries,
                                                         final String releaseYear) {
        return filter(entries, entry -> entry.matchesReleaseYear(releaseYear));
    }


    /**
     * @param entries
     * @param user
     * @return
     */
    public static List<LibraryEntry> filterByOwner(final List<LibraryEntry> entries,
                                                   final String user) {
        return filter(entries, entry -> entry.matchesOwner(user));
    }


    /**
     * @param entries
     * @param user
     * @return
     */
    public static List<LibraryEntry> filterByPlaylistVisibility(final List<LibraryEntry> entries,
                                                                final String user) {
        return filter(entries, entry -> entry.isVisibleToUser(user));
    }


    /**
     * @param entries
     * @param followers
     * @return
     */
    public static List<LibraryEntry> filterByFollowers(final List<LibraryEntry> entries,
                                                       final String followers) {
        return filter(entries, entry -> entry.matchesFollowers(followers));
    }

    /**
     * @param entries
     * @param criteria
     * @return
     */
    private static List<LibraryEntry> filter(final List<LibraryEntry> entries,
                                             final Predicate<LibraryEntry> criteria) {
        return entries.stream()
                .filter(criteria::test)
                .collect(Collectors.toList());
    }
}
