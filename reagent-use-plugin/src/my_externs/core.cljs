(ns my-externs.core
  (:require [reagent.core :as r :refer [atom]]
            [cljsjs.jquery]
            [cljsjs.moment]
            [cljsjs.moment.locale.fi]
            [myexterns.daterangepicker]))

(defn daterange [get-value set-value]
  (r/create-class
   {:reagent-render
    (fn []
      [:input {:type "text"
               :name "daterange"
               :class "form-control"
               :value
               (let [value (get-value)]
                 (js/console.log "date value" (pr-str value))
                 (if (nil? value)
                   ""
                   (str (-> value first .toDateString) " through " (-> value second .toDateString))))}])
    :component-did-mount #(->
                           (.dateRangePicker
                            (js/$ (r/dom-node %))
                            (clj->js {:format "dd@@mm@@yyyy"})
                            )
                           (.bind "datepicker-change"
                                  (fn [event obj]
                                    (set-value
                                     [(-> obj .-date1) (-> obj .-date2)]))))
    }))

(defn daterange-atom [val key]
  (daterange #(key @val) #(swap! val assoc key %)))

(defn ^:export component []
  (let [val (r/atom {:name "" :date nil})]
    (fn []
      [:div {:class "container-fluid"}
       [:a {:class "pull-xs-right nav-link" :href "https://github.com/palfrey/herder/issues"} "Report problems"]
       [:h1 "Conventions"]
       [:ul
        
        ]
       [:hr]

       [:div {:class "form-inline"}
        [:div {:class "form-group"}
         [:label {:for "conventionName"} "Convention name"]
         [:input {:id "conventionName"
                  :name "conventionName"
                  :type "text"
                  :placeholder "Convention name"
                  :class "form-control input-md"
                  :value (:name @val)
                  :on-change #(do (prn (str "VAL===>" @val))
                                  (swap! val assoc :name (-> % .-target .-value))
                                  )
                  }]]

        [:div {:class "form-group"}
         [:label {:for "daterange"} "Dates"]
         [daterange-atom val :date]]
        [:button {:type "button"
                  :class "btn btn-primary"
                  :style {:margin-left "5px"}

                  :on-click #(do
                               (.log js/console (pr-str @val)))}
         "Create a new convention"]]])))

(r/render-component [component] (. js/document (getElementById "app")))
