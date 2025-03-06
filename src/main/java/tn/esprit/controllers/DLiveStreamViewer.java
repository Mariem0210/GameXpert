package tn.esprit.controllers;

import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class DLiveStreamViewer {

    public static void startStream(Stage stage) {
        String streamerName = "gamexpert1"; // Remplacez par le nom du streamer
        String streamUrl = "https://dlive.tv/" + streamerName;

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(streamUrl);

        stage.setScene(new Scene(webView, 800, 600));
        stage.setTitle("DLive Stream");
        stage.show();
    }
}