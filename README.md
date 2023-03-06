# shure-spring-boot-starter

## Shure-spring-boot-security-starter

我们提供了半自动化的SpringSecurity配置，极大的简化了SpringSecurity在前后端分离
场景下的配置。

1. 利用SpringSecurity的匿名接口和非认证接口配置，我们将其简化为注解式配置
   只要在接口上直接注解Anonymous或者Exposed，即可声明一个匿名/开放接口

2. 为保证系统的完整性，我们强烈请求您注入PasswordEncoder，AccessDeniedHandler,AuthenticationEntryPoint

3. 通过实现BaseAccessDecisionVoter，可以实现权限校验，该类可有多个实例

4. 实现ShureRequestFilter，以自动化注入SpringSecurity过滤器链

5. 同时，为了兼容一些无法暴露的监控接口，我们也暴露了配置文件式配置

* shure.security.exposed
* shure.security.anonymous

## Shure-spring-boot-core

在这里，我们提供了一些常用工具。
我们对@ResponseBody拓展了@SingleParamBody注解，支持单参数Body（注意，不兼容SpringValidation）

## Shure-spring-boot-orm

旨在自动化集成ORM框架
