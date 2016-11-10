(ns todos.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [cemerick.url :refer (url url-encode)]
            ))
(enable-console-print!)

(def params (url (.. js/window -location -href)))
(def host-url (str "http://" (:host params) ":" (:port params)))
(def params-q (:query params))
(def past_id (get params-q "past_id"))
(if past_id nil (js/alert "Params past_id not nil!")) 

(def todo-api-url (str host-url "/pasts/" past_id "/navs.json/"))

(defonce todos (r/atom (sorted-map)))

(defonce todos-list-init
  (go (let [response
            (<!
             (http/get todo-api-url
                       {:with-credentials? false
                        :query-params {"since" ""}}))]
        (let [body (:body response) status (:status response)]
          (cond
            (= 200 status) (reset! todos (zipmap  (map :id body) body))
            (= 404 status) (js/alert (str "Not found past" past_id "!"))
            :else (js/alert "Get todo list failure, pelease check the todos api!"))
          ))))

(defn create-todo [text parid body]
  (go (let [response
            (<!
             (http/post todo-api-url
                        {:with-credentials? false
                         :query-params {:content text :parid parid}}))]
        (if (= (:status response) 201)
          (body (:body response))
          (js/alert "Create todo failure!"))
        )))       

(defn ud-url [id] (str host-url "/pasts/" past_id "/navs/" id ".json") ) 

(defn update-todo [id text body]
  (go (let [response
            (<!
             (http/put (ud-url id)
                       {:with-credentials? false
                        :query-params {:content text}}))]
        (body (:status response))
        )))

(defn delete-todo [id body]
  (go (let [response
            (<!
             (http/delete (ud-url id)
                          {:with-credentials? false
                           :query-params {}}))]
        (body (:status response))
        )))


(defonce counter (r/atom 0))

(defn add-todo [text parid]
  (do
    (create-todo
     text
     parid
     #(swap! todos assoc (:id %) {:id (:id %) :content (:content %) :done false}))
    )
  )

(defn toggle [id] (swap! todos update-in [id :done] not))

(defn save [id content]
  (update-todo
   id content
   #(if (= % 204)
      (swap! todos assoc-in [id :content] content)
      (js/alert (str "Update todo " id " failure!")))
   )
  )

(defn delete [id]
  (delete-todo
   id
   #(if (= % 204)
      (swap! todos dissoc id)
      (js/alert (str "Delete todo " id " failure!")))
   )
  )

(defn mmap [m f a] (->> m (f a) (into (empty m))))
(defn complete-all [v] (swap! todos mmap map #(assoc-in % [1 :done] v)))
(defn clear-done [] (swap! todos mmap remove #(get-in % [1 :done])))

(defn todo-input [{:keys [content on-save on-stop]}]
  (let [val (r/atom content)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn [{:keys [id class placeholder]}]
      [:input {:type "text" :value @val
               :id id :class class :placeholder placeholder
               :on-blur save
               :on-change #(reset! val (-> % .-target .-value))
               :on-key-down #(case (.-which %)
                               13 (save)
                               27 (stop)
                               nil)}])))

(defn todo-input-par [{:keys [id content on-save on-stop]}]
  (let [val (r/atom content)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn [{:keys [id class placeholder]}]
      [:input.input-par {:type "text" :value @val
               :id id :class class :placeholder placeholder
               :on-blur save
               :on-change #(reset! val (-> % .-target .-value))
               :on-key-down #(case (.-which %)
                               13 (save)
                               27 (stop)
                               nil)}])))

(def todo-edit (with-meta todo-input
                 {:component-did-mount #(.focus (r/dom-node %))}))

(defn todo-stats [{:keys [filt active done]}]
  (let [props-for (fn [name]
                    {:class (if (= name @filt) "selected")
                     :on-click #(reset! filt name)})]
    [:div
     [:span#todo-count
      [:strong active] " " (case active 1 "item" "items") " left"]
     [:ul#filters
      [:li [:a (props-for :all) "All"]]
      [:li [:a (props-for :active) "Active"]]
      [:li [:a (props-for :done) "Completed"]]]
     (when (pos? done)
       [:button#clear-completed {:on-click clear-done}
        "Clear completed " done])]))

(def new-todo-par
  (fn [id]
    [todo-input-par
     {:id id
      :type "text"
      :placeholder (str "Subneed to be done for " id "?")
      :on-save #(add-todo % id)
      }]
    )
  )

(defn todo-item []
  (let [editing (r/atom false)]
    (fn [{:keys [id done content]}]
      [:li {:class (str (if done "completed ")
                        (if @editing "editing"))}
       [:div.view
        [:label {:on-double-click #(reset! editing true)} content]
        [:button.destroy {:on-click #(delete id)}]
        [:button.reply {:on-click #(set! (.-display (.-style (. js/document (getElementById (str "input-label-id-" id)))) ) "block") }]
        [:label.input-label { :id (str "input-label-id-" id) } (new-todo-par id)]
        ]
       (when @editing
         [todo-edit {:class "edit" :content content
                     :on-save #(save id %)
                     :on-stop #(reset! editing false)}])])))
(defn new-todo []
  [todo-input {:id "new-todo"
               :placeholder "What needs to be done?"
               :on-save add-todo}]
  )


(defn todo-app [props]
  (let [filt (r/atom :all)]
    (fn []
      (let [items (vals @todos)
            done (->> items (filter :done) count)
            active (- (count items) done)]
        [:div
         [:section#todoapp
          [:header#header
           [:h1 "todos tree"]
           (new-todo)
           ]
          (when (-> items count pos?)
            [:div
             [:section#main
              [:ul#todo-list
               (for [todo (filter (case @filt
                                    :active (complement :done)
                                    :done :done
                                    :all identity) items)]
                 ^{:key (:id todo)} [todo-item todo])]]
             [:footer#footer
              [todo-stats {:active active :done done :filt filt}]]])]
         [:footer#info
          [:p "Double-click to edit a todo"]]]))))

(r/render-component [todo-app] (. js/document (getElementById "app")))
