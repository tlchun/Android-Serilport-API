package android.serialport.sample.models;

import java.io.Serializable;

/**
 * Created by lc on 2018/11/16.
 */

public class WeightModel implements Serializable {

    private float pivot_weight;
    private float weight_deviation;

    public WeightModel(float pivot_weight,float weight_deviation){
        this.pivot_weight = pivot_weight;
        this.weight_deviation = weight_deviation;
    }

    public float getPivot_weight() {
        return pivot_weight;
    }

    public void setPivot_weight(float pivot_weight) {
        this.pivot_weight = pivot_weight;
    }

    public float getWeight_deviation() {
        return weight_deviation;
    }

    public void setWeight_deviation(float weight_deviation) {
        this.weight_deviation = weight_deviation;
    }

    @Override
    public String toString() {
        return "WeightModel{" +
                "pivot_weight=" + pivot_weight +
                ", weight_deviation=" + weight_deviation +
                '}';
    }
}
