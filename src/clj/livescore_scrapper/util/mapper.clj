(ns livescore-scrapper.util.mapper
  (:require
    [clojure.string :as str]
    [livescore-scrapper.db.core :as db]))

:gen-class

(defn map-sport
  "This function maps sport from extracted sitemap entry"
  [url]
  (let [segments (str/split url #"/")]
    [(str/capitalize (get segments 3)) url]))

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
        id-map (db/get-url-id-map)
        sport-id (extract-sport-id id-map url)]
    (when-not (nil? sport-id)
      [sport-id
       (str/capitalize (get segments 5))
       url
       (str/capitalize (get segments 4))])))