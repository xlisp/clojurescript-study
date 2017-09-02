(ns highcharts-reagent-gp.app
  (:require [highcharts-reagent-gp.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
