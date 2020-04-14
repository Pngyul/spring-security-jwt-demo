# spring-security-jwt-guide

## 介绍

[Spring Security](https://spring.io/projects/spring-security ) 是 Spring 全家桶中非常强大的一个用来做身份验证以及权限控制的框架，我们可以轻松地扩展它来满足我们当前系统安全性这方面的需求。

但是 Spring Security 相比于其他一些技术比如 JPA 来说更难上手，很多人初学的时候很难通过看视频或者文档发就很快能独立写一个 Demo 出来，于是后面可能就放弃了学习这个东西。

这个 Demo 主要用到了 **Spring Security** 和 **Spring Boot** 这两门技术，并且所有的依赖采用的都是最新的稳定版本。初次之外，这个项目还用到了 JPA 这门技术。项目代码结构如下(chrome 插件：Octoree)，整体还是比较清晰的，由于自己的能力以及时间有限，所以一定还有很多可以优化的地方，有兴趣的朋友可以一起完善，期待你的 PR。

该项目有**JWT**、**JWT+验证码**、**JWT+验证码+数据库存储用户信息+处理权限问题**，并且所以返回结果以及异常**统统返回json**

其实要了解Spring security,最重要的时是了解该**安全框架的原理流程**

下面推荐四篇文章，助你理解 spring-security-jwt-demo。

1. [Spring Security 工作原理概览](https://mp.weixin.qq.com/s/Tup0cP2ZCdzodD8790Hr1w)

2. [使用SpringSecurity完美处理权限问题(一)](https://mp.weixin.qq.com/s/lpznrVx6Bh9X7ZnunrWQSA?)

3. [使用SpringSecurity完美处理权限问题(二)](https://blog.csdn.net/u012702547/article/details/79019510)

4. [干货|一个案例学会Spring Security 中使用 JWT!](https://mp.weixin.qq.com/s/riyFQSrkQBQBCyomE__fLA)