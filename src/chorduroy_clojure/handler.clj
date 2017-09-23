(ns chorduroy-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [chorduroy-clojure.views :as views]))

(defroutes app-routes
  (GET "/" [] (views/index-page))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes
                 (assoc-in site-defaults [:security :anti-forgery] false)))
