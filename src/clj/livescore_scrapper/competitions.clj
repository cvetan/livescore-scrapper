(ns livescore-scrapper.competitions
  (:require
    [livescore-scrapper.db.core :as db]
    [ring.util.http-response :refer :all]
    [clojure.pprint :as pprint]))

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
                    {:id (saved-competition :id)
                     :sport (saved-competition :sport)
                     :name (saved-competition :name)
                     :url (saved-competition :url)
                     :country (saved-competition :country)
                     :enabled (saved-competition :enabled)
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
    (not-found {:status 404
                :message "Competition with supplied ID not found."})
    {:body result}))

(defn delete-competition
  "This function deletes competition"
  [id]
  (println (str "Deleting competition with ID of " id))
  (def result (db/competition-exists-by-id {:id id}))
  (if (= (result :exists) 0)
    (not-found {:status 404
                :message "Competition with supplied ID not found"})
    (do (db/delete-competition {:id id})
        (no-content))))

(defn update-competition
  "This function updates competition"
  [id {:keys [name url sport_id country enabled]}]
  {:status 204
    :body {}})

(defn enable-competition
  "This function enables competition"
  [id]
  (if (= (result :exists) 0)
    (not-found {:status 404
                :message "Competition with supplied ID not found"})
    (do (db/enable-competition {:id id})
        (no-content))))

(defn disable-competition
  "This function enables competition"
  [id]
  (if (= (result :exists) 0)
    (not-found {:status 404
                :message "Competition with supplied ID not found"})
    (do (db/disable-competition {:id id})
        (no-content))))

(defn get-standings
  "This function returns standings for competition"
  [id]
  {:status 200
   :body [{:position 1
           :team "Arsenal"
           :mp 3
           :w 0
           :d 0
           :l 0
           :pts 9}
          {:position 2
           :team "Manchester City"
           :mp 3
           :w 2
           :d 1
           :l 0
           :pts 7}
          {:position 3
           :team "Tottenham"
           :mp 3
           :w 2
           :d 1
           :l 0
           :pts 7}]})

(defn get-results
  "This function returns result for competition"
  [id]
  {:status 200
   :body [{:time "21:00"
           :match "Chelsea - Liverpool"
           :result "2:0"}]})
