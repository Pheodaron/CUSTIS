package com.company.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Sort {
    public static int count = 1;

    public static Path SortAndSaveBatch(List<String> batch) throws IOException {
        Path tmp = Files.createTempFile("", "");
        Collections.sort(batch);
        Files.write(tmp, batch);
        return tmp;
    }

    public static Queue<Path> HalfSortWithSize(BufferedReader br, int size) {
        Queue<Path> files = new ArrayDeque<>();
        List<String> batch = new ArrayList<String>();

        br.lines().forEach(line -> {
            batch.add(line);
            if (batch.size() >= size) {
                try {
                    files.add(SortAndSaveBatch(batch));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                batch.clear();
            }
        });
        return files;
    }

    public static Path MergeTemps(Queue<Path> files) throws IOException {
        while (files.size() != 1) {
            BufferedReader br1 = Files.newBufferedReader(files.poll());
            BufferedReader br2 = Files.newBufferedReader(files.poll());

            Path tmp = Files.createTempFile("", "");
            BufferedWriter bw = Files.newBufferedWriter(tmp);
            files.add(tmp);

            String line1 = br1.readLine();
            String line2 = br2.readLine();

            while (line1 != null || line2 != null) {
                if (line1 == null) {
                    bw.write(line2);
                    bw.newLine();
                    line2 = br2.readLine();
                    continue;
                }
                if (line2 == null) {
                    bw.write(line1);
                    bw.newLine();
                    line1 = br1.readLine();
                    continue;
                }

                if (line1.isBlank()) {
                    line1 = br1.readLine();
                    continue;
                }
                if (line2.isBlank()) {
                    line2 = br2.readLine();
                    continue;
                }

                if (line2.compareTo(line1) > 0) {
                    bw.write(line1);
                    line1 = br1.readLine();
                } else {
                    bw.write(line2);
                    line2 = br2.readLine();
                }
                bw.newLine();
            }

            br1.close();
            br2.close();

            bw.flush();
            bw.close();
        }

        return files.poll();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = Files.newBufferedReader(Path.of("resources/example.txt"));
        Queue<Path> temps = HalfSortWithSize(br, 100);
        Path res = MergeTemps(temps);
        Path answer = Path.of("resources/answer.txt");
        if (Files.exists(answer))
            Files.delete(answer);
        Files.move(res, answer);
        br.close();
    }
}
