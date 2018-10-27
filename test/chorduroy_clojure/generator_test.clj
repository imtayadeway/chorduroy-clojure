(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.core :refer :all]
            [chorduroy-clojure.generator :refer :all]))

(deftest generate-test
  (let [results (generate ["E" "A" "D" "G" "B" "E"])]
    (is (some #{[0 2 2 1 0 0]} (get results "E Major")))
    (is (some #{[0 2 2 0 0 0]} (get results "E Minor")))
    (is (some #{[0 2 0 1 3 0]} (get results "E Dominant 7th")))
    (is (some #{[3 2 0 0 0 3]} (get results "G Major")))
    (is (some #{[nil nil 0 2 3 2]} (get results "D Major")))
    (is (some #{[nil 3 2 0 0 0]} (get results "C Major 7th")))))
