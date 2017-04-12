package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
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
        double ltrGround = ltrGround(lambdaGround(soil), a, distance, standardSinglePipe.getExternalDiameter());

        double heatLoss = heatLoss(mediumTK, surfaceTK, ltrInsulation, ltrGround);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Standardized district heating single pipe heat loss: " + heatLoss);
        alert.showAndWait();

    }

    //dN is the square cross section of the outer most layer (the soil) considered with an equivalent diameter calculated by Equation (6)
    public double dN(double a) {
        return 1.073 * a;
    }

    public double celsiusToKelvin(double temp) {
        return 274.15 + temp;
    }

    //hE is the distance between the centre of the pipe and the ground surface [m] calculated by Equation (5)
    public double hE(double h, double dE) {
        return h + dE / 2;
    }

    public double ltrGround(double lambdaE, double a, double h, double dE) {
        double dN = dN(a);
        double hE = hE(h, dE);
        return (1 / (2 * Math.PI * lambdaE)) * 1 / (Math.cosh(2 * hE / dN));
    }

    public double lambdaGround(String type) {
        double lambda;
        switch (type) {
            case "Dry":
                lambda = 0.92;
                break;

            case "Frozen":
                lambda = 0.93;
                break;

            case "Saturated with water":
                lambda = 0.95;
                break;

            default:
                lambda = 0.92;
                break;
        }
        return lambda;
    }

    public double heatLoss(double mediumT, double surfaceT, double ltrInsulation, double ltrGround) {
        return (mediumT + surfaceT) / (ltrInsulation + ltrGround);
    }

}
