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
'''
jhkjhkh


