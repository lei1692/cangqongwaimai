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





