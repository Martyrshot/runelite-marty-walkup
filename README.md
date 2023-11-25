# Marty's Walk Up Plugin
This plugin is designed to mimic baseball player's walk up
music when they step up to the plate. It allows users to give
each of their friends a sound to play when they log in.

## Settings
**Note:** Sound files must be put in `$RUNELITE_DIR/sounds/marty/entrance/`. You may need to create the directories yourself.


### Friends to Announce
This is where you assign sounds to play when your friends log in.
To specify a sound to play for a particular username, add a new
line with `username=sound.wav`. Each username-sound pair must be
on its own line, and only wav files are currently supported.

### Interrupt Sound
When this box is checked, if a user logs in when a walk up sound
is being played already, the currently playing sound is stopped
and the sound for the user that just logged in is played.

### Mute
When this box is checked all sounds are muted.

### Play your sound on login
This allows the user to specify a sound to be played when
they log in to the game. The user must add themselves to the
Friends to Announce list, and have this box checked. If the box
is unchecked, then no sound will be played for that user.

### Volume
This is how the volume of the sounds is set and ranges from 0-100.
Note: No normalization occurs to the sound files themselves.
