package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Formula;

public class TempChangeController {

    @FXML
    private LayerGridController layerGridController;

    @FXML
    TextField mfrField;

    @FXML
    TextField lengthPipeField;

    @FXML
    TextField heatCapacityField;

    @FXML
    TextField ambientTField;

    @FXML
    TextField initialTField;

    @FXML
    TextField surfaceTField;

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
        layerGridController.initialize();
        btn.disableProperty().bind(
                Bindings.isEmpty(mfrField.textProperty())
                        .or(Bindings.isEmpty(lengthPipeField.textProperty()))
                        .or(Bindings.isEmpty(heatCapacityField.textProperty()))
                        .or(Bindings.isEmpty(ambientTField.textProperty()))
                        .or(Bindings.isEmpty(initialTField.textProperty()))

        );
        btn.disableProperty().bind(
                (Bindings.isEmpty(windField.textProperty()))
                        .and(Bindings.isEmpty(surfaceTField.textProperty()))
        );
    }

    @FXML
    void submit(ActionEvent event) {
        layerGridController.collectLayers();
        double de = layerGridController.getExternalDiameter();
        double ltr = layerGridController.getLTR();

        double m = Double.parseDouble(mfrField.getText());
        double lengthPipe = Double.parseDouble(lengthPipeField.getText());
        double heatCapacity = Double.parseDouble(heatCapacityField.getText());
        double ambient = 274.15 + Double.parseDouble(ambientTField.getText());
        double initial = 274.15 + Double.parseDouble(initialTField.getText());
        double hse = 0.01;
        if (buried.isExpanded()) {
            double surface = 274.15 + Double.parseDouble(surfaceTField.getText());
            hse = Formula.heatTransferOuterSurfaceCoefficient(surface, ambient, de);

        } else if (above.isExpanded()) {
            double windSpeed = Double.parseDouble(windField.getText());
            hse = Formula.heatTransferOuterSurfaceCoefficient(de, windSpeed);
        }
        double utl = getUtl(lengthPipe, ltr, hse, de);
        double alpha = getAlpha(utl, m, heatCapacity);
        double finalTemp = Math.abs(initial - ambient) * Math.exp(-alpha * lengthPipe);
        double tempChange = initial - finalTemp;
        if (buried.isExpanded()) {
            String s = "Single, buried temperature change: " + tempChange;
            showInfo(s);
        } else if (above.isExpanded()){
            String s = "Single, above-ground temperature change: " + tempChange;
            showInfo(s);
        }
    }



    public double surfaceLTR(double de, double windSpeed) {
        double hse;
        if (de > 0.25) {
            hse = 0.0081 / de + Math.PI * Math.sqrt(windSpeed / de);
        } else {
            hse = 3.96 * Math.sqrt(windSpeed / de);
        }
        return 1 / (hse * Math.PI * de);
    }

    public double getAlpha(double utl, double m, double heatCapacity) {
        return (utl * 3.6) / (m * heatCapacity);

    }

    public double getUtl(double l, double ltr, double hse, double de) {
        double ubl = 1 / l;
        double ul = 1 / (ltr + 1 / (hse * Math.PI * de));
        return ubl + ul;
    }


    public void showInfo(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }



}
