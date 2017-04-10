package sample;

/**
 * Created by olzhas on 09/04/2017.
 */
public class Layer {
    int dimension;
    float outerDiameter;
    float minThickness;

    public float innerDiameter() {
        return outerDiameter - 2 * minThickness;
    }

    public Layer(int dimension, float outerDiameter, float minThickness) {
        this.dimension = dimension;
        this.outerDiameter = outerDiameter;
        this.minThickness = minThickness;
    }


}
