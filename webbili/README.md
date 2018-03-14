# Redrock-Assignment_Winter
红岩的大一寒假作业.



# INFO

## 用户接口

### /sign/register

简介: 用户注册.

请求格式: JSON
请求方法: POST

参数:

> username:   		用户名
> password:   		密码
> email:      		邮箱
> code:       		验证码(小写)

返回JSON:

> result: 		codeError		验证码错误
> ​			success			注册成功
> ​			userExist		用户名已存在



### /sign/isUserHas

简介: 检验用户是否存在.

请求格式: JSON
请求方法: GET

参数:

> username:   		用户名

返回JSON:

> result: 		userExist			用户已存在
> ​			canRegister			可以注册	





### /sign/login

简介: 用户登录.

请求格式: JSON
请求方法: POST

参数:

> username:   		用户名
> password:   		密码
> email:      		邮箱
> code:       		验证码(小写)

返回JSON:

> result: 		codeError			验证码错误
> ​			success				登录成功
> ​			passwordError		密码错误
> ​			resultError			输入错误



### /sign/getCode

简介: 获取BASE64传输的图片验证码.

请求格式: 无
请求方法: GET/POST

参数:

> 无

返回JSON:

> BASE64编码的图片流



### /up/upVideo

简介: 上传视频.

请求格式: multipart/form-data
请求方法: POST

参数:

> jwt

返回JSON:

> result(error): 		signError		标签错误
> ​					delete			删除新建的文件夹
> result(success)		success			上传成功
> ​					文件流			视频文件
> ​					videoId			视频ID



### /up/upCover

简介: 上传封面.

请求格式: multipart/form-data
请求方法: POST

参数:

> jwt

返回JSON:

> result(error): 		signError		标签错误
> ​					delete			删除新建的文件夹
> result(success)		success			上传成功
> ​					图片流			封面图片



### /up/stopUp

简介: 停止上传视频.

请求格式: 普通键值对
请求方法:GET

参数:

> jwt
> filename	视频文件名

返回JSON:

> result：		success			停止上传成功
> videoId			视频ID



### /up/upVideoInfo

简介: 更新视频信息.

请求格式: 普通键值对
请求方法: POST

参数:

> videoId		视频id
> title			视频标题
> summary	视频简介
> div			视频分区
> videoUrl		视频路径

返回JSON:

> result：		imgError			未检测到封面
> ​			success				成功上传
> ​			formatError			视频格式错误



### /info/getVideoById

简介: 通过视频ID搜索视频.

请求格式: 普通键值对
请求方法: GET

参数:

> Id:		视频ID

返回JSON:

> result: 		success    	搜索成功
> ​			error		搜索失败
> ​			视频信息   



### /info/getVideoByUserId

简介: 通过用户ID搜索视频.

请求格式: 普通键值对
请求方法: GET

参数:

> userId:		用户ID

返回JSON:

> result: 		success    	搜索成功
> ​			error		搜索失败
> ​			list   		视频信息



### /info/getVideoByKeyTitle

简介: 通过关键字搜索视频.

请求格式: 普通键值对
请求方法: GET

参数:

> title:		title关键字

返回JSON:

> result: 		success    	搜索成功
> ​			error		搜索失败
> ​			list   		视频信息



### /info/getVideoByTime

简介: 通过时间搜索视频.

请求格式: 无
请求方法: GET

参数:

> 

返回JSON:

> result: 		list   		视频信息



### /info/getUserById

简介: 通过ID搜索用户.

请求格式: 普通键值对
请求方法: GET

参数:

> userId:		用户ID

返回JSON:

> result: 		success    	搜索成功
> ​			error		搜索失败
> ​			用户信息



### /info/getDivCnt

简介:获取各个分区的视频数量.

请求格式: 无
请求方法: GET

参数:

> 

返回JSON:

> result: 		fatherDiv :	    分区名
> ​			nums:		    数量



### /info/getExtraInfo

简介:获取其他信息.

请求格式: 普通键值对
请求方法:GET

参数:

> jwt
> username:	用户名

返回JSON:

> result: 	  signError：	标签错误	
> ​		  用户信息：
> ​					签名，注册日期，邮箱，是否管理员	



### /info/updateBaseInfo

简介:更改及基本信息(邮箱和签名).

请求格式: 普通键值对
请求方法: POST

参数:

> jwt
> email:		邮箱
> sign:		签名
> filename:	文件名

返回JSON:

> result: 	  signError：	标签错误	
> ​		  success：	更改成功



### /info/upHeadImg

简介:上传头像.

请求格式: multipart/form-data
请求方法: POST

参数:

> jwt
> path:	头像路径

返回JSON:

> result: 	  signError：	标签错误			



### /manage/deleteVideo

简介: 管理员删除视频.

请求格式: 普通键值对
请求方法: GET

参数:

> jwt
> videoId:		用户ID

返回JSON:

> result: 		success    	删除成功
> ​			error		删除失败



### /manage/putSQL

简介: 管理员执行SQL语句.

请求格式: 普通键值对
请求方法: POST

参数:

> jwt
> sql

返回JSON:

> result: 		success    			执行成功
> ​			sqlError				sql语句错误
> ​			permissionError		非管理员



### /manage/upHits

简介: 点击量自增.

请求格式: 普通键值对
请求方法: GET

参数:

> videoId：	视频ID

返回JSON:

> result: 无