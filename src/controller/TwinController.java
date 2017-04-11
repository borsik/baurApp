package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class TwinController {

    @FXML
    private ChoiceBox dnBox;

    @FXML
    private TextField t0Field;

    @FXML
    private TextField t1Field;

    @FXML
    private TextField t2Field;

    @FXML
    private Button btn;

    @FXML
    public void initialize() {
        btn.disableProperty().bind(
                Bindings.isEmpty(t0Field.textProperty())
                        .or(Bindings.isEmpty(t1Field.textProperty()))
                                .or(Bindings.isEmpty(t2Field.textProperty()))
        );
    }

    @FXML
    void onSubmitClick(ActionEvent event) {
        double t1 = 274.15 + Double.parseDouble(t1Field.getText());
        double t2 = 274.15 + Double.parseDouble(t2Field.getText());
        double t0 = 274.15 + Double.parseDouble(t0Field.getText());

        String dimension = (String) dnBox.getValue();
        String[] dimension2List = {"DN 15","DN 20","DN 25","DN 32","DN 40","DN 50","DN 65","DN 80",
                "DN 100","DN 125","DN 150","DN 200","DN 250"};
        double[] d1List = {125, 125, 140, 160, 160, 200, 225, 250, 315, 400, 450, 400, 710};
        double[] d2List = {21.3, 26.9, 33.7, 42.4, 48.3, 60.3, 76.1, 88.9, 114.3, 139.7, 168.3, 219.1, 273};
        double[] dList = {19, 19, 19, 19, 19, 20, 20, 25, 25, 30, 40, 45, 45};

        int j = -1;
        for (int i = 0; i < dimension2List.length; i++) {
            if (dimension.equalsIgnoreCase(dimension2List[i])) {
                j = i;
                break;
            }
        }

        double r1 = d1List[j] / 2000;
        double r2 = d2List[j] / 2000;
        double d = dList[j] / 2000;

        double hS = Math.log(Math.pow(r1, 2) / (2 * d * r2)) - Math.log(Math.pow(r1, 4) / (Math.pow(r1, 4) - Math.pow(d, 4))) -
                Math.pow(((r2 / (2 * d)) + (2 * r2 * Math.pow(d, 3)) / (Math.pow(r1, 4) - Math.pow(d, 4))), 2) /
                        ((1 + Math.pow((r2 / (2 * d)), 2) - Math.pow((2 * r2 * Math.pow(r1, 2) * d / (Math.pow(r1, 4) - Math.pow(d, 4))), 2)));

        hS = 1 / hS;
        double qT = 4 * Math.PI * 0.025 * hS * ((t1 + t2) - t0);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Standardized district heating twin pipe heat loss: " + qT);
        alert.showAndWait();

    }

}
