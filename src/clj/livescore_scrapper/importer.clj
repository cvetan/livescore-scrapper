(ns livescore-scrapper.importer
  (:require
    [livescore-scrapper.db.core :as db]
    [livescore-scrapper.util.sitemap :as sitemap]
    [livescore-scrapper.util.mapper :as mapper]
    [clojure.pprint :as pprint]
    ))
:gen-class

(defn import-sports
  "This function imports sports from sitemap"
  []
  (let [sitemap-sports (sitemap/find-sports)
        mapped-sports (vec (map (fn [sport]
                             (mapper/map-sport sport))
                           sitemap-sports))]
    (db/import-sports {:sports mapped-sports})))
