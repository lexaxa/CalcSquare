package ru.alexis.calcsquare;

/**
 * Created by Alex on 17.08.2015.
 */
public class TestBits {
    public static void main(String[] args) {

        short p = 0b11_1111_1111;
        short x = 0b00_0001_0001;
        short x1= 0b00_0010_0000; // add number to layer
        short arr[][] = new short[3][3];

        StringBuilder str = new StringBuilder();


        for (int i = 0; i < 20; i++) {
            str.append('o');
        }
        changeStr(str);
        for (int i = 0; i < 20; i++) {
            System.out.println(str.charAt(i));
        }

        str.setCharAt(10,'1');
        System.out.println(str.charAt(10));



        arr[0][0]=0;
        arr[0][1]=0;
        arr[0][2]=0;
        arr[1][0]=1;
        arr[1][1]=1;
        arr[1][2]=1;

        printArray(arr);

        changeArr(arr);

        printArray(arr);


        System.out.println(Integer.toBinaryString(x & p));
        System.out.println(Integer.toBinaryString(x & x1) + "=" + ((x & x1) == x1));

        short x2= 0b00_0010_0001;

        System.out.println(x2);
        System.out.println(Integer.toBinaryString(x2 | 0x00_0010_0000) + "=" + Integer.toBinaryString(x | x1));
    }
    private static void changeStr(StringBuilder str){
        str.append("hello");
    }

    private static void changeArr(short[][] arr) {
        arr[0][0]=1;
        arr[0][1]=1;
        arr[0][2]=1;
        arr[1][0]=2;
        arr[1][1]=2;
        arr[1][2]=2;
    }
    private static void printArray(short[][] arr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }
    public short setBit(short src, short newBit){
        //set need bit        x or 0x0000
        return (short)(src | newBit);
    }
    public boolean isExistBit(short src, short newBit){
        //check, exist bit or no        x and 0xFFFF = x
        return ((src & newBit) == newBit);
    }
    public short resetBit(short src, short newBit){
        //change bit to 0        x xor 0x0010
        return (short)(src ^ newBit);
    }


}