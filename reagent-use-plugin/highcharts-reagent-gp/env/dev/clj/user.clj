(ns user
  (:require [mount.core :as mount]
            [highcharts-reagent-gp.figwheel :refer [start-fw stop-fw cljs]]
            highcharts-reagent-gp.core))

(defn start []
  (mount/start-without #'highcharts-reagent-gp.core/repl-server))

(defn stop []
  (mount/stop-except #'highcharts-reagent-gp.core/repl-server))

(defn restart []
  (stop)
  (start))


