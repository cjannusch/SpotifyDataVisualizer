package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;



public class LoadCSV {

  private ArrayList<SpotifySong> songArray;
  private ArrayList<String> artistNames = new ArrayList<String>();
  private ArrayList<SpotifyArtist> artistArray = new ArrayList<SpotifyArtist>();

  public LoadCSV(String fileName) {
    this.songArray = this.loadData(new File(fileName));

    // uses songArray so takes place after that
    for (int i = 0; i < this.songArray.size(); i++) {
      for (int j = 0; j < this.songArray.get(i).getArtists().length; j++) {
        String[] tempArray = this.songArray.get(i).getArtists();

        if (!this.artistNames.contains(tempArray[j])) {
          this.artistNames.add(tempArray[j]);
        }
      }
    }

    Set<String> set = new HashSet<>(artistNames);
    artistNames.clear();
    artistNames.addAll(set);

  }

  public void loadArtistData() {

    for (int i = 0; (i < artistNames.size()); i++) {

      artistArray
          .add(new SpotifyArtist(artistNames.get(i), searchByArtistName(artistNames.get(i))));
//      System.out.println("added an artist + " + i);

    }

    return;
  }


  /**
   * Returns the value of the field called 'artistArray'.
   * 
   * @return Returns the artistArray.
   */
  public ArrayList<SpotifyArtist> getArtistArray() {
    return this.artistArray;
  }



  /**
   * Returns the value of the field called 'songArray'.
   * 
   * @return Returns the songArray.
   */
  public ArrayList<SpotifySong> getSongArray() {
    return this.songArray;
  }

  /**
   * Returns the value of the field called 'songArray'.
   * 
   * @return Returns the songArray.
   */
  public ArrayList<String> getArtistNames() {
    return this.artistNames;
  }



  public ArrayList<SpotifySong> loadData(File fileName) throws InputMismatchException {

    ArrayList<SpotifySong> array = new ArrayList<SpotifySong>();

    Scanner scnr = null;
    try {
      scnr = new Scanner(fileName);
    } catch (FileNotFoundException exception) {
      System.out.println("CSV file not found!");
    }

    // System.out.println("Got to delimiter");

    if (scnr != null) {

      scnr.useDelimiter(",");
      if (scnr.hasNextLine()) {
        scnr.nextLine();
      }
      while (scnr.hasNextLine()) {

        String constructorString = scnr.nextLine();

        array.add(new SpotifySong(constructorString));
      }

      System.out.println("number of songs added to list: " + array.size());

      scnr.close();
    }
    return array;
  }

  public ArrayList<SpotifySong> searchBySongName(String searchTerm) {

    searchTerm = searchTerm.toLowerCase();

    ArrayList<SpotifySong> songsFromName = new ArrayList();

    for (int i = 0; i < this.songArray.size(); i++) {
      if (this.songArray.get(i).getName().toLowerCase().contains(searchTerm)) {
        songsFromName.add(songArray.get(i));
        // System.out.println(songArray.get(i).toString());
      }
    }
    return songsFromName;
  }


  public ArrayList<SpotifySong> searchByArtistName(String searchTerm) {

    searchTerm = searchTerm.toLowerCase();

    ArrayList<SpotifySong> songsFromArtist = new ArrayList();

    for (int i = 0; i < this.songArray.size(); i++) {

      for (int j = 0; j < this.songArray.get(i).getArtists().length; j++) {

        String[] tempArray = this.songArray.get(i).getArtists();

        if (tempArray[j].toLowerCase().contains(searchTerm)) {
          songsFromArtist.add(songArray.get(i));
        }
      }
    }

    // System.out.println("number of songs by " + searchTerm + " found: " + songsFromArtist.size());
    return songsFromArtist;
  }


}
