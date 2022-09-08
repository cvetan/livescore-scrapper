(ns livescore-scrapper.util.playground
  (:require
    [clojure.pprint :as pprint]
    [clojure.string :as str]))

(defn explode-test
  []
  (def teststring "https://www.flashscore.com/football/")
  (pprint/pprint (count (str/split teststring #"/"))))
