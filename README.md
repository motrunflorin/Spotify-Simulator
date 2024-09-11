# Project: Spotify-like Application

Package page should be in package app (I didn't realised at that time).
Folders stage1 and stage2 have the inputs, checkers and refs for each stage, maybe should be renamed as etapa1 and etapa2 when used.


## Overview
This project involves implementing a Spotify-like application that simulates user interactions with the platform. Actions are simulated via commands from input files. You act as an admin overseeing all user activities, generating reports, and simulating real-time actions on the platform.

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



