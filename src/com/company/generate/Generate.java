package com.company.generate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Generate {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input rows:");
        int rows = Integer.parseInt(reader.readLine());
        System.out.println("Input length:");
        int length = Integer.parseInt(reader.readLine());
        generate(rows, length);
    }

    public static void generate(int rows, int length) throws IOException {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        char[] finalString = new char[length];

        String concatenate = String.format("resources/example.txt");
        Path finalPath = Paths.get(concatenate);
        File file = new File(concatenate);
        if(!file.exists()) {
            var a = file.createNewFile();
        }
        BufferedWriter bw = Files.newBufferedWriter(finalPath);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < length; i++) {
                finalString[i] = chars.charAt(rnd.nextInt(chars.length()));
            }
            bw.write(String.valueOf(finalString));
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }
}
