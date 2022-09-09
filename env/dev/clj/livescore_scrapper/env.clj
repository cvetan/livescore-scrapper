(ns livescore-scrapper.env
  (:require
    [clojure.tools.logging :as log]
    [livescore-scrapper.dev-middleware :refer [wrap-dev]]
    [selmer.parser :as parser]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[livescore-scrapper started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[livescore-scrapper has shut down successfully]=-"))
   :middleware wrap-dev})
