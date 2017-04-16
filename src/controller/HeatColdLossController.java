package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Formula;

import java.util.ArrayList;
import java.util.HashMap;

public class HeatColdLossController {
    @FXML
    ChoiceBox l1Box;

    @FXML
    ChoiceBox l2Box;

    @FXML
    ChoiceBox l3Box;

    @FXML
    ChoiceBox l4Box;

    @FXML
    ChoiceBox l5Box;

    @FXML
    TextField dE1Field;

    @FXML
    TextField dE2Field;

    @FXML
    TextField dE3Field;

    @FXML
    TextField dE4Field;

    @FXML
    TextField dE5Field;

    @FXML
    TextField dI1Field;

    @FXML
    TextField dI2Field;

    @FXML
    TextField dI3Field;

    @FXML
    TextField dI4Field;

    @FXML
    TextField dI5Field;

    @FXML
    TextField windField;

    @FXML
    TextField tField;

    @FXML
    TextField t0Field;

    @FXML
    ChoiceBox soilBox;

    @FXML
    ChoiceBox lengthBox;

    @FXML
    TextField distanceField;

    @FXML
    TitledPane above;

    @FXML
    TitledPane buried;

    @FXML
    private Button btn;

    @FXML
    public void initialize() {
        btn.disableProperty().bind(
                Bindings.isEmpty(t0Field.textProperty())
                        .or(Bindings.isEmpty(tField.textProperty()))
                        .or(Bindings.isEmpty(dE1Field.textProperty()))
                        .or(Bindings.isEmpty(dI1Field.textProperty()))
        );

        dE2Field.disableProperty().bind(l2Box.valueProperty().isEqualTo(""));
        dI2Field.disableProperty().bind(l2Box.valueProperty().isEqualTo(""));

        dE3Field.disableProperty().bind(l3Box.valueProperty().isEqualTo(""));
        dI3Field.disableProperty().bind(l3Box.valueProperty().isEqualTo(""));

        dE4Field.disableProperty().bind(l4Box.valueProperty().isEqualTo(""));
        dI4Field.disableProperty().bind(l4Box.valueProperty().isEqualTo(""));

        dE5Field.disableProperty().bind(l5Box.valueProperty().isEqualTo(""));
        dI5Field.disableProperty().bind(l5Box.valueProperty().isEqualTo(""));

    }

    @FXML
    void submit(ActionEvent event) {
        ArrayList<String> layerMaterial = new ArrayList<>();
        layerMaterial.add((String)l1Box.getValue());
        layerMaterial.add((String)l2Box.getValue());
        layerMaterial.add((String)l3Box.getValue());
        layerMaterial.add((String)l4Box.getValue());
        layerMaterial.add((String)l5Box.getValue());

        ArrayList<String> dETextList = new ArrayList<>();
        dETextList.add(dE1Field.getText());
        dETextList.add(dE2Field.getText());
        dETextList.add(dE3Field.getText());
        dETextList.add(dE4Field.getText());
        dETextList.add(dE5Field.getText());

        ArrayList<String> dITextList = new ArrayList<>();
        dETextList.add(dI1Field.getText());
        dETextList.add(dI2Field.getText());
        dETextList.add(dI3Field.getText());
        dETextList.add(dI4Field.getText());
        dETextList.add(dI5Field.getText());

        ArrayList<Double> dEList = new ArrayList<>();
        for (String s : dETextList) {
            if (!s.isEmpty()) {
                dEList.add(Double.parseDouble(s));
            }
        }

        ArrayList<Double> dIList = new ArrayList<>();
        for (String s : dITextList) {
            if (!s.isEmpty()) {
                dIList.add(Double.parseDouble(s));
            }
        }

        ArrayList<Double> labmdaMaterial = new ArrayList<>();
        String[] mName = {"HDPE", "LDPE", "XLPE", "Steel", "Stainless Steel", "Glassfiber", "PP", "PVC", "PB", "Aluminium", "Copper", "PUR"};
        double[] mLambda = {0.42, 0.32, 0.38, 45, 16, 0.2, 0.22, 0.18, 0.22, 218, 390, 0.025};
        for (String s : layerMaterial) {
            if (!s.isEmpty()) {
                for (int i = 0; i < mName.length; i++) {
                    if (mName[i] == s) {
                        labmdaMaterial.add(mLambda[i]);
                    }
                }
            }
        }

        double ltr = 0;
        for (int i = 0; i < dIList.size(); i++) {
            ltr += (1 / labmdaMaterial.get(i)) * Math.log(dEList.get(i) / dIList.get(i));
        }
        ltr = ltr / (2 * Math.PI);

        double t = 274.15 + Double.parseDouble(tField.getText());
        double t0 = 274.15 + Double.parseDouble(t0Field.getText());

        if (buried.isExpanded()) {
            double lambdaGround = Formula.lambdaGround((String)soilBox.getValue());
            double a = Double.parseDouble((String)lengthBox.getValue()) / 1000;
            double distance = Double.parseDouble(distanceField.getText());
            double ltrGround = Formula.ltrGround(lambdaGround, a, distance, dEList.get(dEList.size() - 1));
            double heatLoss = Formula.heatLoss(t, t0, ltr, ltrGround);
            String s = "Single buried heat/cold loss: " + heatLoss;
            showInfo(s);

        } else if (above.isExpanded()) {
            double windSpeed = Double.parseDouble(windField.getText());
            double dE = dEList.get(dEList.size() - 1);
            double ltrSurfce = surfaceLTR(dE, windSpeed);
            double heatLoss = (t - t0) / (ltr - ltrSurfce);
            String s = "Single above-ground heat/cold loss: " + heatLoss;
            showInfo(s);
        }





    }

    public double surfaceLTR(double extDiameter, double windSpeed) {
        double hSE;
        if (extDiameter > 0.25) {
            hSE = 0.0081 / extDiameter + Math.PI * Math.sqrt(windSpeed / extDiameter);
        } else {
            hSE = 3.96 * Math.sqrt(windSpeed / extDiameter);
        }
        return 1 / (hSE * Math.PI * extDiameter);
    }

    public void showInfo(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }



}
