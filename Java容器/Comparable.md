## Comparable接口
该接口提供给实现了它的类的对象一个自然序（强制执行），`compareTo`方法为自然比较方法。
`Lists implements Comparable`可通过`Collections.sort(List)`实现自动排序；
`Arrays`通过`Arrays.sort(Object[])`实现自动排序。实现了该接口的对象，可以作为
`SortedMap`的`keys`以及`SortedSet`的元素在不用实现`Comparator`接口的情况下。

自然序`natural ordering`：是指具有与[`equals`]()一致，当且仅当`e1.compareTo(e2)`和`e1.equals(e2)`
返回相同`boolean`值。**注意事项**：`null`不作为任何`class`的`instance`实例。
```Java
e.compareTo(null) throw NullPointerException
e.equals(null) return false
```
`sorted sets(and sorted maps)`在未重写`comparator`的情况下，使用自然序与`equals`不一致
的对象作为元素（或键）会破坏`ser(or map)(defined in terms of equals())`的一般性。\
通常情况下实现了`Comparable`接口的类都与`equals`一致，除了:
```Java
java.math.BigDecimal //精度不考虑 4.0 == 4.00 true
```
如果需要违背`(o1.compareTo(o2)==0) == (o1.equals(o2))`条件需要慎重，显示指出。
例如`TreeSet`添加元素`e1,e2`，且`e1.equals(e2) && e1.compareTo(e2) != 0`，则在`add(e2)`时
并不会添加失败，但违背了`Set`的特性。
```Java
int compareTo(@NotNullT o); //Comparable中的唯一方法
```
一个对象调用该方法与另一个对象进行比较`o1.compareTo(o2)`，该方法具有\
对称性：`o1.compareTo(o2) == -o2.compareTo(o1)`\
传递性：`o1.compareTo(o2) == 0, o2.compareTo(o3) == 0, implies o1.compareTo(o3) == 0`\
重写`compareTo`方法实现对类对象的排序。
```Java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyComparable {

    private static class Person implements Comparable<Person> {

        int age;
        String name;

        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public int compareTo(Person o) {
            return this.age - o.age;
        }

        @Override
        public String toString() {
            return "name"+name+"age"+age;
        }
    }

    public static void main(String[] args) {
        Person p1 = new Person(12, "lihao");
        Person p2 = new Person(17, "zaijian");
        List<Person> list = new ArrayList<>();
        list.add(p2);
        list.add(p1);
        Collections.sort(list);
        for (Person person : list) {
            System.out.println(person);
        }
    }
}
```
对于`Comparable`接口，实现了该接口的类中重写`compareTo`方法，使得获取对象数组
的自然序。没有[匿名内部类](https://github.com/foooever/Java2020/blob/master/Java%E8%AF%AD%E8%A8%80%E5%9F%BA%E7%A1%80/%E5%86%85%E9%83%A8%E7%B1%BB.md)的实现方法。
## Comparator接口
作为比较方法，可以作为参数传递给排序方法`Collections.sort(List, Comparator)`或者`Arrays.sort(Object[], Comparator)`作为比较器进行排序。
同时可以作为数据结构如`SortedSet & SortedMap`的比较器，也可作为未实现`Comparable`接口的集合对象的排序工具。
和`Comparable`类似`c.compare(e1, e2) == 0`和`e1.equals(e2)`相同返回值。
`Comparator`最好实现`java.io.Serializable`，与`Comparable`**不同**的是可以有选择比较
`null`。\
`Comparator`是函数式接口`@FunctionalInterface`，比较两个大小关系，返回负数、零和正数，
通过`signum`符号函数转变为`0,1,-1`，其性质与`Comparable`一样。
```Java
    // @throws NullPointerException if an argument is null and this
    //         comparator does not permit null arguments
    // @throws ClassCastException if the arguments' types prevent them from
    //         being compared by this comparator.
    //
    int compare(T o1, T o2);
	
    //当且仅当两个Comparator可相同顺序返回true
    boolean equals(Object obj);
    //如String按长度排序后的二次排序（按字典序）
    default Comparator<T> thenComparing(Comparator<? super T> other) {}
    //Example
    Comparator<String> cmp = Comparator.comparingInt(String::length)
            .thenComparing(String.CASE_INSENSITIVE_ORDER);	
```
重写`Comparator.compare()`实现接口，作为比较器。可以以匿名内部类的方式实现接口。\
[JDK1.8 lambda](https://github.com/foooever/Java2020/blob/master/Java%E8%AF%AD%E8%A8%80%E5%9F%BA%E7%A1%80/Java%E6%96%B0%E7%89%B9%E6%80%A7.md)
表达式对于匿名内部类的书写简化。
```Java
    //匿名内部类
    PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });

    //Lambda表达式写法
    PriorityQueue<Integer> pq1 = new PriorityQueue<>((o1, o2)->o2.compareTo(o1));
```