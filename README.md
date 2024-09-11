# Project: Spotify-like Application

Package page should be in package app (I didn't realised at that time).
Folders stage1 and stage2 have the inputs, checkers and refs for each stage, maybe should be renamed as etapa1 and etapa2 when used.

# STAGE 1

## Overview
This stage involves implementing a Spotify-like application that simulates user interactions with the platform. Actions are simulated via commands from input files. You act as an admin overseeing all user activities, generating reports, and simulating real-time actions on the platform.

## Entities

### Audio File
The application works with two types of audio files:
- Song
- Podcast Episode

Each file type provides different interaction capabilities for the user.

Example input for a song:
```json
{
      "name": "Shape of You",
      "duration": 233,
      "album": "Divide",
      "tags": [
       "#pop",
       "#mostlistenedthisyear",
       "#spotify"
      ],
      "lyrics": "The club isn't the best place to find a lover, So the bar is where I go (mm-mm)",
      "genre": "Pop",
      "releaseYear": 2017,
      "artist": "Ed Sheeran"
}
```
Example input for a podcast episode:
```json
{
    "name": "Elon Musk Returns",
    "duration": 11927,
    "description": "Elon Musk, CEO of SpaceX and Tesla, returns to discuss various topics."
```
### Audio File Collections
There are three types of audio collections:

- library
- playlist
- podcast

The Library contains all songs available on the platform, accessible to all users.

Playlists are user-created collections of songs and can be either public or private. They contain audio files from the library and are based on user preferences.

A podcast is a collection of several related episodes. All podcasts will be specified in the input file and will be available in the library from the start of the simulation. The episodes are ordered according to the initial position specified in the input.

Example input for a podcast:
```json
{
    "name": "The Joe Rogan Experience",
    "owner": "Joe Rogan",
    "episodes": [
      {
        "name": "Elon Musk Returns",
        "duration": 11927,
        "description": "Elon Musk, CEO of SpaceX and Tesla, returns to discuss various topics."
      },
      {
        "name": "Jordan Peterson",
        "duration": 9916,
        "description": "Dr. Jordan Peterson joins Joe to discuss psychology, philosophy, and current events."
      },
      {
        "name": "Neil deGrasse Tyson",
        "duration": 11276,
        "description": "Astrophysicist Neil deGrasse Tyson talks about the universe and science."
      },
      {
        "name": "Elon Musk on Mars",
        "duration": 10533,
        "description": "Elon Musk shares his vision for a human settlement on Mars."
      },
      {
        "name": "The Art of MMA",
        "duration": 8742,
        "description": "Joe Rogan and guests discuss the art and science of mixed martial arts."
      }
   ]
}
```
### Search bar

The search bar is used to search the library for a specific song, playlist, or podcast based on various filters:

      - Songs:
            - by name → Checks if the song's name starts with the specified text in the filter.
            - by album → Checks if the song belongs to the specified album.
            - by tags → Checks if the song has all the specified tags from a given list.
            - by lyrics → Checks if the song contains the specified word or phrase.
            - by genre → Checks if the song belongs to the specified musical genre.
            - by release year → A string, e.g., “<2000”, “>2000”, checks if the release year is less than or greater than the specified year.
            - by artist → Checks if the song was created by the specified artist.
      - Playlists:
            - by name → Checks if the playlist's name starts with the specified text.
            - by owner → Checks if the playlist was created by the specified user.
      - Podcast:
            - by name → Checks if the podcast's name starts with the specified text.
            - by owner → Checks if the podcast was created by the specified creator.

For each type of search, at least one field will be specified. The order in which results are obtained depends on the position of the content in the list of those elements (i.e., in the list of songs, the list of podcasts, or the lists of playlists for each user).

### Music player

To play different audio files, we need a player. It can play songs from the library or from a playlist, running them sequentially. Podcasts are initially played from the first episode, with episodes played one after another. Each time playback returns to a podcast, it resumes from the episode and the exact moment within the episode where it last left off. Episodes are specified in order in the input.

### User

Our platform has multiple users. At this stage, they can interact with the search bar, the player, and create and manage playlists. Each user has a unique username, so for each command related to a user, their username will be specified.

Example input for a user:
```json
{
    "username": "alice22",
    "age": 28,
    "city": "Los Angeles"
}
```
#### Timestamp
      
To simulate the real-time aspect of the application, commands have a field called "timestamp" that indicates the second at which they were executed relative to the start of the test (time t0). Each command is executed instantaneously at a specific moment in time. The time is shared among all users and progresses uniformly, regardless of the commands received. Thus, it is not possible to have a command with timestamp "30" for "user1" followed by a command with timestamp "20" for "user2" (a command with timestamp "t" means that all users are at time "t"). Additionally, the simulation must account for what happens between these time moments, such as the state of the current user's player (which track it is on, whether playback has stopped, etc.).

## Seach bar commands
### Search
Searches are performed based on filters to find a song, playlist, or podcast. Songs are searched within the library, and playlists are only accessible if they belong to the user who issued the command or are public. A list of the top 5 results is returned, and if there are fewer than 5 results, all obtained results are returned. Filters can vary from one search command to another (some fields may be missing), but each search command must specify at least one filter. Additionally, searches are performed from the perspective of the user, meaning that two users can have different results when searching for the same item.

> **Note:** When this command is executed, the source that was loaded in the player is removed. Therefore, after a search, regardless of the results returned, the player will not play anything.

Example input for search song:
```json
{
    "command": "search",
    "username": "alice22",
    "timestamp": 10,
    "type": "song",
    "filters": {
      "name": "Sta"
    }
}
```
Example input for search playlist:
```json
{
    "command": "search",
    "username": "alice22",
    "timestamp": 63,
    "type": "playlist",
    "filters": {
      "owner": "alice22"
    }
}
```
Example input for search podcast:
```json
{
    "command": "search",
    "username": "alice22",
    "timestamp": 100,
    "type": "podcast",
    "filters": {
      "name": "The"
    }
}
```
Example output for search:
```json
{
    "command" : "search",
    "user" : "alice22",
    "timestamp" : 10,
    "message" : "Search returned 3 results",
    "results" : [ "Stairway to Heaven", "Start Me Up", "Stargazing" ]
}
```
### Select
Select from list one of the options obtained from the last search if it generated at least one result. Additionally, if a result with an index higher than the last result is selected, an error is returned. The first result has an index of 1.

#### Possible messages
      1.Successfully selected {track_name}.
      2.Please conduct a search before making a selection.
      3.The selected ID is too high.

Example input for select:
```json
{
    "command": "select",
    "username": "alice22",
    "timestamp": 15,
    "itemNumber": 1
}
```
Example output for select:
```json
{
    "command" : "select",
    "user" : "alice22",
    "timestamp" : 15,
    "message" : "Successfully selected Stairway to Heaven."
}
```
## Player commands
### Load
This command can be issued immediately after selecting a song, playlist, or podcast. It plays the playlist or song from the beginning, and in the case of a podcast, it resumes from where it left off in the last played episode.

#### Possible messages
      1.Playback loaded successfully.
      2.Please select a source before attempting to load.
      3.You can't load an empty audio collection!
      
Example input for load:
```json
{
    "command": "load",
    "username": "alice22",
    "timestamp": 20
}
```
Example output for load:
```json
{
   "command" : "load",
   "user" : "alice22",
   "timestamp" : 20,
   "message" : "Playback loaded successfully."
}
```
> **Note:** This command can only be executed after the user has issued the select command.

### PlayPause

This command toggles the player between play and pause states. If the player is currently in play mode, the playPause command will switch it to pause mode. Conversely, if the player is in pause mode, the playPause command will switch it to play mode.

#### Possible messages
      1.Playback paused successfully.
      2.Playback resumed successfully.
      3.Please load a source before attempting to pause or resume playback.
      
Example input for playPause:
```json
{
    "command": "playPause",
    "username": "alice22",
    "timestamp": 30
}
```
Example output for playPause:
```json
{
    "command" : "playPause",
    "user" : "alice22",
    "timestamp" : 30,
    "message" : "Playback paused successfully."
}
```
> **Note:** This command can only be executed after the user has issued the load command.

### Repeat

Initially, repeat is in the state where nothing is repeated. Depending on the type of content currently being played, the repeat command cycles through different states. Essentially, from state 0 it transitions to state 1, from state 1 to state 2, and from state 2 back to state 0, with the effective state depending on the type of content being played by the player at that moment. Thus, we have the following cases:

      - If we are playing something from a playlist, the states are:
            - 0 - no repeat
            - 1 - repeat all → after finishing all the songs in the list, it starts again with the first song
            - 2 - repeat current song → after finishing the current song, it starts it again
      - If we are playing a song from the library or a podcast, the states are:
            - 0 - no repeat
            - 1 - repeat once → after finishing the current file, it plays it one more time
            - 2 - repeat infinite → plays the current file endlessly
            
#### Possible messages
      1.Repeat mode changed to {repeat_state}.
      2.Please load a source before setting the repeat status.

Example input for repeat:
```json
{
    "command": "repeat",
    "username": "alice22",
    "timestamp": 31
}
```
Example output for repeat:
```json
{
    "command" : "repeat",
    "user" : "alice22",
    "timestamp" : 31,
    "message" : "Repeat mode changed to repeat once."
}
```
> **Note:** This command can only be executed after the user has issued the load command.

### Shuffle

> **Note:** The shuffle command can only be used when listening to a playlist.

When something is loaded into the player, it plays the tracks in order. To play in a different order, the shuffle command is used, which cycles through two states. For a playlist played in shuffle mode, the order in which the tracks are played is determined using the seed specified in the command. Essentially, if we have 10 tracks, we will consider having IDs from 0 to 9 in this order and shuffle them using the Random class and the provided seed. The new vector represents the order in which the tracks will be played if shuffle is active.

Assuming we are on the k-th track in the playlist (where k represents the indices of the tracks given in the order from the input), we have 2 cases:

      1.If shuffle is disabled, the next track will be the k+1-th track, provided repeat is disabled.
      2.If shuffle is enabled, we look at the shuffled index vector, find where the current track index k is in the list, and select the ID that follows k in that shuffled list as the index for the next track in the current playlist. 

#### Possible messages
      1.Shuffle function activated successfully.
      2.Shuffle function deactivated successfully.
      3.The loaded source is not a playlist.
      4.Please load a source before using the shuffle function.

Example input for shuffle:
```json
{
    "command": "shuffle",
    "username": "bob35",
    "timestamp": 850,
    "seed": 1024
}
```
Example output for shuffle:
```json
{
    "command" : "shuffle",
    "user" : "bob35",
    "timestamp" : 850,
    "message" : "Shuffle function activated successfully."
}
```
> **Note:** This command can only be executed after the user has issued the load command.

### Forward/Backward

The forward and backward commands can only be executed when the user is listening to a podcast. These commands allow the user to advance or rewind by 90 seconds. If less than 90 seconds have passed and backward is executed, the player remains at the beginning of the current episode. If there are less than 90 seconds remaining until the end of the episode and forward is executed, the player automatically starts the next episode from the beginning.

#### Possible messages
      1.Skipped forward successfully.
      2.The loaded source is not a podcast.
      3.Please load a source before skipping forward.
      4.Rewound successfully.
      5.Please select a source before rewinding.

> **Note:** This command can only be executed after the user has issued the load command.

Example input for forward:
```json
{
    "command" : "forward",
    "user" : "bob35",
    "timestamp" : 1050
}
```

Example output for forward:
```json
{
    "command" : "forward",
    "user" : "bob35",
    "timestamp" : 1050,
    "message" : "Please load a source before skipping forward."
}
```
Example input for backward:
```json
{
    "command": "backward",
    "username": "bob35",
    "timestamp": 1390
}
```
Example output for backward:
```json
{
    "command" : "backward",
    "user" : "bob35",
    "timestamp" : 1390,
    "message" : "Rewound successfully."
}
```

### Like

This command can only be executed if the player is currently playing a song. The user can like the current song, and if they have already liked it, they can withdraw their like. This forms a list of liked songs, which can be checked using another command.

#### Possible messages
      1.Like registered successfully.
      2.Unlike registered successfully.
      3.Please load a source before liking or unliking.
      4.Loaded source is not a song.

Example input for like:
```json
{
    "command": "like",
    "username": "bob35",
    "timestamp": 205
}
```
Example output for like:
```json
{
    "command" : "like",
    "user" : "bob35",
    "timestamp" : 205,
    "message" : "Like registered successfully."
}
```
> **Note:** This command can only be executed after the user has issued the load command.

### Next

When the next command is executed, the player moves to the next track, considering its current state (shuffle status, repeat status). If it reaches the end of a playlist/podcast or the end of a song played from the library (when only one song is playing), and the repeat condition specifies that nothing should be repeated, then the player will pause after the playlist/podcast has finished and will remain without any loaded source to interact with.

#### Possible messages
      1.Skipped to next track successfully. The current track is {track_name}.
      2.Please load a source before skipping to the next track.

> **Note:** This command can only be executed after the user has issued the load command.

> **Note:** If after issuing next there are no more tracks to play, the following message will be displayed: Please load a source before skipping to the next track.

Example input for next:
```json
{
    "command": "next",
    "username": "bob35",
    "timestamp": 590
}
```
Example output for next:
```json
{
    "command" : "next",
    "user" : "bob35",
    "timestamp" : 590,
    "message" : "Skipped to next track successfully. The current track is Under Pressure."
}
```
### Prev

When pressing prev, we can have the following situations:

      - The player jumps to the beginning of the audio file if at least one second has passed from its content.
      - If no seconds have passed from the current audio file, the player skips to the previous audio file.
      - If the player is at the first song in the playlist, the first episode of the podcast, or in a song from the library, the current content is replayed from the beginning.

#### Possible messages
      1.Returned to previous track successfully. The current track is {track_name}.
      2.Please load a source before returning to the previous track.

Example input for prev:
```json
{
    "command": "prev",
    "username": "bob35",
    "timestamp": 710
}
```
Example output for prev:
```json
{
    "command" : "prev",
    "user" : "bob35",
    "timestamp" : 710,
    "message" : "Returned to previous track successfully. The current track is Start Me Up."
}
```
> **Note:**

>       - If this command is executed, the player starts playing the content, even if it was previously in the paused state.
>       - This command can only be executed after the user has given the load command.

###  AddRemoveInPlaylist


When this command is executed, the current song is added to the specified playlist. If the song already exists in the playlist, it will be removed from the playlist. Playlists are indexed starting from 1, with the first index corresponding to the first playlist created.

#### Possible messages
      1.Successfully added to playlist.
      2.Successfully removed from playlist.
      3.The loaded source is not a song.
      4.The specified playlist does not exist.
      5.Please load a source before adding to or removing from the playlist.

Example input for AddRemoveInPlaylist:
```json
{
    "command": "addRemoveInPlaylist",
    "username": "alice22",
    "timestamp": 24,
    "playlistId": 1
}
```
Example output for AddRemoveInPlaylist:
```json
{
    "command" : "addRemoveInPlaylist",
    "user" : "alice22",
    "timestamp" : 24,
    "message" : "Successfully added to playlist."
}
```
> **Note:** This command can only be executed after the user has issued the load command.
> **Note:** We guarantee that this command will not be executed by a user on a playlist that is currently loaded in the player.

### Status

The state of the current user's player is displayed.

Example input for status:
```json
{
    "command": "status",
    "username": "alice22",
    "timestamp": 59
}
```
Example output for status:
```json
{
    "command" : "status",
    "user" : "alice22",
    "timestamp" : 59,
    "stats" : {
      "name" : "The Power of Design",
      "remainedTime" : 3065,
      "repeat" : "No Repeat",
      "shuffle" : false,
      "paused" : false
    }
}
```
## Playlist commands

### CreatePlaylist

A new empty playlist is created for a user. If the user already has a playlist with this name, an error message is received. The playlist will initially have its visibility set to public.

#### Possible messages
      1.Playlist created successfully.
      2.A playlist with the same name already exists.

Example input for createPlaylist:
```json
{
    "command": "createPlaylist",
    "username": "alice22",
    "timestamp": 5,
    "playlistName": "Playlist bengos"
}
```
Example output for createPlaylist:
```json
{
    "command" : "createPlaylist",
    "user" : "alice22",
    "timestamp" : 5,
    "message" : "Playlist created successfully."
}
```

### SwitchVisibility

A playlist can be either public or private. When it is created, it is public, meaning it is visible to all users. If this command is executed, the playlist will become private if it was public, or public if it was private.

#### Possible messages
      1.Visibility status updated successfully to {true/false}.
      2.The specified playlist ID is too high.

Example input for switchVisibility:
```json
{
    "command" : "switchVisibility",
    "user" : "carol19",
    "timestamp" : 1130,
    "message" : "The specified playlist ID is too high."
}
```
Example output for switchVisibility:
```json
{
    "command": "switchVisibility",
    "username": "carol19",
    "timestamp": 1130,
    "playlistId": 100
}
```
### FollowPlaylist

After selecting a playlist via the search bar, a user can follow it. If the user is already following the playlist, the command will result in unfollowing it. A playlist not owned by the current user is accessible only if it is public.

#### Possible messages
      1.Playlist followed successfully.
      2.Playlist unfollowed successfully.
      3.The selected source is not a playlist.
      4.Please select a source before following or unfollowing.
      5.You cannot follow or unfollow your own playlist.

Example input for followPlaylist:
```json
{
    "command": "follow",
    "username": "carol19",
    "timestamp": 1050
}
```
Example output for followPlaylist:
```json
{
    "command" : "follow",
    "user" : "carol19",
    "timestamp" : 1050,
    "message" : "Please select a source before following or unfollowing."
}
```

> **Note:** This command can only be executed after the user has issued the select command.

### ShowPlaylists

This command will display all the songs from all the playlists owned by the user.

Example input for ShowPlaylists:
```json
{
    "command": "showPlaylists",
    "username": "alice22",
    "timestamp": 65
}
```
Example output for ShowPlaylists:
```json
{
    "command" : "showPlaylists",
    "user" : "alice22",
    "timestamp" : 65,
    "result" : [ {
       "name" : "Playlist bengos",
       "songs" : [ "The Unforgiven" ],
       "visibility" : "public",
       "followers" : 0
    } ]
}
```
## User Statistics

###  ShowPreferredSongs

A list of all the songs that the user has liked will be displayed.

Example input for ShowPreferredSongs:
```json
{
    "command": "showPreferredSongs",
    "username": "carol19",
    "timestamp": 1000
}
```
Example output for ShowPreferredSongs:
```json
{
  "command" : "showPreferredSongs",
  "user" : "carol19",
  "timestamp" : 1000,
  "result" : [ "Bohemian Rhapsody" ]
}
```
## General Statistics 

### GetTop5Songs

A list will be displayed with the top 5 songs in the library that have received the most likes from users. In case of a tie, the songs are chosen based on their order in the library.

Example input for GetTop5Songs:
```json
{
    "command": "getTop5Songs",
    "timestamp": 3300
}
```
Example output for GetTop5Songs:
```json
{
    "command" : "getTop5Songs",
    "user" : null,
    "timestamp" : 3300,
    "result" : [ "Bohemian Rhapsody", "Shape of You", "Don't", "Stairway to Heaven", "Money Trees" ]
}
```

### GetTop5Playlists

A list will be displayed with the top 5 public playlists that have received the most follows from users. In case of a tie, the oldest playlist is chosen. It is guaranteed that no two playlists were created at the same time.

Example input for GetTop5Playlists:
```json
{
    "command": "getTop5Playlists",
    "timestamp": 2560
}
```
Example output for GetTop5Playlists:
```json
{
    "command" : "getTop5Playlists",
    "user" : null,
    "timestamp" : 2560,
    "result" : [ "My first playlist", "Just for fun", "Felt cute might delete later", "Listen on repeat" ]
}
```
# STAGE 2

## Overview

For this stage, you will be implementing an application similar in functionality to Spotify, now including artists and hosts (described below). You will simulate various actions performed by users using commands received from input files. The perspective remains that of an admin overseeing users and application elements.

> **Note:** The commands and the format of input and output from Stage I remain unchanged. Everything implemented in Stage I will still be valid in this current stage. In this stage, you will need to extend the solution from Stage I with new functionalities.

## Page System

With the database in place, we can now simulate a minimal graphical interface by compartmentalizing the application into different pages that a regular user can be on at any given time. The following types of pages will be available for an individual user:

      - HomePage - The page where any user is when they are first added to the platform. Here, the user will be able to see static “recommendations,” meaning they will be able to view the top 5 songs they have liked based on the number of likes and the top 5 playlists followed by the total number of likes on all the songs in those playlists.
      - LikedContentPage - LikedContentPage - The page where any user can view all the songs and playlists they have liked/followed.

These two pages are accessible from anywhere within the application and can be accessed at any time, except when the user is offline.

### Artist page

Each artist will have their own page when added to the platform and will be able to manage certain details about it. A regular user can navigate to the artist’s page by searching for them and then selecting them from the search results, which will clear the search results list. The user will be able to view the artist’s albums, merchandise, and events on this page. Any changes made by the artist to albums, merchandise, and events will be reflected at the same time for all users who are on the artist’s page.

### Host page

Similar to the artist page, a regular user will be able to view the host’s podcasts and announcements on this page.

### Commands for the Page System

Only regular users will be able to use the page system commands!

#### ChangePage

##### Possible messages:
      1.<username> accessed <next page> successfully.
      2.<username> is trying to access a non-existent page.
      
Example input for changePage:
```json
{
    "command": "changePage",
    "username": "alice22",
    "timestamp": 100,
    "nextPage": "Home"
}
```
Example input for changePage:
```json
{
    "command": "changePage",
    "username": "alice22",
    "timestamp": 100,
    "nextPage": "LikedContent"
}
```
Example output for changePage:
```json
    "command" : "changePage",
    "user" : "alice22",
    "timestamp" : 100,
    "message" : "alice22 accessed Home successfully."
}
```
Example output for changePage:
```json
{
    "command" : "changePage",
    "user" : "alice22",
    "timestamp" : 100,
    "message" : "alice22 accessed LikedContent successfully."
}
```
Example output for changePage:
```json
{
    "command" : "changePage",
    "user" : "alice22",
    "timestamp" : 100,
    "message" : "alice22 is trying to access a non-existent page."
}
```

#### PrintCurrentPage

With this command, we will see the page that a specific user is on. We will use a string format for each page, as follows:

      - HomePage
      Liked songs:\n\t[songname1, songname2, …]\n\nFollowed playlists:\n\t[playlistname1, playlistname2, …]
      - LikedContentPage
      Liked Songs:\n\t[songname1 - songartist1, songname2 - songartist2, …]\n\nFollowed Playlists:\n\t[playlistname1 - owner1, playlistname2 - owner2, …]
      - Artist page
      Albums:\n\t[albumname1, albumname2, …]\n\nMerch:\n\t[merchname1 - merchprice1:\n\tmerchdescription1, merchname2 - merchprice2:\n\tmerchdescription2, …]\n\nEvent:\n\t[eventname1 - eventdate1:\n\teventdescription1, eventname2 - eventdate2:\n\teventdescription2, …]
      - Host page
      Podcasts:\n\t[podcastname1:\n\t[episodename1 - episodedescription1, episodename2 - episodedescription2, … ], …]\n\nAnnouncements\n\t[announcementname1\n\tannouncementdescription1\n, announcementname2\n\tannouncementdescription2\n, …]

Example input for printCurrentPage:
```json
{
    "command": "printCurrentPage",
    "username": "alice22",
    "timestamp": 420,
}
```
Example output for printCurrentPage:
```json
{
    "command": "printCurrentPage",
    "user": "alice22",
    "timestamp": 420,
    "message": "Liked songs:\n\t[songname1, songname2]\n\nFollowed Playlists:\n\t[playlistname1 - owner1, playlistname2 - owner2, ...]"
}
```
## New Entities Compared to the First Stage

### Users

Two New Types of Users Are Added, Each with Specific Commands Presented Below:

      - artist - user who can add albums and has their own page within this stage
      - host- user who can add podcasts and has their own page within this stage

### Audio File Collections

An album is a collection of songs created by an artist. Regular users can search for and listen to albums using the select and load functions. Songs cannot be added to the application outside of an album.

### Search bar

Search Bar Updates:

      - Album:
            - by name → checks if the album's name starts with the specified text
            - by owner → checks if the artist who created the album's name starts with the specified text
            - by description → checks if the album's description starts with the specified text
            
      - Artist:
            - by username → checks if the artist's name starts with the specified text
      
      - Host :
            - by username → checks if the host's name starts with the specified text

A normal user will now be able to search for an artist or a host and apply the "select" command on them, which will redirect them to the entry page of the selected item from the search results. The “item number” error is maintained in this case, and the results will be returned as a list of Strings consisting of the names of the resulting artists/hosts. After selection, the results will be cleared, just as in the first phase.

### Music player

Now, a normal user will be able to select an album and then load this type of collection. The album must support the same functionalities as a playlist, including repeat and shuffle.

## Search Bar Commands

### Search

The functionality to search for an album, an artist, or a host is added.

Example input for search album:
```json
{
    "command": "search",
    "username": "alice22",
    "timestamp": 100,
    "type": "album",
    "filters": {
      "name": "Th",
      "owner": "U",
      "description": "Best"
    }
}
```
Example input for search artist:
```json
{
    "command": "search",
    "username": "alice22",
    "timestamp": 100,
    "type": "artist",
    "filters": {
      "name": "The"
    }
}
```
Example input for search host:
```json
{
    "command": "search",
    "username": "alice22",
    "timestamp": 100,
    "type": "host",
    "filters": {
      "name": "The"
    }
}
```
Example output for search:
```json
{
    "command" : "search",
    "user" : "alice22",
    "timestamp" : 10,
    "message" : "Search returned 3 results",
    "results" : [ "This Is Why", "The Mad Stone", "The Eminem Show" ]
}
```
## Player

A normal user's player can have an album as the audio source and can use all the commands available for playlists.

### Admin commands

#### AddUser

As an admin, we can add new users, including normal users, artists, or hosts. This will be done with this command, containing only the basic details for a user, allowing each user to later change their details, such as adding an album for an artist. Existing normal users in the tests will not be modified, and all artists and hosts will be added during each test.

##### Possible messages:
      - The username <username> is already taken.
      - The username <username> has been added successfully.

Example input for addUser:
```json
{
    "command": "addUser",
    "timestamp": 100,
    "type": "user/artist/host",
    "username": "anaaremere",
    "age": 20,
    "city": "New York"
}
```
Example output for addUser:
```json
{
    "command" : "addUser",
    "timestamp" : 100,
    "user": "anaaremere",
    "message" : "The username anaaremere has been added successfully"
}
```
#### DeleteUser

As an admin, we can delete a user, and all their associations with other entities in the application must also be deleted or updated. A user of any type cannot be deleted if another user is currently interacting with any of their entities. For example, if we want to delete a host, and a user is currently listening to one of their podcasts, the host cannot be deleted. Handling cases for this type of command is at your discretion.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> can't be deleted.
      - <username> was successfully deleted.

Example input for deleteUser:
```json
{
    "command": "deleteUser",
    "username": "alice22",
    "timestamp": 120
}
```
Example output for deleteUser:
```json
{
  "command" : "deleteUser",
  "user" : "alice22",
  "timestamp" : 120,
  "message" : "alice22 was successfully deleted."
}
```

#### ShowAlbums

In this command, all albums of an artist will be displayed. It is guaranteed that the username provided in the input is a valid artist and will not be invalid. For each album, the name of the album and a list of the names of the songs in the album will be shown.

Example input for showAlbums:
```json
{
    "command": "showAlbums",
    "username": "Ed Sheeran",
    "timestamp": 400,
}
```
Example output for showAlbums:
```json
{
    "command": "showAlbums",
    "user": "Ed Sheeran",
    "timestamp": 400,
    "result": [ {
       "name": "Divide",
       "songs": [ "Shape of You", "Happier", "Castle on the Hill" ]
    } ]
}
```
#### ShowPodcasts

In this command, all podcasts of a host will be displayed. It is guaranteed that the username provided in the input is a valid host and will not be invalid. For each podcast, the name of the podcast and a list of the names of the episodes in the podcast will be shown.

Example input for showPodcasts:
```json
{
    "command": "showPodcasts",
    "username": "Joe Rogan",
    "timestamp": 420,
}
```
Example output for showPodcasts:
```json
{
    "command": "showPodcasts",
    "user": "Joe Rogan",
    "timestamp": 420,
    "result": [ {
       "name": "The Joe Rogan Experience",
       "episodes": [ "Elon Musk Returns", "Jordan Peterson", "Neil deGrasse Tyson", "Elon Musk on Mars", "The Art of MMA" ]
    } ]
}
```
### Artist commands

#### AddAlbum

This command allows an artist to add a new album to the application. The artist specifies the album's details, including the name, release year, description, and the list of songs that make up the album.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not an artist.
      - <username> has another album with the same name.
      - <username> has the same song at least twice in this album.
      - <username> has added new album successfully.

Example input for addAlbum:
```json
{
    "command": "addAlbum",
    "username": "Ed Sheeran",
    "timestamp": 160,
    "name": "Album1",
    "releaseYear": 2023,
    "description": "Primul album bro!"
    "songs": [
      {
        "name": "Shape of You",
        "duration": 233,
        "album": "Divide",
        "tags": [
          "#pop",
          "#mostlistenedthisyear",
          "#spotify"
        ],
        "lyrics": "The club isn't the best place to find a lover, So the bar is where I go (mm-mm)",
        "genre": "Pop",
        "releaseYear": 2017,
        "artist": "Ed Sheeran"
      },
      {
        "name": "Don't",
        "duration": 219,
        "album": "x",
        "tags": [
          "#pop",
          "#relationship",
          "#spotify"
        ],
        "lyrics": "I met this girl late last year, She said, 'Don't you worry if I disappear",
        "genre": "Pop",
        "releaseYear": 2014,
        "artist": "Ed Sheeran"
      }
    ]
}
```
Example output for addAlbum:
```json
{
    "command": "addAlbum",
    "user": "Ed Sheeran",
    "timestamp": 160,
    "name": "Ed Sheeran has added new album successfully."
}
```
#### RemoveAlbum

An artist can delete one of their albums. If an album with that name exists, it can be deleted if there are no regular users with the album or any of its songs currently loaded (it does not matter if the user is simply on the artist's page) or if there is no playlist containing a song from the album.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not an artist.
      - <username> doesn't have an album with the given name.
      - <username> can't delete this album.
      - <username> deleted the album successfully.

Example input for removeAlbum:
```json
{
    "command": "removeAlbum",
    "username": "Ed Sheeran",
    "timestamp": 180,
    "name": "Album1"
}
```
Example output for removeAlbum:
```json
{
    "command": "removeAlbum",
    "username": "Ed Sheeran",
    "timestamp": 180,
    "message": "Ed Sheeran deleted the album successfully."
}
```
#### AddEvent

The artist can add an event. The event will have a description and a date, which is considered invalid in the following situations:
      
      - The correct format is dd-mm-yyyy, but in the input, it might be invalid, in which case you need to display an error.
      - For February, the day is greater than 28 or, in general, greater than 31. The month is greater than 12, or the year is less than 1900 or greater than 2023.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not an artist.
      - <username> has another event with the same name.
      - Event for <username> does not have a valid date.
      - <username> has added new event successfully.

Example input for addEvent:
```json
{
    "command": "addEvent",
    "username": "Ed Sheeran",
    "timestamp": 200,
    "name": "Event1",
    "description": "Primul Event adaugat!",
    "date": "01-01-2022"
}
```
Example output for addEvent:
```json
{
    "command": "addEvent",
    "user": "Ed Sheeran",
    "timestamp": 200,
    "message": "Ed Sheeran has added new event successfully." 
}
```
#### RemoveEvent

This command allows an artist to delete an existing event.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not an artist.
      - <username> doesn't have an event with the given name.
      - <username> deleted the event successfully.

Example input for removeEvent:
```json
{
    "command": "removeEvent",
    "username": "Ed Sheeran",
    "timestamp": 220,
    "name": "Event1"
}
```
Example output for removeEvent:
```json
{
    "command": "removeEvent",
    "user": "Ed Sheeran",
    "timestamp": 220,
    "message": "Ed Sheeran deleted the event successfully."
}
```
#### AddMerch

The artist has the ability to add merchandise items to the application. These items include descriptions and prices.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not an artist.
      - <username> has merchandise with the same name.
      - Price for merchandise can not be negative.
      - <username> has added new merchandise successfully.

Example input for addMerch:
```json
{
    "command": "addMerch",
    "username": "Ed Sheeran",
    "timestamp": 240,
    "name": "Merch1",
    "description": "Primul Merch adaugat!",
    "price": 100
}
```
Example output for addMerch:
```json
{
    "command": "addMerch",
    "user": "Ed Sheeran",
    "timestamp": 240,
    "message": "Ed Sheeran has added new merchandise successfully."
}
```

### Host commands

#### AddPodcast

This command allows a host to add a new podcast to the application. The host specifies the podcast details, including the name and the list of episodes with the respective details for each episode (name, duration, description).

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not a host.
      - <username> has another podcast with the same name.
      - <username> has the same episode in this podcast.
      - <username> has added new podcast successfully.
      
Example input for addPodcast:
```json
{
    "command": "addPodcast",
    "username": "Mike",
    "timestamp": 260,
    "name": "Podcast1",
    "episodes": [
          {
            "name": "Elon Musk Returns",
            "duration": 11927,
            "description": "Elon Musk, CEO of SpaceX and Tesla, returns to discuss various topics."
          },
          {
            "name": "Jordan Peterson",
            "duration": 9916,
            "description": "Dr. Jordan Peterson joins Joe to discuss psychology, philosophy, and current events."
          }
      ]
}
```
Example output for addPodcast:
```json
{
    "command": "addPodcast",
    "user": "Mike",
    "timestamp": 260,
    "message": "Mike has added new podcast successfully."
}
```
#### RemovePodcast

Allows a host to delete an existing podcast. If there is a podcast with that name, it can be deleted if no regular user currently has the podcast loaded (it does not matter if the user is only on the host's page).

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not a host.
      - <username> doesn't have a podcast with the given name.
      - <username> can't delete this podcast.
      - <username> deleted the podcast successfully.

Example input for removePodcast:
```json
{
    "command": "removePodcast",
    "username": "Mike",
    "timestamp": 280,
    "name": "Podcast1"
}
```
Example output for removePodcast:
```json
{
    "command": "removePodcast",
    "user": "Mike",
    "timestamp": 280,
    "message": "Mike deleted the podcast successfully."
}
```

#### AddAnnouncement

The host can add announcements to inform the audience about various topics or events. These announcements will have a name and a description.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not a host.
      - <username> has already added an announcement with this name.
      - <username> has successfully added new announcement.
      
Example input for addAnnouncement:
```json
{
    "command": "addAnnouncement",
    "username": "Mike",
    "timestamp": 300,
    "name": "Announcement1",
    "description": "Primul anunt adaugat!"
}
```
Example output for addAnnouncement:
```json
{
    "command": "addAnnouncement",
    "user": "Mike",
    "timestamp": 300,
    "message": "Mike has successfully added new announcement."
}
```

#### RemoveAnnouncement

This command allows a host to delete an existing announcement.

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not a host.
      - <username> has no announcement with the given name.
      - <username> has successfully deleted the announcement.

Example input for removeAnnouncement:
```json
{
    "command": "removeAnnouncement",
    "username": "Mike",
    "timestamp": 320,
    "name": "Announcement1"
}
```
Example output for removeAnnouncement:
```json
{
    "command": "removeAnnouncement",
    "user": "Mike",
    "timestamp": 320,
    "message": "Mike has successfully deleted the announcement."
}
```

### Normal user commands

#### SwitchConnectionStatus

All regular users will initially be considered online, including when they are added to the platform, and will have access to all their commands, including those from Phase I. This command will switch a regular user from online mode to offline mode, or vice versa, stopping any access to the platform. The user will not be able to use other commands related to regular users, except for those related to data accessible by admins: playlists, liked songs, and preferred genres. When a user is offline, time will no longer be counted for them, and their player will be stopped until they come back online. If the user is offline at the time of using any of the commands below, the message will be “<username> is offline.”

List of Commands Disabled in Offline Mode:

- search
- select
- load
- playPause
- repeat
- shuffle
- forward
- backward
- like
- next
- prev
- createPlaylist
- addRemoveInPlaylist
- switchVisibility
- follow
- changePage
- printCurrentPage

##### Possible messages:
      - The username <username> doesn't exist.
      - <username> is not a normal user.
      - <username> has changed status successfully.

Example input for switchConnectionStatus:
```json
{
    "command": "switchConnectionStatus",
    "username": "Mike",
    "timestamp": 320,
}
```
Example output for switchConnectionStatus:
```json
{
    "command": "switchConnectionStatus",
    "user": "Mike",
    "timestamp": 320,
    "message": "Mike has changed status successfully."
}
```

## General Statistics

### getTop5Albums

This command will display the names of the top 5 most appreciated albums in the application based on the number of likes. The number of likes for an album is considered to be the sum of all the likes for the songs within that album. If there are fewer than 5 albums, all will be displayed. In case of a tie in the number of likes, albums will be listed in lexicographical order.

Example input for getTop5Albums:
```json
{
    "command": "getTop5Albums",
    "timestamp": 540
}
```
Example output for getTop5Albums:
```json
{
    "command": "getTop5Albums",
    "timestamp": 540,
    "result": [ "Divide", "Led Zeppelin IV", "Hot Space", "Aerosmith", "Vol. 3... Life and Times of S. Carter" ]
}
```

### getTop5Artists

This command will display the names of the top 5 most appreciated artists in the application based on the number of likes. The number of likes for an artist is calculated as the sum of all likes for the songs in all their albums. If there are fewer than 5 artists, all will be displayed.

Example input for getTop5Artists:
```json
{
    "command": "getTop5Artists",
    "timestamp": 560
}
```
Example output for getTop5Artists:
```json
{
    "command": "getTop5Artists",
    "timestamp": 560,
    "result": [ "Jay Z", "Meek Mill", "The Rolling Stones", "Led Zeppelin", "Maroon 5" ]
}
```

### getAllUsers

This command will display the names of all users in the application. The order of display is as follows: regular users, artists, and hosts.

Example input for getAllUsers:
```json
{
    "command": "getAllUsers",
    "timestamp": 580
}
```
Example output for getAllUsers:
```json
{
    "command": "getAllUsers",
    "timestamp": 580,
    "result": [ "alice22", "bob35", "Ed Sheeran", "Mike" ]
}
```

### getOnlineUsers

This command will display the names of all online regular users in the application.

Example input for getOnlineUsers:
```json
{
    "command": "getOnlineUsers",
    "timestamp": 600
}
```
Example output for getOnlineUsers:
```json
{
    "command": "getOnlineUsers",
    "timestamp": 600,
    "result": [ "alice22", "bob35" ]
}
```

