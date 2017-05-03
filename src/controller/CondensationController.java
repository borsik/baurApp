package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Formula;

public class CondensationController {

    @FXML
    ChoiceBox materialBox;

    @FXML
    ChoiceBox ambientBox;

    @FXML
    TextField initialTField;

    @FXML
    TextField surfaceTField;


    @FXML
    TextField deField;

    @FXML
    ChoiceBox humidityBox;

    @FXML
    TextField windField;

    @FXML
    TitledPane above;

    @FXML
    TitledPane buried;

    @FXML
    private Button btn;

    @FXML
    public void initialize() {
        btn.disableProperty().bind(
                Bindings.isEmpty(initialTField.textProperty())
                        .or(Bindings.isEmpty(initialTField.textProperty()))
                        .or(Bindings.isEmpty(deField.textProperty()))

        );
        btn.disableProperty().bind(
                (Bindings.isEmpty(windField.textProperty()))
                        .and(Bindings.isEmpty(surfaceTField.textProperty()))
        );
    }

    @FXML
    void submit(ActionEvent event) {

        double ambient = Double.parseDouble((String)ambientBox.getValue());
        double humidity = Double.parseDouble((String)humidityBox.getValue());
        double de = Double.parseDouble(deField.getText());
        double initial = 274.15 + Double.parseDouble(initialTField.getText());

        String material = (String)materialBox.getValue();
        double lambda = Formula.lambdaMaterial(material);
        double surfaceTempPipe = pipeTemp(humidity, ambient);

        if (surfaceTempPipe == 0) {
            showInfo("Humidity can't be 30 while ambient temperature is -20");
            return;
        }

        surfaceTempPipe += 274.15;
        double hse = 0.01;
        if (buried.isExpanded()) {
            hse = Formula.heatTransferOuterSurfaceCoefficient(surfaceTempPipe, ambient, de);
        } else if (above.isExpanded()) {
            double windSpeed = Double.parseDouble(windField.getText());
            hse = Formula.heatTransferOuterSurfaceCoefficient(de, windSpeed);
        }
        double minInsulationThickness = (2 * lambda / hse) * (Math.abs(initial - ambient) / Math.abs(surfaceTempPipe - ambient) - 1);
        String s = "Minimal insulation thickness (m): " + minInsulationThickness;
        showInfo(s);
    }

    public void showInfo(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }



    public double pipeTemp(double airHumidity, double airAmbientTemp) {
        double[][] pipeTempList = {{0, 10.4, 9.1, 8, 6.9, 6, 5.2, 4.5, 3.7, 2.9, 2.3, 1.7, 1.1, 0.5},
                {12.3, 10.8, 9.6, 8.3, 7.3, 6.4, 5.4, 4.6, 3.8, 3.1, 2.5, 1.8, 1.2, 0.6},
                {12.9, 11.3, 9.9, 8.7, 7.6, 6.6, 5.7, 4.8, 3.9, 3.2, 2.5, 1.8, 1.2, 0.6},
                {13.4, 11.7, 10.3, 9, 7.9, 6.8, 5.8, 5, 4.1, 3.3, 2.6, 1.9, 1.2, 0.6},
                {13.9,12.2,10.7,9.3,8.1,7.1,6,5.1,4.2,3.5,2.7,1.9,1.3,0.7},
                {14.3,12.6,11,9.7,8.5,7.4,6.4,5.4,4.6,3.8,3,2.2,1.5,0.7},
                {14.7,13,11.4,10.1,8.9,7.7,6.7,5.8,4.9,4,3.1,2.3,1.5,0.7},
                {15.1,13.4,11.8,10.4,9.2,8.1,7,6.1,5.1,4.1,3.2,2.3,1.5,0.7},
                {15.6,13.8,12.2,10.8,9.6,8.4,7.3,6.2,5.1,4.2,3.2,2.3,1.5,0.8},
                {16,14.2,12.6,11.2,10,8.6,7.4,6.3,5.2,4.2,3.3,2.4,1.6,0.8},
                {16.5,14.6,13,11.6,10.1,8.8,7.5,6.3,5.3,4.3,3.3,2.4,1.6,0.8},
                {16.9,15.1,13.4,11.7,10.3,8.9,7.6,6.5,5.4,4.3,3.4,2.5,1.6,0.8},
                {17.4,15.5,13.6,11.9,10.4,9,7.8,6.6,5.4,4.4,3.5,2.5,1.7,0.8},
                {17.8,15.7,13.8,12.1,10.6,9.2,7.9,6.7,5.6,4.5,3.5,2.6,1.7,0.8},
                {18.1,15.9,14,12.3,10.7,9.3,8,6.8,5.6,4.6,3.6,2.6,1.7,0.8},
                {18.4,16.1,14.2,12.5,10.9,9.5,8.1,6.9,5.7,4.7,3.6,2.6,1.7,0.8},
                {18.6,16.4,14.4,12.6,11.1,9.6,8.2,7,5.8,4.7,3.7,2.7,1.8,0.8},
                {18.9,16.6,14.7,12.8,11.2,9.7,8.4,7.1,5.9,4.8,3.7,2.7,1.8,0.9},
        };

        double[] airHumidityList = {30,	35,	40,	45,	50,	55,	60,	65,	70,	75,	80,	85,	90,	95};
        double[] airAmbientTempList = {-20, -15, -10, -5, 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26};

        int i, j;
        for (i = 0; i < airHumidityList.length; i++) {
            if (airHumidity == airHumidityList[i]) {
                break;
            }
        }
        for (j = 0; j < airAmbientTempList.length; j++) {
            if (airAmbientTemp == airAmbientTempList[j]) {
                break;
            }
        }
        return pipeTempList[j][i];


    }



}
