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
    (responses/created {:id (created :id)
                        :name (created :name)
                        :url (created :url)
                        :enabled (created :enabled)
                        :createdAt (str (created :created_at))
                        :updatedAt (str (created :updated_at))})))


(defn get-sports-list
  "This function returns list of saved sports"
  []
  {:body (db/fetch-sports)})

(defn get-sport
  "This function returns single sport"
  [id]
  (println (str "Get sport with ID of " id))
  (def result (db/fetch-sport-by-id {:id id}))
  (if (empty? result)
    (responses/not-found "Sport not found with supplied ID")
    {:body result}))

(defn update-sport
  "This function updates sport"
  [id {:keys [name url enabled]}]
  {:status 204
    :body {}})

(defn delete-sport
  "This service function deletes sport"
  [id]
  (println "Delete sport with ID " id)
  (def result (db/sport-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (responses/not-found "Sport with supplied ID not found")
    (do (db/delete-sport-by-id {:id id})
        {:status 204})))

(defn enable-sport
  "This function enables sport"
  [id]
  (println "Enable sport with ID" id))

(defn disable-sport
  "This function disables sport"
  [id]
  (println "Disable sport with ID" id))