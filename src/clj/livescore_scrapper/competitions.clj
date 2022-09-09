(ns livescore-scrapper.competitions
  (:require
    [clojure.pprint :as pprint]
    [livescore-scrapper.crawler :as crawler]
    [livescore-scrapper.db.core :as db]
    [livescore-scrapper.util.mapper :as mapper]
    [ring.util.http-response :refer :all]))

:gen-class

(defn save-competition
  "This service method will create new competition"
  [save-request]
  (println "Saving new competition")
  (def result (db/save-competition save-request))
  (pprint/pprint result)
  (if-not (empty? result)
    (created "" (do (def saved-competition (db/fetch-competition-by-id result))
                    (pprint/pprint saved-competition)
                    {:id        (saved-competition :id)
                     :sport     (saved-competition :sport)
                     :name      (saved-competition :name)
                     :url       (saved-competition :url)
                     :country   (saved-competition :country)
                     :enabled   (saved-competition :enabled)
                     :createdAt (str (saved-competition :createdat))
                     :updatedAt (str (saved-competition :updatedat))}))))

(defn get-competitions-list
  "This sevice function returns list of saved competitions"
  []
  {:body (db/fetch-competitions)})

(defn get-competition
  "This function returns single sport"
  [id]
  (println (str "Get competition with ID of " id))
  (def result (db/fetch-competition-by-id {:id id}))
  (if (empty? result)
    (not-found {:status  404
                :message "Competition with supplied ID not found."})
    {:body result}))

(defn delete-competition
  "This function deletes competition"
  [id]
  (println (str "Deleting competition with ID of " id))
  (def result (db/competition-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (not-found {:status  404
                :message "Competition with supplied ID not found"})
    (do (db/delete-competition {:id id})
        (no-content))))

(defn update-competition
  "This function updates competition"
  [id update-request]
  (def result (db/competition-exists-by-id {:id id}))
  (pprint/pprint result)
  (pprint/pprint update-request)
  (if (= (result :exists) 0)
    (not-found {:status  404
                :message "Competition with supplied ID not found"})
    (do (db/update-competition (merge {:id id} update-request))
        (no-content))))

(defn enable-competition
  "This function enables competition"
  [id]
  (def result (db/competition-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (not-found {:status  404
                :message "Competition with supplied ID not found"})
    (do (db/enable-competition {:id id})
        (no-content))))

(defn disable-competition
  "This function enables competition"
  [id]
  (def result (db/competition-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (not-found {:status  404
                :message "Competition with supplied ID not found"})
    (do (db/disable-competition {:id id})
        (no-content))))

(defn get-standings
  "This function returns standings for competition"
  [id]
  (let [result (db/get-competition-url {:id id})]
    (pprint/pprint result)
    (if (nil? result)
      (not-found {:status  404
                  :message "Competition with supplied ID not found"})
      (let [standings (crawler/crawl-standings (str (result :url) "standings/"))
            mapped-standings (vec (map (fn [row]
                                         (mapper/map-standings-row row))
                                       standings))]
        {:body mapped-standings}))))

(defn get-results
  "This function returns result for competition"
  [id]
  (let [result (db/get-competition-url {:id id})]
    (pprint/pprint result)
    (if (nil? result)
      (not-found {:status  404
                  :message "Competition with supplied ID not found"})
      (let [results (crawler/crawl-results (str (result :url) "results/"))
            mapped-results (vec (map (fn [row]
                                         (mapper/map-results-row row))
                                       results))]
        {:body mapped-results}))))
