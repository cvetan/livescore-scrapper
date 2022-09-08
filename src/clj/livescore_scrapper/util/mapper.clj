(ns livescore-scrapper.util.mapper
  (:require
    [livescore-scrapper.db.core :as db]
    [clojure.string :as str]
    [clojure.pprint :as pprint]))

:gen-class

(defn map-sport
  "This function maps sport from extracted sitemap entry"
  [url]
  (let [segments (str/split url #"/")]
    {:name (str/capitalize (get segments 3))
     :url  url}))

(defn extract-sport-id
  "This function maps URL to sport ID if matching found"
  [id-map url]
  (let [matching (vec (filter (fn [sport]
                            (str/includes? url (sport :url)))
                          id-map))]
    (when-not (empty? matching)
      ((first matching) :id))))

(defn map-competition
  "This function maps sport from extracted sitemap entry"
  [url]
  (let [segments (str/split url #"/")
        id-map (db/get-url-id-map)]
    {:sport_id (extract-sport-id id-map url)
     :name     (str/capitalize (get segments 5))
     :url      url
     :country  (str/capitalize (get segments 4))}))