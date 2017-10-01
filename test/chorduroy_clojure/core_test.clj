(ns chorduroy-clojure.core-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.core :refer :all]))

(deftest walk-scale-test
  (is (= "A#/Bb" (walk-scale "A" 1)))
  (is (= "B" (walk-scale "A" 2)))
  (is (= "A" (walk-scale "G#/Ab" 1)))
  (is (= "G#/Ab" (walk-scale "E" 4))))

(deftest harmonize-test
  (is (= #{"C" "E" "G"} (harmonize {:root "C" :tonality "Major"})))
  (is (= #{"C" "D#/Eb" "G"} (harmonize {:root "C" :tonality "Minor"})))
  (is (= #{"G" "B" "D"} (harmonize {:root "G" :tonality "Major"})))
  (is (= #{"E" "G#/Ab" "B"} (harmonize {:root "E" :tonality "Major"}))))
