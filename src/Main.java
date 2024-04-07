import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends JFrame {
    private List<Integer> list;
    private SortPanel sortPanel;
    private JComboBox<String> sortComboBox;
    private JButton okButton;
    private static final int SIZE = 500;
    private static final int DELAY = 30;

    public Main(List<Integer> list) {
        this.list = list;
        setTitle("Sorting Visualizer");
        setSize(SIZE, SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        sortPanel = new SortPanel();
        add(sortPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        sortComboBox = new JComboBox<>(new String[]{"Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort", "Heap Sort"});
        okButton = new JButton("OK");

        controlPanel.add(sortComboBox);
        controlPanel.add(okButton);

        add(controlPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            String selectedSort = (String) sortComboBox.getSelectedItem();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        switch (Objects.requireNonNull(selectedSort)) {
                            case "Bubble Sort":
                                bubbleSort();
                                break;
                            case "Insertion Sort":
                                insertionSort();
                                break;
                            case "Selection Sort":
                                selectionSort();
                                break;
                            case "Merge Sort":
                                inPlaceMergeSort(0, list.size() - 1);
                                break;
                            case "Quick Sort":
                                quickSort(0, list.size() - 1);
                                break;
                            case "Heap Sort":
                                heapSort();
                                break;
                        }
                    } catch (InterruptedException ignored) {}
                    return null;
                }
            };

            worker.execute();
        });


        setVisible(true);
    }

    public void bubbleSort() throws InterruptedException {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);

                    sortPanel.repaint();
                    Thread.sleep(DELAY);
                }
            }
        }
    }

    public void insertionSort() throws InterruptedException {
        int n = list.size();
        for (int i = 1; i < n; ++i) {
            int key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j) > key) {
                list.set(j + 1, list.get(j));
                j = j - 1;

                sortPanel.repaint();
                Thread.sleep(DELAY);
            }
            list.set(j + 1, key);
        }
    }

    public void selectionSort() throws InterruptedException {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j) < list.get(minIndex)) {
                    minIndex = j;
                }
            }
            int temp = list.get(minIndex);
            list.set(minIndex, list.get(i));
            list.set(i, temp);

            sortPanel.repaint();
            Thread.sleep(DELAY);
        }
    }

    public void inPlaceMergeSort(int left, int right) throws InterruptedException {
        if (left < right) {
            int mid = left + (right - left) / 2;
            inPlaceMergeSort(left, mid);
            inPlaceMergeSort(mid + 1, right);
            merge(left, mid, right);
            sortPanel.repaint();
            Thread.sleep(DELAY);
        }
    }

    private void merge(int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = list.get(left + i);
        for (int j = 0; j < n2; ++j)
            R[j] = list.get(mid + 1 + j);

        int i = 0, j = 0;

        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                list.set(k, L[i]);
                i++;
            } else {
                list.set(k, R[j]);
                j++;
            }
            k++;
        }

        while (i < n1) {
            list.set(k, L[i]);
            i++;
            k++;
        }

        while (j < n2) {
            list.set(k, R[j]);
            j++;
            k++;
        }
    }

    public void quickSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
            sortPanel.repaint();
            Thread.sleep(DELAY);
        }
    }

    private int partition(int low, int high) {
        int pivot = list.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (list.get(j) < pivot) {
                i++;
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        int temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    public void heapSort() throws InterruptedException {
        int n = list.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(n, i);
            sortPanel.repaint();
            Thread.sleep(DELAY);
        }

        for (int i = n - 1; i >= 0; i--) {
            int temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);

            heapify(i, 0);
            sortPanel.repaint();
            Thread.sleep(DELAY);
        }
    }

    private void heapify(int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && list.get(left) > list.get(largest))
            largest = left;

        if (right < n && list.get(right) > list.get(largest))
            largest = right;

        if (largest != i) {
            int swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);

            heapify(n, largest);
        }
    }

    class SortPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth() / list.size();

            for (int i = 0; i < list.size(); i++) {
                int value = list.get(i);
                int x = i * width;
                int height = value * (getHeight() / list.size());
                int y = getHeight() - height;
                g.setColor(Color.GREEN);
                g.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        int n = 50;


        for (int i = 0; i < n; i++) {
            list.add((int) (Math.random() * 50) + 1);
        }

        new Main(list);
    }
}