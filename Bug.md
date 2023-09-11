# 记录bug
## update失败，提示语法错误
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309070951046.png)
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309070952826.png)
驼峰命名指的是关系映射，SQL语句的编写还是要按照数据库的命名规范，即下划线命名法，所以在编写SQL语句时，要注意表名和字段名的大小写，以及表名和字段名之间的下划线。  
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309070953970.png)    
修改为和数据库一致  
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309070956640.png)

### 使用foreach
使用foreach的时候注意要用 ， 分隔  
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309081523960.png)  
## 字符串常量又加了一个引号

### 阿里云对象存储图片，浏览器直接下载，不显示
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309081339943.png)  
不太理解为什么在浏览器直接打开url是下载，而在html中引用却是显示图片

### 公共填充字段
虽然使用了公共填充，但是在mapper中还是要手动填充，否则会报错
因为自动填充是给实体类的属性赋值，而mapper是需要给数据库中的字段赋值，如果不赋值，公共填充就没有意义了，所以要手动填充  
### 分页查询时，使用if判断
使用if判断，如果参数是Integer，不要写 **test="status != null && status!=''"**  
### 使用注解@Cacheable进行 注解缓存
1. 在启动类上添加注解@EnableCaching
2. 必须在配置文件中指定缓存的类型，注解不会生效  
```yml
spring:
  cache:
    type: redis
```
### 方法参数类型字符串的必需URI模板变量'page'不存在  
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309120158721.png)  
![loading failed](https://raw.githubusercontent.com/lei1692/typora/main/image/202309120159881.png)  
**可能传输多个参数**  
直接把传递过来的多个参数封装为一个对象，不需要加注解  

### springboot中多次提示  在系统中发现了多个分页插件，请检查系统配置!
知乎的一篇文章解释的很好 [为什么会出现多个分页插件](https://www.zhihu.com/question/330677156)  
在PageHelperAutoConfiguration源码中，会自动扫描所有的分页插件，然后将其注入到容器中，所以如果有多个分页插件，就会报错  
从源码中可以看出两次自动配置会加入了两个拦截器PageInterceptor，所以才会提示“多个插件”；  
- 解决方法：在@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)中排除PageHelperAutoConfiguration.class





