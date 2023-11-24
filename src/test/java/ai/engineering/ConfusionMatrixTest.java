package ai.engineering;

import ai.engineering.pipeline.monitortool.DesiredPerformance.ConfusionMatrix;
import org.junit.Assert;
import org.junit.Test;


public class ConfusionMatrixTest {
    double delta = 0.0001;

    int[][] data = {
            {50, 5, 0},
            {3, 60, 2},
            {0, 8, 45}
    };

    ConfusionMatrix cm;


    public ConfusionMatrixTest() {
        cm = new ConfusionMatrix(data);
    }

    @Test
    public void getTruePositives() {
        Assert.assertEquals(50, cm.getTP(0), this.delta);
        Assert.assertEquals(60, cm.getTP(1), this.delta);
        Assert.assertEquals(45, cm.getTP(2), this.delta);
    }

    @Test
    public void getTrueNegative() {
        Assert.assertEquals(105, cm.getTN(0), this.delta);
        Assert.assertEquals(95, cm.getTN(1), this.delta);
        Assert.assertEquals(110, cm.getTN(2), this.delta);

        // todo: this seems to be wrong in the original
//        Assert.assertEquals(60+45, MetricsCalculator.calculateTrueNegative(data, 0), this.delta);
//        Assert.assertEquals(50+45, MetricsCalculator.calculateTrueNegative(data, 1), this.delta);
//        Assert.assertEquals(50+60, MetricsCalculator.calculateTrueNegative(data, 2), this.delta);
    }

    @Test
    public void getFalsePositives() {
        Assert.assertEquals(3, cm.getFP(0), this.delta);
        Assert.assertEquals(13, cm.getFP(1), this.delta);
        Assert.assertEquals(2, cm.getFP(2), this.delta);
    }

    @Test
    public void getFalseNegative() {
        Assert.assertEquals(5, cm.getFN(0), this.delta);
        Assert.assertEquals(5, cm.getFN(1), this.delta);
        Assert.assertEquals(8, cm.getFN(2), this.delta);
    }

    @Test
    public void getAccuracy() throws Exception {
        Assert.assertEquals((double) 50 / 55, cm.getAccuracy(0), this.delta);
        Assert.assertEquals((double) 60 / 65, cm.getAccuracy(1), this.delta);
        Assert.assertEquals((double) 45 / 53, cm.getAccuracy(2), this.delta);

        // todo: this seems to be wrong in the original
//        Assert.assertEquals((double) 50 /55, MetricsCalculator.calculateAccuracy(data,"0")/100, this.delta);
//        Assert.assertEquals((double) 60 /65, MetricsCalculator.calculateAccuracy(data,"1")/100, this.delta);
//        Assert.assertEquals((double) 45 /53, MetricsCalculator.calculateAccuracy(data,"2")/100, this.delta);
    }
}


