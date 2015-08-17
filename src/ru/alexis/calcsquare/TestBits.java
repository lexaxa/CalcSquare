package ru.alexis.calcsquare;

/**
 * Created by Alex on 17.08.2015.
 */
public class TestBits {
    public static void main(String[] args) {

        //check, exist bit or no
        // x and 0xFFFF
        short p = 0b11_1111_1111;
        short x = 0b00_0001_0001;
        short x1= 0b00_0010_0000; // add number to layer

        System.out.println(x);
        System.out.println(Integer.toBinaryString(x & p));
        System.out.println(Integer.toBinaryString(x & x1) + "=" + ((x & x1) == x1));

        //set need bit
        // x or 0x0000
        short x2= 0b00_0010_0001;




        //change bit to 0
        // x xor 0x0010

        System.out.println(x2);
        System.out.println(Integer.toBinaryString(x2 | 0x00_0010_0000) + "=" + Integer.toBinaryString(x | x1));

    }
}
