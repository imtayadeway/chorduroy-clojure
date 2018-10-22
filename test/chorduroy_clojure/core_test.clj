(ns chorduroy-clojure.core-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.core :refer :all]))

(def standard-tuning ["E" "A" "D" "G" "B" "E"])

(deftest name-for-chord-test
  (is (= "A Major" (name-for-chord {:root "A" :tonality "Major"})))
  (is (= "A Minor" (name-for-chord {:root "A" :tonality "Minor"}))))

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

(deftest identify-test
  (is (= {:root "E" :tonality "Major"}
         (select-keys (identify [0 2 2 1 0 0] standard-tuning) [:root :tonality])))
  (is (= {:root "A" :tonality "Major"}
         (select-keys (identify [nil 0 2 2 2 0] standard-tuning) [:root :tonality])))
  (is (= {:root "E" :tonality "Dominant 7th"}
         (select-keys (identify [0 2 0 1 3 0] standard-tuning) [:root :tonality])))
  (is (= {:root "C#/Db" :tonality "Minor 7th"}
         (select-keys (identify [nil 4 6 4 5 4] standard-tuning) [:root :tonality])))
  (is (= {:root "E" :tonality "Major 13th"}
         (select-keys (identify [0 2 2 1 2 0] standard-tuning) [:root :tonality])))
  (is (= {:root "E" :tonality "Major 9th"}
         (select-keys (identify [0 2 2 1 0 2] standard-tuning) [:root :tonality]))))
