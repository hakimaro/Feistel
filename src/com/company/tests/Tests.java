package com.company.tests;

import com.company.io.Image;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tests {

    /**
     * Тест на решётчатость
     * @param data - данные, по которым делается тест
     * @param filename - название файла
     * @throws IOException
     */
    public static void LatticeTest(Image data, String filename) throws IOException {
        File file = new File(filename);
        FileWriter writer = new FileWriter(file);
        int pos = 0;
        for(int i = 0; i < data.W; i++) {
            for(int j = 0; j < data.H; j++) {
                for(int k = 0; k < 3; k++) {
                    if (pos % 2 == 0 && pos > 0) {
                        writer.append("\n");
                        writer.flush();
                    }
                    writer.append(Integer.toString(toInt(data.data[i][j][k]))).append(" ");
                    pos++;
                }
            }
        }
    }

    static int toInt(char value) {
        int res = value;
        return res;
    }
}
