package EngineCore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasixUtils {

    private static String currentDirectory = new File("").getAbsolutePath();

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = BasixUtils.class.getClass().getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(BasixUtils.class.getClass().getResourceAsStream(fileName)))){
            String line;
            while((line = br.readLine()) != null){
                list.add(line);
            }
        }
        return list;
    }



}