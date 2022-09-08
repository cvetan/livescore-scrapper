(ns livescore-scrapper.util.mapper
  (:require
    [clojure.string :as str]
    [clojure.pprint :as pprint]))

:gen-class

(defn map-sport
  "This function maps sport from extracted sitemap entry"
  [url]
  (let [segments (str/split url #"/")]
    {:name (str/capitalize (get segments 3))
     :url url}))

(defn map-competition
  "This function maps sport from extracted sitemap entry"
  [url]
  (let [segments (str/split url #"/")]
    {:sport_id "TODO"
     :name (str/capitalize (get segments 5))
     :url url
     :country (str/capitalize (get segments 4))}))
