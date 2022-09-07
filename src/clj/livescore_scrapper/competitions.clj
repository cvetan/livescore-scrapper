(ns livescore-scrapper.competitions)

:gen-class

(defn save-competition
  "This service method will create new competition"
  [{:keys [name url sport_id country enabled]}]
  {:body {:id 1
          :name name
          :sport "Football"
          :url url
          :country country
          :enabled enabled
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}
   :status 201})

(defn get-competitions-list
  "This sevice function returns list of saved competitions"
  []
  {:body [{:id 1
          :name "England Premier League"
          :sport "Football"
          :url "https://www.flashscore.com/football/england/premier-league"
          :country "England"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}
          {:id 2
          :name "italy Serie A"
          :sport "Football"
          :url "https://www.flashscore.com/football/italy/serie-a"
          :country "Italy"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}
          {:id 3
          :name "Spain La Liga"
          :sport "Football"
          :url "https://www.flashscore.com/football/spain/la-liga"
          :country "Spain"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}]})

(defn get-competition
  "This service function returns single sport"
  [id]
  {:body {:id id
          :name "England Premier League"
          :sport "Football"
          :url "https://www.flashscore.com/football/england/premier-league"
          :country "England"
          :enabled true
          :createdAt "2022-08-23T23:3000"
          :updatedAt "2022-08-23T23:3000"}})

(defn update-competition
  "This function updates competition"
  [id {:keys [name url sport_id country enabled]}]
  {:status 204
    :body {}})

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
