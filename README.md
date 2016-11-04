
### 记录ClojureScript与React的学习经历, 包括reagent和om

##### 1. r/atom 
只要修改atom内的任意一值,整个应用的任意一引用到atom的地方,都会底层自动修改掉
```clojure
(defonce counter (r/atom 0))
```
##### 2. r/render-component
渲染模块,可以对cljs纯函数单独测试,单独显示,cljs repl交互式开发
```clojure
(defn todo-app [] [:h1 "test..."])
(r/render-component [todo-app] (. js/document (getElementById "app")))
```
##### 3. cljs.http
通过lambda的回调caller传出 `(:body response)`, 然后通过swap!或者reset!将其值放入atom的原子或者列表里面
```clojure

(ns todos.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defonce todos-list-init
  (go (let [response
            (<!
             (http/get "http://127.0.0.1:3001/todos"
                       {:with-credentials? false
                        :query-params {"since" ""}}))]
        (let [body (:body response)]
          (reset! todos (zipmap  (map :id body) body) )
          ))))

(defn create-todo [text body]
  (go (let [response
            (<!
             (http/post "http://127.0.0.1:3001/todos"
                        {:with-credentials? false
                         :query-params {:title text}}))]
        (body (:body response))
        )))
        
(defn add-todo [text]
  (do
    (create-todo
     text
     #(swap! todos assoc (:id %) {:id (:id %) :title (:title %) :done false}))
    )
  )
  
```
