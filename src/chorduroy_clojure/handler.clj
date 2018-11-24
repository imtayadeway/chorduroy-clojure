(ns chorduroy-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :refer :all]
            [org.httpkit.timer :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [chorduroy-clojure.views :as views]
            [chorduroy-clojure.generator :as generator])
  (:gen-class))

(defn results-handler
  [request]
  (let [{{:strs [sixth fifth fourth third second first]} :form-params} request]
    (with-channel request channel
      (loop [id 0]
        (when (< id 10)
          (schedule-task (* id 5000)
                         (send! channel "<p><em>generating....</em></p>" false))
          (recur (inc id))))
      (schedule-task 60000 (close channel))
      (send! channel
             (->> [sixth fifth fourth third second first]
                  generator/generate
                  views/results-page)
             true))))

(defroutes app-routes
  (GET "/" [] (views/index-page))
  (POST "/results" [] results-handler)
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes
                 (assoc-in site-defaults [:security :anti-forgery] false)))

(defn -main
  []
  (let [port (Integer. (or (System/getenv "PORT") 8080))]
    (run-server app {:port port})))
