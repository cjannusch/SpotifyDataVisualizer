README

Course: cs400
Semester: Summer 2020
Project name: Spotify Meta-data Visualizer
Student Name: Clayton Jannusch

email: cjannusch@wisc.edu

Other notes or comments to the grader:

NOTE ABOUT JAR FILE:
jar name is SpotifyApp.jar.  Also, the program parses the csv files correctly from within eclipse, but not from running the jar itself, this appears to be a problem with how I implemented multi-threading into the program.  I would assume this is above the scope of this class, and I didn't really have the time to restructure the code... So i will include this in the future plans section. The current method of avoiding a complete crash is to throw a DataInputMismatch Error, which allows the program's UI to load, but obviously isn't very useful without any data imported.

NOTE ABOUT PROJECT CHANGING FUNCTIONALITY:
The original plan was to graph the data within a bar chart like format,  I found that even with decent scaling, there were too many meta-data fields to be looking at to really get a good understanding of what the similarities were.  So instead I pivoted my projet idea into a similarities finder,  So it will match a song to a song or an artist to an artist, given the conditions you wish to sort by.

NOTE ABOUT FUTURE WORK AND BUGS:
1. I have noticed that with how SpotifyArtists are structured (iterates through the list of songs, which obviously is large given the data set) when importing a large set of songs like the one provided, it takes a while to load all of the aritists (example with ~200,000 songs it loads 150 artists per second)  This means that this project might be better given a database that doesn't have to import and parse the data each time the user wishes to use it.  If I wanted to keep the import your own data set feature, implenenting a hashtable to find songs given the artists might help a bit, but when a song has multiple artists, that gets complex very quickly.
2. After looking into the Spotify API a little more, I would have liked to make the input data type JSON because that is what the API exports to natively.  Additionally, now that we have learned a little but of javascript, hooking the program up to user authentication would be a pretty neat addition to find similar artists and songs to a specific user.
3.  I would have liked to get the multi-threading working better.  I had learned a lot about how the javaFX thread works throughout this project, so going forward I will be able to plan ahead knowing the limitations of doing calculations on the javaFX thread.