package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 选择排序算法java实现
 */
public class SelectionSort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String[] split = line.split(" ");
        List<String> list = Arrays.asList(split);
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if ("".equals(s)) {
                list.remove(i);
            }
        }
        int[] ints = new int[list.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = Integer.parseInt(list.get(i));
        }
 
        int temp = 0;
        for (int i = 0; i < ints.length; i++) {
            temp = i;
            for (int j = i + 1; j < ints.length; j++) {
                if (ints[j] < ints[temp]) {
                    temp = j;
                }
            }
 
            if (ints[i] > ints[temp]) {
                ints[i] = ints[temp] + ints[i];
                ints[temp] = ints[i] - ints[temp];
                ints[i] = ints[i] - ints[temp];
            }
        }
 
        for (int i = 0; i < ints.length - 1; i++) {
            int anInt = ints[i];
            System.out.print(String.format("%d ", anInt));
        }
        System.out.println(ints[ints.length - 1]);
    }
}