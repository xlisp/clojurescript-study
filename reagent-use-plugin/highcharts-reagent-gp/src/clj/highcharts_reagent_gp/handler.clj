(ns highcharts-reagent-gp.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [highcharts-reagent-gp.layout :refer [error-page]]
            [highcharts-reagent-gp.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [highcharts-reagent-gp.env :refer [defaults]]
            [mount.core :as mount]
            [highcharts-reagent-gp.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
