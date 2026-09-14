import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;




    public static int cardCompare(String card1, String card2) {
        int value1 = getCardValue(card1);
        int value2 = getCardValue(card2);
        char suit1 = card1.charAt(card1.length() - 1);
        char suit2 = card2.charAt(card2.length() - 1);

        if (suit1 < suit2) {
            return -1;
        } else if (suit1 > suit2) {
            return 1;
        } else {
            if (value1 < value2) {
                return -1;
            } else if (value1 > value2) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    private static int getCardValue(String card) {
        if (card == null || card.length() < 2 || card.length() > 3 || "CDHS".indexOf(card.charAt(card.length() - 1)) < 0) {
            throw new IllegalArgumentException("Invalid card: " + card + " (expected AC, 10D, QH, KS, etc.)");
        }
        String valueStr = card.substring(0, card.length() - 1);
        if (valueStr.equals("A")) {
            return 1;
        } else if (valueStr.equals("J")) {
            return 11;
        } else if (valueStr.equals("Q")) {
            return 12;
        } else if (valueStr.equals("K")) {
            return 13;
        } else {
            if (!(valueStr.equals("10") || (valueStr.length() == 1 && valueStr.charAt(0) >= '2' && valueStr.charAt(0) <= '9'))) {
                throw new IllegalArgumentException("Invalid card rank: " + card);
            }
            return Integer.parseInt(valueStr);
        }
    }


    public static ArrayList<String> bubbleSort(ArrayList<String> list) {
        for (String card : list) getCardValue(card);
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (cardCompare(list.get(j), list.get(j + 1)) > 0) {

                    String temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;

                }
            }
            if (!swapped) break;
        }
        return list;
    }


    public static ArrayList<String> mergeSort(ArrayList<String> list) {
        for (String card : list) getCardValue(card);
        return mergeSortValidated(list);
    }

    private static ArrayList<String> mergeSortValidated(ArrayList<String> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        ArrayList<String> leftList = new ArrayList<>(list.subList(0, mid));
        ArrayList<String> rightList = new ArrayList<>(list.subList(mid, list.size()));

        leftList = mergeSortValidated(leftList);
        rightList = mergeSortValidated(rightList);

        return merge(leftList, rightList);
    }




    private static ArrayList<String> merge(ArrayList<String> leftList, ArrayList<String> rightList) {
        ArrayList<String> mergedList = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < leftList.size() && rightIndex < rightList.size()) {
            if (cardCompare(leftList.get(leftIndex), rightList.get(rightIndex)) <= 0) {
                mergedList.add(leftList.get(leftIndex));
                leftIndex++;
            } else {
                mergedList.add(rightList.get(rightIndex));
                rightIndex++;
            }
        }

        while (leftIndex < leftList.size()) {
            mergedList.add(leftList.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex < rightList.size()) {
            mergedList.add(rightList.get(rightIndex));
            rightIndex++;
        }

        return mergedList;
    }

    private static ArrayList<String> readCards(String filename) {
        ArrayList<String> cardList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filename), StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String card = line.trim();
                if (card.isEmpty()) continue;
                try { getCardValue(card); }
                catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(filename + ":" + lineNumber + ": " + e.getMessage(), e);
                }
                cardList.add(card);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read cards: " + filename, e);
        }
        return cardList;
    }

    public static long measureBubbleSort(String filename) {
        ArrayList<String> cardList = readCards(filename);
        long startTime = System.nanoTime();
        bubbleSort(cardList);
        return (System.nanoTime() - startTime) / 1_000_000;
    }

    public static long measureMergeSort(String filename) {
        ArrayList<String> cardList = readCards(filename);
        long startTime = System.nanoTime();
        mergeSort(cardList);
        return (System.nanoTime() - startTime) / 1_000_000;
    }
    void sortComparison(String[] filenames) throws IOException {
        sortComparison(filenames, "sortComparison.csv");
    }

    void sortComparison(String[] filenames, String output) throws IOException {
        if (filenames == null || filenames.length == 0) throw new IllegalArgumentException("At least one input file is required");
        var header = new StringBuilder("algorithm_ms");
        var bubble = new StringBuilder("bubbleSort");
        var merge = new StringBuilder("mergeSort");
        Path outputPath = Path.of(output).toAbsolutePath().normalize();
        for (String filename : filenames) {
            Path inputPath = Path.of(filename).toAbsolutePath().normalize();
            if (inputPath.equals(outputPath) || (Files.exists(inputPath) && Files.exists(outputPath) && Files.isSameFile(inputPath, outputPath))) {
                throw new IllegalArgumentException("Output must not overwrite input: " + filename);
            }
            // Read once; independent copies give both algorithms the same input order.
            var cards = readCards(filename);
            var bubbleInput = new ArrayList<>(cards);
            var mergeInput = new ArrayList<>(cards);
            long start = System.nanoTime();
            bubbleSort(bubbleInput);
            double bubbleMs = (System.nanoTime() - start) / 1_000_000.0;
            start = System.nanoTime();
            mergeSort(mergeInput);
            double mergeMs = (System.nanoTime() - start) / 1_000_000.0;
            header.append(",\"").append(filename.replace("\"", "\"\"")).append("\"");
            bubble.append(',').append(bubbleMs);
            merge.append(',').append(mergeMs);
        }
        // Do not create/truncate the report until all input files are valid.
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write(header.toString()); writer.newLine();
            writer.write(bubble.toString()); writer.newLine();
            writer.write(merge.toString()); writer.newLine();
        }
    }





