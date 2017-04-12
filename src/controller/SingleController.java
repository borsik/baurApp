package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Formula;
import model.Pipe;
import model.PipeFactory;

public class SingleController {

    @FXML
    private ChoiceBox dnBox;

    @FXML
    private ChoiceBox lengthBox;

    @FXML
    private ChoiceBox soilBox;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField tField1;

    @FXML
    private TextField t0Field;

    @FXML
    private Button btn;

    @FXML
    public void initialize() {

        btn.disableProperty().bind(
                Bindings.isEmpty(t0Field.textProperty())
                        .or(Bindings.isEmpty(tField1.textProperty()))
                        .or(Bindings.isEmpty(distanceField.textProperty()))
        );


    }


    @FXML
    void onSubmitClick(ActionEvent event) {
        double distance = Double.parseDouble(distanceField.getText());
        double mediumT = Double.parseDouble(tField1.getText());
        double surfaceT = Double.parseDouble(t0Field.getText());

        double mediumTK = celsiusToKelvin(mediumT);
        double surfaceTK = celsiusToKelvin(surfaceT);

        String dimension = (String) dnBox.getValue();

        PipeFactory pipeFactory = new PipeFactory();
        Pipe standardSinglePipe = pipeFactory.makeStandardSinglePipe(dimension);

        double a = Double.parseDouble((String)lengthBox.getValue()) / 1000;

        double ltrInsulation = standardSinglePipe.getLinearThermalResistance();

        String soil = (String) soilBox.getValue();
        double ltrGround = Formula.ltrGround(Formula.lambdaGround(soil), a, distance, standardSinglePipe.getExternalDiameter());

        double heatLoss = Formula.heatLoss(mediumTK, surfaceTK, ltrInsulation, ltrGround);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Standardized district heating single pipe heat loss: " + heatLoss);
        alert.showAndWait();

    }

    public double celsiusToKelvin(double temp) {
        return 274.15 + temp;
    }

}
