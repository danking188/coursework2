import java.util.ArrayList;
import java.util.List;




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
            return Integer.parseInt(valueStr);
        }
    }


    public static ArrayList<String> bubbleSort(ArrayList<String> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (cardCompare(list.get(j), list.get(j + 1)) > 0) {

                    String temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);

                }
            }
        }
        return list;
    }


    public static ArrayList<String> mergeSort(ArrayList<String> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        ArrayList<String> leftList = new ArrayList<>(list.subList(0, mid));
        ArrayList<String> rightList = new ArrayList<>(list.subList(mid, list.size()));

        leftList = mergeSort(leftList);
        rightList = mergeSort(rightList);

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

    public static long measureBubbleSort(String filename) {
        ArrayList<String> cardList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine())!= null) {
                cardList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        long startTime = System.currentTimeMillis();
        bubbleSort(cardList);
        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    public static long measureMergeSort(String filename) {
        ArrayList<String> cardList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine())!= null) {
                cardList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        long startTime = System.currentTimeMillis();
        mergeSort(cardList);
        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }
    void sortComparison(String[] filenames) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("sortComparison.csv"));

        writer.write(" , ");
        for (String fileName1 : filenames) {
            writer.write(fileName1.replace(".txt", "").replace("sort", "") + ", ");
        }
        writer.newLine();

        writer.write("bubbleSort, ");
        for (String fileName2 : filenames) {
            writer.write(measureBubbleSort(fileName2) + ", ");
        }
        writer.newLine();

        writer.write("mergeSort, ");
        for (String fileName3 : filenames) {
            writer.write(measureMergeSort(fileName3) + ", ");
        }
        writer.newLine();

        writer.close();
    }





