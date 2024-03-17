## 健康食谱后端

#### 1、目录结构

com.

test--master

--healthy Recipes

-----common

----------------constant

----------------context

----------------properties

----------------result

----------------utils

-------config

-------controller

-------exception

-------handler

-------interceptor

-------mapper

-------pojo

----------------dto

----------------entity

----------------vo

-------service

----------------impl

**需要的新的包，以后可以再重新补充**

#### 2、Swagger的使用

**用在controller层**

每新建一个controller加上@Api（‘介绍信息’） 注解，每一个接口方法上加上@ApiOperation('介绍信息')的注解

启动项目后，输入`http://localhost:/doc.html` 就可以进行测试。

#### 3、全局异常处理

**已经通过handler实现全局异常处理**

如果有新的异常，请自行定义异常类，继承BaseException，抛出异常后输入异常信息，就可以自动处理。

下面是个例子

**自定义一个TestException类**

```java
public class TestException extends BaseException{
    public TestException(){}
    public TestException(String msg){
        super(msg);
    }
}



```

使用的时候就throw一个异常即可。