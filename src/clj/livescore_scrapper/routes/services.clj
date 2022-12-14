(ns livescore-scrapper.routes.services
  (:require
    [livescore-scrapper.competitions :as competitions]
    [livescore-scrapper.middleware.formats :as formats]
    [livescore-scrapper.sports :as sports]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [ring.util.http-response :refer :all]))

(defn service-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
    :swagger {:id ::api}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 coercion/coerce-exceptions-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc true
        :swagger {:info {:title "Livescore scrapper API"
                         :description "This API will be used as proxy to flashscore.com website."}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]
   ["/import"
    {:swagger {:tags ["Import API"]}}

    ["/sports"
     {:post {:summary "This request will trigger sports import from sitemap"
             :responses {204 {:description "Sports imported successfully"}}
             :handler (fn [_]
                        (sports/import))}}]

    ["/competitions"
     {:post {:summary "This request will trigger competitions import from sitemap"
             :responses {204 {:description "Competitions imported successfully"}}
             :handler (fn [_]
                        (competitions/import))}}]]

   ["/sports"
    {:swagger {:tags ["Sports API"]}}

    [""
     {:get {:summary "This request returns list of sports"
            :swagger {:produces ["application/json"]}
            :responses {200 {:description "List of sports"
                             :body [{:id int?
                                     :name string?
                                     :url string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}]}}
            :handler (fn [_]
                       (sports/get-sports-list))}
      :post {:summary "This request creates new sport"
             :swagger {:consumes ["application/json"]
                       :produces ["application/json"]}
             :parameters {:body {:name string?
                                 :url string?
                                 :enabled boolean?}}
             :responses {201 {:description "Newly created sport data"
                              :body {:id int?
                                     :name string?
                                     :url string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}}
                         400 {:description "Invalid create sport request"
                              :body {:errors [string?]
                                     :message string?}}}
             :handler (fn [{{{:keys [name url enabled] :as sport-request} :body} :parameters}]
                        (sports/save-sport sport-request))}}

    ]

    ["/:id"
     {:get {:summary "This request returns single sport by ID"
            :swagger {:produces ["application/json"]}
            :parameters {:path {:id int?}}
            :responses {200 {:description "Sport data"
                             :body {:id int?
                                     :name string?
                                     :url string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}}
                        404 {:description "Sport not found"
                             :body {:message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (sports/get-sport id))}

      :put {:summary "This request updates sport"
            :swagger {:produces ["application/json"]
                      :consumes ["application/json"]}
            :parameters {:body {:name string?
                                 :url string?
                                 :enabled boolean?}
                         :path {:id int?}}
            :responses {204 {:description "Sport updated successfully."}
                        400 {:description "Bad request"
                             :body {:status int?
                                    :errors [string?]
                                    :message string?}}
                        404 {:description "Sport not found"
                             :body {:message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters
                           {{:keys [name url enabled] :as update-request} :body} :parameters}]
                       (sports/update-sport id update-request))}

      :delete {:summary "This request deletes sport"
               :parameters {:path {:id int?}}
               :responses {204 {:description "Sport deleted successfully."}
                           404 {:description "Sport not found"
                                :body {:message string?}}}
               :handler (fn [{{{:keys [id]} :path} :parameters}]
                          (sports/delete-sport id))}

      }]

    ["/:id/enable"
     {:put {:summary "This request will enable sport"
            :parameters {:path {:id int?}}
            :responses {204 {:description "Sport enabled successfully."}
                        404 {:description "Sport not found"
                             :body {:message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (sports/enable-sport id))}}]

    ["/:id/disable"
     {:put {:summary "This request will disable sport"
            :parameters {:path {:id int?}}
            :responses {204 {:description "Sport disabled successfully."}
                        404 {:description "Sport not found"
                             :body {:message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (sports/disable-sport id))}}]]


   ["/competitions"
    {:swagger {:tags ["Competitions API"]}}
    [""
     {:get {:summary "This request returns list of competitions"
            :swagger {:produces ["application/json"]}
            :responses {200 {:description "List of competitions"
                             :body [{:id int?
                                     :sport string?
                                     :name string?
                                     :url string?
                                     :country string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}]}}
            :handler (fn [_]
                       (competitions/get-competitions-list))}
      :post {:summary "This request creates new competition"
             :swagger {:consumes ["application/json"]
                       :produces ["application/json"]}
             :parameters {:body {:sport_id int?
                                 :name string?
                                 :url string?
                                 :country string?
                                 :enabled boolean?}}
             :responses {201 {:description "Newly created competition data"
                              :body {:id int?
                                     :sport string?
                                     :name string?
                                     :url string?
                                     :country string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}}
                         400 {:description "Invalid create competition request"
                              :body {:status int?
                                     :errors [string?]
                                     :message string?}}}
             :handler (fn [{{{:keys [sport_id name url country enabled] :as competition-request} :body} :parameters}]
                        (competitions/save-competition competition-request))}}]

    ["/:id"
     {:get {:summary "This request returns single competition by ID"
            :swagger {:produces ["application/json"]}
            :parameters {:path {:id int?}}
            :responses {200 {:description "Competition data"
                             :body {:id int?
                                    :sport string?
                                    :name string?
                                    :url string?
                                    :country string?
                                    :enabled boolean?
                                    :createdAt string?
                                    :updatedAt string?}}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (competitions/get-competition id))}

      :put {:summary "This request updates competition"
            :swagger {:produces ["application/json"]
                      :consumes ["application/json"]}
            :parameters {:path {:id int?}
                         :body {:sport_id int?
                                 :name string?
                                 :url string?
                                 :country string?
                                 :enabled boolean?}}
            :responses {204 {:description "Competition updated successfully."}
                        400 {:description "Bad request"
                             :body {:status int?
                                    :errors [string?]
                                    :message string?}}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?
                                    }}}
            :handler (fn [{{{:keys [id]} :path} :parameters
                           {{:keys [sport_id name url country enabled] :as update-request} :body} :parameters}]
                       (competitions/update-competition id update-request))}
      :delete {:summary "This request deletes competition"
               :parameters {:path {:id int?}}
               :responses {204 {:description "Competition deleted successfully."}
                           404 {:body {:status int?
                                       :message string?}}}
               :handler (fn [{{{:keys [id]} :path} :parameters}]
                          (competitions/delete-competition id))}

      }]

    ["/{id}/standings"
     {:get {:summary "This request fetches standings for competition"
            :swagger {:produces ["application/json"]}
            :parameters {:path {:id int?}}
            :responses {200 {:description "Competition standings"
                             :body [{:position string?
                                     :team string?
                                     :mp int?
                                     :w int?
                                     :d int?
                                     :l int?
                                     :gd string?
                                     :points int?}]}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (competitions/get-standings id))}}]

    ["/{id}/results"
     {:get {:summary "This request fetches results for competition"
            :swagger {:produces ["application/json"]}
            :parameters {:path {:id int?}}
            :responses {200 {:description "Competition results"
                             :body [{:time string?
                                     :match string?
                                     :finalScore string?
                                     :halftimeScore string?}]}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (competitions/get-results id))}}]

    ["/{id}/enable"
     {:put {:summary "This request enables competition"
            :parameters {:path {:id int?}}
            :responses {204 {:description "Competition successfully enabled"}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (competitions/enable-competition id))}}]

    ["/{id}/disable"
     {:put {:summary "This request disables competition"
            :parameters {:path {:id int?}}
            :responses {204 {:description "Competition successfully disabled"}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (competitions/disable-competition id))}}]]
   ])
