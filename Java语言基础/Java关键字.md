## Java关键字
<!-- GFM-TOC -->
* [static](1static关键字)
* [final](2final关键字)
<!-- GFM-TOC -->
### 1.static关键字
static关键字在方便没有创建对象的情况下进行调用，直接根据类名进行调用`XXX.func()`。
可修饰：
* 类（只能修饰内部类--[嵌套类](https://github.com/foooever/Java2020/blob/master/Java%E8%AF%AD%E8%A8%80%E5%9F%BA%E7%A1%80/%E5%86%85%E9%83%A8%E7%B1%BB.md)）；
* 方法：`XXX.func()`而不需要`new XXX().func()`；
* 变量（静态变量/类变量）：不是属于对象的，属于所有对象共享；
* 代码块：`static {}`。

与普通的区别：生命周期、调用方式、存储位置。
#### 静态方法
静态方法不能被重写。
#### 静态变量
```Java
public class StaticKey {
    private static String s1 = "see";
    private String s2 = "listen";

    public static String getS1() {
        return s1;
    }

    public static void setS1(String s1) {
        StaticKey.s1 = s1;
    }

    public static void main(String[] args) {
        System.out.println(s1); //see
        //System.out.println(s2); //Error 无法从静态上下文中引用非静态变量 s2
        //静态方法调用非静态变量必须通过对象调用 new XXX().s2
        System.out.println(new StaticKey().s2); //listen
    }
}
//static变量可以进行赋值操作
class ModifyStatic {
    public static void main(String[] args) {
        StaticKey.setS1("look");
        System.out.println(StaticKey.getS1()); //look
    }
}
```
#### 静态代码块
```Java
public class StaticKey {
    private int i = 9;
    protected int j;
    public StaticKey() { //i,j先于构造函数加载
        System.out.println("father constructor");
        print(i+" "+j);
        j = 39;
    }
    {
        System.out.println("father code block");
    }
    static { //父类静态代码块
        System.out.println("father static code block");
    }
}
class Son extends StaticKey {
    private int k = 47;
    public Son() { //protected 继承访问 k先于构造函数加载
        System.out.println("son constructor");
        print(j+" "+k);
    }
    {
        System.out.println("son code block");
    }
    static {
        System.out.println("son static code block");
    }

    public static void main(String[] args) { //首先调用static main()->类加载
        //该类具有父类先加载父类
        Son son = new Son();
    }

}
//father static code block //父类static变量->父类static代码块
son static code block //子类static变量->子类static代码块
father code block //父类代码块
father constructor //i = 9, j = 0
son code block
son constructor //j = 39, k = 47
```
类初始化顺序：父类static变量->父类static代码块->子类static变量->子类static代码块->
父类普通变量->父类代码块->父类构造函数->子类普通变量->子类代码块->子类构造函数。
![static](https://github.com/foooever/figure/blob/master/Java2020/%E5%9F%BA%E7%A1%80/static.png)
### 2.final关键字
final关键字可以用来修饰变量、方法和类。
#### final变量
final成员变量表示常量，只能被赋值一次，赋值后值不再改变。\
当final修饰一个基本数据类型（8种）时，表示该基本数据类型的值一旦在初始化后
便不能发生变化；如果final修饰一个引用类型时，则在对其初始化之后便不能再让其
指向其他对象了，但该引用所指向的对象的内容是可以发生变化的`final List<Integer> list = new ArrayList<>(); list.add(1);`。
本质上是一回事，因为引用的值是一个地址，final要求值，即地址的值不发生变化。

`static final`修饰的编译期常量占据一段不可改变的存储空间。
```Java
Random rd = new Random(47);
final i1 = rd.nextInt(20); //不同对象创建时i1的值会不同
static final I_2 = rd.nextInt(20); //第一次创建后永远不变
```

final修饰一个成员变量（属性），必须要显示初始化。这里有两种初始化方式，一种是在变量声明
的时候初始化`public static final VALUE_ONE = 99;`；第二种方法是在声明变量的时候不赋初值（空白final），
但是要在这个变量所在的类的**所有的构造函数**中对这个变量赋初值。

当函数的参数类型声明为final时，说明该参数是只读型的。即你可以读取使用该参数，但是无法改变该参数的值。
#### final方法
如果只有在想明确禁止该方法在子类中被覆盖的情况下才将方法设置为final的。即父类的final方
法是不能被子类所覆盖的，也就是说子类是不能够存在和父类一模一样的方法的。

final修饰的方法表示此方法已经是“最后的、最终的”含义，亦即此方法不能被重写（可以重载多
个final修饰的方法）。此处需要注意的一点是：因为重写的前提是子类可以从父类中继承此方法
，如果父类中final修饰的方法同时访问控制权限为private，将会导致子类中不能直接继承到此
方法，因此，此时可以在子类中定义相同的方法名和参数，此时不再产生重写与final的矛盾，
而是在子类中重新定义了**新的方法**。（注：类的private方法会隐式地被指定为final方法。）
```Java
public class B extends A {

    public static void main(String[] args) {

    }

    public void getName() {
        
    }
}

class A {

    /**
     * 因为private修饰，子类中不能继承到此方法，因此，子类中的getName方法是重新定义的、
     * 属于子类本身的方法，编译正常
     */
    private final void getName() {
        
    }

    /* 因为public修饰，子类可以继承到此方法，导致重写了父类的final方法，编译出错
    public final void getName() {
    
    }
    */
}
```
#### final类
当用final修饰一个类时，表明这个类不能被继承。也就是说，如果一个类你永远不会让他被继承，
就可以用final进行修饰。final类中的成员变量可以根据需要设为final，但是要注意final类中的
所有成员方法都会被隐式地指定为final方法。