package application;

import java.util.ArrayList;


//Is the data type that contains the meta-data for a specific Spotify Artist
public class SpotifyArtist {

//  private String artists = "N/A"; // only used for printing to List in Main
//  private String explicit = "N/A"; // only used for printing to List in Main
//  private String id = "N/A"; // only used for printing to List in Main
//  private String key = "N/A"; // only used for printing to List in Main
//  private String mode = "N/A"; // only used for printing to List in Main
//  private String release_date = "N/A"; // only used for printing to List in Main
//  private String year = "N/A"; // only used for printing to List in Main
  private double acousticness;
  private String name;
  private double danceability;
  private int duration_ms;
  private double energy;
  private double instrumentalness;
  private double liveness;
  private int popularity;
  private double speechiness;
  private double tempo;
  private double valence;


  public SpotifyArtist(String name, ArrayList<SpotifySong> songArray) {

    this.name = name;
    double tempDanceability = 0.0;
    int tempDuration_ms = 0;
    double tempEnergy = 0.0;
    double tempInstrumentalness = 0.0;
    double tempLiveness = 0.0;
    int tempPopularity = 0;
    double tempSpeechiness = 0.0;
    double tempTempo = 0.0;
    double tempValence = 0.0;
    double tempAcousticness = 0.0;

    for (int i = 0; i < songArray.size(); i++) {
      tempDanceability = tempDanceability + songArray.get(i).getDanceability();
      tempDuration_ms = tempDuration_ms + songArray.get(i).getDuration_ms();
      tempEnergy = tempEnergy + songArray.get(i).getEnergy();
      tempInstrumentalness = tempInstrumentalness + songArray.get(i).getInstrumentalness();
      tempLiveness = tempLiveness + songArray.get(i).getLiveness();
      tempPopularity = tempPopularity + songArray.get(i).getPopularity();
      tempSpeechiness = tempSpeechiness + songArray.get(i).getSpeechiness();
      tempTempo = tempTempo + songArray.get(i).getTempo();
      tempValence = tempValence + songArray.get(i).getValence();
      tempAcousticness = tempAcousticness + songArray.get(i).getAcousticness();

    }

    this.danceability = tempDanceability / songArray.size();
    this.duration_ms = tempDuration_ms / songArray.size();
    this.energy = tempEnergy / songArray.size();
    this.instrumentalness = tempInstrumentalness / songArray.size();
    this.liveness = tempLiveness / songArray.size();
    this.popularity = tempPopularity / songArray.size();
    this.speechiness = tempSpeechiness / songArray.size();
    this.tempo = tempTempo / songArray.size();
    this.valence = tempValence / songArray.size();

//    System.out.println(this.toStringExtended());

  }



  @Override
  public String toString() {

    String easyToRead = "";

    easyToRead = easyToRead + "Artist Name: " + this.name + " | Average Popularity: "
        + this.popularity + " | Average duration: " + this.duration_ms;


    return easyToRead;
  }

  public String toStringExtended() {

    String easyToRead = "";

    easyToRead = easyToRead + "Artist Name: " + this.name + " | Average Popularity: "
        + this.popularity + " | Average duration: " + this.duration_ms + " | Average energy: "
        + this.energy + " | Average instrumentalness: " + this.instrumentalness
        + " | Average liveness: " + this.liveness + " | Average speechiness: " + this.speechiness
        + " | Average tempo: " + this.tempo + " | Average valence: " + this.valence;


    return easyToRead;
  }



  /**
   * Returns the value of the field called 'acousticness'.
   * 
   * @return Returns the acousticness.
   */
  public double getAcousticness() {
    return this.acousticness;
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
   * Returns the value of the field called 'instrumentalness'.
   * 
   * @return Returns the instrumentalness.
   */
  public double getInstrumentalness() {
    return this.instrumentalness;
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
   * Returns the value of the field called 'popularity'.
   * 
   * @return Returns the popularity.
   */
  public int getPopularity() {
    return this.popularity;
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



}
