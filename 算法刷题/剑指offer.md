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
（含根、叶结点）形成树的一条路径，最长路径的长度为树的深度。

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
