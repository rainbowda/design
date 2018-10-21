# 根据不同条件使用不同实现类的业务代码设计

# 场景
此时有一个场景，需要设计一个根据不同的状态和条件采用不同的业务处理方式。

这样大家可能不是太理解。举个例子，现在大街小巷上的商户都采用了聚合支付的支付方式，聚合支付也就是商户柜台前放了一个支持支付宝、微信、京东钱包、银联等等的二维码，用户可以通过任意一款支付APP进行支付。 

# 解决思路

## 思路①

对每个支付渠道进行定义枚举类型

```java
public enum PayWay {
    ALI_PAY,

    WECHAT_PAY;
}
```

然后在每个对应的service上定义注解，表示对应哪种支付方式

```java
@Pay(PayWay.ALI_PAY)
public class AliPayServiceImpl implements PayService  {}
```

但是仔细思考后，还是存在一些问题

1. 如果增加一个支付方式后还需要修改，`PayWay`这个枚举类型
2. 在程序中，仍需要根据不同的条件做`if else`判断`PayWay`，增加支付方式还是得修改原有的判断逻辑。伪代码如下

```java
if("xxx" == "aliPay"){
    
} else if("xxx" == "wechatPay"){
    
}
//如果增加支付方式还是得增加else if
```

## 思路②

在思路①中存在一些问题，首当其冲的就是`if else`判断问题。先思考一下这个`if else`的作用是什么？

答：根据思路①描述，这个`if else`是用来确定采用哪种支付方式。

我们可以将这块代码抽离出来，让对应的业务实现类实现自己的逻辑实现，然后根据返回值`true` 或者`false`决定是否过滤掉这个业务实现类。接口定义如下，`SupportBean`是封装的一个实体

```
boolean isSupport(SupportBean supportBean);
```

然后在各个业务实现类都实现自己的isSupport方法,伪代码如下

```java
@Override
public boolean isSupport(SupportBean supportBean) {
    if (supportBean.getType() == "xxx"){
        return true;
    }
    
    return false;
}
```



# 设计

> 注：只提供一个架子

## 接口定义

Service接口定义，一个业务执行方法execute（参数自行添加），一个isSupport方法（返回`true`或者`false`）

```java
public interface Service {

    void execute();

    boolean isSupport(SupportBean supportBean);
}
```

## 业务实现类

这里execute方法只是在控制台打印字符串。isSupport方法对SupportBean中的supportNum进行取余，判断余数是否等于0，是则返回true。

类似的实现还有两个，这里就不贴出来了。

```java
@Component
public class AServiceImpl implements Service {
    @Override
    public void execute() {
        System.out.println("A execute");
    }

    @Override
    public boolean isSupport(SupportBean supportBean) {
        return supportBean.getSupportNum() % 3 == 0;
    }
}
```

接下来在定义一个帮助类

## 帮助类

```java
@Component
public class Helper {

    @Autowired
    private List<Service> services;

    public void execute(SupportBean supportBean){

        Service s = services.stream()
                .filter((service) -> service.isSupport(supportBean))
                .findFirst()//NPE异常
                .orElse(null);


        if (s != null){
            s.execute();
        }
    }
}
```

通过工具类的execute方法来获取对应的业务实现类执行的结果，以及对传入的参数进行校验处理等。

> 需要注意的是Lambda表达式的findFirst()会出现NullPointException异常。因为filter对list进行过滤，会存在过滤完list的长度为0，如果此时在调用findFirst则会抛出NullPointException。可以将上面的代码修改为如下代码，这样就可以避免NPE了

```java
Service s = services.stream()
        .filter((service) -> service.isSupport(supportBean))
        .map(Optional::ofNullable)
        .findFirst()
        .flatMap(Function.identity())
        .orElse(null);
```



## 测试

添加一个springboot测试类和一个测试方法。

在contextLoads测试中调用帮助类Helper的execute方法

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private Helper Helper;

    @Test
    public void contextLoads() {
        Helper.execute(new SupportBean(3));
    }

}
```

测试结果

```
A execute
```



## 扩展

在Lambda表达式中是先将业务实现类进行过滤，然后获取第一个业务实现类并执行。

如果此时过滤存在多个业务实现类，而又不能确定优先级，这时需要如何进行扩展呢？

其实很简单，先在Service接口中定义一个`getPriority`方法

```java
int getPriority();
```

然后各自的实现类实现对应的`getPriority`方法



接着修改Lambda表达式即可，在filter后增加sorted方法即可对业务实现类进行排序

```
Service s = services.stream()
        .filter((service) -> service.isSupport(supportBean))
        .sorted(Comparator.comparing(Service::getPriority))
        .map(Optional::ofNullable)
        .findFirst()
        .flatMap(Function.identity())
        .orElse(null);
```

# 总结

整个大体框架基本都搭建完成，如需扩展只需要增加对应的业务实现类，而不用去修改其他类的代码。就连之前设计的枚举都可以不用，可扩展性大大提升。如需使用，只需修改对应的入参和对应的名称即可。

