# 酒店管理系统（微服务版）

基于 Spring Cloud 微服务架构的酒店管理系统，涵盖用户管理、酒店资源管理、订单交易、AI智能助手等核心模块。

## 项目简介

本项目是一个完整的酒店业务系统，采用微服务架构设计，实现了从用户注册登录、酒店/房间浏览、在线预订、订单管理到AI智能推荐的完整业务闭环。

### 核心功能

| 模块 | 功能 |
|------|------|
| **用户服务** | 用户注册、登录、JWT鉴权、个人信息管理、阿里云OSS头像上传 |
| **资源服务** | 酒店管理、房间管理、员工管理、Seata分布式事务、Redis缓存 |
| **交易服务** | 订单创建与支付、RabbitMQ异步消息、邮件通知、通义千问AI对话 |
| **网关服务** | 统一入口、路由转发、JWT Token校验、跨域配置、Swagger聚合 |

## 技术栈

### 核心框架
- **Spring Boot 3.4.2** - 基础框架
- **Spring Cloud 2024.0.0** - 微服务治理
- **Spring Cloud Alibaba 2023.0.3.2** - 阿里巴巴微服务组件

### 微服务组件
| 组件 | 用途 | 面试八股要点 |
|------|------|-------------|
| **Nacos** | 服务注册与发现 | AP/CP模式切换、心跳机制、健康检查 |
| **Gateway** | API网关 | 过滤器链、路由配置、限流、鉴权 |
| **Sentinel** | 流量控制与熔断降级 | 滑动窗口、热点参数限流、熔断策略 |
| **OpenFeign** | 声明式远程调用 | 负载均衡、Fallback降级、请求拦截 |
| **Seata** | 分布式事务 | AT/TCC/Saga模式、全局锁、 undo_log |
| **LoadBalancer** | 客户端负载均衡 | 轮询/随机策略、服务实例选择 |

### 数据层
- **MySQL 8.x** - 关系型数据库
- **MyBatis 3.0.4** - ORM框架（半自动映射、动态SQL）
- **Redis** - 分布式缓存（缓存穿透/击穿/雪崩解决方案）
- **Redisson** - 分布式锁（看门狗机制、可重入锁）
- **RabbitMQ** - 消息队列（异步解耦、削峰填谷）

### 其他技术
- **JWT** - 无状态认证（Token生成与校验、过期刷新）
- **阿里云OSS** - 对象存储（文件上传、CDN加速）
- **LangChain4j + 通义千问** - AI大模型集成
- **Spring Mail** - 邮件服务（QQ邮箱SMTP）
- **Knife4j/Swagger** - API接口文档自动生成
- **Lombok** - 简化Java代码
- **Java 21** - LTS版本

## 八股文知识点汇总

### 1. 微服务架构
- **服务拆分原则**：单一职责、高内聚低耦合
- **服务注册发现**：Nacos CP/AP模式、临时实例与持久实例
- **服务间通信**：OpenFeign vs RestTemplate vs gRPC
- **服务容错**：Sentinel限流降级、Hystrix对比

### 2. 分布式事务
- **Seata AT模式**：自动生成undo_log、全局锁机制
- **CAP定理**：分布式系统一致性与可用性权衡
- **最终一致性**：消息队列+重试机制

### 3. 缓存相关
- **缓存穿透**：布隆过滤器、空值缓存
- **缓存击穿**：互斥锁（Redisson）、永不过期+异步刷新
- **缓存雪崩**：随机过期时间、多级缓存
- **Redis持久化**：RDB vs AOF

### 4. 消息队列
- **RabbitMQ**：交换机类型（Direct/Fanout/Topic/Headers）
- **消息可靠性**：确认机制、持久化、死信队列
- **消息幂等性**：去重表、Redis SETNX

### 5. 安全认证
- **JWT**：三部分组成（Header.Payload.Signature）
- **无状态vs有状态**：Session vs Token
- **Token刷新**：双Token机制、Redis存储

### 6. 设计模式
- **策略模式**：多渠道支付、多类型消息
- **代理模式**：AOP日志、权限校验
- **工厂模式**：Bean创建、对象生成

## 项目结构

```
new_Hotel/
├── hotel_gateway/          # 网关服务 (端口: 8080)
│   ├── filter/             # 全局过滤器（JWT校验）
│   └── utils/              # JWT工具类
│
├── hotel_user/             # 用户服务 (端口: 8081)
│   ├── Controller/         # 用户接口
│   ├── Service/            # 业务逻辑
│   ├── Mapper/             # MyBatis数据层
│   ├── entity/             # 实体类
│   ├── interceptor/        # 拦截器
│   ├── config/             # 配置类
│   └── utils/              # 工具类（JWT/Redis/OSS）
│
├── hotel-resource/         # 资源服务 (端口: 8082)
│   ├── Controller/         # 酒店/房间/员工/OSS接口
│   ├── Service/            # 业务逻辑
│   ├── Mapper/             # MyBatis数据层
│   ├── entity/             # 实体类
│   └── utils/              # 工具类
│
├── hotel-trading/          # 交易服务 (端口: 8083)
│   ├── Controller/         # 订单/AI接口
│   ├── Service/            # 业务逻辑
│   ├── Mapper/             # MyBatis数据层
│   ├── feign/              # OpenFeign远程调用
│   ├── interceptor/        # 拦截器
│   └── utils/              # AI服务工具类
│
└── pom.xml                 # 父POM（版本管理）
```

## 快速开始

### 环境要求

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 21+ | 项目使用Java 21 LTS |
| Maven | 3.8+ | 项目构建工具 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 |
| Nacos | 2.2+ | 服务注册中心 |
| RabbitMQ | 3.10+ | 消息队列（可选） |

### 1. 克隆项目

```bash
git clone https://github.com/your-username/new_Hotel.git
cd new_Hotel
```

### 2. 配置环境

**复制配置文件并填写实际值：**

```bash
# 为每个服务创建配置文件
cp hotel_user/src/main/resources/application.yml.example hotel_user/src/main/resources/application.yml
cp hotel-resource/src/main/resources/application.yml.example hotel-resource/src/main/resources/application.yml
cp hotel-trading/src/main/resources/application.yml.example hotel-trading/src/main/resources/application.yml
cp hotel_gateway/src/main/resources/application.yml.example hotel_gateway/src/main/resources/application.yml
```

**修改各服务的 `application.yml`，填入：**

| 配置项 | 说明 |
|--------|------|
| `spring.datasource.password` | MySQL密码 |
| `spring.data.redis.password` | Redis密码 |
| `jwt.secret-key` | JWT密钥（自定义） |
| `sky.alioss.access-key-id` | 阿里云AccessKey ID |
| `sky.alioss.access-key-secret` | 阿里云AccessKey Secret |
| `sky.alioss.bucket-name` | OSS Bucket名称 |
| `ai.qwen.api-key` | 通义千问API Key（交易服务） |

### 3. 初始化数据库

```sql
CREATE DATABASE hotel_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel_db;

-- 根据项目中的SQL脚本创建表结构
-- 建议使用 MyBatis 自动生成或手动执行DDL
```

### 4. 启动基础服务

```bash
# 1. 启动 Nacos（单机模式）
sh startup.sh -m standalone

# 2. 启动 Redis
redis-server

# 3. 启动 RabbitMQ（可选，交易服务需要）
rabbitmq-server
```

### 5. 编译与启动

```bash
# 编译所有模块
mvn clean install -DskipTests

# 按顺序启动服务（端口不能冲突）
# 1. 用户服务
cd hotel_user && mvn spring-boot:run

# 2. 资源服务
cd hotel-resource && mvn spring-boot:run

# 3. 交易服务
cd hotel-trading && mvn spring-boot:run

# 4. 网关服务（最后启动）
cd hotel_gateway && mvn spring-boot:run
```

### 6. 验证服务

```bash
# 访问 Nacos 控制台
http://localhost:8848/nacos

# 访问网关 Swagger 文档
http://localhost:8080/swagger-ui.html

# 各服务直接访问
http://localhost:8081/swagger-ui.html  # 用户服务
http://localhost:8082/swagger-ui.html  # 资源服务
http://localhost:8083/swagger-ui.html  # 交易服务
```

## 服务端口一览

| 服务 | 端口 | 说明 |
|------|------|------|
| hotel_gateway | 8080 | API网关入口 |
| hotel_user | 8081 | 用户服务 |
| hotel-resource | 8082 | 资源服务 |
| hotel-trading | 8083 | 交易服务 |
| Nacos | 8848 | 服务注册中心 |
| Sentinel Dashboard | 8858 | 流控监控面板 |

## 常见问题

### Q: 启动报错 `Connection refused`
检查 MySQL、Redis、Nacos 是否已启动，配置是否正确。

### Q: OpenFeign 调用失败
确保目标服务已注册到 Nacos，且 `@EnableFeignClients` 注解已添加。

### Q: Seata 分布式事务不生效
检查 `@GlobalTransactional` 注解是否在事务发起方，且 `undo_log` 表已创建。

### Q: Sentinel 限流不生效
确认 Sentinel Dashboard 已启动，且服务已正确连接（检查 `csp.sentinel.dashboard` 配置）。

## License

MIT License
