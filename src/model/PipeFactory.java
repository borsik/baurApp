package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by olzhas on 10/04/2017.
 */
public class PipeFactory {

    private String[] dimensionList = {"DN 15","DN 20","DN 25","DN 32","DN 40","DN 50","DN 65","DN 80",
            "DN 100","DN 125","DN 150","DN 200","DN 250","DN 300",
            "DN 350","DN 400","DN 450","DN 500","DN 600","DN 700","DN 800","DN 900","DN 1000"};

    private double[] l1externalDiameter = {21.3, 26.9, 33.7, 42.4, 48.3, 60.3, 76.1, 88.9, 114.3,
            139.7, 168.3, 219.1, 273, 323.9, 355.6, 406.4, 457, 508, 610, 711, 813, 914, 1016, 1219
    };

    private double[] l1internalDiameter = {17.3, 22.9, 29.1, 37.2, 43.1, 54.5, 70.3, 82.5, 107.1, 132.5, 160.3, 210.1,
            263, 312.7, 344.4, 393.8, 444.4, 495.4, 595.8, 695, 795.4, 894, 994, 1194};

    private double[] l2externalDiameter = {90, 110, 125, 140, 160, 180, 200, 225, 250, 280, 315, 355,
            400, 450, 500, 560, 630, 710, 800, 900, 1000, 1100, 1200, 1400};

    private double[] l2internalDiameter = {21.3, 26.9, 33.7, 42.4, 48.3, 60.3, 76.1, 88.9, 114.3, 139.7, 168.3, 219.1,
            273, 323.9, 355.6, 406.4, 457, 508, 610, 711, 813, 914, 1016, 1219};

    private double[] l3externalDiameter = {90, 110, 125, 140, 160, 180, 200, 225, 250, 280, 315, 355,
            400, 450, 500, 560, 630, 710, 800, 900, 1000, 1100, 1200, 1400};

    private double[] l3internalDiameter = {84, 104, 119, 134, 154, 174, 193.6, 218.2, 242.8, 272.2, 306.8, 346, 390.4,
            439.6, 488.8, 548, 616.8, 695.6, 784.2, 882.6, 981.2, 1079.6, 1178, 1375};


    public Pipe makeStandardSinglePipe(String dimension) {
        Pipe pipe = new Pipe();
        int index = -1;
        for (int i = 0; i < dimensionList.length; i++) {
            if (dimension.equalsIgnoreCase(dimensionList[i])) {
                index = i;
                break;
            }
        }
        pipe.add(45, l1externalDiameter[index], l1internalDiameter[index]);
        pipe.add(0.025, l2externalDiameter[index], l2internalDiameter[index]);
        pipe.add(0.42, l3externalDiameter[index], l3internalDiameter[index]);
        return pipe;
    }



}
