(ns livescore-scrapper.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[livescore-scrapper started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[livescore-scrapper has shut down successfully]=-"))
   :middleware identity})
