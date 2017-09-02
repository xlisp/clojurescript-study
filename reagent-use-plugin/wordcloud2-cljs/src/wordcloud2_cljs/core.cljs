(ns wordcloud2-cljs.core
  (:require [reagent.core :as reagent :refer [atom]]
            [myexterns.wordcloud]))

(defonce app-state (atom {:text "Hello world!"}))
(defonce active-word (atom ""))
(defonce data-list (atom [["foo" 30] ["bar" 60]]))

(defn date-select-did-mount [this]
  (window.WordCloud
   (reagent/dom-node this)
   (clj->js { :list @data-list })
   #(do
      (reset! active-word %)
      (js/alert %)
      )
   )
  )

(defn date-select-update-mount [this]
  (do
    (prn "Do update ...")
    (window.WordCloud
     (reagent/dom-node this)
     (clj->js { :list @data-list })
     #(do
        (reset! active-word %)
        (js/alert %)
        )
     )
    )
  )

(defn date-select-create-class []
  (reagent/create-class {:reagent-render
                         (fn []
                           [:div
                            {:style
                             {:min-width "310px"
                              :max-width "800px"
                              :height    "400px"
                              :margin    "0 auto"}}])
                         :component-did-mount
                         (fn [this]
                           (date-select-did-mount this) )
                         :component-did-update
                         (fn [this]
                           (date-select-update-mount this))
                         }))


(defn hello-world []
  [:h1 (:text @app-state)]
  (date-select-create-class)
  )

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

;; Test=> Change data-list data-list, Not save file, eval (do ...)
(defn test-reset-data-list [] 
  (do
    (reset! data-list [["foo" 50] ["baraa" 60] ["uuu" 122] ["yyy" 22] ["ada" 30]])
    (set! (.-innerHTML (. js/document (getElementById "app"))) "") ;; 暴力delete掉component
    (reagent/render-component [hello-world]
                              (. js/document (getElementById "app")))
    )
  )
