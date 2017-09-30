(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.generator :refer :all]))

(def standard-tuning ["E" "A" "D" "G" "B" "E"])
(def open-g-tuning ["D" "G" "D" "G" "B" "D"])

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

(deftest in-harmony?-test
  (is (in-harmony? "A" {:root "A" :tonality "Major"}))
  (is (in-harmony? "C#/Db" {:root "A" :tonality "Major"}))
  (is (in-harmony? "G#/Ab" {:root "E" :tonality "Major"}))
  (is (not (in-harmony? "B" {:root "A" :tonality "Major"}))))

(deftest positions-for-chord-test
  (let [standard-e-chords (positions-for-chord {:root "E" :tonality "Major"} standard-tuning)
        standard-g-chords (positions-for-chord {:root "G" :tonality "Major"} standard-tuning)
        open-d-chords (positions-for-chord {:root "D" :tonality "Major"} open-g-tuning)
        open-g-chords (positions-for-chord {:root "G" :tonality "Major"} open-g-tuning)]
    (is (some #{[0 2 2 1 0 0]} standard-e-chords))
    (is (some #{[3 2 0 0 0 3]} standard-g-chords))
    (is (some #{[3 2 0 0 3 3]} standard-g-chords))
    (is (some #{[0 2 0 2 3 0]} open-d-chords))
    (is (some #{[nil 0 0 0 0 0]} open-g-chords))
    (is (not-any? #{[0 2 2 1 0 0]} standard-g-chords))
    (is (not-any? #{[nil 0 0 0 0 0]} standard-g-chords))))

(deftest playable?-test
  (is (playable? [0 2 2 1 0 0]))
  (is (not (playable? [0 2 nil 1 0 0])))
  (is (playable? [nil nil 0 2 3 2])))

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (map :name results)
        expected (map name-for-chord the-major-and-minor-chords)]
    (is (= (set expected) (set chords)))))
