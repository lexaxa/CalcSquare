package ru.alexis.calcsquare;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 Описание задачи
 Условия к рассмотрению решения
 - язык программирования Java
 - запуск приложения через public static void main(String args[])
 - args[] 2 параметра: первый - файл входных данных, второй - файл для записи ответа.
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
 Дополнительные условия:
 - Объем используемой памяти не должен превышать 16мб.
 - Должна быть проверка на корректность передаваемых параметров (args[]).
 - Должна быть проверка input.txt на корректность формата.
 Примеры:
 INPUT:         OUTPUT:     INPUT:         OUTPUT:
 1 1 7 7        36          1 1 3 3        7
 2 2 4 4
 */

public class testApp {

    private static final short LAYER_SIZE = 2000;
    private static BufferedReader is;
    private static BufferedWriter out;
    private static int square = 0;                // calculated square
    private static boolean isCheckAllFile = true; // if finded wrong line than continue next check
    private static short[] arrSC = new short[4];

    public static void main(String[] args) {

        Runtime r = Runtime.getRuntime();
        System.out.println("Mem (" + r.totalMemory()/1024 +"): " + r.freeMemory()/1024);

        if(!(args.length == 2 && args[0].equalsIgnoreCase("input.txt") && args[1].equalsIgnoreCase("output.txt"))){
            System.out.println("Wrong input parameters. Must entered input.txt and output.txt files.");
            return;
        }

        String line;
        ArrayList<String> lines = new ArrayList<String>(100);
        //short arr[][] = new short[20][20];
        short arr[][] = new short[2000][2000];
        short arrcoord[] = new short[4];
        short x1,x2,y1,y2;

        Pattern p = Pattern.compile("^(-?\\d+\\s){3}-?\\d+\\s?$");
        Matcher m = p.matcher("");

        System.out.println("Mem (" + r.totalMemory()/1024 +"): " + r.freeMemory()/1024);
        System.out.println("Start calc square");

        try {
            is = new BufferedReader(new FileReader(args[0]));
            out = new BufferedWriter(new FileWriter(args[1]));

            StringBuilder stringBuilder = new StringBuilder();

            // check for wrong lines and add to array
            while ((line = is.readLine()) != null) {
                m.reset(line);
                if(!m.matches()){
                    System.out.println("Find wrong line #" + line);
                    if(!isCheckAllFile) return;
                }else{
                    System.out.println("Ok line #" + line);

                    lines.add(line);
                    stringBuilder.append(line);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < lines.size(); i++) {

            line = lines.get(i);
            System.out.println(line);
            String c[] = line.split(" ");

            x1 = Short.parseShort(c[0]);
            y1 = Short.parseShort(c[1]);
            x2 = Short.parseShort(c[2]);
            y2 = Short.parseShort(c[3]);

            for (short j = 0; j <= 3; j++) {
                arrSC[j] = (short)(Short.parseShort(c[j])+10000);
            }
            if (arrSC[2] < arrSC[0]){
                short res = arrSC[0];
                arrSC[0] = arrSC[2];
                arrSC[2] = res;
            }
            if (arrSC[3] < arrSC[1]){
                short res = arrSC[1];
                arrSC[1] = arrSC[3];
                arrSC[3] = res;
            }
//            System.out.println(getLayer(x1*1010) + "x"+(x1*1010%2000)+"=" + getLayer(y1*1010)+"x"+(y1*1010%2000));
//            System.out.println(getLayer(x2*1010) + "x"+(x2*1010%2000)+"=" + getLayer(y2*1010)+"x"+(y2*1010%2000));
            if(square > 0 && x1>=arrcoord[0] && x2<=arrcoord[2] & y1>=arrcoord[1] & y2<=arrcoord[3]){
                // this figure less than previous. Nothing to calc
            }else {
                calcRect(arr, arrSC);
            }

            arrcoord[0] = x1;
            arrcoord[1] = y1;
            arrcoord[2] = x2;
            arrcoord[3] = y2;
        }

        try {
            out.write(square + "");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Finally square is " + square);

        printArray(arr);

        Runtime r2 = Runtime.getRuntime();
        System.out.println("Mem (" + r2.totalMemory()/1024 +"): " + r2.freeMemory()/1024);
    }
    private static void calcRect(short[][] arr, short[] arrSC) {


        // coord within layer
        short jx;
        short ky;
        /*
        for (int i = 0; i <= 3; i++) {
            arrWL[i] = getCoord(arrSC[i]);
        }
        */

        System.out.println("("+arrSC[0]+","+arrSC[1]+")x("+arrSC[2]+","+arrSC[3]+")");


        for (short j = arrSC[0]; j < arrSC[2]; j++) {
            for (short k = arrSC[1]; k < arrSC[3]; k++) {

                //        5 layer - \/    \/ - 0 layer
                //arr[j][k] = 0b00_0010_0001
                short layer = getLayer(arrSC[0]);
                short checkbit = (short)(1 << layer); // ex. 0b00_0010_0000
                jx = getCoord(j);
                ky = getCoord(k);
                if (!isExistBit(arr[jx][ky],checkbit)) {
                    arr[jx][ky] = setBit(arr[jx][ky], checkbit);
                    square++;
                }
            }
        }
    }

    private static void printArray(short[][] arr) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(Integer.toBinaryString(arr[i][j])+" ");
            }
            System.out.println();
        }
    }
    private static byte getLayer(int val){
        return (byte)(val / LAYER_SIZE);
    }
    private static short getCoord(short coord){
        return (short)(coord % LAYER_SIZE);
    }
    private static short setBit(short src, short newBit){
        //set need bit        x or 0x0000
        return (short)(src | newBit);
    }
    private static boolean isExistBit(short src, short newBit){
        //check, exist bit or no        x and 0xFFFF = x
        return ((src & newBit) == newBit);
    }
    private static short resetBit(short src, short newBit){
        //change bit to 0        x xor 0x0010
        return (short)(src ^ newBit);
    }
}
