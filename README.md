
### 记录ClojureScript与React的学习经历, 包括reagent和om

###### 1. r/atom 
只要修改atom内的任意一值,整个应用的任意一引用到atom的地方,都会底层自动修改掉
```clojure
(defonce counter (r/atom 0))
```
