package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class Controller {
    ObservableList<String> dimensionList = FXCollections.observableArrayList("DN 15", "DN 20");

    @FXML
    private ChoiceBox dimensionBox;

    @FXML
    void onSubmitClick(ActionEvent event) {

        System.out.println(dimensionBox.getValue());
    }

}
