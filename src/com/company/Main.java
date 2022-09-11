package com.company;

import com.company.crypt.Feistel;
import com.company.io.Image;
import com.company.tests.Tests;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("kodim13.bmp"));
        Scanner in = new Scanner(System.in);
        Image original = new Image(image);

        Feistel function = new Feistel();
        System.out.println("Введите 2 ключа в диапазоне [-128; 127].");
        byte[] key = new byte[] {in.nextByte(), in.nextByte()};

        System.out.println("--- Шифрование --- ");
        Image crypted = function.encrypting(original, key).clone();

        System.out.println("Тест на решетчатость"); Tests.LatticeTest(crypted, "crypted_Lattice.txt");

        System.out.println("--- Дешифрование ---");
        Image decrypted = function.decrypting(crypted, key).clone();
        decrypted.createImage("decrypted_kodim13.bmp");

        System.out.println("--- Метод грубой силы ---");
        for(byte key1 = -128; key1 < 127; key1++) {
            for(byte key2 = -128; key2 < 127; key2++) {
                Image attacked = function.decrypting(crypted, new byte[] {key1, key2});
                if (attacked.equals(decrypted)) {
                    System.out.println("Найден ключ: " + key1 + " " + key2);
                    return;
                }
            }
        }
    }
}
