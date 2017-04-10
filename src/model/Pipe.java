package model;

import java.util.LinkedList;

/**
 * Created by olzhas on 10/04/2017.
 */
public class Pipe {
    private LinkedList<Layer> layers;

    public Pipe() {
        this.layers = new LinkedList<>();
    }

    public void add(Layer layer) {
        layers.add(layer);
    }

    public void add(double lambda, double externalDiameter, double internalDiameter) {
        layers.add(new Layer(lambda, externalDiameter, internalDiameter));
    }

    public double getExternalDiameter() {
        return layers.getLast().externalDiameter;
    }

    //ltr is the linear thermal resistance [mK/W] given by Equation (2)
    public double getLinearThermalResistance() {
        double ltr = 0;
        for (Layer l: layers) {
            ltr += (1 / l.lambda) * Math.log(l.externalDiameter / l.internalDiameter);
        }
        return ltr / (2 * Math.PI);
    }
}
