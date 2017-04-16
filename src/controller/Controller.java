package controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Controller {

    @FXML private TabPane tabPane;

    @FXML
    private Tab singleStandardTab;
    @FXML
    private Tab doubleStandardTab;

    @FXML
    private Tab heatColdLosslTab;

    // Inject tab controller
    @FXML
    private SingleController singleController;

    @FXML
    private TwinController twinController;

    @FXML HeatColdLossController heatColdLossController;




    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable,
                                                                        Tab oldValue, Tab newValue) -> {
            Stage stage = (Stage) tabPane.getScene().getWindow();


        });

    }

}
