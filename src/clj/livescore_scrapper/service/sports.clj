(ns livescore-scrapper.service.sports)

:gen-class

(defn save-sport
  "This service method will create new sport"
  [{:keys [name url enabled]}]
  {:body {:id 1
          :name name
          :url url
          :enabled enabled
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}
   :status 201})

(defn get-sports-list
  "This sevice function returns list of saved sports"
  []
  {:body [{:id 1
          :name "Football"
          :url "https://www.flashscore.com/football"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}
          {:id 2
          :name "Basketball"
          :url "https://www.flashscore.com/basketball"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}
          {:id 3
          :name "Tennis"
          :url "https://www.flashscore.com/tennis"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}]})

(defn get-sport
  "This service function returns single sport"
  [id]
  {:body {:id id
          :name "Englend Premier League"
          :url "https://www.flashscore.com"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}})

(defn update-sport
  "This service function updates sport"
  [id {:keys [name url enabled]}]
  {:status 204
    :body {}})