module fr.fleyg.banking {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.media;
    requires transitive java.compiler;
    opens fr.fleyg.banking to javafx.fxml;
    exports fr.fleyg.banking;
}