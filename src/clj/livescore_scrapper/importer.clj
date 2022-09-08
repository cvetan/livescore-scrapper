(ns livescore-scrapper.importer
  (:require
    [livescore-scrapper.db.core :as db]
    [livescore-scrapper.util.mapper :as mapper]
    [livescore-scrapper.util.sitemap :as sitemap]
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

(defn import-competitions
  "This function imports competitions from sitemap"
  []
  (let [sitemap-competitions (sitemap/find-competitions)
        mapped-competitions (vec (map (fn [competition]
                                        (mapper/map-competition competition))
                                      sitemap-competitions))]
    (db/import-competitions {:competitions mapped-competitions})))
