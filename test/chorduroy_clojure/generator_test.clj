(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.generator :refer :all]))

(def standard-tuning ["E" "A" "D" "G" "B" "E"])

(deftest test-chord-entries
  (let [results (generate standard-tuning)
        chords (map :name results)]
    (is (= ["A major"] chords))))
