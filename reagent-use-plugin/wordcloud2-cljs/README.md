# wordcloud2-cljs: ClojureScript的标签云

* 支持标签的加点击事件的回调函数
* 支持更新标签云, 当输入的数据变化时

## Overview

FIXME: Write a paragraph about the library/project and highlight its goals.

## Setup && Usage
* dev run
```bash
lein figwheel
```
* code
```clojure
(:require [myexterns.wordcloud])

(window.WordCloud
 (. js/document (getElementById "app"))
 (clj->js { :list [["foo" 30] ["bar" 60]] }) )

```
* production 
```bash
lein clean && lein cljsbuild once min
```
## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
