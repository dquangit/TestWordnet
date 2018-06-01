import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        statisticType();
        statisticRate();
    }

    private static void statisticRate() throws IOException {
        List<String> lines = loadFile("data/test.txt");
        int right = 0;
        int wrong = 0;
        int nearCorrect = 0;
        for (String line : lines) {
            String[] elements = line.split("\\|");
            int result = Integer.valueOf(elements[6]);
            if (result == 1) {
                right ++;
                continue;
            }

            if (result == 0) {
                nearCorrect ++;
                continue;
            }

            wrong ++;
        }
        int size = lines.size();
        System.out.println("Right: " + right + "/" + size + " " + ((double) right/size*100));
        System.out.println("Wrong: " + wrong + "/" + size + " " + ((double)wrong/size*100));
        System.out.println("Near correct: " + nearCorrect + "/" + size + " " + ((double)nearCorrect/size*100));
    }

    private static void statisticType() throws IOException {
        List<String> quang = loadFile("data/quang.txt");
        Map<String, Integer> statisticMap = new HashMap<>();
        quang.forEach(line -> {
            if (line.isEmpty()) {
                return;
            }
            String[] elements = line.split("\\|");
            String type = elements[2];
            Integer count = statisticMap.get(type);
            if (count == null) {
                count = 1;
            } else {
                count ++;
            }
            statisticMap.put(type, count);
        });
        System.out.println(statisticMap);
    }

    /**
     * Load text file from disk.
     *
     * @param path link to text file
     * @return list of String, an element equivalent one line in text file
     * @throws IOException
     */
    private static List<String> loadFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        Stream<String> stringStream = Files.lines(filePath);
        List<String> result = new ArrayList<>();
        stringStream.forEach(result::add);
        return result;
    }

    private static void get250Random() throws IOException {
        Random random = new Random();
        List<String> data = loadFile("data/quang.txt");
        int size = data.size();
        Set<String> out = new HashSet<>();
        while (out.size() < 250) {
            int index = random.nextInt(size);
            String in = data.get(index);
            out.add(in);
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("data/test.txt"));
        out.forEach(s -> {
            try {
                writer.write(s + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();
    }

//    private static List<String> getInRange(int min, int max, int quantity, List<String> list) {
//
//    }

    private static Map<String, String> chienFile() throws IOException {
        List<String> chien = loadFile("data/chien.txt");
        Map<String, String> result = new HashMap<>();
        chien.forEach(line-> {
            String[] elements = line.split("\\|");
            String key = elements[0].trim();
            String meanings = elements[2].replace("[", "").replace("]", "").trim();
            result.put(key, meanings);
        });
        return result;
    }

    private static Map<String, String> quangFile() throws IOException {
        List<String> quang = loadFile("data/quang.txt");
        Map<String, String> result = new HashMap<>();
        quang.forEach(line -> {
            String[] elements = line.split("\\|");
            String key = elements[0];
            String value = elements[3];
            result.put(key, value);
        });
        return result;
    }

    private static void compareWithAnotherTeam() throws IOException {
        Map<String, String> quang = quangFile();
        Map<String, String> chien = chienFile();
        Map<String, String> comparator = new HashMap<>();
        chien.forEach((key, value) -> {
            String[] valueArray = value.split(",");
            String quangXXX = quang.get(key);
            String[] quangArray = new String[0];
            if (quangXXX != null) {
                quangArray = quangXXX.split(",");
            }
            int count = 0;
            for (String xxx : valueArray) {
                for (String yyy : quangArray) {
                    if (xxx.isEmpty() || yyy.isEmpty()) {
                        continue;
                    }
                    if (xxx.trim().equals(yyy.trim())) {
                        count ++;
                    }
                }
            }
            if (count == 0) {
                comparator.put(key, quang.get(key) + "|" + value);
            }

        });
        System.out.println(comparator.size());
        comparator.forEach((key, value) -> {
            System.out.println(key + "|" + value);
        });
    }
}
