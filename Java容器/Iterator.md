## Iterator
```Java
//集合迭代器（迭代器模式）
public interface Iterator<E> {
    boolean hasNext();
    E next();
    default void remove(); //只能删除迭代器新返回的元素
    default void forEachRemaining();	
}
```
`remove()`操作是唯一安全的在迭代过程中修改集合方式。
`Iterable`接口被`Collection`所继承，其方法`Iterator<T> iterator();`返回一个迭代器`Iterator`;
## fail-fast
`fail-fast`机制，即快速失败机制，是Java集合(Collection)中的一种错误检测机制。当在迭代集合的过程中该集合在结构上发生
改变（添加/删除）的时候，就有可能会发生`fail-fast`，即抛出`ConcurrentModificationException`异常。`fail-fast`机制并不保证在不同步的修
改下一定会抛出异常，它只是尽最大努力去抛出，所以这种机制一般仅用于检测bug。
```Java
//迭代器遍历过程中，删除元素，会发生fail-fast
     public static void main(String[] args) {
           List<String> list = new ArrayList<>();
           for (int i = 0 ; i < 10 ; i++ ) {
                list.add(i + "");
           }
           Iterator<String> iterator = list.iterator();
           int i = 0 ;
           while(iterator.hasNext()) {
                if (i == 3) {
                     list.remove(3);
                }
                System.out.println(iterator.next());
                i ++;
           }
     }
     
     //Map中的Iterator, Iterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
     public static void main(String[] args) {
           Map<String, String> map = new HashMap<>();
           for (int i = 0 ; i < 10 ; i ++ ) {
                map.put(i+"", i+"");
           }
           Iterator<Entry<String, String>> it = map.entrySet().iterator();
           int i = 0;
           while (it.hasNext()) {
                if (i == 3) {
                     map.remove(3+"");
                }
                Entry<String, String> entry = it.next();
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                i++;
        }
     }

     //多线程
public class FailFastTest {
     public static List<String> list = new ArrayList<>();
 
     private static class MyThread1 extends Thread {
           @Override
           public void run() {
                Iterator<String> iterator = list.iterator();
                while(iterator.hasNext()) {
                     String s = iterator.next();
                     System.out.println(this.getName() + ":" + s);
                     try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
                super.run();
           }
     }
 
     private static class MyThread2 extends Thread {
           int i = 0;
           @Override
           public void run() {
                while (i < 10) {
                     System.out.println("thread2:" + i);
                     if (i == 2) {
                           list.remove(i);
                     }
                     try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                     i ++;
                }
           }
     }
 
     public static void main(String[] args) {
           for(int i = 0 ; i < 10;i++){
            list.add(i+"");
        }
           MyThread1 thread1 = new MyThread1();
           MyThread2 thread2 = new MyThread2();
           thread1.setName("thread1");
           thread2.setName("thread2");
           thread1.start();
           thread2.start();
     }
}
```
从`ArrayList`的源码中看`fail-fast`如何工作。
```Java
    public Iterator<E> iterator() {
        return new Itr();
    }
	
	    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;
 
        public boolean hasNext() {
            return cursor != size;
        }
 
        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }
 
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();
 
            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
 
        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }
 
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
```
其中`cursor`表示集合遍历过程中**即将**遍历的元素索引，`lastRet`是`cursor - 1`默认-1，记录刚刚遍历的元素索引（可以remove的元素）
，`expectedModCount`用于判断`fail-fast`，初始值为`modCount`。

`next()`方法每次进行迭代的时候调用`checkForComodification()`判断是否抛出`ConcurrentModificationException`异常。
在该段代码中，当`modCount != expectedModCount`时，就会抛出该异常。但是在一开始的时候，`expectedModCount`初始值默
认等于`modCount`，为什么会出现`modCount != expectedModCount`，很明显`expectedModCount`在整个迭代过程除了一开始
赋予初始值`modCount`外，并没有再发生改变，所以可能发生改变的就只有`modCount`，在前面关于`ArrayList`扩容机制的分
析中，可以知道在`ArrayList`进行`add, remove, clear`等涉及到修改集合中的元素个数的操作时，`modCount`就会发生改变
`(modCount ++)`,所以当另一个线程(并发修改)或者同一个线程遍历过程中，调用相关方法使集合的个数发生改变，就会
使`modCount`发生变化，这样在`checkForComodification`方法中就会抛出`ConcurrentModificationException`异常。

解决`fail-fast`：
* 使用Iterator的remove方法
* 并发JUC中类`CopyOnWriterArrayList & ConcurrentHashMap`线程安全的。
## ListIterator
```Java
//双向移动
Interface ListIterator<E>{
    void add(E e); //添加元素next()返回元素之前或previous返回之后
    boolean hasNext();
    boolean hasPrevious();
    E next();
    int nextIndex();
    E previous();
    int previousIndex();
    void remove();
    void set(E e);
}
```
### 两者的区别`Iterator, ListIterator`
* Iterator可以遍历所有集合，`Map, List, Set`，而只能往后遍历；ListIterator只能遍历List对象，但可以向前遍历`previous, next`；
* ListIterator可以添加元素、修改元素、获取索引`add, set, listIterator(index)`。