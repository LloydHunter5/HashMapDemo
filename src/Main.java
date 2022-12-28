import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/twocities.txt"));

        LinearProbingHashMap<String, Counter> linearProbingHashMap = new LinearProbingHashMap<>();
        SeparateChainingHashMap<String, Counter> separateChainingHashMap = new SeparateChainingHashMap<>();

        long startTimeNano;
        long totalTimeLinearProbing;
        startTimeNano = System.nanoTime();
        while(s.hasNextLine()){
            String line = s.nextLine();
            if(linearProbingHashMap.contains(line)){
                linearProbingHashMap.find(line).increment();
            }else{
                linearProbingHashMap.add(line, new Counter(1));
            }
        }
        totalTimeLinearProbing = System.nanoTime() - startTimeNano;

        s = new Scanner(new File("src/twocities.txt"));
        long totalTimeSeparateChaining;
        startTimeNano = System.nanoTime();
        while(s.hasNextLine()){
            String line = s.nextLine();
            if(separateChainingHashMap.contains(line)){
                separateChainingHashMap.find(line).increment();
            }else{
                separateChainingHashMap.add(line, new Counter(1));
            }
        }
        totalTimeSeparateChaining = System.nanoTime() - startTimeNano;

        String keyWithLargestValueLinearProbing = "";
        String keyWithSmallestValueLinearProbing = "";
        int maxValueLinearProbing = 0;
        int minValueLinearProbing = Integer.MAX_VALUE;
        for(String key : linearProbingHashMap){
            int count = linearProbingHashMap.find(key).getCount();
            if(count > maxValueLinearProbing){
                keyWithLargestValueLinearProbing = key;
                maxValueLinearProbing = count;
            }
            if(count < minValueLinearProbing){
                keyWithSmallestValueLinearProbing = key;
                minValueLinearProbing = count;
            }
        }
        String keyWithLargestValueSeparateChaining = "";
        String keyWithSmallestValueSeparateChaining = "";
        int maxValueSeparateChaining = 0;
        int minValueSeparateChaining = Integer.MAX_VALUE;
        for(String key : linearProbingHashMap){
            int count = linearProbingHashMap.find(key).getCount();
            if(count > maxValueSeparateChaining){
                keyWithLargestValueSeparateChaining = key;
                maxValueSeparateChaining = count;
            }
            if(count < minValueSeparateChaining){
                keyWithSmallestValueSeparateChaining = key;
                minValueSeparateChaining = count;
            }
        }

        // Looks like separate chaining is faster most of the time
        System.out.println("Linear Probing Stats");
        System.out.println("Most Occurrences: " + keyWithLargestValueLinearProbing + ": " + maxValueLinearProbing);
        System.out.println("Least Occurrences: " + keyWithSmallestValueLinearProbing + ": " + minValueLinearProbing);
        System.out.println("Time (ms): " + totalTimeLinearProbing/1000000);
        System.out.println();

        System.out.println("Separate Chaining Stats");
        System.out.println("Most Occurrences: " + keyWithLargestValueSeparateChaining + ": " + maxValueSeparateChaining);
        System.out.println("Least Occurrences: " + keyWithSmallestValueSeparateChaining + ": " + minValueSeparateChaining);
        System.out.println("Time (ms): " + totalTimeSeparateChaining/1000000);
        System.out.println();
    }
}
