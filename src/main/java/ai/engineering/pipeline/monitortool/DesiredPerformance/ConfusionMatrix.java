package ai.engineering.pipeline.monitortool.DesiredPerformance;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ConfusionMatrix {
    private final int[][] data;

    public ConfusionMatrix(int[][] data) {
        this.data = data;
    }

    public int getTP(int index) {
        return this.data[index][index];
    }

    public int getTP() {
        return IntStream.range(0, this.data.length).map(this::getTP).sum();
    }


    public int getTN(int index) {
        return getTP() - getTP(index);
    }

    public int getTN() {
        return IntStream.range(0, this.data.length).map(this::getTN).sum();
    }

    public int getFP(int index) {
        return this.getPredictedTotal(index) - this.data[index][index];
    }

    public int getFP() {
        return IntStream.range(0, this.data.length).map(this::getFP).sum();
    }

    public int getFN(int index) {
        int rowSum = Arrays.stream(this.data[index]).sum();
        return rowSum - this.data[index][index];
    }

    public int getFN() {
        return IntStream.range(0, this.data.length).map(this::getFN).sum();
    }

    public int getActualTotal(int index) {
        return Arrays.stream(this.data[index]).sum();
    }

    public int getPredictedTotal(int index) {
        return Arrays.stream(this.data).mapToInt(row -> row[index]).sum();
    }

    public int getTotal() {
        return IntStream.range(0, this.data.length).map(this::getFN).sum();
    }
}
