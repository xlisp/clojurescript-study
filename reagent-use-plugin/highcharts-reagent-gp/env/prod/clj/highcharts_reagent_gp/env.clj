(ns highcharts-reagent-gp.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[highcharts-reagent-gp started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[highcharts-reagent-gp has shut down successfully]=-"))
   :middleware identity})
