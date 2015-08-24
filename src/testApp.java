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

    private final byte LAYER_LIMIT = 30;
    private final short LAYER_SIZE = 2000;
    private final short REGION_SIZE = 6000;
    private final short MAX_VALUE = 10000;
    private int square = 0;                // calculated square, max 400M
    private boolean isCheckAllFile = true; // if fond wrong line then continue to next check
    private short[] arrSC = new short[4];
    private short arrcoord[] = new short[4];
    private int arr[][] = new int[LAYER_SIZE][LAYER_SIZE];
    private BufferedReader is;
    private BufferedWriter out;
    private ArrayList<String> lines = new ArrayList<String>();

    public static void main(String[] args) {

        if (!(args.length == 2 && args[0].equalsIgnoreCase("input.txt") && args[1].equalsIgnoreCase("output.txt"))) {
            System.out.println("Wrong input parameters. Must entered input.txt and output.txt files.");
            return;
        }

        testApp ta = new testApp();
        ta.readLines(args[0], args[1]);
        ta.shell(ta.lines);
        //System.out.println(lines);
        ta.calcRect(ta.lines);
        ta.writeSquare();
        //printArray(arr);
        Runtime r = Runtime.getRuntime();
        System.out.println("Mem (" + r.totalMemory()/1024 +"kb): " + r.freeMemory()/1024 + "kb: used " + (r.totalMemory()/1024 - r.freeMemory()/1024) + "kb");
    }
    private void readLines(String isArg, String outArg){
        String line;
        //Pattern p = Pattern.compile("^(-?((\\d\\d{0,3})|([1]0{0,4}))\\s){3}-?\\d{1,4}\\s?$");
        //Pattern p = Pattern.compile("^(-?\\d+\\s){3}-?\\d+\\s?$");
        Pattern p = Pattern.compile("^((-?(([1]0{4})|(\\d\\d{0,3})))\\s){3}-?(([1]0{4})|(\\d\\d{0,3}))\\s?$");
        Matcher m = p.matcher("");

        try {
            is = new BufferedReader(new FileReader(isArg));
            out = new BufferedWriter(new FileWriter(outArg));
            System.out.println("INPUT:");

            // check for wrong lines and add correct lines to array
            while ((line = is.readLine()) != null) {
                m.reset(line);
                if (!m.matches()) {
                    System.out.print("Find wrong line #" + line);
                    if (!isCheckAllFile) {
                        System.out.println(" exit from program.");
                        return;
                    } else {
                        System.out.println(" continue check ...");
                    }
                } else {
                    //System.out.println("Ok line #" + line);
                    short[] res = getRightRect(line);
                    System.out.println(line);
                    //divide rect for 6k sections
                    // 0       6000   12000   18000   20000
                    // |        |       |       |       |
                    for (int i = 0; i < (MAX_VALUE * 2) / REGION_SIZE; i++) {
                        if (res[1] >= REGION_SIZE * i && res[1] < REGION_SIZE * (i + 1)) {
                            if (res[3] < REGION_SIZE * (i + 1)) {
                                lines.add(res[0] + " " + res[1] + " " + res[2] + " " + res[3]);
                            } else if (res[3] >= REGION_SIZE * (i + 1) && res[3] < REGION_SIZE * (i + 2)) {
                                lines.add(res[0] + " " + res[1] + " " + res[2] + " " + REGION_SIZE * (i + 1));
                                lines.add(res[0] + " " + REGION_SIZE * (i + 1) + " " + res[2] + " " + res[3]);
                            } else if (res[3] >= REGION_SIZE * (i + 2) && res[3] < REGION_SIZE * (i + 3)) {
                                lines.add(res[0] + " " + res[1] + " " + res[2] + " " + REGION_SIZE * (i + 1));
                                lines.add(res[0] + " " + REGION_SIZE * (i + 1) + " " + res[2] + " " + REGION_SIZE * (i + 2));
                                lines.add(res[0] + " " + REGION_SIZE * (i + 2) + " " + res[2] + " " + res[3]);
                            } else if (res[3] >= REGION_SIZE * (i + 3) && res[3] < REGION_SIZE * (i + 4)) {
                                lines.add(res[0] + " " + res[1] + " " + res[2] + " " + REGION_SIZE * (i + 1));
                                lines.add(res[0] + " " + REGION_SIZE * (i + 1) + " " + res[2] + " " + REGION_SIZE * (i + 2));
                                lines.add(res[0] + " " + REGION_SIZE * (i + 2) + " " + res[2] + " " + REGION_SIZE * (i + 3));
                                lines.add(res[0] + " " + REGION_SIZE * (i + 3) + " " + res[2] + " " + res[3]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeSquare(){
        System.out.println("OUTPUT:\n" + square);

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
    }
    private void calcRect(ArrayList<String> lines) {

        String line;
        byte period = 0;
        //System.out.println("Start calc square");

        for (int iline = 0; iline < lines.size(); iline++) {

            line = lines.get(iline);
            String c[] = line.split(" ");
            for (short j = 0; j <= 3; j++) {
                arrSC[j] = Short.parseShort(c[j]);
            }

            if (square > 0 && arrSC[0] >= arrcoord[0] && arrSC[2] <= arrcoord[2] & arrSC[1] >= arrcoord[1] & arrSC[3] <= arrcoord[3]) {
                // this figure less than previous. Nothing to calc.
                continue;
            }

            //TODO for enhanced performance save prev rect and compare it with next rects
            // ??? write only prev coord for analyze on next step
            for (int i = 0; i < 4; i++) {
                arrcoord[i] = arrSC[i];
            }
            if(arrSC[1]/ REGION_SIZE > period){
                //System.out.println("Reset before " + arrSC[1] + ". Square=" + square);
                resetRegion();
                period++;
            }
            calcSquare(arrSC);
        }
    }
    private void calcSquare(short[] arrSC){

        // coord within layer
        short xx; // ex. arrSC[0] = 7000, jx = 1000, layer=3, 0 <= jx < 2000
        short yy;
        short layer;

        //System.out.println("(" + arrSC[0] + "," + arrSC[1] + ")x(" + arrSC[2] + "," + arrSC[3] + ")");

        for (short y = arrSC[1]; y < arrSC[3]; y++) {
            for (short x = arrSC[0]; x < arrSC[2]; x++) {

                layer = getLayer(y, x);
                int checkbit = 1 << layer; // ex. 0b00_0010_0000

                yy = getCoord(y); // y = 15000; yy = 1000;
                xx = getCoord(x);
                if (!isExistBit(arr[yy][xx], checkbit)) {
                    arr[yy][xx] = setBit(arr[yy][xx], checkbit);
                    square++;
                }
            }
        }
    }
    private void resetRegion() {
        for (int i = 0; i < LAYER_SIZE; i++) {
            for (int j = 0; j < LAYER_SIZE; j++) {
                arr[i][j] = 0;
            }
        }
    }
    private short[] getRightRect(String line) {
        String c[] = line.split(" ");

        // add 10000 to number for set unsign number
        // ex. -10000 + 10000 = 0 and 5000 + 10000 = 15000
        // dimension arrSC is 0 < j < 20000
        for (short j = 0; j <= 3; j++) {
            arrSC[j] = (short)(Short.parseShort(c[j])+ MAX_VALUE);
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
    // from rosettacode.org/
    private void shell(ArrayList<String> a) {
        int increment = a.size() / 2;
        while (increment > 0) {
            for (int i = increment; i < a.size(); i++) {
                int j = i;
                int temp = Integer.parseInt(a.get(i).split(" ")[1]);
                String stemp = a.get(i);
                while (j >= increment && Integer.parseInt(a.get(j - increment).split(" ")[1]) > temp) {
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
    private byte getLayer(short valy, short valx){
        //        5 layer - \/    \/ - 0 layer
        //arr[y][x] = 0b00_0010_0001
        return (byte)(( valx / LAYER_SIZE + (valy/ LAYER_SIZE)*MAX_VALUE*2/LAYER_SIZE)%LAYER_LIMIT);
    }
    private short getCoord(short coord){
        return (short)(coord % LAYER_SIZE);
    }
    private int setBit(long src, long newBit){
        //set need bit        x or 0x0000
        return (int)(src | newBit);
    }
    private boolean isExistBit(long src, long newBit){
        //check, exist bit or no        x and 0xFFFF = x
        return ((src & newBit) == newBit);
    }
    private int resetBit(int src, int newBit){
        //change bit to 0        x xor 0x0010
        return src ^ newBit;
    }
}