module eu.telecomnancy.flashcard {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.google.gson;
    requires javafx.media;
    requires com.google.api.client.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires dropbox.core.sdk;
    requires com.fasterxml.jackson.core;

    opens eu.telecomnancy.flashcard to javafx.fxml, com.google.gson;
    exports eu.telecomnancy.flashcard;


}
