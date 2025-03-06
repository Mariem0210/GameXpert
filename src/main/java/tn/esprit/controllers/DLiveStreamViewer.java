package tn.esprit.controllers;

import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class DLiveStreamViewer {

    public static void startStream(Stage stage) {
        String streamerName = "MohamedAmineDebbich"; // Nom d'utilisateur de la chaîne YouTube
        String apiKey = "AIzaSyBozwkkP7_KMDWaEN59WRct7D3V9N5_hHs"; // La clé API YouTube fournie
        String streamUrl = "https://www.youtube.com/embed/live_stream?channel=" + streamerName + "&autoplay=1&key=" + apiKey;

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(streamUrl);

        stage.setScene(new Scene(webView, 800, 600));
        stage.setTitle("YouTube Live Stream");
        stage.show();
    }
}
