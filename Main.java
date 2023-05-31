import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputPath = "";
        String outputPath = "";

        String targetPath = "";

        String maskPath = "";

        for (String arg : args) {
            if(arg.startsWith("-s")) {
                inputPath = arg.split("\\(")[1].split("\\)")[0];
            }

            if(arg.startsWith("-t")) {
                targetPath = arg.split("\\(")[1].split("\\)")[0];
            }

            if(arg.startsWith("-o")) {
                outputPath = arg.split("\\(")[1].split("\\)")[0];
            }

            if(arg.startsWith("-m")) {
                maskPath = arg.split("\\(")[1].split("\\)")[0];
            }
        }

        ImageStats source = ImageAnalysis.analyse(inputPath);
        ImageStats target = ImageAnalysis.analyse(targetPath);

        BufferedImage image;

        BufferedImage mask = ImageIO.read(new File(maskPath));

        try {
            image = ImageIO.read(new File(inputPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < image.getHeight(); i ++) {
            for(int j = 0; j < image.getWidth(); j ++) {
                Color col = new Color(image.getRGB(j, i));

                Color converted = ImageAnalysis.rgbToYCbCr(col);

                float[] color = new float[3];

                color[0] = converted.getRed();
                color[1] = converted.getGreen();
                color[2] = converted.getBlue();

                color[0] -= source.averageRed;
                color[1] -= source.averageGreen;
                color[2] -= source.averageBlue;

                color[0] *= target.redVariance / source.redVariance;
                color[1] *= target.greenVariance / source.greenVariance;
                color[2] *= target.blueVariance / source.blueVariance;

                color[0] += target.averageRed;
                color[1] += target.averageGreen;
                color[2] += target.averageBlue;

                color[0] = Math.max(0, Math.min(255, color[0]));
                color[1] = Math.max(0, Math.min(255, color[1]));
                color[2] = Math.max(0, Math.min(255, color[2]));

                Color before = new Color((int) color[0], (int) color[1], (int) color[2]);

                Color result = ImageAnalysis.YCbCrToRGB(before);

                float[] hsbValues = new float[3];
                Color.RGBtoHSB(result.getRed(), result.getGreen(), result.getBlue(), hsbValues);

                float[] hsbValues2 = new float[3];
                Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), hsbValues2);

                Color finalColor = new Color(Color.HSBtoRGB(hsbValues[0], hsbValues[1], hsbValues2[2]));

                Color maskColor = new Color(mask.getRGB(j, i));

                Color interpolated = new Color(
                        (int) Math.max(0, Math.min(255, maskColor.getRed() * (finalColor.getRed() / 255.0) + (255 - maskColor.getRed()) / 255.0 * col.getRed())),
                        (int) Math.max(0, Math.min(255, maskColor.getGreen() * (finalColor.getGreen() / 255.0) + (255 - maskColor.getGreen()) / 255.0 * col.getGreen())),
                        (int) Math.max(0, Math.min(255, maskColor.getBlue() * (finalColor.getBlue() / 255.0) + (255 - maskColor.getBlue()) / 255.0 * col.getBlue()))
                );

                image.setRGB(j, i, interpolated.getRGB());
            }
        }

        File file = new File(outputPath);

        try {
            ImageIO.write(image, outputPath.split("\\.")[1], file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Done!");
    }
}
