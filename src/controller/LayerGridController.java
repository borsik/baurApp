package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;

public class LayerGridController {
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

    ArrayList<Double> externalDiameterList;
    ArrayList<Double> internalDiameterList;
    ArrayList<Double> thermalConductivityList;


    @FXML
    public void initialize() {

        dE2Field.disableProperty().bind(l2Box.valueProperty().isEqualTo(""));
        dI2Field.disableProperty().bind(l2Box.valueProperty().isEqualTo(""));

        dE3Field.disableProperty().bind(l3Box.valueProperty().isEqualTo(""));
        dI3Field.disableProperty().bind(l3Box.valueProperty().isEqualTo(""));

        dE4Field.disableProperty().bind(l4Box.valueProperty().isEqualTo(""));
        dI4Field.disableProperty().bind(l4Box.valueProperty().isEqualTo(""));

        dE5Field.disableProperty().bind(l5Box.valueProperty().isEqualTo(""));
        dI5Field.disableProperty().bind(l5Box.valueProperty().isEqualTo(""));

    }

    public double getExternalDiameter() {
        return externalDiameterList.get(externalDiameterList.size() - 1);
    }

    public void collectLayers() {
        externalDiameterList = new ArrayList<>();
        internalDiameterList = new ArrayList<>();
        thermalConductivityList = new ArrayList<>();

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

        for (String s : dETextList) {
            if (!s.isEmpty()) {
                externalDiameterList.add(Double.parseDouble(s));
            }
        }

        for (String s : dITextList) {
            if (!s.isEmpty()) {
                externalDiameterList.add(Double.parseDouble(s));
            }
        }

        String[] mName = {"HDPE", "LDPE", "XLPE", "Steel", "Stainless Steel", "Glassfiber", "PP", "PVC", "PB", "Aluminium", "Copper", "PUR"};
        double[] mLambda = {0.42, 0.32, 0.38, 45, 16, 0.2, 0.22, 0.18, 0.22, 218, 390, 0.025};
        for (String s : layerMaterial) {
            if (!s.isEmpty()) {
                for (int i = 0; i < mName.length; i++) {
                    if (mName[i] == s) {
                        thermalConductivityList.add(mLambda[i]);
                    }
                }
            }
        }
    }

    public double getLTR() {
        double ltr = 0;
        for (int i = 0; i < internalDiameterList.size(); i++) {
            ltr += (1 / thermalConductivityList.get(i)) * Math.log(externalDiameterList.get(i) / internalDiameterList.get(i));
        }
        ltr = ltr / (2 * Math.PI);

        return ltr;

    }
}
