/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.Color;
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
    Color colour;

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

    void setNewPixels() {
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        builder = new StringBuilder();
        
        for (byte b : bytes) {
            //get leading zeroes for bytes
            String format = String.format("%%0%dd", 8);
            String result = String.format(format, Integer.parseInt(Integer.toBinaryString(b)));

            builder.append(result);
        }
        bigString = builder.toString();
        System.out.println(bigString);

        for (int i = 0; i < inputImage.getWidth() -1; i++) { // KAN VARA INVERTERAD? JAG VETINTE
            for (int j = 0; j < inputImage.getHeight() -1; j++) {

                if (temp < bigString.length()) {
                    int c = bigString.charAt(temp) - 48; //either 0 or 1 
                    
                    colour = new Color(inputImage.getRGB(i, j));
                    Color finishedColour = calcNewColor(colour, c);
                    outputImage.setRGB(i, j, finishedColour.getRGB());
                    String oldcolor = Integer.toString(inputImage.getRGB(i, j),2);
                    
                    System.out.println("Red: " + colour.getRed() + " green: " + colour.getGreen() + " blue: " + colour.getBlue());
                    System.out.println("Old Pixel: " + oldcolor);
                    System.out.println("Set pixel (" + i + ", " + j + ") to " + Integer.toString(finishedColour.getRGB(),2));

                } else {
                    System.out.println("nr 1");
                    outputImage.setRGB(i, j, inputImage.getRGB(i, j)); //FEL HÄR VID STORA BILDER
                    System.out.println("nr 2");
                }
                temp++;
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

    private Color calcNewColor(Color oldcolor, int b) {
        
        if(b <= 1){

            int minus = Math.abs(oldcolor.getRGB() % 2);
            
            Color test = new Color(oldcolor.getRGB() - minus + b);
            
            System.out.println("minus = " + minus);
            System.out.println(" b = " +b);
            System.out.println(test.toString());
            return test;
            
        }
        System.out.println("ERRRRRROOOOOOOORRRRRRRR");
         return new Color(0);
    }

}
