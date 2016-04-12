package com.example;

public class Sort {
  
    /**
     * 选择排序: 
     * 首先在数组中查找最小值， 如果该值不在第一个位置， 那么将其和处在第一个位置的元素交换，然后从第二个位置重复
     * 此过程，将剩下元素中最小值交换到第二个位置 。当到最后一位 时，数组排序结束 
     * 复杂度为：O(n^2)
     * 
     * @param array
     */
    static void selectionSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int min_idx = i;
  
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[min_idx]) {
                    min_idx = j;
                }
            }
  
            if (min_idx != i) {
                swap(array, min_idx, i);
            }
  
        }
    }
  
    /**
     * 冒泡排序法: 
     * 是运用数据值比较后，依判断规则对数据位置进行交换，以达到排序的目的 
     * 复杂度都是O(n^2)
     * 
     * @param array
     */
    public static void bubbleSort(int[] array) {// 冒泡排序算法
        int out, in;
        // 外循环记录冒泡次数
        for (out = array.length - 1; out >= 1; out--) {
            boolean flag = false;
            // 进行冒泡
            for (in = 0; in < out; in++) {
                // 交换数据
                if (array[in] > array[in + 1]) {
                    swap(array, in, in + 1);
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
  
        }
    }
  
    /**
     * 插入排序 
     * 是对于欲排序的元素以插入的方式寻找该元素的适当位置，以达到排序的目的。 
     * 插入排序的最差和平均情况的性能是O(n^2)
     * 
     * @param array
     */
    public static void insertSort(int[] array) {// 插入排序算法
        int in, out;
        for (out = 1; out < array.length; out++) {// 外循环是给每个数据循环
            int temp = array[out]; // 先取出来保存到临时变量里
            in = out; // in是记录插入数据之前的每个数据下标
            // while内循环是找插入数据的位置，并且把该位置之后的数据（包括该位置）
            // 依次往后顺移。
            while (in > 0 && array[in - 1] >= temp) {
                array[in] = array[in - 1]; // 往后顺移
                --in; // 继续往前搜索
            }
            array[in] = temp; // 该数据要插入的位置
        }
    }
  
    /**
     * 交换数组数据
     * 
     * @param array
     * @param min_idx
     * @param i
     */
    private static void swap(int[] array, int min_idx, int i) {
        int temp = array[min_idx];
        array[min_idx] = array[i];
        array[i] = temp;
    }
  
    public static void main(String[] args) {
  
        int[] array = new int[] { 1, 2, 6, 5, 7, 9, 0, 121, 4545 };
        bubbleSort(array);
        for (int i : array) {
            System.out.println(i);
        }
  
    }
  
}