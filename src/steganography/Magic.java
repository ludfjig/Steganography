/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ludvig
 */
public class Magic {

    BufferedImage inputImage;
    BufferedImage outputImage;
    int[][] pixelColors;
    BitSet bits;
    String msg;
    String bigString;
    StringBuilder builder;
    int temp = 0;
    File inputFile;
    File saveFile;

    void doMagic(File file, File saveFile, String msg) {
        this.inputFile = file;
        this.msg = msg;
        this.saveFile = saveFile;
        try {
            inputImage = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(Magic.class.getName()).log(Level.SEVERE, null, ex);
        }
        outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), TYPE_INT_ARGB);
        pixelColors = new int[inputImage.getWidth()][inputImage.getHeight()];

        for (int i = 0; i < inputImage.getWidth(); i++) {
            for (int j = 0; j < inputImage.getHeight(); j++) {
                pixelColors[i][j] = getPixelAt(i, j);
            }
        }

//        int color = pixelColors[9][0];
//
//        System.out.println(Integer.toBinaryString(color));
//        char c = 'a';
//        int ch = 97;
//
//        int colorAlpha = (color >> 24) & 0xFF;
//        int colorRed = (color >> 16) & 0xFF;
//        int colorGreen = (color >> 8) & 0xFF;
//        int colorBlue = (color) & 0xFF;
//        System.out.println("Alpha: \t" + Integer.toBinaryString(colorAlpha));
//        System.out.println("Red: \t" + Integer.toBinaryString(colorRed));
//        System.out.println("Green: \t" + Integer.toBinaryString(colorGreen));
//        System.out.println("Blue: \t" + Integer.toBinaryString(colorBlue));
        setNewPixels();

    }

    int getPixelAt(int x, int y) {
        return inputImage.getRGB(x, y);
    }

    void setNewPixels() {
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        builder = new StringBuilder();
        for (byte b : bytes) {

            String format = String.format("%%0%dd", 8);
            String result = String.format(format, Integer.parseInt(Integer.toBinaryString(b)));

            builder.append(result);
        }
        bigString = builder.toString();
        System.out.println(bigString);

        loop:
        for (int i = 0; i < inputImage.getHeight(); i++) { // KAN VARA INVERTERAD? JAG VETINTE
            for (int j = 0; j < inputImage.getWidth(); j++) {

                if (temp < bigString.length()) {
                    int c = bigString.charAt(temp) - 48;
                    outputImage.setRGB(i, j, inputImage.getRGB(i, j) - (inputImage.getRGB(i, j) % 2) + c);
                    System.out.println("Set pixel (" + i + ", " + j +") to " + inputImage.getRGB(i, j));
                    temp++;
                    
                } else {
                    outputImage.setRGB(i, j, inputImage.getRGB(i, j) /*& c*/);
                    
                }
            }
        }
        try {
            ImageIO.write(outputImage, "png", saveFile);
        } catch (IOException ex) {
            Logger.getLogger(Magic.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Done");
    }

    String getTextFromImage(BufferedImage image) {
        StringBuilder builder2 = new StringBuilder();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                builder2.append(image.getRGB(i, j) & 1);
            }
        }
        String finishedString = builder2.toString();
        char[] chars = new char[finishedString.length() / 8];
        int temp2 = 0;

        for (int i = 0; i < finishedString.length(); i += 8) {
            if (i + 8 <= finishedString.length()) {
                System.out.println(i + " " + (i + 8));
                System.out.println(finishedString.substring(i, i + 8));
                System.out.println(Integer.parseInt(finishedString.substring(i, i + 8), 2));
                System.out.println((char) Integer.parseInt(finishedString.substring(i, i + 8), 2));
                chars[temp2] = (char) Integer.parseInt(finishedString.substring(i, i + 8), 2);
            }
            temp2++;
        }

        return new String(chars);

    }

}
