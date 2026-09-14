# Card Sorting — Coursework 2

用 Java / JShell 对比冒泡排序与归并排序。保留原始 `sortComparison.jsh` 和作业报告 `sortComparison.pdf`，新增可直接运行的输入样例、回归测试和 CI。

## 环境与运行

需要 **JDK 17+**，无第三方依赖。在仓库根目录启动：

```bash
jshell sortComparison.jsh
```

在 JShell 中执行：

```java
cardCompare("AC", "2C")
mergeSort(new ArrayList<>(List.of("KS", "10D", "AC")))
sortComparison(new String[]{"examples/cards.txt"})
sortComparison(new String[]{"examples/cards.txt"}, "my-results.csv")
/exit
```

默认结果为 `sortComparison.csv`，三行分别是输入文件表头、冒泡排序耗时、归并排序耗时。自定义输出的父目录需要已存在。

## 输入与排序规则

- UTF-8 文本，每行一张牌；允许首尾空白和空行，允许重复牌。
- 点数：`A, 2, …, 10, J, Q, K`；花色：`C, D, H, S`，必须大写。
- **先按花色 C < D < H < S，再按点数 A < 2 < … < K 排序**，不是扑克胜负规则。
- `bubbleSort` 原地修改列表；`mergeSort` 对多元素输入返回新列表，对零/单元素输入返回原列表。两者均为稳定排序。
- 缺失文件抛出 `UncheckedIOException`；非法牌抛出带文件名和行号的 `IllegalArgumentException`。失败不会写成耗时 `-1`，且输入校验完成前不会截断既有报告。
- 输出路径不能覆盖输入文件。重复运行会覆盖指定的旧报告。

## 耗时说明

CSV 使用 `System.nanoTime()` 计时后换算为小数毫秒，文件读取与列表复制不计入耗时。两种算法使用同一份数据的独立副本；冒泡排序增加了有序输入提前结束。

保留的 `measureBubbleSort` / `measureMergeSort` 方法返回整数毫秒，小输入可能为 `0`。这是一轮课堂实验，包含排序入口的输入检查，未进行 JVM 预热、多轮统计或严谨微基准控制；不要把一次运行的数字当成稳定性能结论。冒泡排序最坏为 O(n²)，大文件可能运行很久；归并排序为 O(n log n)，辅助空间 O(n)。

## 自动测试

```bash
java --add-modules jdk.jshell tests/RunTests.java sortComparison.jsh tests/checks.jsh
```

覆盖花色/点数顺序、空列表、重复牌、固定随机种子的排序对照、非法输入、CSV 输出、读取失败及输入文件防覆盖。GitHub Actions 在 JDK 17 和 21 上执行同一测试。
