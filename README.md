# Constellation
### 星尘APP

#### 1、应用简介

- 应用功能
  - 人脸识别登录
  - QQ登录
  - 星座运势查询
  - 天气查询
  - 快递查询
  - 智能机器人
- 应用框架
  - ![constellationStruct](https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/constellationStruct.png)

#### 2、系统详细介绍

- 条模块功能概述
  - 引导界面：应用启动后首先被加载的页面，该页面由一句随机名言以及进度条组成，因为后续页面的数据所用到的随机壁纸需要联网获取，故需消耗一定的时间，所以加入引导页面以确保后续页面所需要的数据加载完毕。
  - 登录界面：该页面提供用户三种不同的登录方式：账号密码登录（需先注册）、人脸登录（需先将人脸数据绑定到账号上）、QQ登录（无需事先注册，通过拉取手机QQ获得用户的QQ数据，若用户第一次使用QQ登录功能，将根据用户的QQ数据自动进行注册，并将用户的相关数据设为默认，进入主页后需要用户修改相关数据），本页面可跳转到注册页面以便用户进行注册。点击上方返回按钮后便可退出本应用。
  - 注册界面：该页面提供用户账号密码的注册，用户需要提供用户名、密码、生日、性别信息，该数据将保存在本地SQLite数据库。
  - 主界面：本页由一个Frame和一个底部导航栏组成，通过点击底部导航栏的不同图标来切换Fragment。底部导航栏有五个按钮：主页、探索、AI、我的四个按钮。   “主页”按钮：对应的Fragment将显示用户的星座运势，通过ViewPager来切换今日运势、明日运势、本周运势、本月运势以及本年运势，顶部通过TabLayout提示用户当前在哪个页面。
  - 探索页：对应的Fragment显示功能列表，通过RecyclerView显示三个功能图标：天气查询图标、快递查询图标、星座简介图标。点击对应按钮后将显示对应的Fragment。
  - 天气查询页：默认将显示用户当前定位城市的天气，点击城市文字将弹出选择城市的Dialog，可直接选择热门城市，也可搜索城市，切换城市后通过下拉刷新获取新的城市的天气，点击“查看近5日天气”按钮将显示对应城市的5日天气趋势。
  - 快递查询页：输入快递单号后点击查询的图标后即可查询到所有的物流信息，包括时间及到达的地点。
  - 星座简介页：显示星座简介的Fragment，该页面由一个RecyclerView构成，每项都是一个星座简介的卡片。
  - 智能机器人页：显示智能机器人的Fragment，输入问题后点击机器人图标即可显示机器人的回复，问题和回复将以对话的形式列出。
  - “我的”页：该页面展示用户的基本信息，包括星座信息、用户名、生日以及性别信息，该页面还包括四个按钮，分别为：“修改资料”、“绑定人脸”、“删除账号”、“退出登录”。点击“修改资料”按钮将跳转到注册界面，从数据库中取出当前的用户信息后渲染到注册界面的各个控件中，并修改注册界面的提示文本信息，修改完成后点击“提交”按钮将把数据库中的用户数据进行更新，完成后返回主界面，并刷新用户的星座运势等数据；点击“绑定人脸”按钮将跳转到人脸识别界面，该界面将进行人脸识别，识别到人脸后用户可按“注册”按钮将识别到的人脸和用户名绑定，之后便可在登录界面使用人脸登录；点击“删除账号”按钮将弹出一个对话框，对用户的操作进行警告，用户点击“确定”后将从数据库中删除当前用户的数据，并跳转到登录界面。点击 “取消”后将关闭对话框；点击“退出登录”按钮将跳转到登录界面。
- 所用技术
  - ArcFace离线SDK：该SDK包含人脸检测、性别检测、年龄检测、人脸识别等能力，激活后即可在本地无网络环境下工作，本应用的人脸识别登录通过该技术实现。
  - QQ登录SDK：该SDK封装了与QQ开放平台相关的一系列函数可能开发者使用，只需少量配置，即可接入QQ登录，通过该SDK的QQ登录接口与获取用户信息接口即可实现第三方应用接入QQ登录。本应用可通过调用该SDK获取用户信息后自动注册用户信息到数据库实现无需输入账号密码即可登录。
  - 百度地图SDK：百度地图 Android SDK是一套基于Android 4.0及以上版本设备的应用程序接口。 开发者可以使用该套 SDK开发适用于Android系统移动设备的地图应用，通过调用地图SDK接口，可以轻松访问百度地图服务和数据，构建功能丰富、交互性强的地图类应用程序。本应用通过调用该SDK接口获取用户位置信息，从而实现根据用户位置显示用户所在城市的天气信息。
  - jisuAPI：极速数据平台为企业、个人开发者提供各类生活数据API，方便开发者快速简单的开发APP、软件及其他服务平台。极速致力于打造一个标准、简单、极速、准确的数据平台，让开发者从繁杂的数据抓取、整理中解放出来，专注于核心业务的开发，提升效率、开发周期，减少维护成本。本应用的天气数据、快递数据、星座数据以及智能机器人均通过调用该API获得。通过在安卓端发起HTTP请求，API将数据通过JSON的形式返回，开发者只需将JSON数据解析成自己所需要的格式即可。
  - LitePal：LitePal是一款开源的Android数据库框架，采用对象关系映射（ORM）模式，将常用的数据库功能进行封装，可以不用写一行SQL语句就可以完成创建表、增删改查的操作。并且很轻量级，jar包不到100k，几乎零配置。本应用所有数据库操作均通过LitePal框架实现，通过调用LitePal封装好的方法即可方便的实现数据库的所有方法，并且LitePal采用面向对象的方法，调用其方法十分简单易懂。

#### 3、系统界面设计

- 引导界面：该界面由一张背景图、一句随机语录和一条进度条构成，成进度条加载完毕后会进入到登录界面。



<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/guide.jpg" alt="guide" style="zoom: 25%;" />

- 登录界面：该界面为用户提供三种不同的登录方式：账号密码登录、人脸识别登录和QQ登录，此外还可根据保存上次登录时的账号和密码，用户登录过一次后便无需再次输入信息。点击左上角返回按钮可退出本应用，点击右下角注册按钮可进行新用户的注册。该界面的背景来自随机从网络上获取的一张720*1080分辨率的图片。

  <img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/login.jpg" alt="login" style="zoom:25%;" />

- 注册界面：该界面提供新用户的注册功能，新用户注册时需要填写用户名、密码、生日和性别信息，其中生日默认为本日，性别默认为男，点击注册按钮后会将用户输入信息存储到数据库中，点击左上角返回按钮可返回登录界面。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/register.jpg" alt="register" style="zoom:25%;" />

- 脸识别登录界面：点击“人脸登录”按钮后将跳转到该界面，该界面默认使用前置摄像头进行人脸识别，用户也可点击右下角按钮切换到前置摄像头，左下角可选择是否打开活体检测。当成功识别到人脸并能匹配到该人脸绑定的人脸信息，即会自动跳转到主界面。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/face_login.jpg" alt="face_login" style="zoom:25%;" />

- 主界面：本应用所有功能都将通过主界面展示，进入主界面后会弹窗提示用户本应用需要获取一些权限方可正常运行，首次运行时用户点击授权将弹出动态授权窗口。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/main.jpg" alt="main" style="zoom:25%;" />

- 选项菜单

  - 当在主界面中点击右上角三个点时便会弹出选项菜单，其中包括“关于”、“换背景”、“开启/关闭音乐”、“人脸识别设置”四个选项
  - 点击“关于”将弹出一个窗口显示本应用作者的相关信息。
  - 点击“换背景”将弹出二级子菜单，其中包括“灰黑色”、“灰色”、“白色”、“默认背景”、“随机”五个选项，点击不同颜色会将背景改为相应颜色，点击“默认背景”会将背景设为一张默认的图片，点击“随机”，会从网络上随机获取一张720*1080分辨率的精美壁纸作为背景。
  - 点击“开启/关闭音乐”将根据背景音乐的播放情况来开启或关闭背景音乐。
  - 点击“人脸识别设置”将跳转到人脸识别设置界面。

  <img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/option.jpg" alt="option" style="zoom:25%;" />

- 主页：该界面为一个Fragment，其布局为一个TabLayout和一个ViewPage，其中TabLayout用来显示最上方的当前页面标题，ViewPage用来显示“今日运势”、“明日运势”、“本周运势”、“本月运势”、“本年运势”五个界面，这五个界面均为Fragment，通过左右滑动来切换到不同的界面。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/today.jpg" alt="today" style="zoom:25%;" />

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/year.jpg" alt="year" style="zoom:25%;" />

- 探索界面：该界面为一个Fragment，其布局为一个RecyclerView，其中包含一个图片按钮。该RecyclerView中共有三项，每项对应一个功能，包括天气查询、快递查询、星座简介。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/explore.jpg" alt="explore" style="zoom:25%;" />

- 天气查询：该Fragment可查看全国天气信息，默认显示用户当前所在位置的天气信息，可点击左上角加号按钮或者当前城市选择国内其他城市，当选择城市的dialog消失后会自动刷新数据，显示当前已选择城市的天气信息。点击“查看近5日天气”将显示当前城市的5天天气趋势的Fragment。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/weather.png" alt="weather" style="zoom:25%;" />

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/weather_five.jpg" alt="weather_five" style="zoom:25%;" />

- 快递查询：该Fragment可根据用户输入的快递单号自动判断是哪家快递公司并显示物流信息，用户可通过滑动屏幕查看完整物流信息。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/delivery_1.jpg" alt="delivery_1" style="zoom:25%;" />

- 星座简介：该Fragment显示12星座的简介，可通过滑动屏幕查看全部的12星座简介信息。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/constellation_list.jpg" alt="constellation_list" style="zoom:25%;" />

- AI界面：该Fragment可通过输入文字的方式来与机器人进行对话。该Fragment主要包含一个用来输入问题的TextView、一个确定按钮以及一个用来显示对话的RecyclerView，该RecyclerView中每一项均为两个CardView，用来显示问和答。该RecyclerView将自动滑动保持一直显示最新的对话信息，以免用户提问后还需手动滑动屏幕来查看最新信息。

<img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/ai.jpg" alt="ai" style="zoom:25%;" />

- “我的”界面

  - 该Fragment是关于用户信息的一个界面，会显示用户的基本信息，并可在该界面修改用户的资料、将用户的人脸特征与当前登录的账号绑定、从数据库中删除当前账号以及退出登录四个功能。

  <img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/me.jpg" alt="me" style="zoom:25%;" />

  - 点击“修改资料”按钮将跳转到注册界面，并修改注册界面的相关文字信息来提示用户输入修改后的信息。该界面还会将当前用户在数据库中的信息提取出来显示在控件上，以免用户重复输入不需要更改的数据。点击左上角返回按钮可返回至之前界面，点击 “确定”按钮将修改数据库中当前用户的信息，修改完成后将跳转到主界面，并刷新数据，显示用户修改后的星座运势信息。

  <img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/update.jpg" alt="update" style="zoom:25%;" />

  - 点击“绑定人脸”按钮将跳转到人脸预览界面，该界面将会进行人脸识别，若识别到的人脸未与数据库中任何账号绑定，将显示“NOT REGISTER”，此时用户可点击右下角的“注册”按钮来将识别到的人脸与当前登录的账号绑定，下次登录时便可刷脸登录。若识别到的人脸已与数据库中某一账号绑定，将显示“REGISTER xxx”，其中“xxx”代表与该人脸特征绑定的用户名，当检测到的人脸特征与人脸服务器中的人脸特征匹配阈值低于0.8时，将提示“人脸置信度低”。用户可根据需要点击左下角的开关来开启/关闭活体检测功能。

  <img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/face_register.jpg" alt="face_register" style="zoom:25%;" />

  - 点击“删除账号”按钮将弹出窗口对用户进行警告，点击“确定”后将从数据库中删除当前用户的账号信息并跳转到登录界面，点击取消后弹窗将关闭。

  <img src="https://gitee.com/zyl1432397127/pic-go-img/raw/master/img/deleta_account.jpg" alt="deleta_account" style="zoom:25%;" />

  - 点击“退出登录”按钮后将跳转到登录界面，用户可换其他账号进行登录。


