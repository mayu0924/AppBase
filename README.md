AppBase项目简介
--------------------------------------------------------------------------
AppBase是一系列通用类、辅助类、工具类的集合以及第三方sdk的使用，有以下特点：
- **1. 通用性强** 	：常用方法、工具类集合
- **2. 体 积 小** 	：极少引用第三方依赖
- **3. 纯 纯 纯**		：类间独立，极少耦合

其中包括shell命令，静默安装，bitmap处理，文件操作，加密存储器，计数器，均值器，吐司，日志，校验，提示，网络监测等基础功能，以及一些Base64、MD5、Hex、Byte、Number、Dialog、Filed、Class、Package、Telephone、Random等工具类。


1. assit包：辅助
-----
- **AES**：					AES加密
- **MD5**：					加密
- **Averager**：        		均值器， 		添加一些列数字或时间戳，获取其均值。
- **Base64**：          		Base64， 	兼容到android1.0版本的Base64编解码器。
- **Check**：           		检测类， 		检测各种对象是否为null或empty。
- **FlashLight**：      		闪光灯， 		开启、关闭闪光灯。
- **KeyguardLock**：    		锁屏管理， 	锁屏、禁用锁屏，判断是否锁屏
- **LogReader**：       		日志捕获器， 	将某个程序、级别的日志记录到sd卡中，方便远程调试。
- **Network**：         		网络探测器， 	判断网络是否打开、连接、可用，以及当前网络状态。
- **SilentInstaller**： 		安装器， 		静默安装、卸载（仅在root过的手机上）。
- **TimeAverager**：    		计时均值器， 	统计耗时的同时，多次调用可得出其花费时间均值。
- **TimeCounter**：     		计时器， 		顾名思义，统计耗时用的。
- **Toastor**：         		吐司， 		解决多次连续弹出提示问题，可只弹出最后一次，也可连续弹出轻量级提示。
- **WakeLock**：        		屏幕管理， 	点亮、关闭屏幕，判断屏幕是否点亮

2.cache包：缓存【各种数据类型，最终以文件的形式存储】
-----
- **GlobalCache**:			全局的缓存
- **PublicDataCache**：		公用缓存
- **UserCache**：			用户缓存

3. data包：数据处理【SharedPreferences】
-----
- **DataKeeper**：       	加密存储器，持久化工具，可加密，更简单、安全的存储（持久化）、获取数字、布尔值、甚至对象。
- **chipher包**：        	放置加解密辅助类。

4. files包：资源文件存储管理
-----
- **ResourceFileManager**：	应用文件存储管理

5. imgCrop包： 图片裁剪
-----
- **CropImage**：			图片裁剪Activity【缩放 | 自定义比例 | 裁剪 | 保存】

6. io包：文件与IO
-----
- **Charsets**：         	字节编码类
- **FilenameUtils**：    	通用的文件名字、路径操作工具类
- **FileUtils**：        	通用文件操作工具类
- **IOUtils**：          	通用IO流操作工具类
- **StringCodingUtils**：	字符串编码工具类
- **stream包**：         	IO流操作辅助类

7. network包：网络请求
-----
- **network.entity.HTTPConfig**：		网络请求配置【缓存大小 | 缓存名称】
- **network.entity.ParamBuilder**：		参数构造器
- **network.entity.RequestParam**：		请求参数
- **network.entity.ResponseEntity**：	响应实体
- **network.entity.UrlConstants**：		网络请求地址
- **network.LocalData**：				解析本地模拟数据
- **network.MultipartRequest**：			基于Volley实现的【文件上传|多文件上传】
- **network.RequestHelper**：			网络请求
- **UploadUtil.java**：					文件上传

8. orm包：数据库操作【GreenDao|ActiveAndroid】
-----
- **引入第三方开源框架ActiveAndroid.jar,实现数据库操作更简单
- **使用文档：
    http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0104/2255.html
    https://github.com/codepath/android_guides/wiki/ActiveAndroid-Guide
    https://github.com/pardom/ActiveAndroid/downloads 
    http://greendao-orm.com/documentation/how-to-get-started/

9. user包：用户管理
-----
- **CurrentUserManager**：				存储和读取登录用户信息
- **UserInfo**：							用户实体类

10. utils包：常用工具类
-----
- **AndroidUtil**：     android信息， 获取android手机品牌、商家、版本号等信息
- **AppUtil**：         app工具， 检测是否前台运行
- **BitmapUtil**：      位图操作， 拍照，裁剪，圆角，byte、string互转，压缩，放缩，保存等
- **ByteUtil**：        byte工具类
- **ClassUtil**：       类工具， 新建实例，判断类的类型等
- **DialogUtil**：      对话框工具类， 统一全局对话框
- **FieldUtil**：       属性工具类，获取属性值、获取属性泛型类型等
- **FileUtil**：        文件工具类
- **HexUtil**：         16进制工具类，16进制和byte、char像话转化
- **MD5Util**：         MD5工具类
- **NotificationUtil**：通知工具类，便捷显示到顶部栏
- **NumberUtil**：      数字工具类，各种数字安全转化
- **PackageUtil**：     应用程序类，打开、安装，卸载，启动应用以及获取应用信息
- **RandomUtil**：      随机工具类，产生随机string或数字，随机洗牌等
- **ShellUtil**：       shell 命令工具类
- **TelephoneUtil**：   电话工具类，手机号、运营商、IMEI、IMSI等信息
- **VibrateUtil**：     震动工具类，调用系统震动功能
- **Log**：             一个和android系统日志类同名(方便快速替换)的Log工具类，不同的是这个Log具有一键开关功能，方便快速开发打开调试模式。

11. receiver包：通用广播接收器
-----
- **PushReceiver**： 		JPush推送监听
- **PhoneReceiver**：      	电话监听，		来电、去电、通话、挂断的监听以及来去电话号码的获取。
- **SmsReceiver**：       	短信接收器，	升级后可获取短信内容，发送者号码，短信中心号码等。
- **ScreenReceiver**：  	   	屏幕接收器，	可收到屏幕点亮、关闭的广播，并通过回调通知给调用者
- **SmsReceiver**：     		短信接收器，	可获取收到短信的内容，并将内容反馈给调用者

12. service包：通用服务
-----
- **NotificationService**：	通知监听，各类通知服务的监听，获取通知的简述、标题、内容等信息，可以获取诸如QQ、微信、淘宝、浏览器等所有的在通知栏提示的消息。

13. com.google.zxing.client.android包：
-----
- **QRCodeScanActivity.java**：二维码扫描Activity

14. ImageSwitcherActivity.java
-----
- **浏览图片**

15. view包：
-----
- **自定义View**：
--------------- CircleImageView.java【圆图】

				<包名.CircleImageView
                    android:id="@+id/img_avator"
                    android:layout_width="@dimen/my_avatar_size"
                    android:layout_height="@dimen/my_avatar_size"
                    android:layout_centerInParent="true"
                    android:src="@drawable/my_avatar_default_bg" />
                    
--------------- DashedLine.java【虚线】

				<包名.DashedLine
		            android:id="@+id/dash_line"
		            android:layout_width="1dp"
		            android:layout_height="fill_parent"
		            android:layout_marginLeft="20dp"
		          	android:visibility="gone"
		            dash:lineColor="#dddddd" />
		            
--------------- HorizontalListView.java【横向ListView】

				<包名.HorizontalListView
                    android:id="@+id/horizon_listview"
                    android:layout_width="wrap_content"
                    android:layout_height="50dp"
                    android:background="@color/white" >
                    
--------------- VerticalMarqueeTextview.java【垂直滚动TextView】

				<包名.VerticalMarqueeTextview
                    android:id="@+id/scrollText"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:lineSpacingExtra="4dp"
                    android:paddingBottom="5dp"
                    android:paddingLeft="5dp"
                    android:paddingRight="2dp"
                    android:paddingTop="5dp"
                    android:text="即日起凡在车界通预约汽车保养即可免费获赠68项免费检查。更多超值活动敬请期待。"
                    android:textColor="@color/white"
                    android:textSize="12sp" />
                    
--------------- MyBanner.java【无限轮播，加载网络图片】

				<包名.MyBanner
		            android:id="@+id/banner"
		            android:layout_width="@dimen/home_banner_width"
		            android:layout_height="@dimen/home_banner_height"
		            app:pointAutoPlayAble="true"
		            app:pointAutoPlayInterval="10000"
		            app:pointAutoPlayScrollFactor="5"
		            app:pointContainerBackground="@color/translucent"
		            app:pointContainerHeight="10dp"
		            app:pointContainerWidth="match_parent"
		            app:pointEdgeSpacing="10dp"
		            app:pointFocusedImg="@drawable/main_banner_indicator_active"
		            app:pointGravity="bottom|center_horizontal"
		            app:pointSpacing="5dp"
		            app:pointUnfocusedImg="@drawable/main_banner_indicator_default"
		            app:pointVisibility="true" />
		            
16. sdk包：第三方常用sdk的使用：
-----
- **alipay包**：				支付宝支付

		AlipayMent.init().pay();
		
- **wxpay包**：				微信支付

		WeixinPayMent.getInstance().sendPay();

- **gdmap包**：				高德地图定位

		new LocationHelper().startLocation();

- **jpush包**：				极光推送

		JPushController.toggle();
		JPushController.register();
		JPushController.unRegister();
		JPushController.setTag();

- **umeng包**：				友盟自动更新 | 分享 | 第三方登录

		UMSdkManager.init();
		UMSdkManager.share();
		UMSdkManager.login();
		UMSdkManager.logout();
		UMSdkManager.autoCheckUpdate();
		UMSdkManager.forceCheckUpdate();




