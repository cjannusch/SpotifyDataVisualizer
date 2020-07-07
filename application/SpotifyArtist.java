package application;
import java.util.ArrayList;

public class SpotifyArtist {

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

    System.out.println(this.toStringExtended());

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

}
