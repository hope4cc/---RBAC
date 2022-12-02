## 系统用户权限功能实战-数据库层面RBAC

## 一、数据库设计

### 1、数据库表

```mysql
#菜单的图标，可以后面自定义导入其他图标库，这里存储element ui icon
create table if not exists sys_dict
(
    name  varchar(255) null comment '名称',
    value varchar(255) null comment '内容',
    type  varchar(255) null comment '类型'
)
    collate = utf8mb4_unicode_ci;

#菜单
create table if not exists sys_menu
(
    id          int auto_increment comment 'id'
        primary key,
    name        varchar(255) null comment '名称',
    path        varchar(255) null comment '路径',
    icon        varchar(255) null comment '图标',
    description varchar(255) null comment '描述',
    pid         int          null comment '父级id',
    page_path   varchar(255) null comment '页面路径',
    sort_num    int          null comment '排序'
)
    collate = utf8mb4_unicode_ci;

#角色表
create table if not exists sys_role
(
    id          int auto_increment comment 'id'
        primary key,
    name        varchar(50)  null comment '名称',
    description varchar(255) null comment '描述',
    flag        varchar(50)  null comment '唯一标识'
)
    collate = utf8mb4_unicode_ci;

#角色和菜单的关联关系表
create table if not exists sys_role_menu
(
    role_id int not null comment '角色id',
    menu_id int not null comment '菜单id',
    primary key (role_id, menu_id)
)
    comment '角色菜单关系表' collate = utf8mb4_unicode_ci;
    
#用户表
create table if not exists sys_user
(
    id          int auto_increment comment 'id'
        primary key,
    username    varchar(50)                         null comment '用户名',
    password    varchar(50)                         null comment '密码',
    nickname    varchar(50)                         null comment '昵称',
    email       varchar(50)                         null comment '邮箱',
    phone       varchar(20)                         null comment '电话',
    address     varchar(255)                        null comment '地址',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    avatar_url  varchar(255)                        null comment '头像',
    role        varchar(50)                         null comment '角色'
)
    collate = utf8mb4_unicode_ci;

```

### 2、数据库数据

```mysql
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `address`, `create_time`, `avatar_url`, `role`) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '菜菜', 'long@qq.com', '1602774287', '福建厦门', '2022-12-01 21:10:27', 'http://localhost:9090/file/50c27c625d0b4f538d0c8113d7bdbb3b.png', 'ROLE_ADMIN');
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `address`, `create_time`, `avatar_url`, `role`) VALUES (31, 'suxiaofang', '202cb962ac59075b964b07152d234b70', '芳芳', NULL, NULL, NULL, '2022-12-01 17:45:40', NULL, 'ROLE_STUDENT');
COMMIT;

BEGIN;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 4);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 5);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 6);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 7);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 10);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (3, 10);
COMMIT;

BEGIN;
INSERT INTO `sys_role` (`id`, `name`, `description`, `flag`) VALUES (1, '管理员', '管理员', 'ROLE_ADMIN');
INSERT INTO `sys_role` (`id`, `name`, `description`, `flag`) VALUES (2, '学生', '学生', 'ROLE_STUDENT');
INSERT INTO `sys_role` (`id`, `name`, `description`, `flag`) VALUES (3, '老师', '老师', 'ROLE_TEACHER');
COMMIT;

BEGIN;
INSERT INTO `sys_menu` (`id`, `name`, `path`, `icon`, `description`, `pid`, `page_path`, `sort_num`) VALUES (4, '系统管理', NULL, 'el-icon-s-grid', NULL, NULL, NULL, 100);
INSERT INTO `sys_menu` (`id`, `name`, `path`, `icon`, `description`, `pid`, `page_path`, `sort_num`) VALUES (5, '用户管理', '/user', 'el-icon-user', NULL, 4, 'User', 101);
INSERT INTO `sys_menu` (`id`, `name`, `path`, `icon`, `description`, `pid`, `page_path`, `sort_num`) VALUES (6, '角色管理', '/role', 'el-icon-s-custom', NULL, 4, 'Role', 102);
INSERT INTO `sys_menu` (`id`, `name`, `path`, `icon`, `description`, `pid`, `page_path`, `sort_num`) VALUES (7, '菜单管理', '/menu', 'el-icon-menu', NULL, 4, 'Menu', 103);
INSERT INTO `sys_menu` (`id`, `name`, `path`, `icon`, `description`, `pid`, `page_path`, `sort_num`) VALUES (10, '主页', '/home', 'el-icon-house', NULL, NULL, 'Home', 0);
COMMIT;

BEGIN;
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('user', 'el-icon-user', 'icon');
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('house', 'el-icon-house', 'icon');
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('menu', 'el-icon-menu', 'icon');
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('s-custom', 'el-icon-s-custom', 'icon');
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('s-grid', 'el-icon-s-grid', 'icon');
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('document', 'el-icon-document', 'icon');
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('coffee', 'el-icon-coffee\r\n', 'icon');
INSERT INTO `sys_dict` (`name`, `value`, `type`) VALUES ('s-marketing', 'el-icon-s-marketing', 'icon');
COMMIT;
```



### 3、关系图

![image-20221202120809597](https://tva1.sinaimg.cn/large/008vxvgGly1h8pc6unoqrj329a0s0111.jpg)

## 二、项目搭建

### 1、后端项目依赖

Springboot 2.76

mybatis-plus 简化单表的查询

hutool 工具类，都是静态方法，直接调用api

JWT

```xml
   <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- mybatis-plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.1</version>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.10.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>4.1.2</version>
        </dependency>

        <!--		hutool-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.7.20</version>
        </dependency>
    </dependencies>
```

#### 1.1、jwt和token区别

##### jwt 

![截屏2022-12-02 12.20.50](https://tva1.sinaimg.cn/large/008vxvgGly1h8pck37pj9j315e090wg8.jpg)

JWT是json web token缩写。它将用户信息[加密](https://so.csdn.net/so/search?q=加密&spm=1001.2101.3001.7020)到token里，服务器不保存任何用户信息。服务器通过使用保存的密钥验证token的正确性，只要正确即通过验证

1. 用户输入用户名和密码，发送给服务器，服务器验证账号密码成功
2. 服务器使用签名秘钥生成jwt，把用户id放到jwt中
3. 把令牌返给客户端
4. 下次请求的时候就把令牌放在请求头里带上
5. 服务器使用签名秘钥验证jwt是否有效
6. 有效后可以从jwt中获取到用户id

**优点**

1. jwt自身包含用户信息且无法篡改，数据实际保存在客户端，所以我们可以分担数据库或服务器的存储压力 

**缺点**

2. 建议不要放敏感数据
3. Jwt生成之后无法修改（发生变化）
4. 后端无法统计生成jwt
5. 无法吊销令牌，只能等待令牌自身过期
6. 令牌长度与其包含用户信息多少正相关，传输开销较大
7. Jwt是无状态的，如果别人获取到了，别人也能用

##### token

1. 用户前台输入账号和密码，发送给后端服务器，服务器验证账号密码成功后
2. 服务器生成令牌，本质是一个唯一字符串，可以使用uuid或者其他随机生成算法
3. 这里将该令牌存到数据库或redis中，key是uuid，value是用户信息，value用hash 方便以后对单一数据进行增删改

![截屏2022-12-02 12.24.00](https://tva1.sinaimg.cn/large/008vxvgGly1h8pcnhro26j31kc0n076m.jpg)

**优点**

1. 可以隐藏真实数据，安全系数高
2. 适用于分布式/微服务
3. token支持手动控制，过期、吊销等
4. 可以实时查询现有token

**缺点**

1. 存放在数据库或者redis，依赖服务器资源
2. 效率相对jwt比较低

### 2、前端搭建

vue 2、element ui 

![截屏2022-12-02 12.47.36](https://tva1.sinaimg.cn/large/008vxvgGly1h8pdbzleskj31c90u0wip.jpg)

```json
依赖  
"dependencies": {
    "axios": "^0.27.2",
    "core-js": "^3.8.3",
    "echarts": "^5.3.3",
    "element-ui": "^2.15.9",
    "nprogress": "^0.2.0",
    "vue": "^2.6.14",
    "vue-router": "^3.0.7",
    "vuex": "^3.6.2"
  },
```

## 三、项目测试

#### 1、请求验证

>前端通过简单的校验后，发起请求，并且携带存储在cookie 或者localStorage 中的jwt 后端jwt 拦截器首先将请求拦截，获取请求头的token
>
>如果有token，请求通过，并且把token续约
>
>如果没有token，则打回前端跳转至登录页面 用户需重新登录

```java
 @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");

        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        } else {
            //自定义拦截器
            HandlerMethod h = (HandlerMethod) handler;
            AuthAccess authAccess = h.getMethodAnnotation(AuthAccess.class);
            if (authAccess != null) {
                return true;
            }
        }
        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException( Constants.CODE_401, "无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        // 根据token中的userid查询数据库
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }
        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        return true;
    }
```

#### 2、登录流程

![RBAC0](https://tva1.sinaimg.cn/large/008vxvgGly1h8pdd03sdgj31d20rudjb.jpg)

>
>
>经过jwt拦截器校验后，经过DispathServlet 进行分发处理找到对应的controller 下login映射路径下的login 用法
>
>参数是 重新封装的userdto 用于前后端数据传输 
>
>首先前端请求携带的用户名和密码，使用hutool 工具类的isBlank 对 用户吗和密码校验，如果符合以上三种情况直接打回
>
>如果没有错误参数的话 进入业务层的login方法，参数还是userdto

![截屏2022-12-02 12.49.33](https://tva1.sinaimg.cn/large/008vxvgGly1h8pddybbluj31du0jcad7.jpg)

>
>
>进入业务层后，首先对刚刚的密码进行加密存储到数据库
>
>调用 getUserInfo 方法 根据刚刚前端输入到账号密码 去数据库中寻找
>
>如果 不为空的话，说明该用户存在，使用hutool 工具类 BeanUtils.copyProperties 将数据库的中用户信息拷贝到userDTO中此时只是调用set方法，并没有将所有属性拷贝。(也就是说，引用的一个内存地址)
>
>然后设置 jwt： 头部是用户id，签名是用户密码，数据就是userDTO，将生成的token set到userDTO
>
>获取 改用户的角色，因为已经用户表中已经有角色字段，我们可以直接获取，如果分开的，就去用户角色关联关系表中查询
>
>根据角色去查询 该角色绑定的菜单，然后返回的菜单集合set到userDTO
>
>此时我们的userDTO数据装填满了， 返回userDTO到controller
>
>如果查询到用户信息为空，说明数据库中不存在该用户，这是登录 所以用户 用户名或密码输入错误

![截屏2022-12-02 13.01.40](https://tva1.sinaimg.cn/large/008vxvgGly1h8pdqnte2nj30w40u0djb.jpg)

>
>
>用postman 测试

![截屏2022-12-02 12.19.17](https://tva1.sinaimg.cn/large/008vxvgGly1h8pcijv7l3j31a70u0agc.jpg)



#### 3、前台页面

##### 1、不同角色登录

![截屏2022-12-02 13.18.54](https://tva1.sinaimg.cn/large/008vxvgGly1h8pe8jpc06j31b30u0tf1.jpg)

>
>
>使用管理账号登录后可以看到全部菜单

![截屏2022-12-02 13.19.44](https://tva1.sinaimg.cn/large/008vxvgGly1h8pe9wofv0j31ak0u00w1.jpg)

>
>
>使用学生角色登录只能看到主页

![截屏2022-12-02 13.26.33](https://tva1.sinaimg.cn/large/008vxvgGly1h8pehey4eej31bp0u0tbv.jpg)



##### 2、超级管理员可以对其他角色 权限赋予

![截屏2022-12-02 13.28.17](https://tva1.sinaimg.cn/large/008vxvgGly1h8pej839n3j31h40u0wh6.jpg)

##### 3、根据角色分配相对应的菜单

![截屏2022-12-02 13.29.39](https://tva1.sinaimg.cn/large/008vxvgGly1h8pejtijrbj32d00rwtbv.jpg)

##### 4、菜单设置

>
>
>```jva
>queryWrapper.orderByAsc("sort_num"); 根据sort_num字段数值升序，菜单至前数值小，至后数字大即可
>```

![截屏2022-12-02 13.30.48](https://tva1.sinaimg.cn/large/008vxvgGly1h8pel06sdmj31ra0u077a.jpg)



## 四、shiro
