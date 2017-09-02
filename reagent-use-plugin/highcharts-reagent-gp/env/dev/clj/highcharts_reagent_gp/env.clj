(ns highcharts-reagent-gp.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [highcharts-reagent-gp.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[highcharts-reagent-gp started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[highcharts-reagent-gp has shut down successfully]=-"))
   :middleware wrap-dev})
