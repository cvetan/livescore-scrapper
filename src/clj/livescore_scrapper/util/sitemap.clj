(ns livescore-scrapper.util.sitemap
  (:require
    [clojure.xml :as xml]
    [clojure.string :as str]
    [clojure.pprint :as pprint]))

:gen-class

(def sitemap-url "https://www.flashscore.com/sitemap.xml")

(defn parse-xml [url]
  (try (xml/parse url)
       (catch Exception e
         (println
           (str "Caught Exception parsing xml: " (.getMessage e))))))

(defn get-content
  [content]
  (into {} (map (fn [c] [(:tag c) (first (:content c))]) (:content content))))

(defn parse-sitemap
  [url]
  (let [sport (parse-xml url)
        tag (:tag sport)]
    (when sport
      (cond
        (= tag :urlset)
        (map get-content (:content sport))))))

(defn get-url-content
  [sitemap-content]
  (get sitemap-content :loc))

(defn find-sports
  []
  (let [sitemap-content (map get-url-content
                             (parse-sitemap sitemap-url))]
    (filter (fn [entry]
              (= (count (str/split entry #"/")) 4))
            sitemap-content)))

(defn find-competitions
  []
  (let [sitemap-content (map get-url-content
                             (parse-sitemap sitemap-url))]
    (filter (fn [entry]
              (= (count (str/split entry #"/")) 6))
            sitemap-content)))