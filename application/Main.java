package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {

  private LoadCSV loadedCSV;
  private String artistToGraph1;
  private String artistToGraph2;
  private String songToGraph1;
  private String songToGraph2;
  private ListView listView2 = new ListView();
  private ListView listView1 = new ListView();
  private SpotifyArtist SpotifyArtist1;
  private SpotifyArtist SpotifyArtist2;
  private SpotifySong SpotifySong1;
  private SpotifySong SpotifySong2;
  private TextField outputFileField = new TextField("output");
  private Button printOut = new Button("Print Selection to Text File");
  private String outputFileName = "";
  private ArrayList<String> textToBeWritten = new ArrayList<String>();
  private Button goBackToPrimary = new Button("Back");
  private Button skipLoadButton = new Button("Skip loading data");

  // check Boxes

  private CheckBox danceabilityCheckBox = new CheckBox("Sort by Danceability");
  private CheckBox energyCheckBox = new CheckBox("Sort by Energy");
  private CheckBox livenessCheckBox = new CheckBox("Sort by Liveness");
  private CheckBox popularityCheckBox = new CheckBox("Sort by Popularity");
  private CheckBox tempoCheckBox = new CheckBox("Sort by Tempo");
  private CheckBox valenceCheckBox = new CheckBox("Sort by Valence");

  // layout for primary view

  private VBox col1;
  private VBox col2;
  private Button findSimilarButton;
  private Scene loadScene;

  // combo boxes for artists and songs display

  private ComboBox<String> songBox1 = new ComboBox<String>();
  private ComboBox<String> songBox2 = new ComboBox<String>();
  private ComboBox<String> artistBox1 = new ComboBox<String>();
  private ComboBox<String> artistBox2 = new ComboBox<String>();

  // combobox setup requires ObervableLists for sorting

  private ObservableList<String> songs1 = FXCollections.observableArrayList();
  private ObservableList<String> songs2 = FXCollections.observableArrayList();
  private ObservableList<String> artists1 = FXCollections.observableArrayList();
  private ObservableList<String> artists2 = FXCollections.observableArrayList();


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Spotify Data Visualizer");
    primaryStage.getIcons().add(new Image("spotifyImageCustom.png"));

    // setup for grid pane

    Label song1 = new Label("Song 1:");
    Label artist1 = new Label("Artist 1:");
    Label song2 = new Label("Song 2:");
    Label artist2 = new Label("Artist 2:");
    Label data1 = new Label("Data for song/artist 1:");
    Label data2 = new Label("Data for song/artist 2:");
    findSimilarButton = new Button("Find Similar");
    Label outputFileNameLabel = new Label("File Name To Output To:");

    // css styling!!
    outputFileNameLabel.setStyle("-fx-padding: 14 0 0 0;");
    listView1.setStyle("-fx-background-color: BLACK;");
    listView2.setStyle("-fx-background-color: BLACK;");

    // sets the boxes to accept input for sorting
    songBox1.setEditable(true);
    songBox2.setEditable(true);
    artistBox1.setEditable(true);
    artistBox2.setEditable(true);

    GridPane testGridPane = new GridPane();

    col1 = new VBox(song1, songBox1, artist1, artistBox1, outputFileNameLabel, data1, listView1,
        danceabilityCheckBox, energyCheckBox, tempoCheckBox, findSimilarButton);
    col2 = new VBox(song2, songBox2, artist2, artistBox2, outputFileField, data2, listView2,
        livenessCheckBox, popularityCheckBox, valenceCheckBox, printOut);

    data2.setAlignment(Pos.BOTTOM_LEFT);
    col1.setSpacing(20);
    col2.setSpacing(20);
    testGridPane.setPadding(new Insets(10, 10, 10, 10));

    testGridPane.add(col1, 0, 0, 1, 1);
    testGridPane.add(col2, 1, 0, 1, 1);

    ColumnConstraints constraint = new ColumnConstraints();
    constraint.setHgrow(Priority.ALWAYS);
    testGridPane.getColumnConstraints().add(constraint);

    // --------------------------------------------

    // can only accept CSV files

    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));

    Button selectFileButton = new Button("Select File");

    HBox loadSceneBox = new HBox(skipLoadButton, selectFileButton);

    loadScene = new Scene(loadSceneBox, 1080, 720);

    loadScene.getStylesheets().add("/application/application.css");
    primaryStage.setScene(loadScene);
    primaryStage.show();

    Scene primaryScene = new Scene(testGridPane, 700, 900);
    primaryScene.getStylesheets().add("/application/application.css");

    EventHandler<ActionEvent> skipButtonHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        primaryStage.setScene(primaryScene);

        event.consume();
      }
    };

    skipLoadButton.setOnAction(skipButtonHandler);

    selectFileButton.setOnAction(e -> {

      try {
        runPrimaryView(primaryStage, fileChooser, primaryScene, findSimilarButton);
      } catch (InputMismatchException e1) {
        Alert badInput = new Alert(AlertType.CONFIRMATION);
        badInput.show();
        primaryStage.setScene(loadScene);
        primaryStage.show();
      }
    });

  }

  private void runPrimaryView(Stage primaryStage, FileChooser fileChooser, Scene primaryScene,
      Button findSimilar) throws InputMismatchException {

    File selectedFile = fileChooser.showOpenDialog(primaryStage);


    // does the data input as a task on a different thread, this was in hopes to stop the JavaFX
    // thread from crashing as with big enough data sets the program would go unresponsive
    Task<Void> loadData = new Task<Void>() {
      @Override
      public void run() {

        loadedCSV = new LoadCSV(selectedFile.toString());

        for (int i = 0; i < loadedCSV.getSongArray().size(); i++) {

          String tempSongName = loadedCSV.getSongArray().get(i).getName();

          if (tempSongName.length() >= 30)
            tempSongName = tempSongName.substring(0, 30);

          songs1.add(tempSongName);
          songs2.add(tempSongName);
        }

        for (int i = 0; i < loadedCSV.getArtistNames().size(); i++) {

          String tempArtistName = loadedCSV.getArtistNames().get(i);


          // some song names are super long and mess up spacing of the VBoxes
          if (tempArtistName.length() >= 30)
            tempArtistName = tempArtistName.substring(0, 30);

          artists1.add(tempArtistName);
          artists2.add(tempArtistName);
        }

        filterList(songBox1, songs1);
        filterList(songBox2, songs2);
        filterList(artistBox1, artists1);
        filterList(artistBox2, artists2);

        loadedCSV.loadArtistData();

        return;
      }

      // never used but required to compile
      @Override
      protected Void call() throws Exception {
        return null;
      }
    };

    Thread loadThread = new Thread(loadData);

    loadThread.start();

    printOut.setOnAction(action -> {
      outputFileName = outputFileField.getText() + ".txt";
      if (SpotifyArtist2 != null) {
        textToBeWritten.add("You saved the artist: " + SpotifyArtist2.getName());
      }

      if (SpotifySong2 != null) {
        textToBeWritten.add("You saved the song: " + SpotifySong2.getName() + " by: "
            + Arrays.deepToString(SpotifySong2.getArtists()));
      }
    });

    // combobox for song1, doesn't allow comparing a song to an artist

    songBox1.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {

        if (t1 == null)
          return;

        songToGraph1 = t1;

        if (artistToGraph1 != null) {
          artistToGraph1 = null;
          SpotifyArtist1 = null;
          artistBox1.getSelectionModel().clearSelection();
        }

        if (artistToGraph2 != null) {
          artistToGraph2 = null;
          SpotifyArtist2 = null;
          artistBox2.getSelectionModel().clearSelection();
        }

        // convert string song to actual Spotify Song

        for (int i = 0; i < loadedCSV.getSongArray().size(); i++) {

          if (songToGraph1.length() == 30) {

            if (loadedCSV.getSongArray().get(i).getName().contains(songToGraph1)) {
              SpotifySong1 = loadedCSV.getSongArray().get(i);
            }

          }

          if (loadedCSV.getSongArray().get(i).getName().equals(songToGraph1)) {
            SpotifySong1 = loadedCSV.getSongArray().get(i);
          }
        }

        checkSelections();

        // System.out.println("Song1: " + songToGraph1);
      }
    });

    // combobox for song2, doesn't allow comparing a song to a artist

    songBox2.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {

        if (t1 == null)
          return;

        songToGraph2 = t1;

        if (artistToGraph1 != null) {
          artistToGraph1 = null;
          SpotifyArtist1 = null;
          artistBox1.getSelectionModel().clearSelection();
        }

        if (artistToGraph2 != null) {
          artistToGraph2 = null;
          SpotifyArtist2 = null;
          artistBox2.getSelectionModel().clearSelection();
        }


        // convert string song to actual Spotify Song

        for (int i = 0; i < loadedCSV.getSongArray().size(); i++) {

          if (songToGraph2.length() == 30) {

            if (loadedCSV.getSongArray().get(i).getName().contains(songToGraph2)) {
              SpotifySong2 = loadedCSV.getSongArray().get(i);
            }

          }

          if (loadedCSV.getSongArray().get(i).getName().equals(songToGraph2)) {
            SpotifySong2 = loadedCSV.getSongArray().get(i);
          }
        }

        checkSelections();

        // System.out.println("Song2: " + songToGraph2);
      }
    });

    // combobox for artist1, doesn't allow comparing a song to an artist

    artistBox1.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {

        if (t1 == null)
          return;

        artistToGraph1 = t1;

        if (songToGraph1 != null) {
          songToGraph1 = null;
          SpotifySong1 = null;
          songBox1.getSelectionModel().clearSelection();
        }

        if (songToGraph2 != null) {
          songToGraph2 = null;
          SpotifySong2 = null;
          songBox2.getSelectionModel().clearSelection();
        }

        listView1.getItems().clear();

        // convert string song to actual Spotify Artist

        SpotifyArtist1 =
            new SpotifyArtist(artistToGraph1, loadedCSV.searchByArtistName(artistToGraph1));

        checkSelections();

        // System.out.println("Artist1: " + artistToGraph1);
      }
    });

    // combobox for artist2, doesn't allow comparing a song to a song

    artistBox2.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {

        if (t1 == null)
          return;

        artistToGraph2 = t1;

        if (songToGraph1 != null) {
          songToGraph1 = null;
          SpotifySong1 = null;
          songBox1.getSelectionModel().clearSelection();
        }

        if (songToGraph2 != null) {
          songToGraph2 = null;
          SpotifySong2 = null;
          songBox2.getSelectionModel().clearSelection();
        }

        listView2.getItems().clear();

        // convert string song to actual Spotify Artist

        SpotifyArtist2 =
            new SpotifyArtist(artistToGraph2, loadedCSV.searchByArtistName(artistToGraph2));

        checkSelections();

        // System.out.println("Artist2: " + artistToGraph2);
      }
    });


    EventHandler<ActionEvent> backButtonHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        primaryStage.setScene(primaryScene);

        event.consume();
      }
    };

    EventHandler<ActionEvent> findSimilarButtonHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        checkSelections();


        if (songToGraph1 != null && artistToGraph1 == null) {

          SpotifySong2 = findSimilarSong(SpotifySong1);

          checkSelections();

        }

        if (artistToGraph1 != null && songToGraph1 == null) {

          SpotifyArtist2 = findSimilarArtist(SpotifyArtist1);

          checkSelections();

        }

      }
    };

    findSimilarButton.setOnAction(findSimilarButtonHandler);

    goBackToPrimary.setOnAction(backButtonHandler);

    primaryStage.setScene(primaryScene);
    primaryStage.show();

  }

  // Taken from here, given a big data set the box takes a while to load so this helps.
  // https://stackoverflow.com/a/34609439

  private void filterList(ComboBox<String> cb, ObservableList<String> songs) {
    // Create a FilteredList wrapping the ObservableList.
    FilteredList<String> filteredItems = new FilteredList<String>(songs, p -> true);

    // Add a listener to the textProperty of the combobox editor. The
    // listener will simply filter the list every time the input is changed
    // as long as the user hasn't selected an item in the list.
    cb.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
      final TextField editor = cb.getEditor();
      final String selected = cb.getSelectionModel().getSelectedItem();

      // This needs run on the GUI thread to avoid the error described
      // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
      Platform.runLater(() -> {
        // If the no item in the list is selected or the selected item
        // isn't equal to the current input, we refilter the list.
        if (selected == null || !selected.equals(editor.getText())) {
          filteredItems.setPredicate(item -> {
            // We return true for any items that starts with the
            // same letters as the input. We use toUpperCase to
            // avoid case sensitivity.
            if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
              return true;
            } else {
              return false;
            }
          });
        }
      });
    });

    cb.setItems(filteredItems);
  }


  // checks the boxes to see if if a song versus an artist is being compared and resets the fields
  // to be empty to avoid a cluttered UI.
  private void checkSelections() {

    listView1.getItems().clear();
    listView2.getItems().clear();

    if (this.SpotifyArtist1 != null) {
      // graph two artists
      listView1.getItems().add("Name: " + this.SpotifyArtist1.getName());
      listView1.getItems().add("Popularity: " + this.SpotifyArtist1.getPopularity());
      listView1.getItems().add("Acousticness: " + this.SpotifyArtist1.getAcousticness());
      listView1.getItems().add("Danceability: " + this.SpotifyArtist1.getDanceability());
      listView1.getItems().add("Duration_ms: " + this.SpotifyArtist1.getDuration_ms());
      listView1.getItems().add("Energy: " + this.SpotifyArtist1.getEnergy());
      listView1.getItems().add("Instrumentalness: " + this.SpotifyArtist1.getInstrumentalness());
      listView1.getItems().add("Liveness: " + this.SpotifyArtist1.getLiveness());
      listView1.getItems().add("Speechiness: " + this.SpotifyArtist1.getSpeechiness());
      listView1.getItems().add("Tempo: " + this.SpotifyArtist1.getTempo());
      listView1.getItems().add("Valence: " + this.SpotifyArtist1.getValence());
    }

    if (this.SpotifyArtist2 != null) {
      listView2.getItems().add("Name: " + this.SpotifyArtist2.getName());
      listView2.getItems().add("Popularity: " + this.SpotifyArtist2.getPopularity());
      listView2.getItems().add("Acousticness: " + this.SpotifyArtist2.getAcousticness());
      listView2.getItems().add("Danceability: " + this.SpotifyArtist2.getDanceability());
      listView2.getItems().add("Duration_ms: " + this.SpotifyArtist2.getDuration_ms());
      listView2.getItems().add("Energy: " + this.SpotifyArtist2.getEnergy());
      listView2.getItems().add("Instrumentalness: " + this.SpotifyArtist2.getInstrumentalness());
      listView2.getItems().add("Liveness: " + this.SpotifyArtist2.getLiveness());
      listView2.getItems().add("Speechiness: " + this.SpotifyArtist2.getSpeechiness());
      listView2.getItems().add("Tempo: " + this.SpotifyArtist2.getTempo());
      listView2.getItems().add("Valence: " + this.SpotifyArtist2.getValence());
    }

    if (this.SpotifySong1 != null) {
      // graph two songs
      listView1.getItems().add("Name: " + this.SpotifySong1.getName());
      listView1.getItems().add("Artists: " + Arrays.deepToString(this.SpotifySong1.getArtists()));
      listView1.getItems().add("Popularity: " + this.SpotifySong1.getPopularity());
      listView1.getItems().add("Acousticness: " + this.SpotifySong1.getAcousticness());
      listView1.getItems().add("Danceability: " + this.SpotifySong1.getDanceability());
      listView1.getItems().add("Duration_ms: " + this.SpotifySong1.getDuration_ms());
      listView1.getItems().add("Energy: " + this.SpotifySong1.getEnergy());
      listView1.getItems().add("Instrumentalness: " + this.SpotifySong1.getInstrumentalness());
      listView1.getItems().add("Liveness: " + this.SpotifySong1.getLiveness());
      listView1.getItems().add("Speechiness: " + this.SpotifySong1.getSpeechiness());
      listView1.getItems().add("Tempo: " + this.SpotifySong1.getTempo());
      listView1.getItems().add("Valence: " + this.SpotifySong1.getValence());
      listView1.getItems().add("Key: " + this.SpotifySong1.getKey());
      listView1.getItems().add("Release Date: " + this.SpotifySong1.getRelease_date());

      if (this.SpotifySong1.isExplicit()) {
        listView1.getItems().add("Explicit: Yes");
      } else
        listView1.getItems().add("Explicit: No");
    }

    if (this.SpotifySong2 != null) {

      listView2.getItems().add("Name: " + this.SpotifySong2.getName());
      listView2.getItems().add("Artists: " + Arrays.deepToString(this.SpotifySong2.getArtists()));
      listView2.getItems().add("Popularity: " + this.SpotifySong2.getPopularity());
      listView2.getItems().add("Acousticness: " + this.SpotifySong2.getAcousticness());
      listView2.getItems().add("Danceability: " + this.SpotifySong2.getDanceability());
      listView2.getItems().add("Duration_ms: " + this.SpotifySong2.getDuration_ms());
      listView2.getItems().add("Energy: " + this.SpotifySong2.getEnergy());
      listView2.getItems().add("Instrumentalness: " + this.SpotifySong2.getInstrumentalness());
      listView2.getItems().add("Liveness: " + this.SpotifySong2.getLiveness());
      listView2.getItems().add("Speechiness: " + this.SpotifySong2.getSpeechiness());
      listView2.getItems().add("Tempo: " + this.SpotifySong2.getTempo());
      listView2.getItems().add("Valence: " + this.SpotifySong2.getValence());
      listView2.getItems().add("Key: " + this.SpotifySong2.getKey());
      listView2.getItems().add("Release Date: " + this.SpotifySong2.getRelease_date());

      if (this.SpotifySong2.isExplicit()) {
        listView2.getItems().add("Explicit: Yes");
      } else
        listView2.getItems().add("Explicit: No");
    }


  }


  // overrides the JavaFX stop method, this will write all of the cached output lines to a text file
  // of a given name
  @Override
  public void stop() {
    // should only get here if the user decides to remove the output.txt name and forgets to input a
    // new name.
    if (outputFileName.equals(""))
      outputFileName = "YouForgotToSetAFileNameSilly.txt";

    try {
      FileWriter writer = new FileWriter(outputFileName);

      for (int i = 0; i < textToBeWritten.size(); i++) {
        writer.write(textToBeWritten.get(i) + "\n");
      }

      writer.close();

    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }


  // finds a similar song for the given song1. I'm pretty proud of how this whole method came
  // together =) Picks randomly from a list of similar Songs.
  private SpotifySong findSimilarSong(SpotifySong songToFindFor) {

    listView2.getItems().clear();
    SpotifyArtist2 = null;
    SpotifySong2 = null;

    boolean compareByDanceability = danceabilityCheckBox.isSelected();
    boolean compareByEnergy = energyCheckBox.isSelected();
    boolean compareByLiveness = livenessCheckBox.isSelected();
    boolean compareByPopularity = popularityCheckBox.isSelected();
    boolean compareByTempo = tempoCheckBox.isSelected();
    boolean compareByValence = valenceCheckBox.isSelected();

    ArrayList<SpotifySong> matchingSongs = new ArrayList();

    for (int i = 0; i < loadedCSV.getSongArray().size(); i++) {

      SpotifySong songToCompare = loadedCSV.getSongArray().get(i);

      while (compareByDanceability || compareByEnergy || compareByLiveness || compareByPopularity
          || compareByTempo || compareByValence) {

        if (compareByDanceability) {

          if (Math.abs(songToFindFor.getDanceability() - songToCompare.getDanceability()) >= .05) {
            break;
          }

        }

        if (compareByEnergy) {

          if (Math.abs(songToFindFor.getEnergy() - songToCompare.getEnergy()) >= .05) {
            break;
          }

        }

        if (compareByLiveness) {

          if (Math.abs(songToFindFor.getLiveness() - songToCompare.getLiveness()) >= .05) {
            break;
          }

        }

        if (compareByPopularity) {

          if (Math.abs(songToFindFor.getPopularity() - songToCompare.getPopularity()) >= 20) {
            break;
          }

        }

        if (compareByTempo) {

          if (Math.abs(songToFindFor.getTempo() - songToCompare.getTempo()) >= 20) {
            break;
          }

        }

        if (compareByValence) {

          if (Math.abs(songToFindFor.getValence() - songToCompare.getValence()) >= .1) {
            break;
          }

        }

        matchingSongs.add(songToCompare);
        break;

      }
    }


    if (matchingSongs.size() == 0)
      return null;

    Random randGen = new Random();
    int index = randGen.nextInt(matchingSongs.size());

    if (matchingSongs.get(index).getName().equals(SpotifySong1.getName()))
      index = (index + 1) % matchingSongs.size();


    Alert testFeedback = new Alert(AlertType.INFORMATION);
    testFeedback.setContentText(
        "there were: " + (matchingSongs.size() - 1) + " matches, so i picked a random one for you");

    if (matchingSongs.size() == 1)
      testFeedback.setContentText(
          "there were: " + (matchingSongs.size() - 1) + " matches, try unchecking a sorting box");

    testFeedback.showAndWait().ifPresent(rs -> {
      if (rs == ButtonType.OK) {
      }
    });

    return matchingSongs.get(index);
  }


  // finds a similar song for the given artist1. Picks randomly from a list of similar artists.
  private SpotifyArtist findSimilarArtist(SpotifyArtist artistToFindFor) {

    listView2.getItems().clear();
    SpotifyArtist2 = null;
    SpotifySong2 = null;

    boolean compareByDanceability = danceabilityCheckBox.isSelected();
    boolean compareByEnergy = energyCheckBox.isSelected();
    boolean compareByLiveness = livenessCheckBox.isSelected();
    boolean compareByPopularity = popularityCheckBox.isSelected();
    boolean compareByTempo = tempoCheckBox.isSelected();
    boolean compareByValence = valenceCheckBox.isSelected();

    ArrayList<SpotifyArtist> matchingArtists = new ArrayList();

    for (int i = 0; i < loadedCSV.getArtistArray().size(); i++) {

      SpotifyArtist artistToCompare = loadedCSV.getArtistArray().get(i);

      while (compareByDanceability || compareByEnergy || compareByLiveness || compareByPopularity
          || compareByTempo || compareByValence) {

        if (compareByDanceability) {

          if (Math
              .abs(artistToFindFor.getDanceability() - artistToCompare.getDanceability()) >= .10) {
            break;
          }

        }

        if (compareByEnergy) {

          if (Math.abs(artistToFindFor.getEnergy() - artistToCompare.getEnergy()) >= .10) {
            break;
          }

        }

        if (compareByLiveness) {

          if (Math.abs(artistToFindFor.getLiveness() - artistToCompare.getLiveness()) >= .10) {
            break;
          }

        }

        if (compareByPopularity) {

          if (Math.abs(artistToFindFor.getPopularity() - artistToCompare.getPopularity()) >= 10) {
            break;
          }

        }

        if (compareByTempo) {

          if (Math.abs(artistToFindFor.getTempo() - artistToCompare.getTempo()) >= 4) {
            break;
          }

        }

        if (compareByValence) {

          if (Math.abs(artistToFindFor.getValence() - artistToCompare.getValence()) >= .10) {
            break;
          }

        }

        matchingArtists.add(artistToCompare);
        break;

      }
    }


    if (matchingArtists.size() == 0) {
      System.out.println("found nothing");
      return null;
    }

    Random randGen = new Random();
    int index = randGen.nextInt(matchingArtists.size());

    if (matchingArtists.get(index).getName().equals(SpotifyArtist1.getName()))
      index = (index + 1) % matchingArtists.size();

    Alert testFeedback = new Alert(AlertType.INFORMATION);
    testFeedback.setContentText("there were: " + (matchingArtists.size() - 1)
        + " matches, so i picked a random one for you");

    if (matchingArtists.size() == 1)
      testFeedback.setContentText(
          "there were: " + (matchingArtists.size() - 1) + " matches, try unchecking a sorting box");

    testFeedback.showAndWait().ifPresent(rs -> {
      if (rs == ButtonType.OK) {
      }
    });

    return matchingArtists.get(index);
  }



}


