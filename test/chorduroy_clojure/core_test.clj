(ns chorduroy-clojure.core-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.core :refer :all]))

(deftest name-for-chord-test
  (is (= "A Major" (name-for-chord {:root "A" :tonality "Major"})))
  (is (= "A Minor" (name-for-chord {:root "A" :tonality "Minor"}))))

(deftest in-harmony?-test
  (is (in-harmony? "A" {:root "A" :tonality "Major"}))
  (is (in-harmony? "C#/Db" {:root "A" :tonality "Major"}))
  (is (in-harmony? "G#/Ab" {:root "E" :tonality "Major"}))
  (is (not (in-harmony? "B" {:root "A" :tonality "Major"}))))

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

(deftest position-to-chart-test
  (let [e-chord [{:open "E" :fret 0}
                 {:open "A" :fret 2}
                 {:open "D" :fret 2}
                 {:open "G" :fret 1}
                 {:open "B" :fret 0}
                 {:open "E" :fret 0}]
        g-chord [{:open "E" :fret 3}
                 {:open "A" :fret 2}
                 {:open "D" :fret 0}
                 {:open "G" :fret 0}
                 {:open "B" :fret 0}
                 {:open "E" :fret 3}]
        d-chord [{:open "E" :fret nil}
                 {:open "A" :fret nil}
                 {:open "D" :fret 0}
                 {:open "G" :fret 2}
                 {:open "B" :fret 3}
                 {:open "E" :fret 2}]]
    (is (= "--0--\n--0--\n--1--\n--2--\n--2--\n--0--" (position-to-chart e-chord)))
    (is (= "--3--\n--0--\n--0--\n--0--\n--2--\n--3--" (position-to-chart g-chord)))
    (is (= "--2--\n--3--\n--2--\n--0--\n--x--\n--x--" (position-to-chart d-chord)))))

(deftest identify-test
  (is (= {:root "E" :tonality "Major"}
         (select-keys (identify [0 2 2 1 0 0] ["E" "A" "D" "G" "B" "E"]) [:root :tonality])))
  (is (= {:root "A" :tonality "Major"}
         (select-keys (identify [nil 0 2 2 2 0] ["E" "A" "D" "G" "B" "E"]) [:root :tonality]))))
