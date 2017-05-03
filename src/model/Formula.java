package model;

import java.util.HashMap;

/**
 * Created by olzhas on 12/04/2017.
 */
public class Formula {

    public static double heatLoss(double mediumT, double surfaceT, double ltrInsulation, double ltrGround) {
        return (mediumT + surfaceT) / (ltrInsulation + ltrGround);
    }

    public static double ltrGround(double lambdaE, double a, double h, double dE) {
        double dN = dN(a);
        double hE = hE(h, dE);
        return (1 / (2 * Math.PI * lambdaE)) * 1 / (Math.cosh(2 * hE / dN));
    }

    //dN is the square cross section of the outer most layer (the soil) considered with an equivalent diameter calculated by Equation (6)
    public static double dN(double a) {
        return 1.073 * a;
    }

    //hE is the distance between the centre of the pipe and the ground surface [m] calculated by Equation (5)
    public static double hE(double h, double dE) {
        return h + dE / 2;
    }

    public static double lambdaGround(String type) {
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


    public static double lambdaMaterial(String material) {
        String[] mName = {"HDPE", "LDPE", "XLPE", "Steel", "Stainless Steel", "Glassfiber", "PP", "PVC", "PB", "Aluminium", "Copper", "PUR"};
        double[] mLambda = {0.42, 0.32, 0.38, 45, 16, 0.2, 0.22, 0.18, 0.22, 218, 390, 0.025};
        for (int i = 0; i < mName.length; i++) {
            if (mName[i].equals(material)) {
                return mLambda[i];
            }
        }
        return 0;
    }

    public static double heatTransferOuterSurfaceCoefficient(double extDiameter, double windSpeed) {
        if (extDiameter <= 0.25) {
            return 0.0081 / extDiameter + Math.PI * Math.sqrt(windSpeed / extDiameter);
        } else {
            return 3.96 * Math.sqrt(windSpeed / extDiameter);
        }
    }

    public static double heatTransferOuterSurfaceCoefficient(double externalDiameter, double surfaceTemperature, double ambientTemperature) {
        double hSE = Math.pow(Math.abs(surfaceTemperature - ambientTemperature) / externalDiameter, 0.25);
        if (externalDiameter <= 0.25) {
            return 1.25 * hSE;
        } else {
            return 1.32 * hSE;
        }
    }


}
