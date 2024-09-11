# Project: Spotify-like Application

Package page should be in package app (I didn't realised at that time).\
Folders stage1 and stage2 have the inputs, checkers and refs for each stage, maybe should be renamed as etapa1 and etapa2 when used.


## Overview
This project involves implementing a Spotify-like application that simulates user interactions with the platform. Actions are simulated via commands from input files. You act as an admin overseeing all user activities, generating reports, and simulating real-time actions on the platform.

## Entities

### Audio File
The application works with two types of audio files:
- Song
- Podcast Episode

Each file type provides different interaction capabilities for the user.\
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
The Library contains all songs available on the platform, accessible to all users.\
Playlists are user-created collections of songs and can be either public or private. They contain audio files from the library and are based on user preferences.\
A podcast is a collection of several related episodes. All podcasts will be specified in the input file and will be available in the library from the start of the simulation. The episodes are ordered according to the initial position specified in the input.\

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
