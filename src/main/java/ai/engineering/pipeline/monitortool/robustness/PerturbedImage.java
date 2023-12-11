package ai.engineering.pipeline.monitortool.robustness;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PerturbedImage extends BufferedImage {
    public final double noiseLevel;
    public PerturbedImage(BufferedImage image, double noiseLevel) {
        // Create a copy of image with the correct color profile.
        super(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.createGraphics().drawImage(image, 0, 0, null);

        // Add L2 Norm noise to the image
        this.noiseLevel = noiseLevel;
        this.addL2NormNoise();
    }

    private void addL2NormNoise() {
        double[][][] noise = new double[this.getWidth()][this.getHeight()][3];
        double sumOfSquares = 0.0;
        for (int i = 0; i < noise.length; i++) {
            for (int j = 0; j < noise[i].length; j++) {
                for (int k = 0; k < noise[i][j].length; k++) {
                    double delta = Math.random();
                    noise[i][j][k] = delta;
                    sumOfSquares += delta;
                }
            }
        }

        // scale the generated noise
        double l2Norm = Math.sqrt(sumOfSquares);
        double scalingFactor = this.noiseLevel / l2Norm;

        // Scale each element in the array
        for (int i = 0; i < noise.length; i++) {
            for (int j = 0; j < noise[i].length; j++) {
                this.setRGB(i, j, addNoise(this.getRGB(i, j), noise[i][j], scalingFactor));
            }
        }
    }

    private static int addNoise(int rgbPixel, double[] noise, double scalingFactor) {
        int red = (rgbPixel & 0xff0000) >> 16;
        int green = (rgbPixel & 0xff00) >> 8;
        int blue = rgbPixel & 0xff;

        red += (int) Math.round(noise[0] * scalingFactor);
        green += (int) Math.round(noise[1] * scalingFactor);
        blue += (int) Math.round(noise[2] * scalingFactor);

        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return (red << 16 | green << 8 | blue);
    }
}
