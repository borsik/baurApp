package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Pipe;
import model.PipeFactory;

public class DoubleStandardController {

    @FXML
    private ChoiceBox dimensionBox1;

    @FXML
    private TextField distanceField1;

    @FXML
    private TextField surfaceTField;

    @FXML
    private TextField mediumT1Field;

    @FXML
    private TextField mediumT2Field;



    @FXML
    void onSubmitClick(ActionEvent event) {
        double distance = Double.parseDouble(distanceField1.getText());
        double mediumT1 = Double.parseDouble(mediumT1Field.getText());
        double mediumT2 = Double.parseDouble(mediumT2Field.getText());
        double surfaceT = Double.parseDouble(surfaceTField.getText());

        double mediumTK1 = celsiusToKelvin(mediumT1);
        double mediumTK2 = celsiusToKelvin(mediumT1);
        double surfaceTK = celsiusToKelvin(surfaceT);

        String dimension = (String) dimensionBox1.getValue();

        PipeFactory pipeFactory = new PipeFactory();
        Pipe standardSinglePipe = pipeFactory.makeStandardSinglePipe(dimension);


        double ltrInsulation = standardSinglePipe.getLinearThermalResistance();


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
