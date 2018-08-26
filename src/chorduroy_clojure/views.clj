(ns chorduroy-clojure.views
  (:use [hiccup core page form]))
(require '[chorduroy-clojure.core :refer :all])

(defn index-page
  []
  (html5
   [:head
    [:title "Chorduroy"]]
   [:body
    (form-to [:post "/results"]
             (for [string strings]
               (list (label {:for (str string "_String")} (str string "_String") (str string " String"))
                     (drop-down {:id string :name (clojure.string/lower-case string)} string the-chromatic-scale)))
             (submit-button {:name "commit" :value "Submit" :data-disable-with "Submit" :type "Submit"} "Submit"))]))

(defn results-page
  [results]
  (html5
   [:head
    [:title "Chorduroy"]]
   [:body
    (for [[name positions] results]
      (list [:h2 name]
            [:table
             [:tr
              (for [position (sort-by #(reduce + (remove nil? %)) positions)]
                (list [:td
                       [:pre (position/to-chart position)]]
                      [:td]))]]))]))
