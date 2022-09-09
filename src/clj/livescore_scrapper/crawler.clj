(ns livescore-scrapper.crawler
  (:require
    [clojure.string :as str]
    [etaoin.api :as e])
  (:gen-class))

(def driver (e/boot-driver :firefox {:headless true}))

(defn crawl-standings
  "This function will crawl website on the given url"
  [url]
  (e/with-firefox-headless
    driver
    (e/go driver url)
    (e/wait-visible driver {:css "div.tableWrapper"})
    (let [rows (e/query-all driver {:css "div.ui-table__row"})]
      (vec (map (fn [row]
              (str/split (e/get-element-text-el driver row) #"\n")) rows)))))

(e/quit driver)