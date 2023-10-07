rwmod混淆器
一种低性能耗损的混淆器，不过它的处理速度很慢。
从rwmod进行混淆，将所有文件提取到一级伪装为文件夹，并使用难以阅读的字典（默认使用Android的字库）进行重命名。
纯在很多bug和兼容性问题。
.ini配置不正确（待修正）

复制不同级的ini且包含不同all-units.template是不兼容的，这违背了规则（所有文件至一级）
如： a.ini copyFrom:a/b.ini
a/b.ini a/all-units.template