🧩 octopus-spring-boot-starter-mybatis-plus
一个通用的 Spring Boot 自动填充字段模块，支持在插入/更新时自动填充指定字段（如 createTime、updateTime、creator 等），适用于 MyBatis Plus 场景。

📌 特性
✅ 支持从请求头（Header）→ 请求参数（Param）→ 默认值依次获取字段值
✅ 支持动态字段值（如 new Date()、LocalDateTime.now()）
✅ 可配置字段类型和填充时机（INSERT / UPDATE / BOTH）
✅ 支持 YAML 配置化，无需编写业务代码
✅ 易于扩展：支持 SPI 插件机制添加自定义字段来源
📦 使用方式
1. 添加依赖
   将以下依赖添加到你的 pom.xml 中：

xml
深色版本
<dependency>
<groupId>org.zhouxp.octopus</groupId>
<artifactId>octopus-spring-boot-starter-mybatis-plus</artifactId>
<version>3.0.0</version>
</dependency>
⚠️ 注意：你需要先构建并安装该 starter 到本地 Maven 仓库，或发布到私有仓库。

2. 启用自动填充功能
   确保你的项目中已启用 MyBatis Plus 的自动填充功能（通常在启动类上加注解即可）：

java
深色版本
@SpringBootApplication
public class Application {
public static void main(String[] args) {
SpringApplication.run(Application.class, args);
}
}
3. 配置自动填充规则（application.yml）
   在 application.yml 中添加如下配置：

yaml
深色版本
auto-fill:
enabled: true
rules:
- field-name: creator
field-type: java.lang.String
source-key: X-User
default-value: anonymous

    - field-name: status
      field-type: java.lang.Integer
      source-key: status
      default-value: 0

    - field-name: createTime
      field-type: java.util.Date

    - field-name: updateTime
      field-type: java.time.LocalDateTime
字段名	类型	说明
field-name	String	实体类中的字段名
field-type	Class	字段类型（支持基本类型、String、Date、LocalDateTime）
source-key	String	请求头或请求参数的 key
default-value	String	默认值，用于兜底
🛠️ 原理说明
本模块通过实现 MetaObjectHandler 接口，在插入或更新操作时自动填充字段值。其优先级如下：

Header → 2. Param → 3. Default Value
对于时间类型字段（如 Date, LocalDateTime），直接使用当前时间填充，每次操作都会刷新。
