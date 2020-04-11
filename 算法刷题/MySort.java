import java.util.Arrays;

public class MySort {

    private int[] array;

    private int size;

    public MySort(int[] array, int size) {
        this.array = array;
        this.size = size;
    }

    //InsertSort
    public void insertSort() {
        if (size <= 1) {
            return;
        }

        for (int i = 1; i < size; i++) {

            int tmp = array[i];
            int k = i - 1;

            while (k >= 0 && array[k] > tmp) {
                array[k + 1] = array[k];
                k--;
            }
            array[k + 1] = tmp;
        }
        return;
    }

    //ShellSort
    public void shellSort() {

        if (size <= 1) return;

        int[] shellData = {5, 3, 1};

        for (int i = 0; i < shellData.length; i++) {
            shellSort(shellData[i]);
        }
    }

    private void shellSort(int k) {

        for (int i = 0; i < k; i++) {

            for (int j = i + k; j < size; j++) {

                int tmp = array[j];
                int t = j - k;
                while (t >= i && array[t] > tmp) {
                    array[t + k] = array[t];
                    t -= k;
                }
                array[t + k] = tmp;
            }
        }
        return;
    }

    //BubbleSort
    public void bubbleSort() {

        if (size <= 1) return;

        for (int i = 0; i < size; i++) {

            int j = 1;
            while (j < size - i) {
                if (array[j] < array[j - 1]) {
                    swap(j, j - 1);
                }
                j++;
            }
        }

        return;
    }

    //QuickSort
    public void quickSort() {

        if (size <= 1) return;

        quickSort(0, size - 1);
    }
    private void quickSort(int lo, int hi) {

        if (lo >= hi) return;
        int pivot = partition(lo, hi);
        quickSort(lo, pivot - 1);
        quickSort(pivot + 1, hi);
    }

    private int partition(int lo, int hi) {

        int pivotValue = array[lo];

        while (lo < hi) {

            while (lo < hi && array[hi] >= pivotValue) {
                hi--;
            }
            array[lo] = array[hi];
            while (lo < hi && array[lo] <= pivotValue) {
                lo++;
            }
            array[hi] = array[lo];
        }
        array[lo] = pivotValue;
        return lo;
    }

    //SelectionSort
    public void selectionSort() {

        if (size <= 1) return;

        for (int i = 0; i < size - 1; i++) {
            int value = array[i];
            int j = 0;

            int k = i;
            while (k < size) {
                if (array[k] < value) {
                    value = array[k];
                    j = k;
                }
                k++;
            }
            if (value < array[i]) {
                swap(i, j);
            }
        }
        return;
    }

    //HeapSort
    public void heapSort() {

        if (size <= 1) return;

        //heapify
        for (int i = 1; i < size; i++) {
            siftUp(i);
        }

//        for (int i = (size - 1) / 2; i >= 0; i--) {
//            siftDown(i, size - 1);
//        }

        for (int i = size - 1; i > 0; i--) {
            swap(i, 0);
            siftDown(0, i - 1);
        }
    }

    private void siftUp(int index) {

        while (index >= 0 && array[index / 2] < array[index]) {
            swap(index, index / 2);
            index /= 2;
        }
    }

    private void siftDown(int index, int lastIndex) {

        while (index * 2 <= lastIndex) {
            int j = index * 2;
            if (j + 1 <= lastIndex && array[j + 1] > array[j]) {
                j = j + 1;
            }

            if (array[j] <= array[index]) {
                break;
            }
            swap(index, j);
            index = j;
        }
    }

    //MergeSort
    public void mergeSort() {
        if (size <= 1) return;

        mergeSort(0, size - 1);
    }

    private void mergeSort(int lo, int hi) {

        if (lo< hi) {

            int mid = (lo + hi) / 2;
            mergeSort(lo, mid);
            mergeSort(mid + 1, hi);
            mergeInPlace(lo, mid, hi);
        }
    }

    private void merge(int lo, int mid, int hi) {
        int[] arr = new int[hi - lo + 1];
        int k = 0;

        int i = lo;
        int j = mid + 1;
        while (i <= mid && j <= hi) {
            arr[k++] = array[i] > array[j] ? array[j++] : array[i++];
        }

        while (i <= mid) {
            arr[k++] = array[i++];
        }

        while (j <= hi) {
            arr[k++] = array[j++];
        }
        k = 0;
        while (lo <= hi) {
            array[lo++] = arr[k++];
        }
    }

    private void mergeInPlace(int lo, int mid ,int hi) {

        int i = lo;
        int j = mid + 1;

        while (i < j && j <= hi) {
            int index = j;
            while (i < j && array[i] <= array[index]) {
                i++;
            }
            while (j <= hi && array[j] < array[i]) {
                j++;
            }
            reverse(i, index - 1);
            reverse(index, j - 1);
            reverse(i, j - 1);
            i += j - index;
        }
    }

    private void reverse(int lo, int hi) {

        while (lo < hi) {
            swap(lo, hi);
            lo++;
            hi--;
        }
    }

    private void swap(int i, int j) {

        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
