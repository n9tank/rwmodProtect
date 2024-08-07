# rwmodProtect

+ rwmodProtect 是一个专为Rusted Warfare（铁锈战争）设计的mod混淆器。它以较低的性能代价提供较好的防护效果和兼容性。

## 功能
- [x] 伪造文件夹<br>将文件伪装为文件夹（这不能对音乐文件使用），使常规程序，无法正常浏览文件。如果仅剔除伪文件夹，依然需要封包或修正。
- [x] 重构文件树<br>移除明显的关系树，并使用特殊字符干扰阅读，这可能对性能有所改善。
- [x] 精简文件<br>移除无用文件和代码，并使用最大压缩，这将改善占用和性能，不过由于混淆的屎山实现，这可能生成一些可以避免的代码。
- [ ] 更多暂不支持

## 兼容性
- [x] 解析兼容<br>已完成对ini解析的主要兼容。
- [x] 宏支持<br>已完成对宏的主要兼容与运算。
- [x] copyFrom<br>已完成对copyFrom的主要兼容。
- [x] 路径兼容<br>已完成对路径的主要兼容。
- [x] 地图与音乐文件<br>已完成对外部文件的主要兼容
- [x] all-units.template<br>all-units.template优先于copyFrom。使用copyFrom复制ini，不会被追加ini持有的all-units.template。(lite不予支持）
- [x] @copyFrom_skipThisSection<br>@copyFrom_skipThisSection定义为false或0时拿回之前的所有键，反之丢弃。(lite不予支持）
- [ ] 不规范的持有<br>持有mod-info.txt，背景音乐，地图以及地图图像，使打包出错或造成多次复制，以及被修改的键。
- [ ] 非法路径<br>使用"/"或"\\"作为路径头，这导致游戏加载出现bug，使加载结果不一致，新的版本没有多次匹配检查，这可能引发异常。
- [x] 重排序<br>由于屎山代码，对多态值进行强制衍射，这可能引起重排序，特别是action的显示循序、autoTrigger和autoTriggerOnEvent事件链的执行循序。
- [ ] 不检查的内容<br>为了性能移除了一些不必要的检查，这些不和法的文件将引发异常。

请避免多态，对宏以及多态资源的实现基于暴力衍射，这可能引起一些兼容问题和格外的性能代价。

## 配置
配置路径移动到/sdcard/Android/data/rust.rwTool/files，避免卸载残留。
* .ini<br>默认的加载配置，这将覆盖安装包内的加载配置。
* .txt<br>默认的加载字典，每行一项，这将覆盖默认的字符计算。(lite不予支持）
* lib.zip<br>默认的加载依赖，用于copyFrom:CORE:实现。

## 重排序规则 (lite不予支持）
为避免重排序导致出现的兼容问题，请在zip文件根目录放置规则文件<br>rules.md
<pre>=action
a/=action,hiddenAction
b/c.ini=turret,leg_keep</pre>

## 开发规则
为解决兼容问题和获取原生性能，你可以在此规则的基础上开发你的MOD。<br>诺要彻底解决此问题，需重构屎山实现虚拟宏和多态引索。

* 任何包含资源的键不能为多态值
  * 此值不能使用宏，即不能包含“${}”。
  * 此值不能进行多态的路径，两个文件复制同一份文件，它们的父路径不同，使值指向两份文件。