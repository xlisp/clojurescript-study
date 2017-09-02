(ns highcharts-reagent-gp.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [highcharts-reagent-gp.core-test]))

(doo-tests 'highcharts-reagent-gp.core-test)

