map 匹配文件头<map，文件尾部map>写出
较慢

tmx 匹配<xml前的int size 零拷贝写出
快，头文本不为<xml无法处理

gz 仅写出gzip
非常快

zip 使用zip打包的方式，处理zip伪文件夹
快 zip4j

fzp 处理zip伪文件夹（覆盖）
很快 只能处理正常的zip文件

配置文件
/sdcard/rustedWarfare/hex/.ini

searchSize=200
lessRAM=T
encode=utf-8

searchSize
定义1f8b的搜索范围
游戏版本更新时，可能需要提升它，避免错误
lessRAM
少使用内存 T:true F:false
tmx/map/mp2会节约不少内存，但性能下降
encode
tmx/map扫描字节编码

实验版本：
https://cra.lanzouf.com/b0ah1i1eh
密码:00
tool1*.apk

github：
https://github.com/n9tank/rusted-tool