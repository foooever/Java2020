## JDK1.8新特性
### 1.函数式接口
函数式接口`@FunctionInterface`：是指只有一个抽象方法（可以多个非抽象方法如static方法）的接口，可以隐式的转化为`Lambda`表达式。
如`java.lang.Runnable, java.util.Comparator`。
```Java
package com.notes.java8.functionInterface;

/**
 * 文件描述 函数式接口:
 *      有且仅有一个抽象方法，但是可以有多个非抽象方法的接口。
 **/
@FunctionalInterface
public interface Hello {

    /**
     * abstract 方法，只能有一个
     */
    void hello();

    /**
     * 允许定义默认方法
     */
    default void hi(){
        System.out.println("this is default method");
    }

    /**
     * 允许定义静态方法
     */
    static void hei() {
        System.out.println("this is static method");
    }

    /**
     * 允许定义 java.lang.Object 里的 public 方法
     */
    @Override
    boolean equals(Object obj);
}
```
四大基本函数式接口
```Java
//Function
@FunctionalInterface
public interface Function<T, R> { //接受参数T返回R类型对象，使用apply()方法获取方法执行内容
    R apply(T t);
}
//example
    User user = new User(88, "bb");

    String name = uft.apply(user); //return bb
    System.out.println(name);
    /**
    * Function<T, R> lambda写法
     */
    private static Function<User, String> uft = u -> u.getName();
	
//Consumer
@FunctionalInterface
public interface Consumer<T> { //接受参数T无返回值，使用accept()方法对参数进行操作
    void apply(T t);
}

//Supplier
@FunctionalInterface
public interface Supplier<T> { //接受参数T，使用get()方法获取结果
    T get(T t);
}

//Predicate
@FunctionalInterface
public interface Predicate<T> { //接受参数T，使用test()方法获取测试结果
    boolean test(T t);
}
```
### 2.Lambda表达式
#### 2.1表达式语法
```Java
    (paramters) -> expression;
    (paramters) -> {statements;} //大括号语句块 
    展开如：
    (Type1 param1, Type2 param2, Type2 param2, ...) -> {
        statement1;
        statement2;
        statement3;
        ...
        return statementX;
    }
```
不需要声明参数类型，编译器可以统一识别，一个参数无需圆括号，多个参数需要定义圆括号，
主体包含一个语句无需{}并且return关键词都不需要，多个主体语句需要{}并指明return。
```Java
import java.util.Comparator;
import java.util.PriorityQueue;

interface Demo {
    String f();
}
interface Demo1 {
    String g(String name);
}
interface Demo2 {
    String k(String name, int age);
}
public class Lambda {
    public static void main(String[] args) {
        //参数为空，一条语句return多余
        //lambda表达式中()实现了Demo.f()
        Demo no_param = () -> "hi, lambda";
        Demo no_param1 = () -> {return "hello, lambda";};
        System.out.println(no_param.f()); //hi, lambda
        System.out.println(no_param1.f()); //hello, lambda

        //单个参数
        //lambda表达式中name是参数实现了Demo1.g(String name)
        Demo1 param = name -> name;
        Demo1 param1 = name -> {return name;};
        System.out.println(param.g("hei")); //hei
        System.out.println(param1.g("girls")); //girls

        //多个参数
        //参数不需要类型，编译器自动识别，{}块中多行语句要显示指定返回的语句
        Demo2 multi_params = (age, name) -> age+" "+name;
        Demo2 multi_params1 = (age, name) -> {
            System.out.println("内部");
            return age+" "+name;
        };
        System.out.println(multi_params.k("li", 12)); //li 12
        System.out.println(multi_params1.k("zhang", 22)); //内部 zhang 22

        //匿名内部类
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        
        //Lambda表达式写法
        PriorityQueue<Integer> pq1 = new PriorityQueue<>((o1, o2)->o2.compareTo(o1));
    }
}
```
#### 2.2方法引用
* 对象::实例方法`objectName::instanceMethod`，将lambda的参数当做方法的参数使用
* 类::静态方法`ClassName::staticMethod`，将lambda的参数当做方法的参数使用
* 类::实例方法`ClassName::instanceMethod`，将lambda的第一个参数当做方法的调用者，其他的参数作为方法的参数
```Java
    //objectName::intanceMethod
    Consumer<String> sc = System.out::println;
    //等效
    Consumer<String> sc2 = (x) -> System.out.println(x);
	
    //ClassName::staticMethod
    Function<Integer, String> sf = String::valueOf;
    //等效
    Function<Integer, String> sf2 = (x) -> String.valueOf(x);

    //ClassName::intanceMethod
    BiPredicate<String, String> sbp = String::equals;
    //等效
    BiPredicate<String, String> sbp2 = (x, y) -> x.equals(y);
```