### （剑指）两个链表的第一个公共结点（20200504）
描述：输入两个链表，找出它们的第一个公共结点。（注意因为
传入数据是链表，所以错误测试数据的提示是用其他方式显示的
，保证传入数据是正确的）

思路1：公共长度为k，则`p1,p2`间隔k进行遍历。
```Java
    public ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        ListNode p1 = pHead1;
        ListNode p2 = pHead2;
        int countList1 = 0;
        int countList2 = 0;
        while (p1 != null) {
            countList1++;
            p1 = p1.next;
        }
        while (p2 != null) {
            countList2++;
            p2 = p2.next;
        }
        p1 = pHead1;
        p2 = pHead2;
        //取前后指针p1,p2间隔k进行遍历
        if (countList1 > countList2) {
            for (int i = 0; i < countList1 - countList2; i++) {
                p1 = p1.next;
            }
        } else if (countList1 < countList2) {
            for (int i = 0; i < countList2 - countList1; i++) {
                p2 = p2.next;
            }
        }
        while (p1 != null && p2 != null) {
            if (p1.equals(p2)) {
                return p1;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        return null;
    }
```
官方给出思路2：考虑问题的性质，需要从尾部进行比较，单链表智能从头开始，故而针对此种“后进先出”采用辅助栈进行操作。
### （剑指）数字在排序数组中出现的次数（20200504）
描述：统计一个数字在排序数组中出现的次数。

思路：二分搜索
```Java
    public int GetNumberOfK(int [] array , int k) {
        int flag = binarySearch(array, k);
        if (flag == -1) {
            return 0;
        } else {
            int count = 0;
            int i = flag;
            while (i >= 0) {
                if (array[i] == k) {
                    count++;
                } else {
                    break;
                }
                i--;
            }
            i = flag;
            while (i < array.length) {
                if (array[i] == k) {
                    count++;
                } else {
                    break;
                }
                i++;
            }
            return count - 1;
        }
    }
    
    private int binarySearch(int[] array, int target) {
        if (array.length <= 0) {
            return -1;
        }
        int lo = 0;
        int hi = array.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (array[mid] == target) {
            //此处添加条件 判断是否为第一个target/最后一个 if ((mid >0 && array[mid - 1] != target) || mid == 0)
                return mid;
            } else if (array[mid] > target) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return -1;
    }
```
剑指官方给出的修改后的二分搜索`BinarySearch`。
### （剑指）二叉树的深度（20200504）
描述：输入一棵二叉树，求该树的深度。从根结点到叶结点依次经过的结点
（含根、叶结点）形成树的一w1条路径，最长路径的长度为树的深度。

思路：递归
```Java
    public int TreeDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return treeDepth(root);
    }
    
    private int treeDepth(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(treeDepth(node.left), treeDepth(node.right));
    }
```
### （剑指）平衡二叉树（20200505）
描述：输入一棵二叉树，判断该二叉树是否是平衡二叉树。
在这里，我们只需要考虑其平衡性，不需要考虑其是不是排序二叉树。

思路：递归判断+深度
```Java
public boolean IsBalanced_Solution(TreeNode root) {
        if (root == null) return true;
        return IsBalanced_Solution(root.left) && 
               IsBalanced_Solution(root.right) && 
               Math.abs(TreeDepth(root.left) - TreeDepth(root.right)) <= 1;
    }
```
### （剑指）数组中只出现一次的数字（20200505）
描述：一个整型数组里除了两个数字之外，其他的数字都出现了两次。
请写程序找出这两个只出现一次的数字。（O（N）时间复杂度+O（1）空间复杂度）

思路：Java中的二进制操作，异或操作和按某位0/1值进行区分。将2个不同数字简单化为一个，
考虑如“抽乌龟游戏”的性质进行两两配对，消除对的方式为异或。
```Java
    public void FindNumsAppearOnce(int [] array,int num1[] , int num2[]) {
        int resultOR = 0;
        for (int i = 0; i < array.length; i++) {
            resultOR ^= array[i];//两个单只异或结果必然不为0
        }
        int flag = 1;
        while ((flag & resultOR) == 0) {
            flag = flag<<1;//取出第一位1的位数
        }
        for (int i = 0; i < array.length; i++) {
            //进行分类，按2个“一个数组中含有一个单只而其他均为双”
            if ((array[i] & flag) == 0) {
                num1[0] ^= array[i];
            } else {
                num2[0] ^= array[i];
            }
        }
    }
```
### （剑指）和为S的连续正数序列（20200505）
描述：小明很喜欢数学,有一天他在做数学作业时,要求计算出9~16的和,他马上就写出了正确答案
是100。但是他并不满足于此,他在想究竟有多少种连续的正数序列的和为100(至
少包括两个数)。没多久,他就得到另一组连续正数和为100的序列:18,19,20,21,22。现在把问题交给你,你能不能也很快的找出所有和为S的连续正数序列? Good Luck!

输出描述:输出所有和为S的连续正数序列。序列内按照从小至大的顺序，序列间
按照开始数字从小到大的顺序。

思路：滑动窗口。
```Java
    public ArrayList<ArrayList<Integer> > FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        if (sum <= 2) return list;
        
        int lo = 1;
        int hi = 2;
        
        int count = lo + hi;
        ArrayList<Integer> li = new ArrayList<>();
        li.add(lo);
        li.add(hi);
        while (lo < hi && hi <= (sum / 2 + 1)) {
            if (count == sum) {
                ArrayList<Integer> tmp = new ArrayList<>();
                tmp.addAll(li);
                list.add(tmp);//传引用
                hi++;
                count += hi;
                li.add(hi);
            } else if (count < sum) {
                hi++;
                count += hi;
                li.add(hi);
            } else {
                count -= lo;
                lo++;
                li.remove(0);
            }
        }
        return list;
    }
```
### （剑指）剪绳子（20200508）
描述：给你一根长度为n的绳子，请把绳子剪成整数长的m段（m、n都是整数，n>1并且m>1），每段绳子的长度记为k[0],k[1],...,k[m]。请问k[0]xk[1]x...xk[m]可能的最大乘积是多少？例如，当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此时得到的最大乘积是18。

思路1：DP思路，后面绳子长度增加时，只需考虑`dp[j]*dp[i - j]`在后面`j`处砍一刀。
```Java
    public int cutRope(int target) {
        //dp解法 m > 1缘故对于2.3长度的特殊处理
        if (target < 2) return 0;
        if (target == 2) return 1;
        if (target == 3) return 2;
        int[] dp = new int[target + 1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 3;
        for (int i = 4; i <= target; i++) {
            for (int j = 1; j <= i / 2; j++) {
                dp[i] = Math.max(dp[i], dp[j] * dp[i - j]);
            }
        }
        return dp[target];
    }
```
思路2：贪心算法，尽可能多的剪出长度为3的绳子，当最后剩余4是将其分为2×2。
### （剑指）机器人的运动范围（20200509）
描述：地上有一个m行和n列的方格。一个机器人从坐标0,0的格子开始移动，
每一次只能向左，右，上，下四个方向移动一格，但是不能进入行坐标和列
坐标的数位之和大于k的格子。 例如，当k为18时，机器人能够进入方格（35,37），因为3+5+3+7 = 18。但是，它不能进入方格（35,38）
，因为3+5+3+8 = 19。请问该机器人能够达到多少个格子？

思路：`boolean[][] flag`数组进行标记访问节点，分四个方向进行遍历。回溯法（相比递归某种程度上需要进行状态的恢复）
```Java
public class Solution {
    private boolean[][] flag;
    private int[][] action = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    public int movingCount(int threshold, int rows, int cols)
    {
        flag = new boolean[rows][cols];
        moving(threshold, 0, 0, rows, cols);
        int retCount = 0;
        for (boolean[] f : flag) {
            for (boolean ff : f) {
                if (ff) {
                    retCount++;
                }
            }
        }
        
        return retCount;
    }
    
    private void moving(int k, int xx, int yy, int rows, int cols) {
        if (isValid(k, xx, yy, rows, cols)) {
            if (flag[xx][yy]) {
                return;
            }
            flag[xx][yy] = true;
            for (int i = 0; i < 4; i++) {
                int newX = xx + action[i][0];
                int newY = yy + action[i][1];
                moving(k, newX, newY, rows, cols);
            }
        }
    }
    
    private boolean isValid(int k, int xx, int yy, int rows, int cols) {
        if (xx < 0 || xx >= rows || yy < 0 || yy >= cols) {
            return false;
        }
        if (countDigital(xx) + countDigital(yy) > k) {
            return false;
        }
        return true;
    }
    
    private int countDigital(int n) {
        int count = 0;
        while (n > 0) {
            count += n % 10;
            n /= 10;
        }
        return count;
    }
}
```
### 搜索算法（20200509）
#### BFS && DFS
BFS步骤
* 1.从顶点开始，将顶点可达的进队
* 2.出队，进行相关操作，在重复1中操作

DFS步骤
* 1.递归终止条件，如矩阵节点(9,9)或叶子节点(node.left == node.right == null)
* 2.递归主体，搜索的方向，矩阵{上、右、下、左}或二叉树left & right
* 3.针对回溯，进行标记`flag[x] = 1; DFS(x.left); flag[x] = 0`诸如此类 
```Java
public class Solution {
    private boolean[][] flag;
    private int[][] action = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    public int movingCount(int threshold, int rows, int cols)
    {
        if (threshold < 0) {
            return 0;
        }
        flag = new boolean[rows][cols];
        DFS(threshold, 0, 0, rows, cols);
        if (!flag[0][0]) {
            flag[0][0] = true;
        }
        int retCount = 0;
        for (boolean[] f : flag) {
            for (boolean ff : f) {
                if (ff) {
                    retCount++;
                }
            }
        }
        
        return retCount;
    }
    private class Node {
        int xx;
        int yy;
        public Node(int xx, int yy) {
            this.xx = xx;
            this.yy = yy;
        }
    }
    //BFS
    private void BFS(int k, int xx, int yy, int rows, int cols) {
        Queue<Node> queue = new LinkedList<>();
        flag[xx][yy] = true;
        queue.add(new Node(xx, yy));
        while (!queue.isEmpty()) {
            Node node = queue.remove();
            
            for (int i = 0; i < 4; i++) {
                int newX = node.xx + action[i][0];
                int newY = node.yy + action[i][1];
                if (isValid(k, newX, newY, rows, cols)){
                    if (flag[newX][newY]) {
                        continue;
                    }
                    flag[newX][newY] = true;
                    queue.add(new Node(newX, newY));
                }
            }
        }
    }

    //DFS
    private void DFS(int k, int xx, int yy, int rows, int cols) {
        //此时搜索的终止条件判断if flag[xx][yy] == flag[good_x][good_y] 递归终止条件
        //可以进行相应操作
        /*
        if (flag[xx][yy]) {
            return;
        }*/

        //遍历的方向，此时为4个方向
        for (int i = 0; i < 4; i++) {
            int newX = xx + action[i][0];
            int newY = yy + action[i][1];
            if (isValid(k, newX, newY, rows, cols)){
                if (flag[newX][newY]) {
                    continue;
                }
                //标记访问节点
                flag[newX][newY] = true;
                DFS(k, newX, newY, rows, cols);
                //回溯状态，本题不需要恢复状态
                //对于寻找路径的题型 flag[newX][newY] = false
                
            }
        }
    }
    
    private boolean isValid(int k, int xx, int yy, int rows, int cols) {
        if (xx < 0 || xx >= rows || yy < 0 || yy >= cols) {
            return false;
        }
        if (countDigital(xx) + countDigital(yy) > k) {
            return false;
        }
        return true;
    }
    
    private int countDigital(int n) {
        int count = 0;
        while (n > 0) {
            count += n % 10;
            n /= 10;
        }
        return count;
    }
}
```
### 单源最短路径（Dijkstra算法）
```Java
import java.util.Scanner;

public class DijkstraAlgorithm {
    private static int MaxValue = 100000;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入顶点数和边数:");
        //顶点数
        int vertex = input.nextInt();
        //边数
        int edge = input.nextInt();

        int[][] matrix = new int[vertex][vertex];

        //初始化邻接矩阵
        //邻接表的形式
        for (int i = 0; i < vertex; i++) {
            for (int j = 0; j < vertex; j++) {
                matrix[i][j] = MaxValue;
            }
        }
        for (int i = 0; i < edge; i++) {
            int source = input.nextInt();
            int target = input.nextInt();
            int weight = input.nextInt();
            matrix[source][target] = weight;
        }

        //单源最短路径，源点
        int source = input.nextInt();
        //调用dijstra算法计算最短路径
        dijkstra(matrix, source);
    }

    //单源最短路径Dijkstra算法
    //S:已经计算最短路径的节点
    //T：未计算节点
    //对于源点到T集合中的可达节点进行最短路径计算，(v0->vx)中间（v0->vk-vx）vk只能是S中节点
    private static void dijkstra(int[][] matrix, int source) {
        //最短路径长度
        int[] shortest = new int[matrix.length];

        //是否在S中，已经计算
        int[] visited = new int[matrix.length];

        //记录单源路径
        String[] path = new String[matrix.length];

        //initial path
        for (int i = 0; i < matrix.length; i++) {
            path[i] = new String(source + "->" + i);
        }

        shortest[source] = 0;
        visited[source] = 1;

        for (int i = 1; i < matrix.length; i++) {
            int min = Integer.MAX_VALUE; //代表无路径
            int index = -1; //每次选取最短路径节点加入故而index不会等于-1

            for (int j = 0; j < matrix.length; j++) {
                //针对未计算的节点visited[j] == 0
                if (visited[j] == 0 && matrix[source][j] < min) {
                    min = matrix[source][j];
                    index = j;
                }
            }

            //update route
            shortest[index] = min;
            visited[index] = 1;

            //update index as internal node
            //更新T集合(visited[k] == 0)中从源点到节点的最短路径
            for (int m = 0; m < matrix.length; m++) {
                if (visited[m] == 0 && matrix[source][index] + matrix[index][m] < matrix[source][m]) {
                    matrix[source][m] = matrix[source][index] + matrix[index][m];
                    path[m] = path[index] + "->" + m;
                }
            }
        }

        //打印最短路径
        for (int i = 0; i < matrix.length; i++) {
            if (i != source) {
                if (shortest[i] == MaxValue) {
                    System.out.println(source + "到" + i + "不可达");
                } else {
                    System.out.println(source + "到" + i + "的最短路径为：" + path[i] + "，最短距离是：" + shortest[i]);
                }
            }
        }
    }
}
```
参考:https://blog.csdn.net/qq_34842671/article/details/90083037
### 多源最短路径
循环多次单源最短路径方法O(N^3)时间复杂度；另一种方法称为`Floyd`方法。\
场景：计算城市之间的距离等情况。\
思路：可以用BFS或者DFS进行遍历，终止条件为目标顶点。

分析：对于两点之间最短距离，如果无中转节点，则其原来值为最小，现在可
进行中转（a->b -- a->k1->k2->...->b其在ki为中转节点）。
```Java
//邻接矩阵初始化i == j <- 0 否则初始化为Integer.MAX_VALUE
//若要存储路径，path[i][j]初始化为i，没进行一次e[i][j]更新则更新path[i][j] = path[k][j];
for(k=1;k<=n;k++)
    for(i=1;i<=n;i++)
        for(j=1;j<=n;j++)
            if(e[i][j]>e[i][k]+e[k][j])
                e[i][j]=e[i][k]+e[k][j];
```
对于该方法`Floyd`计算单源最短路径只需要二层循环进行固定
```Java
    private static void floyd(int[][] matrix, int source) {
        int[][] path = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                path[i][j] = i;
            }
        }
        for (int k = 0; k < matrix.length; k++) {
            //单源 source
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[source][j] > matrix[source][k] + matrix[k][j]) {
                    matrix[source][j] = matrix[source][k] + matrix[k][j];
                    path[source][j] = path[k][j];
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            if (i != source) {
                System.out.print(source + "->");
                int tmp = path[source][i];
                while (tmp != 0) {
                    System.out.print(tmp + "->");
                    tmp = path[source][tmp];
                }
                System.out.print(i);
                System.out.println("，最短路径为：" + matrix[source][i]);
            }
        }
    }
```
参考：https://blog.csdn.net/LiuHuan_study/article/details/85411607
### DAG及其应用
#### AOV网（弧表示活动优先关系，顶点为活动）
拓扑排序--可以用户判断是否有环（循环引用），或者任务调度的开始时间。
```Java
//拓扑排序
    public static void topologicalSort(int[][] matrix) {
        int[] inDegree = findInDegree(matrix);
        System.out.println(Arrays.toString(inDegree));
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < matrix.length; i++) {
            if (inDegree[i] == 0) {
                stack.push(i);
            }
        }

        int count = 0;

        while (!stack.isEmpty()) {
            int top = stack.pop();
            System.out.print(top + "->");
            count++;

            for (int i = 0; i < matrix[0].length; i++) {
                if (matrix[top][i] < MaxValue && (--inDegree[i]) == 0) {
                    stack.push(i);
                }
            }
        }

        if (count < matrix.length) {
            System.out.println("error");
        } else {
            System.out.println("ok");
        }

    }

    private static int[] findInDegree(int[][] matrix) {
        int[] ret = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] < MaxValue) {
                    ret[j]++;
                }
            }
        }
        return ret;
    }
```
#### AOE网（顶点表示事件（之前的活动都结束），边表述活动）
关键路径--路径长度最长的路径称之为关键路径，决定完成整个工程所需要的时间，(vi, vj)的最短路径为
代表了vj事件最早发生事件，决定vj后继活动的最早发生时间。对于AOE网中活动ai(边)，`ete(i),lte(i)`分别表示
活动ai的最早和最迟开始时间，`ete(i) = lte(i)`称为关键活动，关键路径上所有活动都是关键活动决定了工程的总时间。

`etv(k), ltv(k)`表示节点（事件）的最早最晚发生时间。
```Java
//etv(k)计算
if (k == 0) { etv(k) = 0}
else etv(k) = max{etv(i) + len(vi, vk)} //vi表示存在边(vi, vk)的节点

//ltv(k)
if k = n - 1(最后一个节点) ltv(k) = etv(k)
else ltv(k) = min{ltv(j) - len(vk, vj)} //vj表示从vk节点出发所到达节点
```
求解关键路径
* 1.从源点v0出发，根据拓扑排序计算源点到汇点每个顶点的etv(事件最早发生时间)，若有环则无关键路径；
* 2.从汇点出发， ltv[n - 1] =etv[n - 1]，按照逆拓扑排序计算每个节点的ltv；
* 3.根据etv,ltv求每条边(ei)的ete,lte,若ete = lte 则为关键路径。
```Java
    //ete(v_i, v_k) = etv[i]; //节点i的最早开始时间
    //lte(v_i, v_k) = ltv[k] - weight(v_i, v_k);
```
```Java
import java.util.Stack;
//边节点
class EdgeNode {

    public int adjvex; //邻接表结构adlist的(vi, vj)的vj顶点
    public int weight; //边权
    public EdgeNode next; //邻接表next

    public EdgeNode(int adjevex, EdgeNode next) {
        this.adjvex = adjevex;
        this.next = next;
    }

    public EdgeNode(int adjevex, int weight, EdgeNode next) {
        this.adjvex = adjevex;
        this.weight = weight;
        this.next = next;
    }
}

//顶点节点
class VertexNode {

    public int in; //顶点入度
    public Object data; //顶点
    public EdgeNode firstedge; //邻接表头节点

    public VertexNode(Object data, int in, EdgeNode firstedge) {
        this.data = data;
        this.in = in;
        this.firstedge = firstedge;
    }
}

public class CriticalPathSort {

    int[] etv, ltv; //事件的最早最晚开始时间
    Stack stack = new Stack(); //存储入度为0的顶点，便于每次寻找入度为0的顶点时都遍历整个邻接表
    Stack stack2 = new Stack(); //将顶点序号压入拓扑序列的栈
    static VertexNode[] adjList; //邻接表



    public boolean TopologicalSort() {
        EdgeNode e;
        int k, top;
        int count = 0;
        etv = new int[adjList.length];
        for (int i = 0; i < adjList.length; i++) {
            if(adjList[i].in == 0) {
                stack.push(i);
            }
        }

        //初始化etv--最早开始时间
        //前驱节点+前驱节点到目标节点权值和中最大的一个（必须等前驱节点全部完成）
        for (int i = 0; i < adjList.length; i++) {
            etv[i] = 0;
        }

        while(!stack.isEmpty()) {
            top = (int) stack.pop();
            count++;
            stack2.push(top); //记录topologicalSort
            for (e = adjList[top].firstedge; e != null; e = e.next) {
                k = e.adjvex;
                if((--adjList[k].in) == 0) {
                    stack.push(k);
                }

                //进行更新etv的值
                //etv[k] = max{etv[top] + weight(v_top, v_k)}
                if(etv[top] + e.weight > etv[k]) {
                    etv[k] = etv[top] + e.weight;
                }
            }
        }

        //topologicalSort的应用之一，判断工程是否有环，可以顺利实施？
        if(count < adjList.length) return false;
        else return true;

    }


    public void CriticalPath() {
        EdgeNode e;
        int top, k;
        int ete, lte;
        if(!this.TopologicalSort()) {
            System.out.println("该网中存在回路!");
            return;
        }

        //topologicalSort逆序（先进后出stack2中存放）计算ltv
        ltv = new int[adjList.length];
        for (int i = 0; i < adjList.length; i++) {
            ltv[i] = etv[etv.length - 1];
        }

        //更新ltv
        //ltv[top] = min{ltv[k] - weight(v_top, v_k)}
        //v_top -> v_k weight = weight(v_top, v_k)在v_top的邻接链表中
        //后继节点开始取决于当前节点，故而当前节点最晚开始时间是后继节点ltv-weight(当前， 后继)中最小的值
        while(!stack2.isEmpty()) {
            top = (int) stack2.pop();
            for(e = adjList[top].firstedge; e != null; e = e.next) {
                k = e.adjvex;
                if(ltv[k] - e.weight < ltv[top]) {
                    ltv[top] = ltv[k] - e.weight;
                }
            }
        }

        //ete(v_i, v_k) = etv[i]; //节点i的最早开始时间
        //lte(v_i, v_k) = ltv[k] - weight(v_i, v_k);
        for (int i = 0; i < adjList.length; i++) {
            for(e = adjList[i].firstedge; e != null; e = e.next) {
                k = e.adjvex;
                ete = etv[i];
                lte = ltv[k] - e.weight;
                if(ete == lte) {
                    System.out.print("<" + adjList[i].data + "," + adjList[k].data + "> length: " + e.weight + ",");
                }
            }
        }
    }

    //获取单个节点的邻接链表最后一个节点
    public static EdgeNode getAdjvex(VertexNode node) {
        EdgeNode e = node.firstedge;
        while(e != null) {
            if(e.next == null) break;
            else
                e = e.next;
        }
        return e;
    }

    public static void main(String[] args) {
        int[] ins = {0, 1, 1, 2, 2, 1, 1, 2, 1, 2};
        int[][] adjvexs = {
                {2, 1},
                {4, 3},
                {5, 3},
                {4},
                {7, 6},
                {7},
                {9},
                {8},
                {9},
                {}
        };
        int[][] widths = {
                {4, 3},
                {6, 5},
                {7, 8},
                {3},
                {4, 9},
                {6},
                {2},
                {5},
                {3},
                {}
        };

        //创建邻接表
        adjList = new VertexNode[ins.length];
        for (int i = 0; i < ins.length; i++) {
            adjList[i] = new VertexNode("V"+i, ins[i],null);
            if(adjvexs[i].length > 0) {
                for (int j = 0; j < adjvexs[i].length; j++) {
                    if(adjList[i].firstedge == null)
                        adjList[i].firstedge = new EdgeNode(adjvexs[i][j], widths[i][j], null);
                    else {
                        getAdjvex(adjList[i]).next = new EdgeNode(adjvexs[i][j], widths[i][j], null);
                    }
                }
            }
        }

        CriticalPathSort c = new CriticalPathSort();
        c.CriticalPath();

    }
}
```
参考：https://www.cnblogs.com/lishanlei/p/10707808.html