import java.util.Stack;

public class BST<E extends Comparable<E>> {

    private class Node {
        public E e;
        public Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }

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

    //向二分搜索树中添加元素
    public void add(E e) {
        if (root == null) {
            root = new Node(e);
            size++;
        } else {
            add(root, e);
        }
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

    //BST前序遍历
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {

        if (node == null) {
            return;
        }
        System.out.println(node.e);
        preOrder(node.left);
        preOrder(node.right);
    }

    private void preOrderNR(Node node) {

        Stack<Node> stack = new Stack<>();
        stack.push(node);

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

    private void postOrderNR(Node node) {
        Stack<Node> stack = new Stack<>();
        Node cur = node;
        while (cur != null) {
            stack.push(cur);
            cur = cur.left;
        }

        Node pre = null;
        while (!stack.isEmpty()) {
            cur = stack.pop();
            if (cur.right == null || cur.right == pre) {
                System.out.println(cur.e);
                pre = cur;
            } else {
                stack.push(cur);
                cur = cur.right;
                while (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                }
            }
        }
    }

    //找出BST中最小值
    public E minimum() {
        if (size == 0)  {
            throw new IllegalArgumentException("empty");
        }
        return minimum(root).e;
    }

    private Node minimum(Node node) {

        if (node.left == null) {
            return node;
        } else {
            return minimum(node.left);
        }
    }

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

    //删除BST中元素e
    public void remove(E e) {
        root = remove(root ,e);
    }

    //删除node为根的BST中元素e，返回新BST根
    private Node remove(Node node, E e) {
        if (node == null) {
            return null;
        }

        if (e.compareTo(node.e) < 0) {
            node.left = remove(node.left, e);
            return node;
        } else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);
            return node;
        } else {

            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }
            if (node.right == null) {
                Node leftNode = node.left;
                node.right = null;
                size--;
                return node;
            }

            Node successor = minimum(node.right);
            successor.left = node.left;
            successor.right = removeMin(node.right);
            node.left = node.right = null;
            return successor;
        }
    }
}
