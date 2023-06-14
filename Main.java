import imgdisplay.ImgDisplay;
import imgtools.ColTransfer;
import imgtools.ImageAnalysis;
import imgtools.ImageStats;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        boolean display = false;

        String inputPath = "";
        String outputPath = "";

        String targetPath = "";

        String maskPath = "";

        BufferedImage sourceImg = null;
        BufferedImage targetImg = null;
        BufferedImage maskImg = null;

        for (String arg : args) {
            if(arg.equals("-d")) display = true;

            if(arg.startsWith("-s")) {
                inputPath = arg.split("\\(")[1].split("\\)")[0];

                sourceImg = ImageIO.read(new File(inputPath));
            }

            if(arg.startsWith("-t")) {
                targetPath = arg.split("\\(")[1].split("\\)")[0];

                targetImg = ImageIO.read(new File(targetPath));
            }

            if(arg.startsWith("-o")) {
                outputPath = arg.split("\\(")[1].split("\\)")[0];
            }

            if(arg.startsWith("-m")) {
                maskPath = arg.split("\\(")[1].split("\\)")[0];

                maskImg = ImageIO.read(new File(maskPath));
            }

            if(arg.startsWith("-b")) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://www.google.com/search?q=" + arg.split("\\(")[1].split("\\)")[0]));
                }

                Scanner scanner = new Scanner(System.in);

                String urlString = scanner.nextLine();

                URL url = new URL(urlString);

                targetImg = ImageIO.read(url);
            }

            if(arg.startsWith("-l")) {
                Scanner scanner = new Scanner(System.in);

                String urlString = scanner.nextLine();

                URL url = new URL(urlString);

                targetImg = ImageIO.read(url);
            }
        }

        ImageStats source = ImageAnalysis.analyse(sourceImg);
        ImageStats target = ImageAnalysis.analyse(targetImg);

        BufferedImage image = sourceImg;

        if(display) {
            ImgDisplay imgDisplay = new ImgDisplay(image, 1200, 780);
        }

        BufferedImage mask = maskImg;

        image = ColTransfer.transfer(image, source, target, mask);

        File file = new File(outputPath);

        try {
            ImageIO.write(image, outputPath.split("\\.")[1], file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Done!");
    }
}
