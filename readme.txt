prj-crm-01

一、
    - 搭建环境，使用spring集成mybatis，aspectj完成事务管理

    搭建步骤：
    1.新建maven项目
    2.加入maven的依赖
         1）spring依赖  
         2）mybatis依赖  
         3）mysql驱动  
         4）spring的事务的依赖
         5）mybatis和spring集成的依赖： mybatis官方体用的，用来在spring项目中创建mybatis的SqlSesissonFactory，dao对象的
         6）spring-aspects依赖，使用aspect的aop完成事务管理
    3. 创建实体类
    4. 创建实体类对应的dao接口和mapper文件
    5. 创建mybatis主配置文件
    6. 创建service接口和实现类，提供一个属性dao, 并为该属性提供set方法，便于xml配bean
    7.创建spring的配置文件：声明mybatis的对象交给spring创建
        1）数据源DataSource
        2）SqlSessionFactory
        3) Dao对象
        4）声明自定义的service
    搭建成功，目前缺少事务，和web监听器。

    - 配置web监听器，使服务器启动时候创建IoC容器，且只有一个容器。
      使servlet的创建时机,放在ServletContext初始化时，保证Spring容器在整个应用中的唯一性。
      spring容器创建好后，在整个应用的的生命周期中随时可以被访问，即具有全局性。所以将其以属性的形式放入到ServletContext上下文环境中。
      1. 导入依赖 spring-web
      2. 注册监听器 ContextLoaderListener
         该监听器作用：创建容器对象，并将容器对象放入ServletConetxt空间中

      3. 指定spring配置文件所在位置
      4. 使用spring提供的WebApplicationContextUtils获取对象
    错误点：注册sqlSessionFactory异常。指定mybatis主配置文件，时路径前加上classpath

            <!--2. 注册sqlSessionFactory的bean-->
            <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
                <!--指定数据源-->
                <property name="dataSource" ref="dataSource"/>
                <!--指定 mybatis主配置文件-->
                <property name="configLocation" value="classpath:mybatis-config.xml"/>
            </bean>
    - 使用AspectJ的AOP配置事务管理

二 、用户模块

    - 后端模块搭建
        1. 创建目录结构,提供相关工具类
        2. 创建用户表(tbl_user)，提供基础数据
        3. 封装User bean, 跟表中字段一致，提供set get方法
        4. 创建UserDao, Usermapper，
    - 登录前端页面修改
        1. 登录页面加载完毕后，光标聚焦到账号输入框
        2. 为登录按钮绑定点击事件
        3. 为当前窗口绑定敲键盘事件，使敲键盘也能进行登录
        4. 养成习惯，封装好的自定义方法，写在$(function(){})外面
    - 登录模块后端
        1. 自定义异常
        2. Dao层 根据用户名和密码查询，将查询结果封装为一个User对象
        3. Service层
            1. 验证用户名密码
            2. 验证失效时间
            3. 验证锁定状态
            4. 验证ip地址
        4. 添加字符编码过滤器
        5. 登录后从session中取user,展示在主页上
           ${sesscionScope.user.name}
           EL表达式中带scope的域对象可以省略
        6. 登录拦截过滤器. 有时浏览器会走缓存，所以不经过过滤器
        7. 登录页面：如果登录页面不是顶级窗口，将其设置为顶级窗口

三 、市场活动模块
    - 准备工作
        1. 建表
            tbl_activity 市场活动表
            tb_activity_remark  市场活动备注表
        2. 前端资源修改，解决404
        3. 登录页不是顶层窗口时，将当前窗口设置为顶层窗口
        4. 创建工作台包结构
        5. 创建表对应的实体类；Activity ActivityRemark
        6. dao层：ActivityDao.java ActivityDao.xml.修改mybatis主配置文件
        7. service层：ActivityService impl.ActivityServiceImpl 处理市场活动业务
        8. controller层: ActivityController 市场活动控制器 (/workbench/activity/***.do)

    - 处理模态窗口
        1. 给按钮绑定单击事件，在自定义函数中展开模态窗口 $("#btnid").modal("show");
        2. 走后台，取得用户信息列表，为所有者下拉框铺值。（AJAX请求）
        3. 将所有者框中默认选中的是“当前登录用户"，从session中取
        4. 添加日历控件

    - 创建市场活动
        1. AJAX提交表单数据
        2. $.trim() 去空格
        3. 登录业务，自定义异常. spring配置文件为方法指定需要回滚的异常。aspectj代理对象中抓住异常后是会上抛的。
        4. 手动关闭模块窗口前，清空添加操作模态窗口中的数据
    - 分页查询市场活动信息列表(带参数)

        分页查询入口：
            one. 页面加载完毕时，自动调用分页查询
            two：点击查询按钮
            three: 添加 修改 删除 之后
            four: 点击分页组件
        1. 页面加载完毕触发一个方法，pageList(pageNo, pageSize)
        2. 后台方法：(/workbench/activity/pageList.do)
        3. 后台MVC. 创建通用的vo，用于给前端传值的. Pagination<T>
        4. 前端动态展现列表数据
        5. 使用分页插件
        6. 将查询条件保存在隐藏域中，在分页查询时候将隐藏域的值设置到参数框中。
           在执行分页带参查询时候，将隐藏域的值设置到条件框中
        7. 为全选复选框绑定事件，触发全选操作
        8. 单选按钮绑定事件

    - 删除市场活动
        1. deleteBtn,为删除按钮绑定事件，执行市场活动删除操作
        2. delete.do
        3. 拼接参数
        4. ajax
        5. 执行市场活动删除操作
            查询需要删除的备注数量
            删除备注，返回收到影响的条数
            删除市场活动
        6. if(window.confirm("msg")){执行相应操作}
    - 市场活动修改
        1. 点击修改按钮（editBtn）,过后台
            选择器: $(input[name=xz])
                   $(input[name=xz]:checked) 复选框中属性为checked的
        2. 进行判断，0条或多条不能修改，只有单条能进行 修改
        3. 请求url: /workbench/activity/getUserListAndActivity.do
        4. 需要后台提供数据：市场活动信息对象，用户列表
        6. textarea
        7. 所有数据铺完之后，展开模态窗口
        8. 使用map封装需要返回的数据
        9. 执行更新 请求url: /workbench/activity/update.do
    - 跳转到详细信息页
        1. 传统请求过后台，workbench/activity/detail.do，跳转到详细信息页
        2. 在详细信息页，使用EL表达式展示市场活动详细信息数据
        3. 页面加载完毕发送ajax请求，获取该市场活下的所有的的备注信息
           后台接口：/workbench/activity/getRemarkListByAid.do 根据市场活动id查备注
        4. 删除指定id的备注 workbench/activity/deleteRemark.do
        5. 添加备注：workbench/activity/saveRemark.do
        6. 修改备注：workbench/activity/updateRemark.do

四 、线索模块
    - 准备工作
        1. 导入“线索”、“客户”、“联系人”、“交易”相关表
        2. 将“线索”、“客户”、“联系人”、“交易”模块的html修改为jsp，解决404错误。
        3. 创建线索后台包结构 dao service controller domain
        4. 数据字典 写活拉框的（动态的） DicType DicValue
    -  服务器缓存中操作数据字典
        1. 使用监听器 web.listener  SysInitListener 系统初始化时，将数据字典初始化到服务器内存中去
           map中套list
           map<String, List<DicValue>>
        (创建失败，clue包结构需要重新)
    - 打开线索创建模态窗口
        1. 为创建按钮绑定事件，打开线索模块添加模态窗口
        2. 模态窗口打开前，ajax请求获取用户列表
        2. 后台接口：“workbench/clue/getUserList.do” 获取用户列表
        3. 线索添加操作，后台接口 “workbench/clue/save.do”
        4. 下拉框，jstl.
    - 线索添加
        1. 处理字段，要和数据库表中一致
        2. 为保存按钮绑定事件，执行线索的添加操作
        2. 后台提供接口，保存线索对象，"workbench/clue/save.do"

    - 分页查询线索信息列表(带参数)
            分页查询入口：
                one. 页面加载完毕时，自动调用分页查询
                two：点击查询按钮
                three: 添加 修改 删除 之后
                four: 点击分页组件
            1. 服务端接口：(/workbench/clue/pageList.do)
                - 后台MVC. 创建通用的vo，用于给前端传值的. Pagination<T>
            2. 页面加载完毕触发一个方法，pageList(pageNo, pageSize)
             - 前端动态展现列表数据
             - 加入分页插件
            3. 点击查询按钮，为查询按钮绑定事件
               - 将查询条件保存在隐藏域中，在分页查询时候将隐藏域的值设置到参数框中。
               - 在执行分页带参查询时候，将隐藏域的值设置到条件框中
            4. 为全选复选框绑定事件，触发全选操作
            5. 单选按钮绑定事件
    - 删除线索对象
            1. deleteBtn,为删除按钮绑定事件，执行市场活动删除操作
            2. delete.do ("/workbench/clue/delete.do")
            3. 拼接参数
            4. ajax
            5. 执行线索相关删除操作
                查询需要删除的备注数量
                删除备注，返回收到影响的条数
                查询需要删除的线索关联的市场活动数量
                解除关联，返回收到的影响的条数
                删除线索对象
            6. if(window.confirm("msg")){执行相应操作}
    - 线索对象修改
            1. 点击修改按钮（editBtn）,过后台
                选择器: $(input[name=xz])
                       $(input[name=xz]:checked) 复选框中属性为checked的
            2. 进行判断，0条或多条不能修改，只有单条能进行 修改
            3. 请求url: /workbench/clue/getUserListAndClueById.do
            4. 需要后台提供数据：线索对象信息，用户列表
            6. textarea
            7. 所有数据铺完之后，展开模态窗口
            8. 使用map封装需要返回的数据
            9. 执行更新 请求url: /workbench/clue/update.do
    - 跳转到详细信息页
            1. 传统请求过后台，workbench/clue/detail.do，跳转到详细信息页
            2. 在详细信息页，使用EL表达式展示市场活动详细信息数据
            3. 页面加载完毕发送ajax请求，获取该线索下的所有的的备注信息
               后台接口：/workbench/clue/getRemarkListByAid.do 根据市场活动id查备注
            4. 删除指定id的备注 workbench/clue/deleteRemark.do
            5. 添加备注：workbench/clue/saveRemark.do
            6. 修改备注：workbench/clue/updateRemark.do
    - 展现关联的市场活动列表
        市场活动 线索 多对多关系
            1. 获取线索所相关的市场活动
               后台接口: "workbench/clue/getActivityListByClueId.do" 根据线索id查询关联的市场活动列表
            2. 接口: "workbench/clue/unbund.do" 执行解除关联操作
            3. 点击关联市场活动按钮，打开模态窗口
                * modal窗口弹出之前：清空tbody
                * modal窗口弹出之前：清空输入框
                * modal窗口弹出之前：重置复选框
                * 文本框获取焦点
            4. 后台接口: "workbench/clue/getActivityListByNameAndNotByClueId.do"
            5. 排除掉已经关联的市场活动
     - 关联（多对多）
        1. 为关联按钮绑定事件
        2. 处理单选框，多选框
        3. 后台接口："workbench/clue/bund.do"