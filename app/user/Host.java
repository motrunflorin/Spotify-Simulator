package app.user;

import app.CommandRunner;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.LibraryEntry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Host extends User {
    private  ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;

    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        this.podcasts = new ArrayList<>();
    }

    /**
     * @param podcast
     */
    public String addPodcast(final Podcast podcast) {
        if (hasPodcastWithName(podcast.getName())) {
            return getUsername() + " has another podcast with the same name.";
        }

        podcasts.add(podcast);

        return getUsername() + " has added new podcast successfully.";
    }

    /**
     * @param name
     * @return
     */
    public String removePodcast(final String name) {
        boolean removed = podcasts.removeIf(podcast -> podcast.getName().equals(name));
        return removed
                ? getUsername() + " deleted the podcast successfully."
                : getUsername() + " has no podcast with the given name.";
    }

    /**
     * @param podcastName
     * @return
     */
    public Podcast getPodcastByName(final String podcastName) {
        return podcasts.stream()
                .filter(podcast -> podcast.getName().equals(podcastName))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return
     */
    public List<String> showPodcasts() {
        return podcasts.stream()
                .map(Podcast::getName)
                .collect(Collectors.toList());
    }

    /**
     * @param name
     * @param announcementDescription
     * @return
     */
    public String addAnnouncement(final String name, final String announcementDescription) {
        // Initialize the announcements list if it's not initialized
        if (announcements == null) {
            announcements = new ArrayList<>();
        }

            if (hasDuplicateAnnouncements()) {
                return getUsername() + " has already added an announcement with this name";
            }

        // Add the announcement
        announcements.add(new Announcement(name, announcementDescription));
        return getUsername() + " has successfully added new announcement.";

    }

    /**
     * @param name
     * @return
     */
    public String removeAnnouncement(final String name) {
        boolean removed = announcements.removeIf(announcement ->
                announcement.getName().equals(name));
        return removed
                ? getUsername() + " has successfully deleted the announcement."
                : getUsername() + " has no announcement with the given name.";
    }

    /**
     * @param podcast
     * @return
     */
    public boolean hasDuplicateEpisodes(final Podcast podcast) {
        List<String> episodeNames = podcast.getEpisodes().stream()
                .map(LibraryEntry::getName)
                .toList();

        return episodeNames.size() != episodeNames.stream().distinct().count();
    }

    /**
     * @param podcastName
     * @return
     */
    public boolean hasPodcastWithName(final String podcastName) {
        return podcasts.stream().anyMatch(podcast -> podcast.getName().equals(podcastName));
    }

    /**
     * @return
     */
    public boolean hasDuplicateAnnouncements() {
        if (announcements == null) {
            return false;
        }

        List<String> announcementNames = announcements.stream()
                .map(Announcement::getName)
                .toList();

        return announcementNames.size() != announcementNames.stream().distinct().count();
    }

    public List<Episode> getTopEpisodes() {
        // Get all episodes from all podcasts
        return podcasts.stream()
                .flatMap(podcast -> podcast.getEpisodes().stream())
                .toList();
    }

    public String tops() {
        // Call getTopEpisodes to get the list of all episodes
        List<Episode> allEpisodes = getTopEpisodes();

        // Create the topEpisodes part of the result
        String topEpisodesString = allEpisodes.stream()
                .map(episode -> String.format("%s : %d", episode.getName(), episode.getListens()))
                .collect(Collectors.joining(", ", "topEpisodes : {", "}"));

        // Get the number of listeners (assume all listens are unique users)
        long listenersCount = allEpisodes.stream().map(Episode::getListens).distinct().count();

        // Create the listeners part of the result
        String listenersString = "listeners : " + listenersCount;

        // Create the podcasts part of the result
        String podcastsString = showPodcasts().stream()
                .collect(Collectors.joining(", ", "podcasts : {", "}"));

        // Combine the strings to form the final result
        return "{" + topEpisodesString + "," + listenersString + "," + podcastsString + "}";
    }

}
