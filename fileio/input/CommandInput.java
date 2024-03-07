package fileio.input;

import java.util.List;

public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type; // song / playlist / podcast // artist // user // host
    private FiltersInput filters; // pentru search
    private Integer itemNumber; // pentru select
    private Integer repeatMode; // pentru repeat
    private Integer playlistId; // pentru add/remove song
    private String playlistName; // pentru create playlist
    private Integer seed; // pentru shuffle

    private int age;
    private String city;
    private String name;
    private int releaseYear;
    private String description;
    private List<SongInput> songs;
    private String userType;
    private String date;
    private int price;
    private List<EpisodeInput> episodes;
    private String nextPage;

    private String recommendationType;


    public CommandInput() {
    }
    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    public FiltersInput getFilters() {
        return filters;
    }

    public void setFilters(final FiltersInput filters) {
        this.filters = filters;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(final Integer repeatMode) {
        this.repeatMode = repeatMode;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(final Integer seed) {
        this.seed = seed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<SongInput> getSongs() {
        return songs;
    }

    public void setSongs(final List<SongInput> songs) {
        this.songs = songs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public List<EpisodeInput> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(final List<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(final String nextPage) {
        this.nextPage = nextPage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(final String userType) {
        this.userType = userType;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + ", age=" + age
                + ", city='" + city + '\''
                + ", name='" + name + '\''
                + ", releaseYear=" + releaseYear
                + ", description='" + description + '\''
                + ", songs=" + songs
                + ", date='" + date + '\''
                + ", price=" + price
                + ", episodes=" + episodes
                + ", nextPage='" + nextPage + '\''
                + '}';
    }
}
