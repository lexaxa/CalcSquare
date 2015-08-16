package ru.alexis.calcsquare;

import java.io.*;
import java.util.ArrayList;

/**
Описание задачи
Условия к рассмотрению решения
? язык программирования Java
? запуск приложения через public static void main(String args[ ])
? args[] 2 параметра: первый - файл входных данных, второй - файл для записи ответа.
 java testApp input.txt output.txt
Задача
Дано N прямоугольников со сторонами, параллельными осям координат. Требуется
определить площадь фигуры, образованной объединением данных прямоугольников.
Входные данные
Входной файл (первый параметр вызова), в котором идет N строк, содержащих по 4 числа: x1,
y1, x2, y2 - координаты двух противоположных углов прямоугольника. Все координаты –
целые числа, не превосходящие по абсолютной величине 10 000. (1 <= N <= 100)
Выходные данные
В выходной файл (второй параметр вызова) выведите одно целое число – площадь фигуры.
Дополнительные условия
? Объем используемой памяти не должен превышать 16мб.
? Должна быть проверка на корректность передаваемых параметров (args[]).
? Должна быть проверка input.txt на корректность формата.
Примеры
 INPUT:
 1 1 7 7
 OUTPUT:
 36
 INPUT:
 1 1 3 3
 2 2 4 4
 OUTPUT:
 7
 */


public class testApp {

    private static BufferedReader is;
    private static BufferedWriter out;
    private static long square = 0;

    public static void main(String[] args) {

        String line = "";
        int arr[][] = new int[100][100];
        int arrcoord[] = new int[4];

        System.out.println("Start calc square");
        try {
            is = new BufferedReader(new FileReader(args[0]));
            out = new BufferedWriter(new FileWriter(args[1]));

            while ((line = is.readLine()) != null) {
                System.out.println(line);
                String c[] = line.split(" ");

                int coord1 = Integer.parseInt(c[0]);
                int coord2 = Integer.parseInt(c[1]);
                int coord3 = Integer.parseInt(c[2]);
                int coord4 = Integer.parseInt(c[3]);

                if(square > 0 && arrcoord[0]>=coord1 && arrcoord[2]<=coord3 & arrcoord[1]>=coord2 & arrcoord[3]<=coord4){
                    // this figure less than previous. Nothing to calc
                }else {
                    for (int j = coord1; j < coord3; j++) {
                        for (int k = coord2; k < coord4; k++) {
                            if (arr[j][k] == 0) {
                                square++;
                            } else {
                                arr[j][k] = 1;
                            }
                        }
                    }
                }

                arrcoord[0] = coord1;
                arrcoord[1] = coord2;
                arrcoord[2] = coord3;
                arrcoord[3] = coord4;

            }

            out.write(square + "");
/*            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    System.out.print(arr[i][j]+" ");
                }
                System.out.println();
            }
*/
            System.out.println("Finally square is " + square);

        }catch(Exception e) {
            e.printStackTrace();
        }

        try {
            if(is != null){
                is.close();
            }
            if(out != null){
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
