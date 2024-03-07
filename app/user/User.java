package app.user;


import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;

import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import lombok.Getter;
import lombok.Setter;
import page.PageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class User extends UserFile {
    private final String username = super.getName();
    private final int age;
    private final String city;
    private final ArrayList<Playlist> playlists;
    private final ArrayList<Song> likedSongs;
    private final ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private boolean online;;
    @Setter
    private PageType currentPageType;
    @Setter
    private String ownerpage;
    private LibraryEntry lastSelected;
    private LibraryEntry lastLoaded;
    private final ArrayList<Episode> episodes;

    /**
     * @param username
     * @param age
     * @param city
     */
    public User(final String username, final int age, final String city) {
        super(username);
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        online = true;
        this.currentPageType = PageType.HOME;
        this.lastSelected = null;
        this.ownerpage = username;
        this.episodes = new ArrayList<>();
        this.lastLoaded = null;
    }

    /**
     * @param filters
     * @param type
     * @return
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        return searchBar.search(filters, type)
                .stream()
                .map(LibraryEntry::getName)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * @param itemNumber
     * @return
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;
        lastSelected = searchBar.select(itemNumber);

        if (lastSelected == null) {
            return "The selected ID is too high.";
        }

        String userType = (Admin.isArtist(lastSelected.getName())) ? "ARTIST"
                : (Admin.isHost(lastSelected.getName())) ? "HOST" : "";

        if (!userType.isEmpty()) {
            currentPageType = PageType.valueOf(userType);
            if (!(lastSelected instanceof Album)) {
                ownerpage = ((User) lastSelected).getUsername();
            }
            return "Successfully selected %s's page.".formatted(lastSelected.getName());
        }

        return "Successfully selected %s.".formatted(lastSelected.getName());
    }


    /**
     * @return
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && !searchBar.getLastSearchType().equals("album")
                && !searchBar.getLastSearchType().equals("podcast")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        handleLoadedState();

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());

        searchBar.clearSelection();

        player.pause();

        lastLoaded = searchBar.getLastSelected();

        return "Playback loaded successfully.";
    }


    /**
     *
     */
    public void handleLoadedState() {
        getLastSelected().setLoaded(true);

        boolean ok = false;
        for (User user : Admin.getUsers()) {
            if (!user.getName().equals(getName())) {
                if (user.lastLoaded != null
                        && user.lastLoaded.getName().equals(lastLoaded.getName())) {
                    ok = true;
                }
            }
        }

        if (!ok && lastLoaded != null) {
            lastLoaded.setLoaded(false);
        }
    }

    /**
     * @return
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * @return
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = switch (repeatMode) {
            case NO_REPEAT -> "no repeat";
            case REPEAT_ONCE -> "repeat once";
            case REPEAT_ALL -> "repeat all";
            case REPEAT_INFINITE -> "repeat infinite";
            case REPEAT_CURRENT_SONG -> "repeat current song";
        };

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * @param seed
     * @return
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!("playlist".equals(player.getType()) || "album".equals(player.getType()))) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }

        return "Shuffle function deactivated successfully.";
    }

    /**
     * @return
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * @return
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }
        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * @return
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song")
                && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            //maybe add album/playlist to return
            return "Loaded source is not a song /album /playlist.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();

        return "Like registered successfully.";
    }

    /**
     * @return
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }
        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return ("Skipped to next track successfully. "
                + "The current track is %s.").formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * @return
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return ("Returned to previous track successfully. "
                + "The current track is %s.").formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * @param name
     * @param timestamp
     * @return
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * @param id
     * @return
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * @param playlistId
     * @return
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * @return
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * @return
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * @return
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * @return
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * @return
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};

        Map<String, Long> genreCounts = likedSongs.stream()
                .filter(song -> Arrays.asList(genres).contains(song.getGenre()))
                .collect(Collectors.groupingBy(Song::getGenre, Collectors.counting()));

        String preferredGenre = genreCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("unknown");

        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }


    /**
     *
     */
    public void switchOnlineStatus() {
        online = !online;
        player.pause();
    }

    /**
     * @param time
     */
    public void simulateTime(final int time) {
        if (online) {
            player.simulatePlayer(time);
        } else {
            player.pause();
        }
    }

    /**
     * @param song
     */
    public void removeLikedSong(final Song song) {
        likedSongs.remove(song);
    }

    /**
     * @return
     */
    public boolean hasSongPlaying() {
        // Iterate over all users
        for (User user : Admin.getUsers()) {
            // Check if the user's player has a current song playing
            if (user.getPlayer().getCurrentAudioFile() != null) {
                if (Admin.isSong(user.getPlayer().getCurrentAudioFile().getName())) {
                    Song currentSong = (Song) user.getPlayer().getCurrentAudioFile();
                    // Check if the current song matches any song in playlists
                    for (Playlist playlist : getPlaylists()) {
                        for (Song song : playlist.getSongs()) {
                            if (currentSong.isPlaying()) {
                                if (song.getName().equals(currentSong.getName())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
