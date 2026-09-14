void check(boolean ok) { if (!ok) throw new AssertionError("Check failed"); }
void rejects(Runnable action) { try { action.run(); } catch (IllegalArgumentException expected) { return; } throw new AssertionError("Expected invalid input rejection"); }
check(cardCompare("AC", "2C") < 0);
check(cardCompare("KC", "AD") < 0);
check(cardCompare("QH", "QH") == 0);
for (String invalid : new String[]{"", "1C", "11H", "0S", "AX", "ac"}) { rejects(() -> cardCompare(invalid, "AC")); }
rejects(() -> mergeSort(new ArrayList<>(List.of("invalid"))));
check(bubbleSort(new ArrayList<>()).isEmpty());
var random = new Random(42);
var deck = new ArrayList<String>();
for (String suit : new String[]{"C", "D", "H", "S"}) for (String rank : new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}) deck.add(rank + suit);
for (int size : new int[]{0, 1, 2, 52, 100, 250}) { var cards = new ArrayList<String>(); for (int i = 0; i < size; i++) cards.add(deck.get(random.nextInt(deck.size()))); var expected = new ArrayList<>(cards); expected.sort((a, b) -> cardCompare(a, b)); check(bubbleSort(new ArrayList<>(cards)).equals(expected)); check(mergeSort(cards).equals(expected)); }
try { measureBubbleSort("missing-cards-file.txt"); throw new AssertionError("Missing file was accepted"); } catch (UncheckedIOException expected) { }
var output = Files.createTempFile("sort-check-", ".csv");
try { sortComparison(new String[]{"examples/cards.txt"}, output.toString()); var lines = Files.readAllLines(output); check(lines.size() == 3); check(lines.get(0).equals("algorithm_ms,\"examples/cards.txt\"")); check(Double.parseDouble(lines.get(1).split(",")[1]) >= 0); var saved = Files.readString(output); try { sortComparison(new String[]{"missing-cards-file.txt"}, output.toString()); throw new AssertionError("Missing file accepted"); } catch (UncheckedIOException expected) { } check(Files.readString(output).equals(saved)); } finally { Files.deleteIfExists(output); }
try { sortComparison(new String[]{"examples/cards.txt"}, "examples/cards.txt"); throw new AssertionError("Input overwrite accepted"); } catch (IllegalArgumentException expected) { }
