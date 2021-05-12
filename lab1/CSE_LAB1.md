[TOC]

# CSE_LAB1

By 黄子豪 18302010034

## 文件系统基础设计

本文件系统中一共有4个重要角色：file,filemanager,block和blockmanager。其中file是模仿面向用户的文件层，block则是模拟计算机底层实现的块层，filemanager和blockmanager分别管理它们——对应于文件管理系统和磁盘。

由filemanager创建新文件，文件创建后可以向文件中写入内容，但其实写入的内容会交给具有一定大小的block，由block来保存数据，此外文件还提供read操作，其实都是会变成对于block的读写。

为了实现duplication，采用了logicblock，它的容量与block的容量相同，即每一个文件被分为若干个logicblock，而每一个logicblock对应于2个及以上的实际的block，当其中一个损坏时仍然可以从其他备份读取。

此外由于读写都涉及到指定位置，因此为每个file维护了一个cursor，指向当前光标位置，move操作也会改变光标的值。

## 使用流程

本文件系统实现了两种使用方式，对于广大程序猿来讲大可直接调用write/read等接口，但是作为本文件系统亮点之一，**用户友好型的命令行交互程序**必将博得更多用户的欢心。如下图，运行程序后会给出使用本系统的所有提示：![image-20201024211347970](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024211347970.png)

接下来新建一个文件，并向其中写入20201024：![image-20201024211448338](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024211448338.png)

接下来我们来看看文件系统里有哪些文件吧！![image-20201024211531818](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024211531818.png)

可以看到刚才新建的文件已经写入8个字节成功！接下来我们读取它的详细内容：![image-20201024211621281](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024211621281.png)

保存并退出程序：![image-20201024211653313](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024211653313.png)

那么文件系统持久化做到了吗？重新运行程序来检验一下：![image-20201024211724182](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024211724182.png)

file1处理无误！

当然以上只是最简单的功能使用示例，更多用法已经在上图中有介绍，总而言之这已经是类似于早期的Linux文件系统啦！

## 异常处理说明

在ErrorCode中定义了种异常，依次是*IO_EXCEPTION、CHECKSUM_CHECK_FAILED、CURSOR_OUT_OF_BOUND、CLASS_NOT_FOUND、BUFFER_OVERFLOW、LOGICBLOCK_BROKEN、WRONG_PARAMETER、FILE_NOT_EXIST*，下面进行举例说明：

1. 读取file.meta/block.meta/block.data时都可能发生IO异常，对应于文件或者块的损坏，为此设定了错误处理代码，会打印出日志：![image-20201024152208478](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024152208478.png)![image-20201024152442820](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024152442820.png)
2. 即使能够读取到block的内容，仍然要检查checksum以确保块的内容没有被改变，当checksum不正确时会引发*CHECKSUM_CHECK_FAILED*异常，打印出提示哪个块错误，然后返回空的字节：![image-20201024152940561](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024152940561.png)
3. 对于文件的move操作，有可能把光标移到合理的范围之外，例如超过文件的size或者小于0，这时会引发*CURSOR_OUT_OF_BOUND*异常，提示信息后光标会被修改回旧值：![image-20201024153255319](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024153255319.png)
4. 由于是使用对象流的方式保存和读取file.meta和block.meta，因此可能会发生类的类型不匹配的错误，为此定义了*CLASS_NOT_FOUND*异常：![image-20201024154220029](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024154220029.png)
5. 在文件系统中每个block都有固定的capacity，如果传入write的byte数组的长度超过它的capacity，就会引发*BUFFER_OVERFLOW*异常，此时将停止写入：![image-20201024154607508](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024154607508.png)
6. 对于每个logicblock都有超过2个实际的block与之对应，如果其中一个损坏不会导致文件损坏，但是当它的所有block都损坏了之后，就会引发*LOGICBLOCK_BROKEN*异常，这个文件的这一部分内容不得不丢失：![image-20201024155345737](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024155345737.png)
7. 用户会输入很多参数，但是却不一定会正确输入，面对这些不合法的参数，系统会引发*WRONG_PARAMETER*异常，提示用户参数输入错误，下面是move光标时的一个例子，用户的where只能输入0,1或者2，其他值不会改变光标的值：![image-20201024160127487](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024160127487.png)
8. 用户想要读取或者写入一个文件时，需要输入文件名和它属于的filemanager，但是文件有可能不存在，这时会引发*FILE_NOT_EXIST*异常：![image-20201024161739311](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024161739311.png)

## Bonus

本文件系统值得一提的有两点，一个是buffer的使用，大大减少了直接访问硬盘（读写block.data）的次数，另一个是user-friendly的命令行操作界面。

### buffer

总体模仿文件系统的cache，实现思路是对于每一个文件有一个缓冲区byte数组，当一个新文件第一次写时，并不直接把它的内容直接写回到block里，而是模仿文件系统，暂时将它保存在buffer中，当一个已存在的文件第一次读时，也把它的内容保存到buffer里。通过这样的两种实现，此后对于文件的读写操作以及移动文件光标，都不需要再次操作block，直接访问buffer就可以，从而大大减少了直接访问硬盘（在这里是读写block）的次数，尤其是对同一个文件多次读写时的效率大大提高。

向buffer中写：![image-20201024164811970](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024164811970.png)

从buffer中读：![image-20201024164857198](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024164857198.png)

只有真正的要关闭文件或者用户显式保存文件时，才执行flush将buffer的内容写回block：![image-20201024165029448](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024165029448.png)

### 命令行界面

将整个用户对于系统的使用流程，可视化为命令行，输入指令即可实现所有操作，不需要在代码层操作，更加用户友好，由于用户手册已经介绍过，这里只展示命令行界面：

![image-20201024212051980](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20201024212051980.png)

***Thanks for reading!***