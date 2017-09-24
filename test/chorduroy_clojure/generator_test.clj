(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.generator :refer :all]))

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (map :name results)]
    (is (= the-major-and-minor-chords (set chords)))))