package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Pipe;
import model.PipeFactory;

public class SingleStandardController {

    @FXML
    private ChoiceBox dimensionBox;

    @FXML
    private ChoiceBox lengthBox;

    @FXML
    private ChoiceBox soilBox;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField mediumTempField;

    @FXML
    private TextField surfaceTempField;


    @FXML
    void onSubmitClick(ActionEvent event) {
        double distance = Double.parseDouble(distanceField.getText());
        double mediumT = Double.parseDouble(mediumTempField.getText());
        double surfaceT = Double.parseDouble(surfaceTempField.getText());

        double mediumTK = celsiusToKelvin(mediumT);
        double surfaceTK = celsiusToKelvin(surfaceT);

        String dimension = (String) dimensionBox.getValue();

        PipeFactory pipeFactory = new PipeFactory();
        Pipe standardSinglePipe = pipeFactory.makeStandardSinglePipe(dimension);

        double a = Double.parseDouble((String)lengthBox.getValue()) / 1000;

        double ltrInsulation = standardSinglePipe.getLinearThermalResistance();

        String soil = (String) soilBox.getValue();
        double ltrGround = ltrGround(lambdaGround(soil), a, distance, standardSinglePipe.getExternalDiameter());

        double heatLoss = heatLoss(mediumTK, surfaceTK, ltrInsulation, ltrGround);

        System.out.println(heatLoss);

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
