package com.company.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Image {
    public BufferedImage image;
    public int W;
    public int H;
    public char[][][] data;

    /**
     * Конструктор класса. Берет все основные данные из изображения
     * Этот класс сделан, чтобы удобнее работать с данными и последующим созданием изображения.
     * image - это способ хранения объекта для последующего создания изображения.
     * W - ширина изображения.
     * H - высота изображения.
     * data - набор пикселей. Трехмерный, так как на каждый пиксель приходится 3 цвета.
     * @param image - данные об изображении.
     */
    public Image(BufferedImage image) {
        this.image = image;
        W = image.getWidth();
        H = image.getHeight();
        data = new char[W][H][3];
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                Color color = new Color(image.getRGB(i, j));
                data[i][j][0] = (char) color.getRed();
                data[i][j][1] = (char) color.getGreen();
                data[i][j][2] = (char) color.getBlue();
            }
        }
    }

    /**
     * Создание изображения. Заносит данные в новое изображение
     * Работает с полем data и image.
     * @param filename - имя файла
     */
    public void createImage(String filename) {
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                try {
                    Color color = new Color(data[i][j][0], data[i][j][1], data[i][j][2]);
                    image.setRGB(i, j, color.getRGB());
                } catch (IllegalArgumentException e) {
                    for(int k = 0; k < 3; k++) {
                        System.out.print(data[i][j][k] + ", ");
                    }
                    System.out.println();
                }
            }
        }
        try {
            ImageIO.write(image, "bmp", new File(filename));
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }

    public Image clone() {
        Image clone = new Image(image);
        for(int i = 0; i < W; i++) {
            for(int j = 0; j < H; j++) {
                System.arraycopy(data[i][j], 0, clone.data[i][j], 0, 3);
            }
        }
        return clone;
    }

    public boolean equals(Image image) {
        for(int i = 0; i < W; i++) {
            for(int j = 0; j < H; j++) {
                for(int k = 0; k < 3; k++) {
                    if (data[i][j][k] != image.data[i][j][k]) return false;
                }
            }
        }
        return true;
    }

    public double[][] getColor(int color) {
        double[][] res = new double[W][H];
        for(int i = 0; i < W; i++) {
            for(int j = 0; j < H; j++) {
                res[i][j] = data[i][j][color];
            }
        }
        return res;
    }

    public ArrayList<Boolean> toBits() {
        ArrayList<Boolean> bits = new ArrayList<>();
        for(int i = 0; i < W; i++) {
            for(int j = 0; j < H; j++) {
                for(int color = 0; color < 3; color++) {
                    for(int b = 7; b >= 0; b--) {
                        int bit = (data[i][j][color] >> b) & 1;
                        bits.add(bit == 1);
                    }
                }
            }
        }
        return bits;
    }
}
