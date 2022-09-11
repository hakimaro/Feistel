package com.company.crypt;

import com.company.io.Image;

import java.util.Random;

public class Feistel {
    private int r = 10; // Количество раундов. Больше ставить нет смысл

    /**
     * Шифрование
     * @param data - исходные данные
     * @param key - ключ
     * @return - закодированные исходные данные
     */
    public Image encrypting(Image data, byte[] key) {
        Image res = data.clone();
        int[] S = generateS(key);
        char[] block = new char[2];
        int[][] position = new int[2][3];
        int idx = 0;
        for (int r = 0; r < this.r; r++) {
            for (int i = 0; i < data.W; i++) {
                for (int j = 0; j < data.H; j++) {
                    for (int color = 0; color < 3; color++) {
                        if (idx == block.length) {
                            block = encryptBlock(block, r, S[r]);
                            res.data[position[0][0]][position[0][1]][position[0][2]] = block[0];
                            res.data[position[1][0]][position[1][1]][position[1][2]] = block[1];
                            idx = 0;
                        }
                        block[idx] = res.data[i][j][color];
                        position[idx][0] = i;
                        position[idx][1] = j;
                        position[idx][2] = color;
                        idx++;
                    }
                }
            }
            res.createImage("Encrypted_R=" + r + ".bmp");
        }
        return res;
    }

    /**
     * Шифрование блока
     *
     * @param block - блок размерности 2
     * @param round - номер раунда
     * @param S     - ключ-значение
     * @return - зашифрованный блок
     */
    private char[] encryptBlock(char[] block, int round, int S) {
        char A = block[0];
        char B = block[1];
        A ^= (F(B, S, round));
        A = get8Bits(A);
        return new char[]{B, A};
    }

    /**
     * Дешифрование
     * @param data - закодированные данные
     * @param key - ключ
     * @return - декодированные данные
     */
    public Image decrypting(Image data, byte[] key) {
        Image res = data.clone();
        int[] S = generateS(key);
        char[] block = new char[2];
        int[][] position = new int[2][3];
        int idx = 0;
        for (int i = 0; i < data.W; i++) {
            for (int j = 0; j < data.H; j++) {
                for (int color = 0; color < 3; color++) {
                    if (idx == block.length) {
                        block = decryptBlock(block, S);
                        res.data[position[0][0]][position[0][1]][position[0][2]] = block[0];
                        res.data[position[1][0]][position[1][1]][position[1][2]] = block[1];
                        idx = 0;
                    }
                    block[idx] = res.data[i][j][color];
                    position[idx][0] = i;
                    position[idx][1] = j;
                    position[idx][2] = color;
                    idx++;
                }
            }
        }
        return res;
    }

    /**
     * Декодирование блока
     * @param block - закодированный блок
     * @param S - массив ключ-значений
     * @return - декодированный блок
     */
    private char[] decryptBlock(char[] block, int[] S) {
        char A = block[1];
        char B = block[0];
        for (int i = r - 1; i >= 0; i--) {
            A ^= (F(B, S[i], i));
            A = get8Bits(A);
            char tmp = A;
            A = B;
            B = tmp;
        }
        return new char[]{B, A};
    }

    /**
     * Функция шифрования
     *
     * @param value - шифруемое значение
     * @param S     - ключ-значение
     * @param round - номер раунда
     * @return - шифрованное value
     */
    private byte F(int value, int S, int round) {
        return (byte) rightShift(value, leftShift(S | round, (S | ~value) & ~S) | round);
    }

    /**
     * Генератор ключей
     *
     * @param key - изначальный ключ
     * @return - сгенерированный новый ключ на r-раундов.
     */
    private int[] generateS(byte[] key) {
        int[] S = new int[r];
        Random generator_1 = new Random(key[0]);
        for (int i = 0; i < S.length / 2; i++) {
            S[i] = generator_1.nextInt();
        }
        Random generator_2 = new Random(key[1]);
        for (int i = S.length / 2; i < S.length; i++) {
            S[i] = generator_2.nextInt();
        }

        for (int i = 0; i < S.length; i++) {
            S[i] = ((S[i] ^ (S[i] & key[i % key.length])));
        }
        return S;
    }

    /**
     * Взять 8 первых бит
     * @param value - число, у которого берется 8 бит
     * @return - первые 8 бит числа в виде числа :)
     */
    char get8Bits(char value) {
        char res = 0;
        for(int i = 0; i < 8; i++) {
            res |= (((value >> i) & 1) << i);
        }
        return res;
    }

    /**
     * Циклический сдвиг вправо
     *
     * @param val - число, которое сдвигаем
     * @param pos - количество позиций, на которое сдвигаем
     * @return
     */
    private int rightShift(int val, int pos) {
        return (val >>> pos) | (val << (8 - pos));
    }

    /**
     * Циклический сдвиг влево
     *
     * @param val - число, которое сдвигаем
     * @param pos - количество позиций, на которое сдвигаем
     * @return
     */
    private int leftShift(int val, int pos) {
        return (val << pos) | (val >>> (8 - pos));
    }
}
