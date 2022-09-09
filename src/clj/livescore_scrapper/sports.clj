(ns livescore-scrapper.sports
  (:require
    [livescore-scrapper.db.core :as db]
    [livescore-scrapper.importer :as importer]
    [livescore-scrapper.util.responses :as responses]
    [ring.util.http-response :refer :all]))

(defn save-sport
  "This service method will create new sport"
  [{:keys [name url enabled]}]
  (def result (db/create-sport! {:name name
                                  :url url
                                  :enabled enabled}))
  (if-not (empty? result)
    (created "" {:id (result :id)
                 :name (result :name)
                 :url (result :url)
                 :enabled (result :enabled)
                 :createdAt (str (result :created_at))
                 :updatedAt (str (result :updated_at))})))


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
  (println "Update sport with ID" id)
  (def result (db/sport-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (responses/not-found "Sport with supplied ID not found")
    (do (db/update-sport {:id id
                          :name name
                          :url url
                          :enabled enabled})
        (no-content))))

(defn delete-sport
  "This service function deletes sport"
  [id]
  (println "Delete sport with ID " id)
  (def result (db/sport-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (responses/not-found "Sport with supplied ID not found")
    (do (db/delete-sport-by-id {:id id})
        (no-content))))

(defn enable-sport
  "This function enables sport"
  [id]
  (println "Enable sport with ID" id)
  (def result (db/sport-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (responses/not-found "Sport with supplied ID not found")
    (do (db/enable-sport {:id id})
        (no-content))))

(defn disable-sport
  "This function disables sport"
  [id]
  (println "Disable sport with ID" id)
  (def result (db/sport-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (responses/not-found "Sport with supplied ID not found")
    (do (db/disable-sport {:id id})
        (no-content))))

(defn import
  "This function will run import of sports from sitemap"
  []
  (importer/import-sports)
  (no-content))