import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class Main {

    public static void partialSum(int[] array, int start, int end, int threadNum, AtomicLong partialResult) {
        long startTime = System.currentTimeMillis();

        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Thread " + threadNum + " calculated sum: " + sum + " in " + (endTime - startTime) + " ms");

        partialResult.addAndGet(sum);
    }

    public static void main(String[] args) {
        int arraySize = 500000;
        int numThreads = 11;

        int[] array = IntStream.generate(() -> 1).limit(arraySize).toArray();

        AtomicLong totalSum = new AtomicLong(0);

        int partSize = arraySize / numThreads;

        List<Thread> threads = new ArrayList<>();

        long totalStartTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            int start = i * partSize;
            int end = (i == numThreads - 1) ? arraySize : start + partSize;
            int threadNum = i + 1;

            Thread thread = new Thread(() -> partialSum(array, start, end, threadNum, totalSum));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long totalEndTime = System.currentTimeMillis();
        System.out.println("Total sum: " + totalSum.get());
        System.out.println("Total execution time: " + (totalEndTime - totalStartTime) + " ms");
    }
}
