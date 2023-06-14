package imgtools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageAnalysis {
    static Color rgbToYCbCr(Color RGB)
    {
        float R = RGB.getRed();
        float G = RGB.getGreen();
        float B = RGB.getBlue();

        float Y = (float)(16 + (65.738 * R + 129.057 * G + 25.064 * B) / 256);
        float Cb = (float)(128 + (-37.945 * R - 74.494 * G + 112.439 * B) / 256);
        float Cr = (float)(128 + (112.439 * R - 94.154 * G - 18.285 * B) / 256);

        Y = Math.max(0, Math.min(255, Y));
        Cb = Math.max(0, Math.min(255, Cb));
        Cr = Math.max(0, Math.min(255, Cr));

        return new Color((int) Y, (int) Cb, (int) Cr);
    }

    public static Color YCbCrToRGB(Color color)
    {
        double Y = color.getRed();
        double Cb = color.getGreen();
        double Cr = color.getBlue();

        int r = (int)(Y + 1.402 * (Cr - 128));
        int g = (int)(Y - 0.344136 * (Cb - 128) - 0.714136 * (Cr - 128));
        int b = (int)(Y + 1.772 * (Cb - 128));

        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        return new Color(r, g, b);
    }

    public static ImageStats analyse(BufferedImage image) {
        double averageRed = 0;
        double averageGreen = 0;
        double averageBlue = 0;

        int count = image.getHeight() * image.getWidth();

        for(int i = 0; i < image.getHeight(); i += 1) {
            for(int j = 0; j < image.getWidth(); j += 1) {
                Color col = new Color(image.getRGB(j, i));
                col = rgbToYCbCr(col);

                averageRed += col.getRed();
                averageGreen += col.getGreen();
                averageBlue += col.getBlue();
            }
        }

        averageRed /= count;
        averageGreen /= count;
        averageBlue /= count;

        double redVariance = 0;
        double greenVariance = 0;
        double blueVariance = 0;

        for(int i = 0; i < image.getHeight(); i += 1) {
            for(int j = 0; j < image.getWidth(); j += 1) {
                Color col = new Color(image.getRGB(j, i));
                col = rgbToYCbCr(col);

                int red = col.getRed();
                int green = col.getGreen();
                int blue = col.getBlue();

                double diffRed = red - averageRed;
                double diffGreen = green - averageGreen;
                double diffBlue = blue - averageBlue;

                diffRed *= diffRed;
                diffGreen *= diffGreen;
                diffBlue *= diffBlue;

                redVariance += diffRed;
                greenVariance += diffGreen;
                blueVariance += diffBlue;
            }
        }

        redVariance /= (count + 0f);
        greenVariance /= (count + 0f);
        blueVariance /= (count + 0f);

        redVariance = Math.sqrt(redVariance);
        greenVariance = Math.sqrt(greenVariance);
        blueVariance = Math.sqrt(blueVariance);

        return new ImageStats(
                averageRed,
                averageGreen,
                averageBlue,

                redVariance,
                greenVariance,
                blueVariance
        );
    }
}
