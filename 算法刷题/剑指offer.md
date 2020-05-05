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