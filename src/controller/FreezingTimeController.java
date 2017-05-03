package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import model.Formula;

public class FreezingTimeController {

    @FXML
    private LayerGridController layerGridController;

    @FXML
    TextField mField;

    @FXML
    TextField lengthPipeField;

    @FXML
    TextField heatCapacityField;

    @FXML
    TextField ambientTField;

    @FXML
    TextField initialTField;

    @FXML
    TextField finalTField;

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
                Bindings.isEmpty(mField.textProperty())
                        .or(Bindings.isEmpty(lengthPipeField.textProperty()))
                        .or(Bindings.isEmpty(heatCapacityField.textProperty()))
                        .or(Bindings.isEmpty(ambientTField.textProperty()))
                        .or(Bindings.isEmpty(initialTField.textProperty()))
                        .or(Bindings.isEmpty(finalTField.textProperty()))

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

        double m = Double.parseDouble(mField.getText());
        double lengthPipe = Double.parseDouble(lengthPipeField.getText());
        double heatCapacity = Double.parseDouble(heatCapacityField.getText());
        double ambient = 274.15 + Double.parseDouble(ambientTField.getText());
        double initial = 274.15 + Double.parseDouble(initialTField.getText());
        double finalTemp = 274.15 + Double.parseDouble(finalTField.getText());
        double hse = 0.01;
        if (buried.isExpanded()) {
            double surface = 274.15 + Double.parseDouble(surfaceTField.getText());
            hse = Formula.heatTransferOuterSurfaceCoefficient(surface, ambient, de);

        } else if (above.isExpanded()) {
            double windSpeed = Double.parseDouble(windField.getText());
            hse = Formula.heatTransferOuterSurfaceCoefficient(de, windSpeed);
        }
        double utl = getUtl(lengthPipe, ltr, hse, de);
        double heatFlowRate = utl * lengthPipe * (initial - ambient);
        System.out.println(heatFlowRate);
        double coolingTime = (initial - finalTemp) * m * heatCapacity * Math.log((initial - ambient) / (finalTemp - ambient));
        System.out.println(coolingTime);
        coolingTime = coolingTime / (3.6 * heatFlowRate);
        if (buried.isExpanded()) {
            String s = "Single buried cooling time (h): " + coolingTime;
            showInfo(s);
        } else if (above.isExpanded()){
            String s = "Single above-ground cooling time (h): " + coolingTime;
            showInfo(s);
        }
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
