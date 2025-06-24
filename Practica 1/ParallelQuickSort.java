import java.util.Random;
import java.util.concurrent.*;

public class ParallelQuickSort {
    private static final int HILOS = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(HILOS);

    public static void parallelQuickSort(double[] arr, int low, int high, int profundidad) {
        if (low < high) {

            int pivote = partition(arr, low, high);
            if((executor.getMaximumPoolSize() - executor.getActiveCount()) > 0){
                Future<?> izquierda = executor.submit(() -> {
                    parallelQuickSort(arr, low, pivote - 1, profundidad+1);
                });
    
                Future<?> derecha = executor.submit(() -> {
                    parallelQuickSort(arr, pivote + 1, high, profundidad+1);
                });
    
                try {
                    izquierda.get();
                    derecha.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                parallelQuickSort(arr, low, pivote - 1, profundidad+1);
                parallelQuickSort(arr, pivote + 1, high, profundidad+1);
            }
        }
    }

    private static int partition(double[] arr, int low, int high) {
        double pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static double[] generateArray(int size) {
        double[] arr = new double[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextDouble();
        }
        return arr;
    }

    public static void main(String[] args) {
        double[] arr = generateArray(100000);
        System.out.println("Arreglo desordenado: \n" + java.util.Arrays.toString(arr));

        long tiempo = System.currentTimeMillis();
        parallelQuickSort(arr, 0, arr.length - 1, 0);

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Deteniendo");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        long tiempoDeEjecucion = System.currentTimeMillis() - tiempo;
        System.out.println("Arreglo ordenado \n: " + java.util.Arrays.toString(arr));
        System.out.println("Tiempo de ejecucion: " + tiempoDeEjecucion + " ms");
    }
}
