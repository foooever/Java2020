## AVL平衡二叉树
### 性质
* 首先AVL是一个二叉搜索树[BST][1]
* 其次，对任意节点左、右节点子树的高度差不超过1
### AVL的实现
AVL树是在BST基础上进行改进的，故而在BST的基础上进行修改，采用(Key, Value)泛型的方式
实现AVLTree。
```Java
AVL ADT
AVLTree<K extends Comparable<K>, V>

int getSize(); //AVL规模
boolean isEmpty(); //是否为空树
void add(K key, V value); //添加元素
void set(K key, V newValue); //修改元素
boolean contains(K key); //是否包含key，查询
V remove(K key); //删除操作
```
其中，存储结构是二叉树，采用内部类的形式进行定义。由于平衡二叉树的独特性质，
添加`height`记录节点的高度值，用于计算平衡因子。
```Java
    private class Node {
        K key;
        V value;
        Node left;
        Node right;
        int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            height = 1; //叶子节点的高度为1
        }
    }

    private Node root;
    private int size;

    //构造函数
    public AVLTree() {
        root = null;
        size = 0;
    }
```
由于平衡二叉树在进行元素操作时会破坏其平衡性，故而通过节点的平衡因子进行
平衡性的度量。辅助函数如下：
```Java
    //Node的高度
    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    //获得node节点的平衡因子
    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
```
#### 增-添加元素
添加元素和BST的添加过程类似，进行节点的比较，此时是比较key的值；但由于平衡二叉树
的性质，在添加元素后会破坏平衡性，需要进行调整。同时由于添加了元素，node的高度需要进行维护。
```Java
    public void add(K key, V value) {
        root = add(root, key, value);
    }

    //向以node为根的BST中添加元素（key, value），递归算法
    //返回新节点插入后BST的根
    //需要维护高度值height
    private Node add(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = add(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = add(node.right, key, value);
        } else { //==
            node.value = value;
        }

        //更新height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        //计算平衡因子
        int balanceFactor = getBalanceFactor(node);

        //维护AVL性质平衡性
        //右旋(当前节点平衡打破，且node.left是左倾子树（左>右高度）)
        //LL
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {//
            return rightRotate(node);
        }

        //左旋 右倾的性质
        //RR
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }

        //LR
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        //RL
        if (balanceFactor < - 1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
```
在以node为根的树中插入新元素后，会发生的破坏平衡性的情况有以下四种：
* LL-在node为根的AVL树中插入新元素的位置为`node.left.left(LL)`上破坏了平衡性
* RR-在node为根的AVL树中插入新元素的位置为`node.right.right(RR)`上破坏了平衡性
* LR-在node为根的AVL树中插入新元素的位置为`node.left.right(LR)`上破坏了平衡性
* RL-在node为根的AVL树中插入新元素的位置为`node.right.left(RL)`上破坏了平衡性

针对LL的情况如下所示：
```Java
    //       y                             x
    //      / \                          /   \
    //     x   T4  向右旋转（y）        z     y
    //    / \     -------------->      / \   / \
    //   z  T3                        T1 T2 T3 T4
    //  / \
    // T1 T2
    // 大小关系为T1<z<T2<x<T3<y<T4
```
y节点为当前node，破坏了平衡性，此时情况为LL，具体表述为：
`y.left.height - y.right.height > 1 && x.left.height - x.right.height >= 0`，插入
的节点为z，为当前node(y)的左子树的左子树上。此时需要对y进行顺时针旋转（右旋）。
返回x作为新根的平衡二叉树。
```Java
    //右旋返回新的根节点
    //对节点y进行右旋，返回旋转后的跟x
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T3 = x.right;

        //右旋
        x.right = y;
        y.left = T3;

        //更新height值,x,y
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }
```
同理，对于RR情况，是完全对称的。其表述为：
`balanceFactor < -1 && getBalanceFactor(node.right) <= 0`
```Java
    //左旋返回新的根节点
    //对节点y进行左旋，返回旋转后的跟x
    //       y                                 x
    //      / \                              /   \
    //     T1  x        向左旋转（y）       y     z
    //        / \     -------------->      / \   / \
    //       T2  z                        T1 T2 T3 T4
    //          / \
    //         T3 T4
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node T3 = x.left;

        //左旋
        x.left = y;
        y.right = T3;

        //更新height值,x,y
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }
```
对于LR情况：`balanceFactor > 1 && getBalanceFactor(node.left) < 0`，
左子树右倾的情况。先将x左旋变为LL情况再右旋y即平衡。
```Java
    //LR
    //       y                      y                   z
    //      / \                    / \                /   \
    //     x   T4  向左旋转（x）  z   T4  LL(右旋y)  x     y
    //    / \     ---------->    / \      ------->  / \   / \
    //   T1  z                  x  T3              T1 T2 T3 T4
    //      / \                / \
    //     T2 T3              T1 T2
    node.left = leftRotate(node.left);//node(y)
    return rightRotate(node);
```
RL情况与LR相对称：`balanceFactor < - 1 && getBalanceFactor(node.right) > 0`，
右子树左倾的情况。先将x右旋变为RR情况在进行左旋y即平衡。
```Java
    //       y                      y                        z
    //      / \                    / \                     /   \
    //     T1  x    向右旋转（x） T1  z  向左旋转（y）    y     x
    //        / \  ----------->      / \    -------->    / \   / \
    //       z  T4                  T2  x               T1 T2 T3 T4
    //      / \                        / \                    
    //     T2 T3                      T3 T4 
    node.right = rightRotate(node.right);//node(y)
    return leftRotate(node);
```
添加操作`void add(K key, V value)`与BST的不同之处在于，递归插入节点后需要进行以下操作：
(node)1.更新node.height的值；2.计算平衡因子；3.维持AVL性质（自平衡机制）。
#### 改-修改元素
```Java
    //改与BST完全一样，不会破坏AVL性质
    public void set(K key, V newValue) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new IllegalArgumentException("doesn't exist");
        }
        node.value = newValue;
    }

    public V get(K key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    private Node getNode(Node node, K key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return getNode(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }
```
#### 删-删除元素
删除元素的操作和BST类似，不同之处与增操作相似，在删除元素后：1.修改node.height；2.计算平衡因子；3.维持AVL性质（自平衡机制）。
```Java
    //删除key
    public V remove(K key) {
        Node node = getNode(root, key);
        if (node != null) {
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    //最小元素节点
    private Node minimum(Node node) {

        if (node.left == null) {
            return node;
        } else {
            return minimum(node.left);
        }
    }

    private Node remove(Node node, K key) {

        if (node == null) {
            return null;
        }

        Node retNode; //node会破坏AVL性质，对最后node进行自平衡调整

        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
            retNode = node;
        } else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            retNode = node;
        } else { //待删除节点是当前节点

            if (node.left == null) {
                Node rightNode = node.right;
                node.left = null;
                size--;
                retNode = rightNode;
            }

            else if (node.right == null) {
                Node leftNode = node.left;
                node.right = null;
                size--;
                retNode = leftNode;
            } else {

                //待删除节点左右子树均不为空
                Node successor = minimum(node.right);
                successor.left = node.left;
                //successor.right = removeMin(node.right); //仍为维持AVL
                successor.right = remove(node.right, successor.key);//删除最小节点（succersor.key）
                node.left = node.right = null;
                retNode = successor;
            }
        }

        //删除的是叶子节点
        if (retNode == null) {
            return null;
        }

        //更新height
        retNode.height = 1 + Math.max(getHeight(retNode.left), getHeight(retNode.right));

        //计算平衡因子
        int balanceFactor = getBalanceFactor(retNode);

        //维护以node为根的子树的AVL性质平衡性

        //右旋(当前节点平衡打破，且node.left是左倾子树（左>右高度）)
        //LL
        if (balanceFactor > 1 && getBalanceFactor(retNode.left) >= 0) {//
            return rightRotate(retNode);
        }

        //左旋 右倾的性质
        //RR
        if (balanceFactor < -1 && getBalanceFactor(retNode.right) <= 0) {
            return leftRotate(retNode);
        }

        //LR
        if (balanceFactor > 1 && getBalanceFactor(retNode.left) < 0) {
            retNode.left = leftRotate(retNode.left);
            return rightRotate(retNode);
        }

        //RL
        if (balanceFactor < - 1 && getBalanceFactor(retNode.right) > 0) {
            retNode.right = rightRotate(retNode.right);
            return leftRotate(retNode);
        }

        return retNode;
    }
```
### 平衡二叉树的应用（Set和Map的底层实现之一）
此处实现方式有点[代理][2]（继承和组合的中庸之道，将一个成员对象置于新类中，同时暴露成员对象所有方法）的意思。
```Java
//Set
public class AVLSet<E extends Comparable<E>> implements Set<E> {

    private AVLTree<E, Object> avl;

    public AVLSet() {
        avl = new AVLTree<>();
    }

    @Override
    public void add(E e) {
        avl.add(e, null);
    }

    @Override
    public void remove(E e) {
        remove(e);
    }

    @Override
    public boolean contains(E e) {
        return avl.contains(e);
    }

    @Override
    public int getSize() {
        return avl.getSize();
    }

    @Override
    public boolean isEmpty() {
        return avl.isEmpty();
    }
}

//Map
public class AVLMap<K extends Comparable<K>, V> implements Map<K, V> {

    private AVLTree<K, V> avl;

    public AVLMap() {
        avl = new AVLTree<>();
    }

    @Override
    public void add(K key, V value) {
        avl.add(key, value);
    }

    @Override
    public V remove(K key) {
        return avl.remove(key);
    }

    @Override
    public boolean contains(K key) {
        return avl.contains(key);
    }

    @Override
    public V get(K key) {
        return avl.get(key);
    }

    @Override
    public void set(K key, V value) {
        avl.set(key, value);
    }

    @Override
    public int getSize() {
        return avl.getSize();
    }

    @Override
    public boolean isEmpty() {
        return avl.isEmpty();
    }
}
```

[1]:https://github.com/foooever/Java2020/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84/%E4%BA%8C%E5%8F%89%E6%90%9C%E7%B4%A2%E6%A0%91BST/%E4%BA%8C%E5%8F%89%E6%90%9C%E7%B4%A2%E6%A0%91BST.md
[2]:https://github.com/foooever/Java2020/blob/master/Java%E8%AF%AD%E8%A8%80%E5%9F%BA%E7%A1%80/%E5%A4%8D%E7%94%A8.md