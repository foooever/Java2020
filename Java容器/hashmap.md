## HashMap原理源码分析
* 在`hash table`基础上实现了`Map`接口，`key, value`均允许`null`，与`Hashtable`几乎相同，
仅在允许空值`nulls`和`unsynchronized`上有所不同（`hashtable`的`synchornized`实现原理->`JUC`中`concurrentHashMap`）
* `put, get`方法常数级，`Iteration`需要时间为`capcity(the number of buckets) + the number of k-v mapping`
* `capcity, load factor(.75 default 平衡时空代价)`，扩容**2倍**`rehashed(internal data structures are rebuilt)`
* 使用大量相同`hashCode()`值的`keys`会降低哈希表的性能
* `HashMap`不是线程安全的，如果线程安全`Map m = Collections.synchronizedMap(new HashMap(...));`或者`concurrentHashMap`
### HashMap的比较规则
对于`Key`的唯一性，采用以下方式：
```Java
//判断key是否相同
if hashCode() ==
    if equals() ==
        same
    else
        not same
else
    not same
	
//hashmap的hash函数
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
//Entry(Node<K, V>)的hashCode()
    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }
```
### HashMap内部存储结构--Hash表（Buckets）
数组有下标可以随机存取（存储区间连续），查找很快，但增删很慢；链表（树）只能顺序查找很慢，但增删很快；
故而两者结合形成哈希表。
#### 存储结构
```Java
transient Node<K,V>[] table; //Buckets，每个Bucket存储一个（Entry）Node<K, V>
transient int size; //k-v数量
transient int modCount; //modify_count 用于fail-fast
int threshold; //下一次resize的阈值
final float loadFactor; //装填因子

```
```Java
//以内部类的形式定义了Node<K,V>数据结构作为table的元素
//Basic hash bin node, used for most entries.(会变化TreeNode)
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }
```
#### 初始化
```Java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16初始buckets大小
static final int MAXIMUM_CAPACITY = 1 << 30; //最大容量2^30次方对应后面2次幂扩容方法
static final float DEFAULT_LOAD_FACTOR = 0.75f;
static final int TREEIFY_THRESHOLD = 8; //扩充为树（TreeMap）
static final int UNTREEIFY_THRESHOLD = 6; //缩为链表
static final int MIN_TREEIFY_CAPACITY = 64;
```
#### 构造函数
```Java
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }
//hashmap构造的哈希表大小都是2的幂次方
//获取下一个2^x次幂（is perfect）
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
```
#### resize()
扩容的时机：当`hashmap`中元素越来越多，碰撞的几率越来越高时，为提高查询效率，对table数据进行扩容。

扩容的策略；×2（2的幂指数大小），若初始为空，初始化为默认容量(16)，否则
将容量扩容为2的幂指数次方大小（2*oldCap）。
```Java
    /**
     * Initializes or doubles table size.  If null, allocates in
     * accord with initial capacity target held in field threshold.
     * Otherwise, because we are using power-of-two expansion, the
     * elements from each bin must either stay at same index, or move
     * with a power of two offset in the new table.
     *
     * @return the table
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold; //阈值
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) { //达到了最大无法扩容只能容忍冲突
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY) //×2扩容
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) { //此时是initial capacity
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr; //赋值新的阈值threshold
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)//哈希的策略取出e.hash中后多少位
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode) //如果是TreeNode
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order链表形式复制
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {//两倍分开的策略，非常之巧妙
                            next = e.next;
                            if ((e.hash & oldCap) == 0) { //当hash值首位为0
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else { //首位为1因此分出2个链表，分别存入j,j+oldCap位置
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
```
#### 增加元素
```Java
//在hashmap中添加元素
//hashmap的hash函数
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    //返回key对应oldValue或者null
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    //putVal()方法
    /**
     * Implements Map.put and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @param evict if false, the table is in creation mode.
     * @return previous value, or null if none
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //如果为空，新创建hashmap
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        //如果hash策略结果所在位置还没有元素，newNode
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else { //存在哈希冲突p为此处Node<K, V>
            Node<K,V> e; K k;
            //此处解释了仅仅靠hash函数是不够的的，需要equals()方法
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;//相同直接插入
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {//普通节点Node<K, V>
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash); //超过链表的阈值需要变为树
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount; //modify op
        if (++size > threshold) //添加元素后如果需要扩容则进行扩容
            resize();
        afterNodeInsertion(evict);
        return null; //此时情况为新插入key-value
    }
```
其中当链表的长度大于`TREE_THRESHOLD`是需要将链表转化为红黑树`treeifyBin`
```Java
    final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
        //当buckets数组很小时，进行扩容操作而非构建树
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) { //此处非空
            TreeNode<K,V> hd = null, tl = null; //声明“头尾”
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab); //生成红黑树
        }
    }
```
#### 查询
```Java
    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }
	
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    //遍历过程，首先看所在哈希表是否为空，为空返回null
    //其次，首个节点是否符合，不符合递归查找
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    public V getOrDefault(Object key, V defaultValue) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }
```
### 有关hashmap的题
#### 1.hashmap的实现原理
是基于哈希表的Map接口的非同步实现，键值允许为空，此类不保证map的顺序。
hashmap综合了数组的随机查询能力强和链式结构增删快的优点；hashmap是基于hash算法
实现的：
* 当put元素时，利用key的hash值计算当前对象在数组中的下标
* 存储时如果出现相同hash值，若key相同则覆盖，key不同（出现冲突）则将当前key-value置
放到链表之中
* 获取时，通过hash值找到数组下标，在Node结构中进行查找
#### 2.JDK1.8相比JDK1.7有什么不同
* resize 扩容优化
* 引入了红黑树，目的是避免单条链表过长而影响查询效率
* 解决了多线程死循环问题，但仍是非线程安全的，多线程时可能会造成数据丢失问题

不同|	JDK 1.7|	JDK 1.8|
-|-|-|
存储结构|	数组 + 链表|	数组 + 链表 + 红黑树|
初始化方式|	单独函数：inflateTable()|	直接集成到了扩容函数resize()中|
hash值计算方式|	扰动处理 = 9次扰动 = 4次位运算 + 5次异或运算|	扰动处理 = 2次扰动 = 1次位运算 + 1次异或运算|
存放数据的规则|	无冲突时，存放数组；冲突时，存放链表|	无冲突时，存放数组；冲突 & 链表长度 < 8：存放单链表；冲突 & 链表长度 > 8：树化并存放红黑树|
插入数据方式|	头插法|	尾插法|
扩容后存储位置的计算方式|	全部按照原来方法进行计算（即hashCode ->> 扰动函数 ->> (h&length-1)）|	按照扩容后的规律计算（即扩容后的位置=原位置 or 原位置 + 旧容量）|
#### 3.putVal()的具体流程
```Java
1.if table is null -> resize() to 2
2.else hash(key) 
3.if table[hash[key] & (n-1)] is null 4
4.new Node<>() 10
5.else if key is in table[hash[key] & (n-1)] 6
6.oldValue = newValue 10
7.else(5) if table[i] is TreeNode -> TreeNode.putVal 10
8.else lookup list if key is exist 6
9.else 4 if list.size() > 8 -> treeify 10
10.if ++size > threshold -> resize()
11.end
```
#### 4.扩容操作
* 每次初始化或者超过阈值就要进行扩容（×2）
* 每次扩展后Node对象要么在原位置，妖媚在oldCap + 原位置
* 将同一bucket中Node分为2部分靠（hash(key) & oldCap(2的幂次方)高位0/1进行划分）

#### 5.hashmap解决哈希冲突
哈希冲突是指进行哈希值计算时出现相同的哈希值，也称为哈希碰撞
* 使用链地址法（使用散列表）来链接拥有相同hash值的数据；
* 使用2次扰动函数（hash函数）来降低哈希冲突的概率，使得数据分布更平均；
* 引入红黑树进一步降低遍历的时间复杂度，使得遍历更快；

#### 6.能否使用任何类作为Map的key
可以使用任何类作为 Map 的 key，然而在使用之前，需要考虑以下几点：
需要重写equals()和hashCode()方法。
* 如果类重写了 equals() 方法，也应该重写 hashCode() 方法。
* 类的所有实例需要遵循与 equals() 和 hashCode() 相关的规则。
* 如果一个类没有使用 equals()，不应该在 hashCode() 中使用它。
* 用户自定义 Key 类最佳实践是使之为不可变的，这样 hashCode() 值可以被缓存起来，拥有更好的性能。不可变的类也可以确保 hashCode() 和 equals() 在未来不会改变，这样就会解决与可变相关的问题了

#### 7.为什么HashMap中String、Integer这样的包装类适合作为K
这些类别能保证哈希值不可更改和计算准确性，内部重写了equals和hashCode方法

#### 8.如果使用Object作为HashMap的Key，应该怎么办呢
重写了equals和hashCode方法

#### 9.HashMap为什么不直接使用hashCode()处理后的哈希值直接作为table的下标
hashCode()返回的整型变量(-2^31~2^31-1)而hashmap的容量最大为2^30，而且通常匹配不到
最大值，并且存储空间有限，直接使用hashCode()计算出的哈希值作为下标会出现无法匹配的情况。

#### 10.HashMap 的长度为什么是2的幂次方
为了让其存取高效，减少碰撞，哈希函数设计通常为%操作，当2的幂次方作为容量，%操作可转化为&相同。
更加的高效。

#### 11.HashMap 与 HashTable 有什么区别
* 线程安全：HashMap 是非线程安全的(Map m = Collections.synchronizedMap(new HashMap(...));)，HashTable 是线程安全的；HashTable 内部的方法基本都经过 synchronized 修饰。（如果你要保证线程安全的话就使用 ConcurrentHashMap 吧！）；
* 效率：因为线程安全的问题，HashMap 要比 HashTable 效率高一点。另外，HashTable 基本被淘汰，不要在代码中使用它；
* 对Null key和Null value的支持：HashMap中，null可以作为键，这样的键只有一个，可以有一个或多个键所对应的值为 null。但是在 HashTable 中 put 进的键值只要有一个 null，直接抛NullPointerException。
* **初始容量大小和每次扩充容量大小的不同**：
	* 创建时如果不指定容量初始值，Hashtable默认的初始大小为11，之后每次扩充，容量变为原来的2n+1。HashMap 默认的初始化大小为16。之后每次扩充，容量变为原来的2倍。
	* 创建时如果给定了容量初始值，那么Hashtable 会直接使用你给定的大小，而HashMap会将其扩充为2的幂次方大小。也就是说 HashMap 总是使用2的幂作为哈希表的大小。
* 底层数据结构：JDK1.8 以后的HashMap在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为8）时，将链表转化为红黑树，以减少搜索时间。Hashtable 没有这样的机制。
* 推荐使用：在 Hashtable 的类注释可以看到，Hashtable 是保留类不建议使用，推荐在单线程环境下使用 HashMap 替代，如果需要多线程使用则用 ConcurrentHashMap 替代。

#### 12.TreeMap和HashMap的适用场景
TreeMap是有序的Map结构，当需要对集合进行遍历，TreeMap是最好的选择，HashMap插入删除查询更快

#### 13.HashMap和ConcurrentHashMap的区别
* ConcurrentHashMap对整个桶数组进行了分割分段(Segment)，然后在每一个分段上都用
lock锁进行保护，相对于HashTable的synchronized锁的粒度更精细了一些，并发性能更
好，而HashMap没有锁机制，不是线程安全的。（JDK1.8之后ConcurrentHashMap启用了
一种全新的方式实现,利用CAS算法。）
* HashMap的键值对允许有null，但是ConCurrentHashMap都不允许。

#### 14.ConcurrentHashMap和Hashtable的区别
ConcurrentHashMap 和 Hashtable 的区别主要体现在实现线程安全的方式上不同。
* 底层数据结构： JDK1.7的 ConcurrentHashMap 底层采用分段的数组+链表 实现，
JDK1.8 采用的数据结构跟HashMap1.8的结构一样，数组+链表/红黑二叉树。
Hashtable和JDK1.8之前的HashMap的底层数据结构类似都是采用 数组+链表的形式，
数组是HashMap的主体，链表则是主要为了解决哈希冲突而存在的；
* 实现线程安全的方式（重要）：
	* 在JDK1.7的时候，ConcurrentHashMap（分段锁）对整个桶数组进行了分割分段
	(Segment)，每一把锁只锁容器其中一部分数据，多线程访问容器里不同数据段的
	数据，就不会存在锁竞争，提高并发访问率。（默认分配16个Segment，
	比Hashtable效率提高16倍。） 到了 JDK1.8 的时候已经摒弃了Segment的概念，
	而是直接用Node 数组+链表+红黑树的数据结构来实现，并发控制使用synchronized
	和 CAS 来操作。（JDK1.6以后 对synchronized锁做了很多优化）整个看起来就像
	是优化过且线程安全的HashMap，虽然在JDK1.8中还能看到Segment的数据结构，
	但是已经简化了属性，只是为了兼容旧版本；
	* Hashtable(同一把锁) :使用synchronized来保证线程安全，效率非常低下。
	当一个线程访问同步方法时，其他线程也访问同步方法，
	可能会进入阻塞或轮询状态，如使用 put 添加元素，另一个线程不能使用put添加
	元素，也不能使用get，竞争会越来越激烈效率越低。

#### 15.ConcurrentHashMap底层具体实现知道吗？实现原理是什么
首先将数据分为一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个
段数据时，其他段的数据也能被其他线程访问。

在JDK1.7中，ConcurrentHashMap采用Segment + HashEntry的方式进行实现。
* 该类包含两个静态内部类 HashEntry 和 Segment ；前者用来封装映射表的键值对，后者用来充当锁的角色；
* Segment 是一种可重入的锁 ReentrantLock，每个 Segment 守护一个HashEntry 数组里得元素，当对 HashEntry 数组的数据进行修改时，必须首先获得对应的 Segment 锁。

在JDK1.8中，放弃了Segment臃肿的设计，取而代之的是采用Node + CAS + Synchronized来保证并发安全进行实现，synchronized只锁定当前链表或红黑二叉树的首节点，
这样只要hash不冲突，就不会产生并发，效率又提升N倍。

参考：https://thinkwon.blog.csdn.net/article/details/104588551
