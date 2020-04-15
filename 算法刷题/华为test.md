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
### 6.质数因子
输入一个正整数，按照从小到大的顺序输出它的所有质因子（如180的质因子为2 2 3 3 5 ）
最后一个数后面也要有空格。
思路：暴力递归。
```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long tmp = in.nextLong();
        System.out.println(new Main().getResult(tmp));
    }
    public String getResult(long ulDataInput) {
        String res = "";
        for (long i = ulDataInput / 2; i > 1; i--) { //先处理大因子以避免重复计算
            if (ulDataInput % i == 0 && isValid(i)) {
                res = i + " ";
                ulDataInput /= i;
                break;
            }
        }
        return res == "" ? ulDataInput + " " : getResult(ulDataInput) + res;
    }
    public boolean isValid(long num) { //判断是否为质数（素数）
        boolean flag = true;
        for (long i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}//input 180
output 2 2 3 3 5
```
### 7.取近似值
写出一个程序，接受一个正浮点数值，输出该数值的近似整数值。如果小数点后数值大于等
于5,向上取整；小于5，则向下取整。
```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        double input = in.nextDouble();
        int ret = (int)input; //显式转换是向下取整
        ret += input - ret >= 0.5 ? 1 : 0;
        System.out.println(ret);
    }
}
```
### 8.合并表记录
数据表记录包含表索引和数值（int范围的整数），请对表索引相同的记录进行合并，
即将相同索引的数值进行求和运算，输出按照key值升序进行输出。
思路：采用`TreeMap`的`SortedMap`进行存储。其复杂度`O(NlongN)?`其中`contains()`复杂度`O(logN)?`。
```Java
import java.util.Scanner;
import java.util.TreeMap;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        TreeMap<Integer, Integer> treeMap = new TreeMap<>((o1, o2) -> o1.compareTo(o2));
        for (int i = 0; i < n; i++) {
            int key = in.nextInt();
            int value = in.nextInt();
            if (!treeMap.containsKey(key)) {
                treeMap.put(key, value);
            } else {
                treeMap.put(key, treeMap.get(key) + value);
            }
        }
        for (int i : treeMap.keySet()) { //遍历
            System.out.println(i+" "+treeMap.get(i));
        }
    }
}//input
4
0 1
0 2
1 2
3 4
//output
0 3
1 2
3 4
```
### 9.提取不重复整数
输入一个int型整数，按照从右向左的阅读顺序，返回一个不含重复数字的新的整数。
```Java
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.next();
        Set<Character> set = new HashSet<>();
        String ret = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            if (!set.contains(str.charAt(i))) {
                ret += str.charAt(i);
                set.add(str.charAt(i));
            }
        }
        System.out.println(Integer.parseInt(ret));
        
        in.close();
    }
}
```
### 10.字符个数统计
编写一个函数，计算字符串中含有的不同字符的个数。字符在ACSII码范围内
(0~127)，换行表示结束符，不算在字符里。不在范围内的不作统计。
思路：空间换时间，计数；也可以用`Set`存储。
```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] buket = new int[127];
        String str = in.next();
        for (int i = 0; i < str.length(); i++) {
            buket[str.charAt(i)-0]++;
        }
        int sum = 0;
        for(int i : buket) {
            if (i > 0) {
                sum++;
            }
        }
        System.out.println(sum);
    }
}//input abc
output 3
```
### 11.数字颠倒
输入一个整数，将这个整数以字符串的形式逆序输出程序不考虑负数的
情况，若数字含有0，则逆序形式也含有0，如输入为100，则输出为001。
```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String res = "";
        while (n > 10) {
            int mod = n % 10;
            res += mod;
            n /= 10;
        }
        res = res + n;
        System.out.println(res);
    }
}//input 1516000
output 0006151
```
### 12.字符串反转
写出一个程序，接受一个字符串，然后输出该字符串反转后的字符串。
（字符串长度不超过1000）
```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        System.out.println(reverse(str));
    }
    public static String reverse(String str) {
        if (str.length() <= 1) return str;
        String res = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            res += str.charAt(i);
        }
        return res;
    }
}//input abcd
output dcba
```
### 13.句子逆序
将一个英文语句以单词为单位逆序排放。例如“I am a boy”，逆序排放后为“boy a am I”
所有单词之间用一个空格隔开，语句中除了英文字母外，不再包含其他字符。
```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        System.out.println(reverse(str));
    }
    public static String reverse(String sentence) {
        if (sentence.length() <= 1) return sentence;
        int index = 0;
        while (index < sentence.length() && sentence.charAt(index) != ' ') {
            index++;
        }
        if (index == sentence.length()) return sentence; //巧妙的处理了空格的问题
        return reverse(sentence.substring(index + 1)) + sentence.charAt(index) + sentence.substring(0, index);
    }
}//Input I am a boy
output boy a am I
```
### 14.字符串的连接最长路径查找
给定n个字符串，请对n个字符串按照字典序排列。
```Java
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            list.add(in.next());
        }
        Collections.sort(list); //String中实现了Comparable接口重写了compareTo方法
        for (String str : list) {
            System.out.println(str);
        }
    }
}//input 2 cap to
output cap to
```
### 15.求int型正整数在内存中存储时1的个数
输入一个int型的正整数，计算出该int型数据在内存中存储时1的个数。
```Java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        System.out.println(get1Nums(n));
        in.close();
    }
    
    public static int get1Nums(int n) {
        if (n < 2) return n & 1; //&操作取末位1
        return (n & 1) + get1Nums(n >> 1);
    }
}//input 5
output 2
```
[剑指15题]()
```Java
//上述get1Nums在输入负整数时会出错
    public static int get1Nums(int n) {
        int count = 0;
        while (n > 0) {
            count++;
            n = n & (n - 1); // n中最后一位1右边的全部去掉了变为了0
        }
        return count;
    }
```
### 16.购物单
王强今天很开心，公司发给N元的年终奖。王强决定把年终奖用于购物，他把想买的物品分为
两类：主件与附件，附件是从属于某个主件的，下表就是一些主件与附件的例子：
* 主件	附件
* 电脑	打印机，扫描仪
* 书柜	图书
* 书桌	台灯，文具
* 工作椅	无

如果要买归类为附件的物品，必须先买该附件所属的主件。每个主件可以有 0 个、 1 个或 2 个附件。附件不再有从属于自己的附件。王强想买的东西很多，为了不超出预算，他把每件物品规定了一个重要度，分为 5 等：用整数 1 ~ 5 表示，第 5 等最重要。他还从因特网上查到了每件物品的价格（都是 10 元的整数倍）。他希望在不超过 N 元（可以等于 N 元）的前提下，使每件物品的价格与重要度的乘积的总和最大。
    设第 j 件物品的价格为 v[j] ，重要度为 w[j] ，共选中了 k 件物品，编号依次为 j 1 ， j 2 ，……， j k ，则所求的总和为：
v[j 1 ]*w[j 1 ]+v[j 2 ]*w[j 2 ]+ … +v[j k ]*w[j k ] 。（其中 * 为乘号）
    请你帮助王强设计一个满足要求的购物单。
思路：依赖背包问题->分组背包问题，首先创建分组再每一个每组只会选择一种情况，转化为01背包问题。
```Java
//没有AC 改不动了 显示数组越界
class Shop {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Map<Integer, Integer>[] group = new Map[m + 1];
        for(int i = 0; i < group.length; i++) {
            group[i] = new HashMap<>();
        }
        for (int i = 1; i <= m; i++) {
            int v = in.nextInt();
            int p = in.nextInt();
            int q = in.nextInt();
            //分组
            if (v <= n) {
                if (q == 0) {
                    group[i].put(v, v * p);
                }
                if (q != 0) {
                    for (int k : group[q + 1].keySet()) {
                        if (k + v <= n) {
                            int value = group[q].get(k);
                            group[q].put(k + v, v * p + value);
                        }
                    }
                }
            }
        }
        int[] dp = new int[n / 10];
        dp[0] = 0;
        for(int i = 1; i < group.length; i++) {
            if (!group[i].isEmpty()) {
                for (int k : group[i].keySet()) {
                    for (int j = dp.length - 1; j >= k / 10; j--) {
                        dp[j] = Math.max(dp[j], dp[j - k / 10] + group[i].get(k));
                    }
                }
            }
        }
        System.out.println(dp[n / 10 - 1]);
    }
}
```