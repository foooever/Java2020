## 二叉搜索树BST
### BST性质
二叉搜索树（Binary Search Tree），是一种动态数据结构，BST的性质在于
每一个节点的值都大于其左子树的所有节点值并且小于其右子树所有节点的值。
```Java
BST ADT
BST<E extends Comparable<E>>

int getSize(); //BST规模
boolean isEmpty(); //是否为空树
void add(E e); //添加元素
boolean contains(E e); //是否包含元素e，查询
void traverse(Node node); //遍历操作
void remove(E e); //删除元素e
```
### BST实现
由于`BST`的特殊性，其存储的元素必须具有可比性，故而其底层实现为：
```Java
public class BST<E extends Comparable<E>> { }
```
扩展了`Comparable`的元素才可存入`BST`中。二叉树的存储结构如下：
```Java
    private class Node {
        public E e;
        public Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }
```
采用内部类的方式定义二叉树的存储结构，二叉搜索树`BST`的定义为：
```Java
    private Node root;
    private int size;

    public BST() {
        root = null;
        size = 0 ;
    }
	
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
```
包含一个根节点`root`和树的规模`size`。
#### 增--添加元素
向`BST`中添加元素，首先找到所添加的位置，再进行插入操作。首先判断根
是否为空，若为空则根`root`为插入位置，否则判断该元素与根大小关系，根据
`BST`的性质递归插入`root.left`**或**`root.right`。
```Java
    //向二分搜索树中添加元素
    public void add(E e) {
        root = add(root, e);
    }

    //向node为根的BST中添加元素e，递归算法
    //返回插入新节点后BST的根
    private Node add(Node node, E e) {
        if (node == null) {
            size++;
            return new Node(e);
        }

        if (e.compareTo(node.e) < 0) {
            node.left = add(node.left, e);
        } else if (e.compareTo(node.e) > 0){
            node.right = add(node.right, e);
        }
        return node;
    }
```
#### 查--查询元素
查询一个元素是否在`BST`中。首先判断与根`root`的大小关系，若相等返回`ture`
，否则递归查询`e.compareTo(root.e)`左子树**或**右子树。
```Java
    //看BST中是否包含元素e
    public boolean contains(E e) {
        return contains(root ,e);
    }

    private boolean contains(Node node, E e) {

        if (node == null) {
            return false;
        }

        if (e.compareTo(node.e) == 0) {
            return true;
        } else if (e.compareTo(node.e) < 0) {
            return contains(node.left, e);
        }else {
            return contains(node.right, e);
        }
    }
```
#### 遍历
遍历的方式有`DFS`深度优先和`BFS`广度优先，`DFS`对应于`preOrder,inorder,postOrder`
而`BFS`对应于层序遍历`levelOrder`。
`DFS`遍历的作用在于对节点进行操作，其伪代码如下:
```Java
    function traverse(DFS)(Node node) {
        if (node == null) return;
        op(); //先序遍历
        traverse(node.left);
        op(); //中序遍历
        traverse(node.right);
        op(); //后序遍历
    }
```
不同`DFS`的区别仅仅在于`op()`的位置，非递归形式采用`Stack`进行实现。
以前序遍历为例：
```Java
    //BST前序遍历
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {

        if (node == null) {
            return;
        }
        System.out.println(node.e);//op()
        preOrder(node.left);
        preOrder(node.right);
    }

    private void preOrderNR(Node node) { //非递归形式

        Stack<Node> stack = new Stack<>();
        stack.push(e);

        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            System.out.println(cur.e);
            if (cur.right != null) {
                stack.push(cur.right);
            }

            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
    }
```
后序遍历的非递归形式不太相同：
```Java
    private void postOrderNR(Node node) {
        Stack<Node> stack = new Stack<>();
        Node cur = node;
        while (cur != null) { //向左走到头
            stack.push(cur);
            cur = cur.left;
        }

        Node pre = null;
        while (!stack.isEmpty()) {
            cur = stack.pop();
            if (cur.right == null || cur.right == pre) { //此时左右子树都已访问完毕
                System.out.println(cur.e);
                pre = cur;
            } else { //此时cur.right != null && cur.left == pre
            //处理右子树
                stack.push(cur);
                cur = cur.right;
                while (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                }
            }
        }
    }
```
层序遍历`BFS`采用队列实现其非递归形式。
#### 删--删除元素
删除`BST`中任意元素，有以下三种情况：
* 待删除元素所在节点无左子树，只需将其右子树拼接为其父节点左子树；
* 待删除元素所在节点无右子树，只需将其左子树拼接为其父节点右子树；
* 待删除元素左右子树皆有，找出该节点后继节点（右子树中最小值进行删除）进行替换。
找出树中最小元素所在节点，向左走到头即`left == null`。
```Java
    //找出BST中最小值
    public E minimum() {
        if (size == 0)  {
            throw new IllegalArgumentException("empty");
        }
        return minimum(root).e;
    }

    //返回最小元素所在节点
    private Node minimum(Node node) {

        if (node.left == null) {
            return node;
        } else {
            return minimum(node.left);
        }
    }
```
删除最小元素，找到最小元素，进行删除：
```Java
    //删除BST中最小元素
    public E removeMin() {
        E ret = minimum();
        root = removeMin(root);
        return ret;
    }

    //删除node节点为根的BST最小值，并返回新BST的根节点
    private Node removeMin(Node node) {

        if (node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size--;
            return rightNode;
        }
        node.left = removeMin(node.left);
        return node;
    }
```
根据上文中三种删除元素时的情况进行分析，删除元素如下：
```Java
    //删除BST中元素e
    public void remove(E e) {
        root = remove(root ,e);
    }

    //删除node为根的BST中元素e，返回新BST根
    private Node remove(Node node, E e) {
        if (node == null) {
            return null;
        }
        
		//寻找元素e所在节点
        if (e.compareTo(node.e) < 0) {
            node.left = remove(node.left, e);
            return node;
        } else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);
            return node;
        } else { //待删除元素e节点为当前节点node

            if (node.left == null) { //case 1 左子树空
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }
            if (node.right == null) { //case 2 右子树空
                Node leftNode = node.left;
                node.right = null;
                size--;
                return node;
            }
            
			//case 3 左右子树皆不空，寻找当前节点后继节点
            Node successor = minimum(node.right);
            successor.left = node.left;
            successor.right = removeMin(node.right);
            node.left = node.right = null;
            return successor;
        }
    }
```