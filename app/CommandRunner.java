package app;


import app.audio.Collections.PlaylistOutput;
import app.player.PlayerStats;
import app.searchBar.Filters;

import app.user.User;
import app.utils.statistics.StatisticsContext;
import app.utils.statistics.TopAlbumsStrategy;
import app.utils.statistics.TopArtistsStrategy;
import app.utils.statistics.TopPlaylistsStrategy;
import app.utils.statistics.TopSongsStrategy;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import page.PageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CommandRunner {
   public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;
    private CommandRunner() {

    }

    /**
     * @param inputCommand
     * @param message
     * @return
     */
    private static ObjectNode createResponseNode(final CommandInput inputCommand,
                                                 final String message) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode search(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        ObjectNode responseNode = JSON_NODE_FACTORY.objectNode();
        responseNode.put("command", inputCommand.getCommand());
        responseNode.put("user", inputCommand.getUsername());
        responseNode.put("timestamp", inputCommand.getTimestamp());

        if (user == null) {
            String errorMessage = "User not found: " + inputCommand.getUsername();
            responseNode.put("message", errorMessage);
            responseNode.putArray("results");
        } else if (!user.isOnline()) {
            String offlineMessage = user.getUsername() + " is offline.";
            responseNode.put("message", offlineMessage);
            responseNode.putArray("results");
        } else {
            Filters filters = new Filters(inputCommand.getFilters());
            String type = inputCommand.getType();
            ArrayList<String> results = user.search(filters, type);
            String message = "Search returned " + results.size() + " results";

            responseNode.put("message", message);
            ArrayNode resultsArray = JSON_NODE_FACTORY.arrayNode();
            results.forEach(result -> resultsArray.add(result));
            responseNode.put("results", resultsArray);
        }

        return responseNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode select(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.select(inputCommand.getItemNumber());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode load(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.load();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode playPause(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.playPause();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode repeat(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.repeat();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode shuffle(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        Integer seed = inputCommand.getSeed();
        String message = user.shuffle(seed);
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode forward(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.forward();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode backward(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.backward();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode like(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        ObjectNode responseNode = OBJECT_MAPPER.createObjectNode();
        responseNode.put("command", inputCommand.getCommand());
        responseNode.put("user", inputCommand.getUsername());
        responseNode.put("timestamp", inputCommand.getTimestamp());

        // Check if the user is offline
        if (!user.isOnline()) {
            String offlineMessage = user.getUsername() + " is offline.";
            responseNode.put("message", offlineMessage);
        } else {
            String message = user.like();
            responseNode.put("message", message);
        }

        return responseNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode next(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.next();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode prev(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.prev();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode createPlaylist(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.createPlaylist(inputCommand.getPlaylistName(),
                inputCommand.getTimestamp());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.addRemoveInPlaylist(inputCommand.getPlaylistId());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode switchVisibility(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.switchPlaylistVisibility(inputCommand.getPlaylistId());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode showPlaylists(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(playlists));
        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode follow(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());
        String message = user.follow();
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode status(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());

        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("stats", OBJECT_MAPPER.valueToTree(stats));

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode showLikedSongs(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());

        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(songs));

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode getPreferredGenre(final CommandInput inputCommand) {
        User user = Admin.getUser(inputCommand.getUsername());

        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode getTop5Songs(final CommandInput inputCommand) {
        List<String> songs = new StatisticsContext<>(new TopSongsStrategy())
                .getTopList(Admin.getSongs());

        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", JsonNodeFactory.instance.arrayNode().addAll(songs.stream()
                .map(JsonNodeFactory.instance::textNode)
                .collect(Collectors.toList())));

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode getTop5Playlists(final CommandInput inputCommand) {
        List<String> playlists = new StatisticsContext<>(new TopPlaylistsStrategy())
                .getTopList(Admin.getPlaylists());

        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", JsonNodeFactory.instance.arrayNode().addAll(playlists.stream()
                .map(JsonNodeFactory.instance::textNode)
                .collect(Collectors.toList())));

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     *
     */
    public static ObjectNode switchConnectionStatus(final CommandInput inputCommand) {
        String message = Admin.switchConnectionStatus(inputCommand.getUsername());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode addUser(final CommandInput inputCommand) {
        String message = Admin.addUser(inputCommand.getUsername(), inputCommand.getType(),
                inputCommand.getAge(), inputCommand.getCity());

        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode addAlbum(final CommandInput inputCommand) {
        String message = Admin.addAlbum(inputCommand.getUsername(), inputCommand.getName(),
                inputCommand.getReleaseYear(),
                inputCommand.getDescription(), inputCommand.getSongs());

        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode showAlbums(final CommandInput inputCommand) {
        List<Map<String, Object>> albumsAndSongs = Admin.showAlbums(inputCommand.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(albumsAndSongs));

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode printCurrentPage(final CommandInput inputCommand) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("timestamp", inputCommand.getTimestamp());

        User user = Admin.getUser(inputCommand.getUsername());

        if (user.isOnline()) {

            String currentPageContent = PageManager.printCurrentPage(inputCommand.getUsername());

            if (currentPageContent != null) {
                objectNode.put("message", currentPageContent);
            }

        } else {
            objectNode.put("message", user.getUsername() + " is offline.");
        }

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode addEvent(final CommandInput inputCommand) {
        String message = Admin.addEvent(inputCommand.getUsername(), inputCommand.getName(),
                inputCommand.getDescription(), inputCommand.getDate());

        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode addMerch(final CommandInput inputCommand) {
        String message = Admin.addMerch(inputCommand.getUsername(), inputCommand.getName(),
                inputCommand.getDescription(), inputCommand.getPrice());

        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode deleteUser(final CommandInput inputCommand) {
        String message = Admin.deleteUser(inputCommand.getUsername(), inputCommand.getTimestamp());
       
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode addPodcast(final CommandInput inputCommand) {
        String message = Admin.addPodcast(inputCommand.getUsername(), inputCommand.getName(),
                inputCommand.getEpisodes());
       
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode showPodcasts(final CommandInput inputCommand) {
        List<Map<String, Object>> podcasts = Admin.showPodcasts(inputCommand.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(podcasts));

        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode removePodcast(final CommandInput inputCommand) {
        String message = Admin.removePodcast(inputCommand.getUsername(), inputCommand.getName());
       
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode changePage(final CommandInput inputCommand) {
        String message = Admin.changePage(inputCommand.getUsername(), inputCommand.getNextPage());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode addAnnouncement(final CommandInput inputCommand) {
        String message = Admin.addAnnouncement(inputCommand.getUsername(),
                inputCommand.getName(),
                inputCommand.getDescription());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode removeAnnouncement(final CommandInput inputCommand) {
        String message = Admin.removeAnnouncement(inputCommand.getUsername(),
                inputCommand.getName());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode removeAlbum(final CommandInput inputCommand) {
        String message = Admin.removeAlbum(inputCommand.getUsername(), inputCommand.getName());
        return createResponseNode(inputCommand, message);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode removeEvent(final CommandInput inputCommand) {
        String message = Admin.removeEvent(inputCommand.getUsername(), inputCommand.getName());
        return createResponseNode(inputCommand, message);
    }

    private static ObjectNode generateResultForAdmin(final CommandInput inputCommand, List<String> data) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(data));
        return objectNode;
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode getTop5Albums(final CommandInput inputCommand) {
        List<String> albums = new StatisticsContext<>(new TopAlbumsStrategy())
                .getTopList(Admin.getAlbums());

        return generateResultForAdmin(inputCommand, albums);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode getTop5Artists(final CommandInput inputCommand) {
        List<String> artists = new StatisticsContext<>(new TopArtistsStrategy())
                .getTopList(Admin.getArtists());
       
        return generateResultForAdmin(inputCommand, artists);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode getAllUsers(final CommandInput inputCommand) {
        List<String> users = Admin.getAllUsers();
       
        return generateResultForAdmin(inputCommand, users);
    }

    /**
     * @param inputCommand
     * @return
     */
    public static ObjectNode getOnlineUsers(final CommandInput inputCommand) {
        List<String> onlineUsers = Admin.getOnlineUsers();
       
        return generateResultForAdmin(inputCommand, onlineUsers);
    }

    private static ObjectNode generateResultForUser(final CommandInput inputCommand, String result) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", result);

        return objectNode;
    }

    public static ObjectNode wrapped(final CommandInput inputCommand) {
        String result = Admin.wrapped(inputCommand.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", inputCommand.getCommand());
        objectNode.put("user", inputCommand.getUsername());
        objectNode.put("timestamp", inputCommand.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(result));

        return objectNode;
    }
}
