package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;

import app.user.Artist;

import app.user.Host;
import app.user.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import page.PageType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Admin {

    @Getter
    private static List<User> users = new ArrayList<>();
    @Getter
    private static List<Artist> artists = new ArrayList<>();
    @Getter
    private static List<Host> hosts = new ArrayList<>();
    @Getter
    private static List<Song> songs = new ArrayList<>();
    @Getter
    private static List<Podcast> podcasts = new ArrayList<>();
    @Getter
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;

    private Admin() {
    }

    /**
     * @param userInputList
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = userInputList.stream()
                .map(userInput -> new User(userInput.getUsername(),
                        userInput.getAge(), userInput.getCity()))
                .collect(Collectors.toList());
    }

    /**
     * @param songInputList
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = songInputList.stream()
                .map(songInput -> new Song(
                        songInput.getName(),
                        songInput.getDuration(),
                        songInput.getAlbum(),
                        songInput.getTags(),
                        songInput.getLyrics(),
                        songInput.getGenre(),
                        songInput.getReleaseYear(),
                        songInput.getArtist()))
                .collect(Collectors.toList());
    }

    /**
     * @param podcastInputList
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = podcastInputList.stream()
                .map(podcastInput -> new Podcast(
                        podcastInput.getName(),
                        podcastInput.getOwner(),
                        podcastInput.getEpisodes().stream()
                                .map(episodeInput -> new Episode(
                                        episodeInput.getName(),
                                        episodeInput.getDuration(),
                                        episodeInput.getDescription()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    public static List<Playlist> getPlaylists() {
        return users.stream()
                .flatMap(user -> user.getPlaylists().stream())
                .collect(Collectors.toList());
    }

    /**
     * @param userList
     * @param username
     * @param <T>
     * @return
     */
    public static <T extends User> T getUserByUsername(final List<T> userList,
                                                       final String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param username
     * @return
     */
    public static User getUser(final String username) {
        return getUserByUsername(users, username);
    }

    /**
     * @param username
     * @return
     */
    public static Artist getArtist(final String username) {
        return getUserByUsername(artists, username);
    }

    /**
     * @param username
     * @return
     */
    public static Host getHost(final String username) {
        return getUserByUsername(hosts, username);
    }

    /**
     * @param newTimestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     *
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        hosts = new ArrayList<>();
        artists = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }

    /**
     * @param name
     * @return
     */
    public static String switchConnectionStatus(final String name) {
        User user = getUser(name);

        if (user == null) {
            return "The username " + name + " doesn't exist.";
        }

        if (isHost(name) || isArtist(name)) {
            return name + " is not a normal user.";
        }

        user.switchOnlineStatus();
        return name + " has changed status successfully.";
    }

    /**
     * @return
     */
    public static List<String> getOnlineUsers() {
        return users.stream()
                .filter(user -> user.isOnline() && !isArtist(user.getUsername())
                        && !isHost(user.getUsername()))
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    /**
     * @param name
     * @param type
     * @param age
     * @param city
     * @return
     */
    public static String addUser(final String name, final String type,
                                 final int age, final String city) {
        if (getUser(name) != null) {
            return "The username " + name + " is already taken.";
        }

        switch (type) {
            case "user":
                users.add(new User(name, age, city));
                break;
            case "artist":
                if (getArtist(name) == null) {
                    Artist newArtist = new Artist(name, age, city);
                    artists.add(newArtist);
                    users.add(newArtist);
                }
                break;
            case "host":
                if (getHost(name) == null) {
                    Host newHost = new Host(name, age, city);
                    hosts.add(newHost);
                    users.add(newHost);
                }
                break;
            default:
                return "Invalid user type: " + type;
        }

        return "The username " + name + " has been added successfully.";
    }

    /**
     * @param username
     * @param name
     * @param releaseYear
     * @param description
     * @param songInputs
     * @return
     */
    public static String addAlbum(final String username, final String name, final int releaseYear,
                                  final String description, final List<SongInput> songInputs) {

        Artist artist = getArtist(username);

        if (artist == null) {
            return username + " is not an artist.";
        }

        // Check if the artist already has an album with the same name
        if (artist.getAlbumByName(name) != null) {
            return username + " has another album with the same name.";
        }

        // Convert SongInput objects to Song objects
        List<Song> songs = songInputs.stream()
                .map(songInput -> new Song(songInput.getName(), songInput.getDuration(),
                        songInput.getAlbum(), songInput.getTags(), songInput.getLyrics(),
                        songInput.getGenre(), songInput.getReleaseYear(), songInput.getArtist()))
                .peek(newSong -> Admin.songs.add(newSong))
                .collect(Collectors.toList());

        // Create and add the new album to the artist's albums
        Album newAlbum = new Album(name, releaseYear, description, songs, username);

        if (artist.hasDuplicateSongs(newAlbum)) {
            return username + " has the same song at least twice in this album.";
        }

        artist.addAlbum(newAlbum);

        // Add the new album to the Admin's albums list
        Admin.albums.add(newAlbum);

        return username + " has added new album successfully.";
    }

    /**
     * @param username
     * @return
     */
    public static List<Map<String, Object>> showAlbums(final String username) {
        Artist artist = getArtist(username);

        if (artist == null) {
            return new ArrayList<>();
        }

        return artist.getAlbums().stream()
                .map(album -> {
                    Map<String, Object> albumInfo = new LinkedHashMap<>();
                    albumInfo.put("name", album.getName());
                    albumInfo.put("songs", album.getSongs().stream().map(Song::getName)
                            .collect(Collectors.toList()));
                    return albumInfo;
                })
                .collect(Collectors.toList());
    }

    /**
     * @param username
     * @param name
     * @param description
     * @param date
     * @return
     */
    public static String addEvent(final String username, final String name,
                                  final String description, final String date) {
        // Check if the username exists
        User existingUser = getUser(username);

        if (existingUser == null) {
            return "The username " + username + " doesn't exist.";
        }

        // Check if the user is an artist
        if (!isArtist(username)) {
            return username + " is not an artist.";
        }

        Artist artist = (Artist) existingUser;

        return artist.addEvent(name, description, date);
    }

    /**
     * @param username
     * @param name
     * @return
     */
    public static String removeEvent(final String username, final String name) {
        // Check if the username exists
        User existingUser = getUser(username);

        if (existingUser == null) {
            return "The username " + username + " doesn't exist.";
        }

        // Check if the user is an artist
        if (!isArtist(username)) {
            return username + " is not an artist.";
        }

        Artist artist = (Artist) existingUser;

        return artist.removeEvent(name);
    }

    /**
     * @param username
     * @param name
     * @param description
     * @param price
     * @return
     */
    public static String addMerch(final String username, final String name,
                                  final String description, final int price) {
        // Check if the username exists
        User existingUser = getUser(username);

        if (existingUser == null) {
            return "The username " + username + " doesn't exist.";
        }

        // Check if the user is an artist
        if (!isArtist(username)) {
            return username + " is not an artist.";
        }

        Artist artist = (Artist) existingUser;


        return artist.addMerch(name, description, price);
    }

    /**
     * @param username
     * @return
     */
    public static String deleteUser(final String username, final int timestamp) {
        // Find the user by username
        User userToDelete = getUser(username);

        // Check if the user exists
        if (userToDelete == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (userToDelete.hasSongPlaying()) {
            return username + " can't be deleted.";
        }

        // Remove the user from the appropriate list based on their type
        if (isArtist(username)) {
            Artist artist = (Artist) userToDelete;

            // Check if his page is visualized
            if (getUsers().stream().anyMatch(user ->
                    !user.getUsername().equals(username)
                            && user.getOwnerpage().equals(username))) {
                updateTimestamp(timestamp);
                return username + " can't be deleted.";
            }

            // Remove the artist's albums and songs
            artist.getAlbums().forEach(album -> {
                // Remove album from admin and other users
                getAlbums().remove(album);

                // Remove album from other users' playlists
                getPlaylists().forEach(Playlist::removeAlbumAndSongs);

                // Remove songs from admin and other users
                album.getSongs().forEach(song -> {
                    getSongs().remove(song);

                    // Remove songs from other users' playlists
                    getPlaylists().forEach(pl -> pl.removeSong(song));

                    // Remove the song from the liked songs of all users
                    getUsers().forEach(u -> u.removeLikedSong(song));
                });
            });

            getArtists().remove(artist);
        } else if (isHost(username)) {
            // Check if his page is visualized
            if (getUsers().stream().anyMatch(user ->
                    !user.getUsername().equals(username) && user.getOwnerpage().equals(username))) {
                return username + " can't be deleted.";
            }

            // Check if any loaded podcast is owned by the host
            if (getPodcasts().stream().anyMatch(podcast ->
                    podcast.isLoaded() && podcast.getOwner().equals(username))) {
                return username + " can't be deleted.";
            }

            getHosts().remove((Host) userToDelete);
        } else { // User is a regular user
            // Remove the user's playlists
            userToDelete.getPlaylists().forEach(playlist -> {
                // Remove playlist from other users
                getUsers().stream()
                        .filter(otherUser -> !otherUser.equals(userToDelete))
                        .forEach(otherUser -> otherUser.getFollowedPlaylists().remove(playlist));
            });

            // Decrease the followers count for the playlists that the user has followed
            userToDelete.getFollowedPlaylists().forEach(Playlist::decreaseFollowers);
        }

        users.remove(userToDelete);

        return username + " was successfully deleted.";
    }

    /**
     * @return
     */
    public static List<String> getAllUsers() {
        List<String> allUserNames = new ArrayList<>();

        // Add normal users to the list
        for (User user : users) {
            if (!isArtist(user.getUsername()) && !isHost(user.getUsername())) {
                allUserNames.add(user.getUsername());
            }
        }

        // Add artists to the list
        for (User user : users) {
            if (isArtist(user.getUsername())) {
                allUserNames.add(user.getUsername());
            }
        }

        // Add hosts to the list
        for (User user : users) {
            if (isHost(user.getUsername())) {
                allUserNames.add(user.getUsername());
            }
        }

        return allUserNames;
    }

    /**
     * @param username
     * @param name
     * @param episodeInputs
     * @return
     */
    public static String addPodcast(final String username, final String name,
                                    final List<EpisodeInput> episodeInputs) {

        User user = getUser(username);

        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }

        Host host = getHost(username);

        if (host == null) {
            return  username + " is not a host.";
        }

        // Check if the host already has a podcast with the same name
        Podcast existingPodcast = host.getPodcastByName(name);
        if (existingPodcast != null) {
            return username + " has another podcast with the same name.";
        }

        // Convert episodeInput objects to Episode objects
        List<Episode> episodes = episodeInputs.stream()
                .map(input -> new Episode(input.getName(), input.getDuration(),
                        input.getDescription()))
                .collect(Collectors.toList());


        // Create and add the new podcast to the host's podcasts
        Podcast newPodcast = new Podcast(name, username, episodes);
        host.addPodcast(newPodcast);

        // Add the new podcast to the Admin's podcasts list
        Admin.podcasts.add(newPodcast);

        if (host.hasDuplicateEpisodes(newPodcast)) {
            return username + " has the same episode at least twice in this podcast.";
        }

        return username + " has added new podcast successfully.";
    }

    /**
     * @param username
     * @param name
     * @return
     */
    public static String removePodcast(final String username, final String name) {
        User existingUser = getUser(username);

        if (existingUser == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (!isHost(username)) {
            return username + " is not a host.";
        }

        Host host = (Host) existingUser;
        Podcast podcastToRemove = findPodcastToRemove(host, name);

        if (podcastToRemove == null) {
            return username + " doesn't have a podcast with the given name.";
        }

        if (podcastToRemove.isLoaded()) {
            return username + " can't delete this podcast.";
        }

        removePodcast(host, podcastToRemove);

        return username + " deleted the podcast successfully.";
    }

    /**
     * @param host
     * @param name
     * @return
     */
    private static Podcast findPodcastToRemove(final Host host, final String name) {
        return host.getPodcasts().stream()
                .filter(podcast -> podcast.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param host
     * @param podcast
     */
    private static void removePodcast(final Host host, final Podcast podcast) {
        Admin.getPodcasts().remove(podcast);
        host.getPodcasts().remove(podcast);
    }

    /**
     * @param username
     * @param name
     * @param description
     * @return
     */
    public static String addAnnouncement(final String username, final String name,
                                         final String description) {
        User existingUser = getUser(username);

        if (existingUser == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (!isHost(username)) {
            return username + " is not a host.";
        }

        Host host = (Host) existingUser;
        return host.addAnnouncement(name, description);
    }

    /**
     * @param username
     * @param name
     * @return
     */
    public static String removeAnnouncement(final String username, final String name) {
        User existingUser = getUser(username);

        if (existingUser == null) {
            return "The username " + username + " doesn't exist.";
        }

        // Check if the user is a host
        if (!isHost(username)) {
            return username + " is not a host.";
        }

        Host host = (Host) existingUser;

        return host.removeAnnouncement(name);
    }

    /**
     * @param username
     * @return
     */
    public static List<Map<String, Object>> showPodcasts(final String username) {
        Host host = getHost(username);
        if (host == null) {
            return new ArrayList<>();
        }

        return mapPodcastsToInfo(host.getPodcasts());
    }

    /**
     * @param podcasts
     * @return
     */
    private static List<Map<String, Object>> mapPodcastsToInfo(final List<Podcast> podcasts) {
        return podcasts.stream()
                .map(podcast -> {
                    Map<String, Object> podcastInfo = new LinkedHashMap<>();
                    podcastInfo.put("name", podcast.getName());
                    podcastInfo.put("episodes",
                            podcast.getEpisodes().stream().map(Episode::getName)
                                    .collect(Collectors.toList()));
                    return podcastInfo;
                })
                .collect(Collectors.toList());
    }

    /**
     * @param username
     * @param albumName
     * @return
     */
    public static String removeAlbum(final String username, final String albumName) {
        User existingUser = getUser(username);

        if (existingUser == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (!isArtist(username)) {
            return username + " is not an artist.";
        }

        Artist artist = getArtist(username);

        Album album = artist.getAlbumByName(albumName);

        if (album == null) {
            return username + " doesn't have an album with the given name.";
        }

        if (album.getSongs().stream().anyMatch(Song::isLoaded)
                || isAlbumUsedInPlaylists(album, username)
                || isAnyAlbumLoaded(artist)) {
            return username + " can't delete this album.";
        }

        artist.getAlbums().remove(album);
        albums.remove(album);

        return username + " deleted the album successfully.";
    }

    /**
     * @param album
     * @param artistUsername
     * @return
     */
    private static boolean isAlbumUsedInPlaylists(final Album album, final String artistUsername) {
        return album.getSongs().stream()
                .anyMatch(song -> getPlaylists().stream()
                        .anyMatch(playlist -> playlist.containsSong(song)
                                && !playlist.getOwner().equals(artistUsername)));
    }

    /**
     * @param artist
     * @return
     */
    private static boolean isAnyAlbumLoaded(final Artist artist) {
        return artist.getAlbums().stream().anyMatch(Album::isLoaded);
    }


    /**
     * @param username
     * @param pageTypeString
     * @return
     */
    public static String changePage(final String username, final String pageTypeString) {
        User user = getUser(username);

        if (isArtist(username) || isHost(username)) {
            return username + " is trying to access a non-existent page.";
        }

        try {
            PageType pageType = PageType.fromString(pageTypeString);
            user.setCurrentPageType(pageType);
            user.setOwnerpage(username);
            return username + " accessed " + pageType + " successfully.";
        } catch (IllegalArgumentException e) {
            return username + " is trying to access a non-existent page.";
        }
    }

    /**
     * @param username
     * @return
     */
    public static boolean isHost(final String username) {
        return hosts.stream().anyMatch(host -> host.getUsername().equals(username));
    }

    /**
     * @param username
     * @return
     */
    public static boolean isArtist(final String username) {
        return artists.stream().anyMatch(artist -> artist.getUsername().equals(username));
    }

    /**
     * @param username
     * @return
     */
    public static boolean isUser(final String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * @param name
     * @return
     */
    public static boolean isSong(final String name) {
        return songs.stream().anyMatch(song -> song.getName().equals(name));
    }

    public static String wrapped(final String name) {
        if (isHost(name)) {
            Host host = getHost(name);
            return host.tops();
        }

        return "something else"; 
    }





}
