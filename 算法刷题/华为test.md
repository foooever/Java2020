## 牛客网华为题

### 3.明明的随机数
明明想在学校中请一些同学一起做一项问卷调查，为了实验的客观性，他先用计算机生成了N个
1到1000之间的随机整数（N≤1000），对于其中重复的数字，只保留一个，把其余相同的数去掉
，不同的数对应着不同的学生的学号。然后再把这些数从小到大排序，按照排好的顺序去找同学
做调查。请你协助明明完成“去重”与“排序”的工作(同一个测试用例里可能会有多组数据，希望
大家能正确处理)。

思路：本题对于输入数据，需要去重和排序，输入过程是一种动态的过程故而采用优先队列进行存储，
在存储过程中进行判断，优先队列中`contains()`复杂度为O(N)，因此整体复杂度为O(N^2)。
优先队列排序的过程即堆顶出堆的过程。
```Java
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            for (int i = 0; i < n; i++) {
                int tmp = in.nextInt();
                if (!pq.contains(tmp)) { //O(N)复杂度
                    pq.add(tmp);
                }
            }
            while (!pq.isEmpty()) {
                System.out.println(pq.remove());
            }
        }
    }
}
//Input: 
3
2
2
1
Output:
1
2
```
Tips：Java中生成随机数的操作:
```Java
//两种随机数
java.lang.Math.Random;
java.util.Random
//第一种
//Math.Random()返回带正号的double值，取值[0.0, 1.0)左闭右开
public class RandomCoder {
    public static void main(String[] args) {
        random();
    }
 
    private static void random() {
        double random = Math.random();//产生一个[0，1)之间的随机数
        System.out.println(random);
    }
}

//第二种
//Random()：创建一个新的随机数生成器。
//Random(long seed)：使用单个long种子创建一个新的随机数生成器。
Random r = new Random(); //默认随机数种子 当前系统时间毫秒数
Random r = new Random(20); //随机数种子20
int i = r.nextInt(10); //此处10为随机数区间[0, 10)
//相同随机数种子生成的而随机数序列相同，若想不同借助数组和集合
```
### 4.字符串分隔
连续输入字符串，请按长度为8拆分每个字符串后输出到新的字符串数组；长度不是
8整数倍的字符串请在后面补数字0，空字符串不处理。（输入2次，每个字符长度小于100）

思路：递归的方式对输入字符串进行处理，递归函数为`getSplit()`。
```Java
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            String str = in.nextLine();
            getSplit(str, list);
        }
        for (String s : list) {
            System.out.println(s);
        }
    }

    public static void getSplit(String str, List<String> list) {
        if (str.length() == 0) { //空串不处理
            return;
        }
        if (str.length() >= 8) { //长串切分
            list.add(str.substring(0, 8));
            getSplit(str.substring(8), list);
            return;
        }

        //不足8长度
        int len = 8 - str.length();
        for (int i = 0; i < len; i++) {
            str = str + '0';
        }
        list.add(str);
        return;
    }
}
//Input: 
3
2
2
1
Output:
1
2
```
### 5.进制转换
写出一个程序，接受一个十六进制的数，输出该数值的十进制表示。（多组同时输入）

```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            int sum = 0;
            int i = str.length() - 1;
            //ASCII码 0-64 A-81
            while (str.charAt(i) != 'x') {
                char ch = str.charAt(i);
                int tmp = ch >= '0' && ch <= '9' ? ch - '0' : ch - 'A' + 10;
                sum += tmp * Math.pow(16, str.length() - 1 - i);
                i--;
            }
            System.out.println(sum);
        }
        in.close();
    }
}
//Input:0xA
Output:10

```
