package ru.alexis.calcsquare;

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

public class testApp2 {

    private static final short LAYER_SIZE = 2000;
    private static final short MAX_VALUE = 10000;
    private static final byte LAYER_LIMIT = 20;
    private static int square = 0;                // calculated square
    private static boolean isCheckAllFile = true; // if fond wrong line than continue to next check
    private static short[] arrSC = new short[4];
    private static short arrcoord[] = new short[4];
    private static int arr[][] = new int[LAYER_SIZE][LAYER_SIZE];
    private static BufferedReader is;
    private static BufferedWriter out;

    public static void main(String[] args) {

        Runtime r = Runtime.getRuntime();
        System.out.println("Mem (" + r.totalMemory()/1024 +"kb): " + r.freeMemory()/1024 + "kb: used "
        + (r.totalMemory()/1024 - r.freeMemory()/1024)+ "kb");

        if(!(args.length == 2 && args[0].equalsIgnoreCase("input.txt") && args[1].equalsIgnoreCase("output.txt"))){
            System.out.println("Wrong input parameters. Must entered input.txt and output.txt files.");
            return;
        }

        String line;
        ArrayList<String> lines = new ArrayList<String>(100);

        Pattern p = Pattern.compile("^((-?(([1]0{4})|(\\d\\d{0,3})))\\s){3}-?(([1]0{4})|(\\d\\d{0,3}))\\s?$");
        //Pattern p = Pattern.compile("^(-?((\\d\\d{0,3})|([1]0{0,4}))\\s){3}-?\\d{1,4}\\s?$");
        //Pattern p = Pattern.compile("^(-?\\d+\\s){3}-?\\d+\\s?$");
        Matcher m = p.matcher("");

        System.out.println("Start calc square");

        try {
            is = new BufferedReader(new FileReader(args[0]));
            out = new BufferedWriter(new FileWriter(args[1]));

            // check for wrong lines and add correct lines to array
            while ((line = is.readLine()) != null) {
                m.reset(line);
                if(!m.matches()){
                    System.out.println("Find wrong line #" + line);
                    if(!isCheckAllFile) return;
                }else{
                    System.out.println("Ok line #" + line);
                    short[] res = getRightRect(line);
                    StringBuilder sb = new StringBuilder();
                    for (byte j = 0; j <= 3; j++) {
                        sb.append(res[j] + MAX_VALUE + " ");
                    }
                    lines.add(sb.toString());
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

        shell(lines);
        System.out.println(lines);

        for (int i = 0; i < lines.size(); i++) {
            calcRect(lines.get(i));
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

        System.out.println("Mem (" + r.totalMemory()/1024 +"kb): " + r.freeMemory()/1024 + "kb: used "
                + (r.totalMemory()/1024 - r.freeMemory()/1024) + "kb");

    }
    // from rosettacode.org/
    public static void shell(ArrayList<String> a) {
        int increment = a.size() / 2;
        while (increment > 0) {
            for (int i = increment; i < a.size(); i++) {
                int j = i;
                int temp = Integer.parseInt(a.get(i).split(" ")[0]);
                String stemp = a.get(i);
                while (j >= increment && Integer.parseInt(a.get(j - increment).split(" ")[0]) > temp) {
                    a.set(j, a.get(j - increment));
                    j = j - increment;
                }
                a.set(j, stemp);
            }
            if (increment == 2) {
                increment = 1;
            } else {
                increment *= (5.0 / 11);
            }
        }
    }
    private static void calcRect(String line) {

        // coord within layer
        short xx; // ex. arrSC[0] = 7000, jx = 1000, layer=3, 0 <= jx < 2000
        short yy;
        short layer;

        getRightRect(line);

        if(square > 0 && arrSC[0]>=arrcoord[0] && arrSC[2]<=arrcoord[2] & arrSC[1]>=arrcoord[1] & arrSC[3]<=arrcoord[3]){
            // this figure less than previous. Nothing to calc.
            return;
        }

        //TODO for enchanced performance save prev rect and compare it with next rects
        // ??? write prev coord for analyze on next step
        for (int i = 0; i < 4; i++) {
            arrcoord[i] = arrSC[i];
        }

       System.out.println("("+arrSC[0]+","+arrSC[1]+")x("+arrSC[2]+","+arrSC[3]+")");

        for (short y = arrSC[1]; y < arrSC[3]; y++) {

            if(y==5000) {
                y = 5000;
            }


            if(y>=6000 && y % 6000 == 0){
                System.out.println("Reset before " + y + ". Square is " + square);
                for (int i = 0; i < LAYER_SIZE; i++) {
                    for (int j = 0; j < LAYER_SIZE; j++) {
                        arr[i][j] = 0;
                    }
                }
            }

            for (short x = arrSC[0]; x < arrSC[2]; x++) {

                layer = getLayer(y, x); // ???
                int checkbit = 1 << layer; // ex. 0b00_0010_0000

                yy = getCoord(y); // y = 15000; yy = 1000;
                xx = getCoord(x);
                if (!isExistBit(arr[yy][xx], layer)) {
                    arr[yy][xx] = setBit(arr[yy][xx], checkbit);
                    square++;
                }
            }
        }
    }
    private static short[] getRightRect(String line) {
        String c[] = line.split(" ");

        // add 10000 to number for set unsign number
        // ex. -10000 + 10000 = 0 and 5000 + 10000 = 15000
        // dimension arrSC is 0 < j < 20000
        for (short j = 0; j <= 3; j++) {
            arrSC[j] = Short.parseShort(c[j]);
        }

        // set rect coord to x1, y1 - bottom-left, and x2, y2 - upper-right
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
        return arrSC;
    }
    private static void printArray(int[][] arr) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(Integer.toBinaryString(arr[i][j])+" ");
                //System.out.print(Long.toBinaryString(arr[i][j])+" ");
            }
            System.out.println();
        }
    }
    private static byte getLayer(short valy, short valx){
        //        5 layer - \/    \/ - 0 layer
        //arr[j][k] = 0b00_0010_0001
        return (byte)(( valx / LAYER_SIZE + (valy/ LAYER_SIZE)*MAX_VALUE*2/LAYER_SIZE)%LAYER_LIMIT);
        //return (byte)( valx / LAYER_SIZE + (valy/ LAYER_SIZE)*MAX_VALUE/LAYER_SIZE);
    }
    private static short getCoord(short coord){
        return (short)(coord % LAYER_SIZE);
    }
    private static int setBit(long src, long newBit){
        //set need bit        x or 0x0000
        return (int)(src | newBit);
    }
    private static boolean isExistBit(long src, long newBit){
        //check, exist bit or no        x and 0xFFFF = x
        return ((src & newBit) == newBit);
    }
    private static int resetBit(int src, int newBit){
        //change bit to 0        x xor 0x0010
        return src ^ newBit;
    }
}
