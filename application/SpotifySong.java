package application;

import java.util.Arrays;
import java.util.InputMismatchException;

//Is the data type that contains the meta-data for a specific Spotify Song

/**
 * TODO Put here a description of what this class does.
 *
 * @author Clayton. Created Jul 2, 2020.
 */
public class SpotifySong {

  private double acousticness;
  private String[] artists; // song only
  private double danceability;
  private int duration_ms;
  private double energy;
  private boolean explicit; // song only
  private String id; // song only
  private double instrumentalness;
  private String key; // song only
  private double liveness;
  private double loudness;
  private int mode; // 0=minor 1=major //song only
  private String name;
  private int popularity;
  private String release_date; // song only
  private double speechiness;
  private double tempo;
  private double valence;
  private int year; // song only


  /**
   * Returns the value of the field called 'acousticness'.
   * 
   * @return Returns the acousticness.
   */
  public double getAcousticness() {
    return this.acousticness;
  }

  /**
   * Returns the value of the field called 'artists'.
   * 
   * @return Returns the artists.
   */
  public String[] getArtists() {
    return this.artists;
  }

  /**
   * Returns the value of the field called 'danceability'.
   * 
   * @return Returns the danceability.
   */
  public double getDanceability() {
    return this.danceability;
  }

  /**
   * Returns the value of the field called 'duration_ms'.
   * 
   * @return Returns the duration_ms.
   */
  public int getDuration_ms() {
    return this.duration_ms;
  }

  /**
   * Returns the value of the field called 'energy'.
   * 
   * @return Returns the energy.
   */
  public double getEnergy() {
    return this.energy;
  }

  /**
   * Returns the value of the field called 'explicit'.
   * 
   * @return Returns the explicit.
   */
  public boolean isExplicit() {
    return this.explicit;
  }

  /**
   * Returns the value of the field called 'id'.
   * 
   * @return Returns the id.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Returns the value of the field called 'instrumentalness'.
   * 
   * @return Returns the instrumentalness.
   */
  public double getInstrumentalness() {
    return this.instrumentalness;
  }

  /**
   * Returns the value of the field called 'key'.
   * 
   * @return Returns the key.
   */
  public String getKey() {
    return this.key;
  }

  /**
   * Returns the value of the field called 'liveness'.
   * 
   * @return Returns the liveness.
   */
  public double getLiveness() {
    return this.liveness;
  }

  /**
   * Returns the value of the field called 'loudness'.
   * 
   * @return Returns the loudness.
   */
  public double getLoudness() {
    return this.loudness;
  }

  /**
   * Returns the value of the field called 'mode'.
   * 
   * @return Returns the mode.
   */
  public int getMode() {
    return this.mode;
  }

  /**
   * Returns the value of the field called 'name'.
   * 
   * @return Returns the name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the value of the field called 'popularity'.
   * 
   * @return Returns the popularity.
   */
  public int getPopularity() {
    return this.popularity;
  }

  /**
   * Returns the value of the field called 'release_date'.
   * 
   * @return Returns the release_date.
   */
  public String getRelease_date() {
    return this.release_date;
  }

  /**
   * Returns the value of the field called 'speechiness'.
   * 
   * @return Returns the speechiness.
   */
  public double getSpeechiness() {
    return this.speechiness;
  }

  /**
   * Returns the value of the field called 'tempo'.
   * 
   * @return Returns the tempo.
   */
  public double getTempo() {
    return this.tempo;
  }

  /**
   * Returns the value of the field called 'valence'.
   * 
   * @return Returns the valence.
   */
  public double getValence() {
    return this.valence;
  }

  /**
   * Returns the value of the field called 'year'.
   * 
   * @return Returns the year.
   */
  public int getYear() {
    return this.year;
  }


  public SpotifySong(String inputLine) throws InputMismatchException {


    try {

      String artistsString = null;

      if (inputLine.contains("  ")) {
        int startPos = inputLine.indexOf("[");
        int endPos = inputLine.lastIndexOf("]");

        artistsString = inputLine.substring(startPos, endPos + 1);
        artistsString = artistsString.replace('[', '\0');
        artistsString = artistsString.replace(']', '\0');
        artistsString = artistsString.replace('\'', '\0');
        artistsString = artistsString.trim();
        this.artists = artistsString.split("  ");
        StringBuffer newInputLine = new StringBuffer(inputLine);
        newInputLine.replace(startPos, endPos + 2, "");
        inputLine = newInputLine.toString();
      }

      String[] inputArray = inputLine.split(",");

      if (artistsString != null) {
        String[] fillerInputArray = new String[20];
        for (int i = 1; i < inputArray.length; i++) {
          fillerInputArray[i + 1] = inputArray[i];
        }
        fillerInputArray[0] = inputArray[0];
        fillerInputArray[1] = "MULTIPLE ARTISTS";
        inputArray = fillerInputArray;

        for (int i = 0; i < artists.length; i++) {
          this.artists[i] = this.artists[i].trim();
        }
      } else {
        this.artists = new String[] {inputArray[1].substring(2, inputArray[1].length() - 2)};
      }


      this.acousticness = Double.parseDouble(inputArray[0]);


      this.danceability = Double.parseDouble(inputArray[2]);



      // System.out.println(inputArray.toString());
      this.duration_ms = Integer.parseInt(inputArray[3]);
      this.energy = Double.parseDouble(inputArray[4]);
      this.explicit = false;
      if (inputArray[5].contains("1"))
        this.explicit = true;


      this.id = inputArray[6];
      this.instrumentalness = Double.parseDouble(inputArray[7]);


      // pretty sure a switch statement is faster than ifs in this case
      switch (Integer.parseInt(inputArray[8])) {

        case 0:
          this.key = "C";
        case 1:
          this.key = "C#";
        case 2:
          this.key = "D";
        case 3:
          this.key = "D#";
        case 4:
          this.key = "E";
        case 5:
          this.key = "F";
        case 6:
          this.key = "F#";
        case 7:
          this.key = "G";
        case 8:
          this.key = "G#";
        case 9:
          this.key = "A";
        case 10:
          this.key = "A#";
        case 11:
          this.key = "B";
      }

      this.liveness = Double.parseDouble(inputArray[9]);
      this.loudness = Double.parseDouble(inputArray[10]);

      this.mode = Integer.parseInt(inputArray[11]); // 0=minor 1=major
      this.name = inputArray[12];
      this.popularity = Integer.parseInt(inputArray[13]);
      this.release_date = inputArray[14];
      this.speechiness = Double.parseDouble(inputArray[15]);
      this.tempo = Double.parseDouble(inputArray[16]);
      this.valence = Double.parseDouble(inputArray[17]);
      this.year = Integer.parseInt(inputArray[18]);

    } catch (Exception erethang) {
      throw new InputMismatchException();
    }

  }

  @Override
  public String toString() {
    String easyToRead = "";


    easyToRead =
        easyToRead + "Song Name: " + this.name + " | Artists: " + Arrays.deepToString(this.artists);


    return easyToRead;
  }



}
