package controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Controller {

    @FXML private TabPane tabPane;

    @FXML
    private Tab singleStandardTab;
    @FXML
    private Tab doubleStandardTab;

    // Inject tab controller
    @FXML
    private SingleController singleController;

    @FXML
    private TwinController twinController;




    public void init() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable,
                                                                        Tab oldValue, Tab newValue) -> {
            if (newValue == singleStandardTab) {
                System.out.println("- 2.Tab bar -");
                System.out.println("xxx_tab2bar_xxxController=" + singleController); //if =null => inject problem
            } else if (newValue == doubleStandardTab) {
                System.out.println("- 1.Tab foo -");
                System.out.println("xxx_tab1foo_xxxController=" + twinController); //if =null => inject problem
            } else {
                System.out.println("- another Tab -");
            }
        });

    }

}
