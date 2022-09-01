(ns livescore-scrapper.service.sports
  (:require
    [livescore-scrapper.db.core :as db]
    [clojure.pprint :as pprint]
    [ring.util.http-response :as response]
    [livescore-scrapper.util.responses :as responses]))

(defn save-sport
  "This service method will create new sport"
  [{:keys [name url enabled]}]
  (def created (db/create-sport! {:name name
                                  :url url
                                  :enabled enabled}))
  (if-not (empty? created)
    {:body created}))


(defn get-sports-list
  "This sevice function returns list of saved sports"
  []
  {:body (db/fetch-sports)})

(defn get-sport
  "This service function returns single sport"
  [id]
  (println (str "Get sport with ID of " id))
  (def result (db/fetch-sport-by-id {:id id}))
  (if (empty? result)
    (responses/not-found "Sport not found with supplied ID")
    {:body result}))

(defn update-sport
  "This service function updates sport"
  [id {:keys [name url enabled]}]
  {:status 204
    :body {}})