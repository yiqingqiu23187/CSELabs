package Main;

import Expection.ErrorCode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class test {
    public static void main(String args[]) {
        System.out.println("请输入A组成绩:");
        Scanner scanner = new Scanner(System.in);
        String[] A = scanner.nextLine().split(" ");
        String[] B = scanner.nextLine().split(" ");

        System.out.println("进入决赛的选手编号为:");
        double first = 0;
        double second = 0;
        int firstindex = 0;
        int secondindex = 0;

        for (int i = 1; i < 8; i+=2) {
            double value = Double.parseDouble(A[ i]);
            if (value > first) {
                second = first;
                secondindex = firstindex;
                first = value;
                firstindex = i-1;
            } else if (value > second) {
                second = value;
                secondindex = i-1;
            }
        }
        System.out.print(A[firstindex] + " " + A[secondindex] + " ");

        first = 0;
        second = 0;
        firstindex = 0;
        secondindex = 0;

        for (int i = 1; i < 8; i += 2) {
            double value = Double.parseDouble(B[i]);
            if (value > first) {
                second = first;
                secondindex = firstindex;
                first = value;
                firstindex = i-1;
            } else if (value > second) {
                second = value;
                secondindex = i-1;
            }
        }
        System.out.println(B[firstindex] + " " + B[secondindex] + " ");

    }
}

//    private  static void removeAllData(){
//        for (int i = 0;i<3;i++){
//            java.io.File fmpath = new java.io.File(Main.FMPath+i);
//            java.io.File[] files = fmpath.listFiles();
//            for (java.io.File file:files){
//                file.delete();
//            }
//            java.io.File bmpath = new java.io.File(Main.BMPath+i);
//            java.io.File[] files1 = bmpath.listFiles();
//            for (java.io.File file:files1){
//                file.delete();
//            }
//        }
//
//
//    }