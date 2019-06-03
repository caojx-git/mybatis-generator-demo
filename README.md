# mybatis-generator-demo

该项目用于快速生成vo、model、mapper、mapping

github地址：https://github.com/caojx-git/mybatis-generator-demo

## 一、快速入门
### 1.1 caojx.learn.StartUp

该代码位于test/java目录下，StartUp是生成代码的入口   
generatorConfig.xml 用 model、mapper、mapping的生成，代码生成路径可自行配置    
generatorConfig-vo.xml 用于vo的生成，代码生成路径可自行配置

```java
package caojx.learn;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 扩展mybatis_generator，执行main方法生成对应代码
 * generatorConfig-vo.xml用于生成 vo
 * generatorConfig.xml用于生成model mapper sqlMapper
 *
 * @author caojx
 * @version 1.0
 * @date 2018年7月6日
 */
public class StartUp {
    public static void main(String[] args) {
        try {
            System.out.println("--------------------start mybatis-generator-demo-------------------");
            List<String> warnings = new ArrayList<String>();
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(Thread.currentThread().getContextClassLoader().getResourceAsStream("generatorConfig.xml"));
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            System.out.println("--------------------end mybatis-generator-demo-------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
    }
}
```



### 1.2 主要配置项

#### 1.代码生成插件配置
将author改成自己的名称，不配置使用默认，注意使用mybatis-plus中实体标注的主键生成策略注解使用 @TableId(value = "id", type = IdType.AUTO) 需要自行修改见https://mp.baomidou.com/guide/annotation.html#tableid
```xml
        <!-- 实现自定义的代码生成器插件 -->
        <plugin type="caojx.learn.plugin.MapperPlugin">
            <property name="targetProject" value="src/test/java"/>
            <property name="targetPackage" value="caojx.learn.mapper"/>
            <!-- 使用 tk.mybatis 请在pom.xml引入tk.mybatis依赖  -->
            <!--<property name="daoSuperClass" value="caojx.learn.util.BaseMapper"/>-->

            <!--
                使用 mybatis-plus 请在pom.xml引入mybatis-plus-boot-starter依赖
                
                注意：mybatis-plus中自定义了主键生成策略，见https://mp.baomidou.com/guide/annotation.html#tableid
                使用注解 @TableId(value = "id", type = IdType.AUTO)
                
                tk.mybatis或jpa主键生成策略注解用
                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
            -->
            <property name="daoSuperClass" value="com.baomidou.mybatisplus.core.mapper.BaseMapper"/>


            <!--Mapper扩展名-->
            <property name="daoSuffix" value="Mapper"/>
            <!--是否生成getter和setter-->
            <property name="generatorGetterAndSetter" value="false"/>
            <!--author-->
            <!-- <property name="author" value="caojx"/>-->
        </plugin>

        <!--自定义注释插件-->
        <commentGenerator type="caojx.learn.comment.MyCommentGenerator">
            <property name="suppressAllComments" value="false"></property>
            <!--是否生成getter和setter-->
            <property name="generatorGetterAndSetter" value="false"/>
            <!--author-->
            <!-- <property name="author" value="caojx"/>-->
        </commentGenerator>
```

#### 2.配置需要生成的表
```xml
 <!--指定数据库表-->
<table  tableName="user" enableSelectByExample="false" enableDeleteByExample="false"
        enableCountByExample="false" enableUpdateByExample="false"
        selectByExampleQueryId="false" domainObjectName="AreaCode">
    <generatedKey column="ID" sqlStatement="MySql" identity="true"/>
    <!--设置别名, 不指定的话(下边的可以不配置)，按照默认规则-->
    <columnOverride column="id" property="id" />
    <columnOverride column="name" property="name" />
    <columnOverride column="age" property="age" />
    <columnOverride column="email" property="email" />
    <columnOverride column="birthday" property="birthday" />
</table>
```

### 1.3.生成示例

#### 3.1. user表

```sql
create table user
(
  id    bigint      not null comment '主键ID' primary key,
  name  varchar(30) null comment '姓名',
  age   int         null comment '年龄',
  email varchar(50) null comment '邮箱',
  birthday datetime null comment '生日'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4  COMMENT='用户表';
```

#### 3.2. UserInfoMapper.java

```java
package caojx.learn.mapper;

import caojx.learn.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户表Mapper
 * 
 * @author caojx
 * @version \$Id: UserMapper.java,v 1.0 2019/05/27 11:10 caojx
 * @date 2019/05/27 11:10
 */
public interface UserMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<User> {
}
```

#### 3.3. User.java

注意使用mybatis-plus中实体标注的主键生成策略注解使用 @TableId(value = "id", type = IdType.AUTO) 需要自行修改见https://mp.baomidou.com/guide/annotation.html#tableid

```java
package caojx.learn.model;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表实体
 * 
 * @author caojx
 * @version \$Id: User.java,v 1.0 2019/06/03 14:23 caojx
 * @date 2019/06/03 14:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements java.io.Serializable {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private Date birthday;
}
```

#### 3.4. UserMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="caojx.learn.mapper.UserMapper">
  <resultMap id="BaseResultMap" type="caojx.learn.model.User">
    <id column="id" jdbcType="BIGINT" property="id" />
    <result column="name" jdbcType="VARCHAR" property="name" />
    <result column="age" jdbcType="INTEGER" property="age" />
    <result column="email" jdbcType="VARCHAR" property="email" />
    <result column="birthday" jdbcType="TIMESTAMP" property="birthday" />
  </resultMap>
  <sql id="Base_Column_List">
    id, name, age, email, birthday
  </sql>
</mapper>
```

#### 3.5. UserVo.java

```java
package caojx.learn.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表VO
 * 
 * @author caojx
 * @version \$Id: UserVO.java,v 1.0 2019/06/03 14:27 caojx
 * @date 2019/06/03 14:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="UserVO对象", description="用户表VO对象")
public class UserVO implements java.io.Serializable {
    @ApiModelProperty(value = "主键ID", name = "id")
    private Long id;

    @ApiModelProperty(value = "姓名", name = "name")
    private String name;

    @ApiModelProperty(value = "年龄", name = "age")
    private Integer age;

    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    @ApiModelProperty(value = "生日", name = "birthday")
    private String birthday;
}
```



## 二、更新日志



- 2019.5.24
 1. 支持mybatis-pulus或者tk.mybatis

- 2019.1.7
 1. 修复MySQL SMALLINT 转Java由Short改为Integer


- 2018.9.3  
 1. 修复MySQL TINYINT 转Java由Byte改为Integer