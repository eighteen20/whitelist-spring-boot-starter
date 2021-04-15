# whitelist-spring-boot-starter

### 使用

application.yml

```yaml
middleware:
  white:
    users: aaa,000,bbb # 放行的用户ID
```

```java
 @DoWhiteList(key = "userId", returnJson = "{\"code\":\"999\",\"info\":\"非白名单可访问用户拦截！\"}")
```