package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;

public class Main extends Application {

  LoadCSV loadedCSV;
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
  // private ArrayList<SpotifySong> incorrectArtistList1 = new ArrayList<SpotifySong>();
  // private ArrayList<SpotifySong> incorrectArtistList2 = new ArrayList<SpotifySong>();
  TextField outputFileField = new TextField("output");
  Button printOut = new Button("Print Selection to Text File");
  String outputFileName = "";
  ArrayList<String> textToBeWritten = new ArrayList<String>();

  GridPane graphGridPane = new GridPane();
  Scene graphScene = new Scene(graphGridPane, 1080, 720);
  Button goBackToPrimary = new Button("Back");

  // chart stuff

   NumberAxis xAxis = new NumberAxis("Values for X-Axis", 0, 3, 1);
   NumberAxis yAxis = new NumberAxis("Values for Y-Axis", 0, 3, 1);

  // line chart example

   ObservableList<XYChart.Series<Double, Double>> lineChartData =
   FXCollections.observableArrayList(
   new LineChart.Series<Double, Double>("Series 1", FXCollections.observableArrayList(
   new XYChart.Data<Double, Double>(0.0, 1.0), new XYChart.Data<Double, Double>(1.2, 1.4),
   new XYChart.Data<Double, Double>(2.2, 1.9), new XYChart.Data<Double, Double>(2.7, 2.3),
   new XYChart.Data<Double, Double>(2.9, 0.5))),
   new LineChart.Series<Double, Double>("Series 2", FXCollections.observableArrayList(
   new XYChart.Data<Double, Double>(0.0, 1.6), new XYChart.Data<Double, Double>(0.8, 0.4),
   new XYChart.Data<Double, Double>(1.4, 2.9), new XYChart.Data<Double, Double>(2.1, 1.3),
   new XYChart.Data<Double, Double>(2.6, 0.9))));
   LineChart chart = new LineChart(xAxis, yAxis, lineChartData);

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Spotify Data Visualizer");
    // primaryStage.initStyle(StageStyle.UTILITY);
    primaryStage.getIcons().add(new Image("spotifyImageCustom.png"));

    // setup for grid pane

    Label song1 = new Label("Song 1:");
    Label artist1 = new Label("Artist 1:");
    Label song2 = new Label("Song 2:");
    Label artist2 = new Label("Artist 2:");
    // Button findSimilarSong = new Button("Find Similar Song");
    // Button findSimilarArtist = new Button("Find Similar Artist");
    Label data1 = new Label("Data for song/artist 1:");
    Label data2 = new Label("Data for song/artist 2:");
    Button graphData = new Button("Graph Data");
    Label outputFileNameLabel = new Label("File Name To Output To:");


    // css styling!!
    outputFileNameLabel.setStyle("-fx-padding: 14 0 0 0;");
    listView1.setStyle("-fx-background-color: BLACK;");
    listView2.setStyle("-fx-background-color: BLACK;");

    // combobox setup requires ObervableLists for sorting

    ComboBox<String> cb = new ComboBox<String>();
    ComboBox<String> cb2 = new ComboBox<String>();
    ComboBox<String> cb3 = new ComboBox<String>();
    ComboBox<String> cb4 = new ComboBox<String>();
    cb.setEditable(true);
    cb2.setEditable(true);
    cb3.setEditable(true);
    cb4.setEditable(true);
    ObservableList<String> songs1 = FXCollections.observableArrayList();
    ObservableList<String> songs2 = FXCollections.observableArrayList();
    ObservableList<String> artists1 = FXCollections.observableArrayList();
    ObservableList<String> artists2 = FXCollections.observableArrayList();


    // test Gridpane with vBoxes

    GridPane testGridPane = new GridPane();

    VBox col1 = new VBox(song1, cb, artist1, cb3, outputFileNameLabel, data1, listView1, graphData);
    VBox col2 = new VBox(song2, cb2, artist2, cb4, outputFileField, data2, listView2, printOut);
    data2.setAlignment(Pos.BOTTOM_LEFT);
    col1.setSpacing(20);
    col2.setSpacing(20);
    testGridPane.setPadding(new Insets(10, 10, 10, 10));

    testGridPane.add(col1, 0, 0, 1, 1);
    testGridPane.add(col2, 1, 0, 1, 1);

    // debugging layout line since im lazy like that
    // testGridPane.setGridLinesVisible(true);

    ColumnConstraints constraint = new ColumnConstraints();
    constraint.setHgrow(Priority.ALWAYS);
    testGridPane.getColumnConstraints().add(constraint);

    // --------------------------------------------


    filterList(cb, songs1);
    filterList(cb2, songs2);
    filterList(cb3, artists1);
    filterList(cb4, artists2);



    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));

    Button selectFileButton = new Button("Select File");
    Scene loadScene = new Scene(selectFileButton, 1080, 720);

    loadScene.getStylesheets().add("/application/application.css");

    primaryStage.setScene(loadScene);
    primaryStage.show();



    Scene primaryScene = new Scene(testGridPane, 700, 700);
    primaryScene.getStylesheets().add("/application/application.css");

    selectFileButton.setOnAction(e -> {
      runPrimaryView(cb2, cb3, cb4, primaryStage, cb, songs1, songs2, fileChooser, primaryScene,
          artists1, artists2, graphData);
    });



  }

  private void runPrimaryView(ComboBox<String> cb2, ComboBox<String> cb3, ComboBox<String> cb4,
      Stage primaryStage, ComboBox<String> cb, ObservableList<String> songs,
      ObservableList<String> songs2, FileChooser fileChooser, Scene primaryScene,
      ObservableList<String> artists, ObservableList<String> artists2, Button graphData) {
    File selectedFile = fileChooser.showOpenDialog(primaryStage);
    loadedCSV = new LoadCSV(selectedFile.toString());

    for (int i = 0; i < loadedCSV.getSongArray().size(); i++) {

      String tempSongName = loadedCSV.getSongArray().get(i).getName();

      if (tempSongName.length() >= 30)
        tempSongName = tempSongName.substring(0, 30);

      songs.add(tempSongName);
      songs2.add(tempSongName);
    }

    for (int i = 0; i < loadedCSV.getArtistNames().size(); i++) {

      String tempArtistName = loadedCSV.getArtistNames().get(i);


      // some song names are super long and mess up spacing of the VBoxes
      if (tempArtistName.length() >= 30)
        tempArtistName = tempArtistName.substring(0, 30);

      artists.add(tempArtistName);
      artists2.add(tempArtistName);
    }

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

    cb.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {
        songToGraph1 = t1;

        if (artistToGraph1 != null) {
          artistToGraph1 = null;
          SpotifyArtist1 = null;
        }

        if (artistToGraph2 != null) {
          artistToGraph2 = null;
          SpotifyArtist2 = null;
        }

        // convert string song to actual Spotify Song

        for (int i = 0; i < loadedCSV.getSongArray().size(); i++) {

          if (loadedCSV.getSongArray().get(i).getName().equals(songToGraph1)) {
            SpotifySong1 = loadedCSV.getSongArray().get(i);
          }
        }

        checkSelections();

        System.out.println("Song1: " + songToGraph1);
      }
    });

    // combobox for artist1, doesn't allow comparing a song to a song

    cb2.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {
        songToGraph2 = t1;

        if (artistToGraph1 != null) {
          artistToGraph1 = null;
          SpotifyArtist1 = null;
        }

        if (artistToGraph2 != null) {
          artistToGraph2 = null;
          SpotifyArtist2 = null;
        }


        // convert string song to actual Spotify Song

        for (int i = 0; i < loadedCSV.getSongArray().size(); i++) {

          if (loadedCSV.getSongArray().get(i).getName().equals(songToGraph2)) {
            SpotifySong2 = loadedCSV.getSongArray().get(i);
          }
        }

        checkSelections();

        System.out.println("Song2: " + songToGraph2);
      }
    });

    // combobox for song2, doesn't allow comparing a song to an artist

    cb3.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {
        artistToGraph1 = t1;

        if (songToGraph1 != null) {
          songToGraph1 = null;
          SpotifySong1 = null;
        }

        if (songToGraph2 != null) {
          songToGraph2 = null;
          SpotifySong2 = null;
        }

        listView1.getItems().clear();

        // convert string song to actual Spotify Artist

        SpotifyArtist1 =
            new SpotifyArtist(artistToGraph1, loadedCSV.searchByArtistName(artistToGraph1));

        checkSelections();

        System.out.println("Artist1: " + artistToGraph1);
      }
    });

    // combobox for artist2, doesn't allow comparing a song to a song

    cb4.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String t1) {
        artistToGraph2 = t1;

        if (songToGraph1 != null) {
          songToGraph1 = null;
          SpotifySong1 = null;
        }

        if (songToGraph2 != null) {
          songToGraph2 = null;
          SpotifySong2 = null;
        }

        listView2.getItems().clear();

        // convert string song to actual Spotify Artist

        SpotifyArtist2 =
            new SpotifyArtist(artistToGraph2, loadedCSV.searchByArtistName(artistToGraph2));

        checkSelections();

        System.out.println("Artist2: " + artistToGraph2);
      }
    });

    // TODO still need to do this buttonHandler

    EventHandler<ActionEvent> graphHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // System.out.print("songToGraph1: " + songToGraph1 + "\n" + "songToGraph2: " + songToGraph2
        // + "\n" + "artistToGraph1: " + artistToGraph1 + "\n" + "artistToGraph1: "
        // + artistToGraph2 + "\n");

        BorderPane borderPane = new BorderPane();

        borderPane.setBottom(goBackToPrimary);
         borderPane.setCenter(chart);


        // HBox test123 = new HBox(chart, goBackToPrimary);

        Scene testGraph = new Scene(borderPane, 1080, 720);

        primaryStage.setScene(testGraph);



        event.consume();
      }
    };

    EventHandler<ActionEvent> backButtonHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        primaryStage.setScene(primaryScene);

        event.consume();
      }
    };


    goBackToPrimary.setOnAction(backButtonHandler);

    graphData.setOnAction(graphHandler);


    primaryStage.setScene(primaryScene);
    primaryStage.show();
  }

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

  @Override
  public void stop() {
    System.out.println("Stage is closing");



    if (outputFileName.equals(""))
      outputFileName = "YouForgotToSetAFileNameSilly.txt";

    try {
      FileWriter writer = new FileWriter(outputFileName);

      for (int i = 0; i < textToBeWritten.size(); i++) {
        writer.write(textToBeWritten.get(i) + "\n");
      }

      writer.close();

    } catch (IOException exception) {

    }



    // Save file
  }

  public void ImageBarChartSample() {

    String imageBarChartCss =
        ImageBarChartSample.class.getResource("ImageBarChart.css").toExternalForm();

    BarChart barChart = new BarChart(new CategoryAxis(), new NumberAxis());
    barChart.setLegendVisible(false);
    barChart.getStylesheets().add(imageBarChartCss);

    barChart.getData()
        .add(new XYChart.Series<String, Integer>("Sales Per Product",
            FXCollections.observableArrayList(new XYChart.Data<String, Integer>("SUV", 120),
                new XYChart.Data<String, Integer>("Sedan", 50),
                new XYChart.Data<String, Integer>("Truck", 180),
                new XYChart.Data<String, Integer>("Van", 20))));

    graphScene = new Scene(barChart, 350, 300);
    graphScene.getStylesheets()
        .add(ImageBarChartSample.class.getResource("ImageBarChart.css").toString());
  }



}


