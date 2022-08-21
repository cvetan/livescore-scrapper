(ns livescore-scrapper.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [livescore-scrapper.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[livescore-scrapper started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[livescore-scrapper has shut down successfully]=-"))
   :middleware wrap-dev})
