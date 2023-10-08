rwmod混淆器
一种低性能耗损的混淆器，不过它的处理速度很慢。
从rwmod进行混淆，将所有文件提取到一级伪装为文件夹，并使用难以阅读的字典（默认使用Android的字库）进行重命名。
纯在很多bug和兼容性问题。
.ini配置不正确（待修正）

要使用all-units.template，请确保复制对象（仅ini，不包括不加载）的all-units.template与目标的相同。
如：以下场景，便会出现兼容问题。
a.ini copyFrom:a/b.ini
a/b.ini a/all-units.template
诺要避免，请更改名称，并使用copyFrom:在第一个目标复制它。
尽管可以试着重写兼容层，但是这导致明确的分级，混淆效果将大幅下降（这是即为不推荐的，所以有此规则）