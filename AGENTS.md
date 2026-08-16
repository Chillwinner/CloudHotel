# AGENTS.md

Spring Cloud Alibaba 微服务项目（Maven 多模块）。此文件为 AI 编码助手提供项目约定。

## 项目结构

- 根 `src/`：**遗留单体版本**（com.aura.hotel），一般不要修改
- `hotel_user/`：用户服务，端口 8081
- `hotel-resource/`：资源服务，端口 8082
- `hotel-trading/`：交易服务，端口 8083
- `hotel_gateway/`：网关服务，端口 8080
- 公共统一返回：各服务内 `common/Result.java`、`common/PageResult.java`

## 构建 / 测试命令

```bash
mvn clean install -DskipTests   # 全量构建（跳过测试）
mvn test                        # 运行测试
mvn spring-boot:run             # 在具体模块目录下启动该服务
```

> 构建/启动依赖外部组件：MySQL(3306)、Redis(6379)、Nacos(8848)，部分功能还需要 RabbitMQ、ES、Sentinel Dashboard、Seata Server。

## 代码约定

- 包名统一 `com.Aura.*`（微服务模块），根 src 单体为 `com.aura.hotel`
- 分层：Controller（`Controller/`）→ Service（`Service/`）→ Mapper（`Mapper/` 接口 + `resources/Mapper/*.xml`）
- Mapper XML 位于 `src/main/resources/Mapper/`，实体在 `entity/`，统一继承/使用 `common.Result`
- 使用 Lombok（`@Data` 等）简化实体，不加手写 getter/setter
- 用户上下文：网关解析 JWT 后通过请求头传递，各服务用 `utils/UserContext`（ThreadLocal）获取 userId
- 缓存统一走 `utils/CacheService`（逻辑过期 + Redisson 读写锁），不要直接裸操作 Redis；写操作后需删除相关缓存 key
- 密钥/密码只放本地 `application.yml`（已被 .gitignore 忽略），新增密钥配置必须同步更新 `application.yml.example` 的占位，**不得提交真实密钥**
- 新增配置项命名保持现有风格（如 `sky.alioss.*`、`ai.qwen.*`、`jwt.*`）

## 技术栈要点

- Java 21、Spring Boot 3.5.0、Spring Cloud 2025.0.0、Spring Cloud Alibaba 2025.0.0.0（版本由根 pom 统一管理，子模块勿硬写版本）
- MyBatis 3.0.4 + MySQL；Redis + Redisson；RabbitMQ；Seata；Sentinel；Nacos
- 交易服务集成 LangChain4j 1.0.0-beta3（通义千问 + Function Calling + RAG + 流式），AI 相关代码在 `hotel-trading` 的 `utils/` 与 `config/AiConfig.java`
