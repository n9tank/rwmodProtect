铁锈战争 rustedWarfare rwmod混淆器
一种低性能耗损的混淆器，不过它的处理速度很慢。
从rwmod进行混淆，将所有文件提取到一级伪装为文件夹，并使用难以阅读的字典（默认使用Android的字库）进行重命名。
纯在很多bug和兼容性问题。
.ini配置不正确（待修正）

已编译的测试程序：https://cra.lanzouj.com/b0agtlmih 密码:00

已尝试完全兼容all-units.template，不保证可用。

不要使用 [\\/]+ 作为根路径，这会导致游戏的一些bug，且抛错。
不要使用不同字符，相同路径来绕过重复检查器，这会让它不工作，而出错。
如：a/b a\b

考虑到开发规范，一些特殊处理没有必要性，所以。
tmx的名称不能相同，这将抛错。
ini使用的资源不能为（不区分大小）mod-info.txt，*_map.png，*.tmx，${music.sourceFolder}/*.ogg。

不要${a.b}的格式引用图像资源，该内容的追踪器尚未实现。

已添加多线程支持，对于非注册的加载对象是单线程的，你可以在配置文件中[set]cust中定义添加自定义加载对象，以支持多线程。

由于luke的global存在bug，因此请所有的global放在[core]下，避免兼容问题。

文件：user.dir/
.ini 配置
lib.zip 加载依赖，用于copyFrom:CORE:

命令：
<rwmod> 默认rwmod混淆(*_r.rwmod)
lib <apk> 制作依赖在当前目录(${user.dir}/.zip)