Java 是一门跨平台、面向对象、强类型的高级编程语言，由 Sun Microsystems 于 1995 年推出，目前由 Oracle 维护。Java 语言凭借其稳定性、安全性、生态完整性，成为企业级开发、后端服务、大数据、人工智能、移动端开发的主流语言。

一、Java 语言核心特性
1. 跨平台性
   Java 程序通过编译生成字节码文件（.class），字节码不依赖任何操作系统，只需要对应平台的 JVM（Java 虚拟机）即可运行。实现了“一次编写，到处运行”的核心目标。

2. 面向对象
   Java 是纯面向对象语言，支持封装、继承、多态三大特性。所有代码必须定义在类中，除了基本数据类型外，一切皆对象。

3. 强类型语言
   每个变量必须声明类型，类型不匹配会直接编译报错，提高程序安全性与稳定性。

4. 自动内存管理
   JVM 提供垃圾回收机制（GC），自动回收不再使用的对象内存，开发者无需手动管理内存，避免内存泄漏与野指针问题。

5. 安全性高
   Java 拥有安全管理器、字节码校验、权限控制机制，适合网络环境与企业级应用。

二、Java 基础语法核心要点
1. 基本数据类型
   Java 提供 8 种基本数据类型：
- 整数类型：byte、short、int、long
- 浮点类型：float、double
- 字符类型：char
- 布尔类型：boolean

所有基本类型都有对应的包装类：Integer、Long、Boolean 等，支持自动装箱与拆箱。

2. 变量与常量
   变量必须声明类型才能使用。常量使用 final 关键字定义，赋值后不可修改。

示例：
final int MAX_VALUE = 100;
int age = 20;

3. 流程控制
   Java 支持 if-else、switch、for、while、do-while 等流程控制语句。
   switch 从 Java 7 开始支持 String，Java 14 开始支持增强 switch。

4. 数组
   数组是相同类型数据的集合，长度固定。
   示例：
   int[] arr = new int[5];
   String[] names = {"张三", "李四", "王五"};

三、面向对象核心机制
1. 类与对象
   类是对象的模板，对象是类的实例。
   示例：
   class Person {
   String name;
   int age;
   void sayHello() {
   System.out.println("Hello, my name is " + name);
   }
   }

2. 封装
   使用 private 修饰成员变量，提供 public 的 get/set 方法访问，保证数据安全性。

3. 继承
   使用 extends 关键字实现继承，子类可以复用父类功能。
   Java 只支持单继承，但可以实现多个接口。

4. 多态
   多态表现为方法重写、方法重载、接口实现。
   多态的三大条件：继承、重写、父类引用指向子类对象。

四、Java 异常处理
Java 使用 try-catch-finally 捕获异常。
异常分为 Error（严重错误）与 Exception（可处理异常）。
RuntimeException 为运行时异常，其他为编译时异常。

示例：
try {
int result = 10 / 0;
} catch (ArithmeticException e) {
e.printStackTrace();
} finally {
System.out.println("最终一定执行");
}

五、Java 集合框架
集合用于存储对象，主要分为：
1. List：有序、可重复（ArrayList、LinkedList）
2. Set：无序、不可重复（HashSet、TreeSet）
3. Map：键值对存储（HashMap、TreeMap）

常用集合方法：add、remove、get、contains、keySet、values 等。

六、Java IO 与文件操作
Java IO 分为字节流与字符流：
- 字节流：InputStream、OutputStream
- 字符流：Reader、Writer

NIO 提供非阻塞 IO，适合高并发场景。

七、Java 多线程基础
1. 线程创建方式
- 继承 Thread 类
- 实现 Runnable 接口
- 实现 Callable 接口（带返回值）
- 使用线程池 Executors

2. 线程同步
   使用 synchronized 关键字或 Lock 接口保证线程安全，避免并发问题。

3. 线程池
   线程池可以复用线程，提高性能，常用 ThreadPoolExecutor。

八、Java 8 新特性
1. Lambda 表达式
2. Stream 流式编程
3. 函数式接口
4. Optional 空指针安全
5. 新的日期 API（LocalDate、LocalTime）

九、Java 开发规范
1. 类名使用大驼峰
2. 方法名与变量名使用小驼峰
3. 常量全大写
4. 代码缩进统一
5. 注释清晰明了
6. 避免魔法值
7. 方法功能单一

Java 是一门持续进化的语言，目前最新长期支持版为 Java 21，提供虚拟线程、模式匹配、序列集合、结构化并发等强大功能。