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
    (is (some #{[0 2 2 1 0 0]} (map frets-from standard-e-chords)))
    (is (some #{[3 2 0 0 0 3]} (map frets-from standard-g-chords)))
    (is (some #{[3 2 0 0 3 3]} (map frets-from standard-g-chords)))
    (is (some #{[0 2 0 2 3 0]} (map frets-from open-d-chords)))
    (is (some #{[nil 0 0 0 0 0]} (map frets-from open-g-chords)))
    (is (not-any? #{[0 2 2 1 0 0]} (map frets-from standard-g-chords)))
    (is (not-any? #{[nil 0 0 0 0 0]} (map frets-from standard-g-chords)))))

(deftest playable?-test
  (is (playable? [{:fret 0} {:fret 2} {:fret 2} {:fret 1} {:fret 0} {:fret 0}]))
  (is (not (playable? [{:fret 0} {:fret 2} {:fret nil} {:fret 1} {:fret 0} {:fret 0}])))
  (is (playable? [{:fret nil} {:fret nil} {:fret 0} {:fret 2} {:fret 3} {:fret 2}]))
  (is (not (playable? [{:fret 1} {:fret 2} {:fret 3} {:fret 4} {:fret 5} {:fret 6}])))
  (is (playable? [{:fret nil} {:fret nil} {:fret nil} {:fret nil} {:fret nil} {:fret nil}]))
  (is (playable? [{:fret 0} {:fret 7} {:fret 6} {:fret 7} {:fret 7} {:fret 0}])))

(deftest get-position-notes-test
  (let [e-notes (get-position-notes [{:open "E" :fret 0}
                                     {:open "A" :fret 2}
                                     {:open "D" :fret 2}
                                     {:open "G" :fret 1}
                                     {:open "B" :fret 0}
                                     {:open "E" :fret 0}])
        d-notes (get-position-notes [{:open "E" :fret nil}
                                     {:open "A" :fret nil}
                                     {:open "D" :fret 0}
                                     {:open "G" :fret 2}
                                     {:open "B" :fret 3}
                                     {:open "E" :fret 2}])]
    (is (= #{"E" "G#/Ab" "B"} e-notes))
    (is (= #{"D" "F#/Gb" "A"} d-notes))))

(deftest sufficient?-test
  (let [e-chord {:root "E" :tonality "Major"}
        sufficient-position [{:open "E" :fret 0}
                             {:open "A" :fret 2}
                             {:open "D" :fret 2}
                             {:open "G" :fret 1}
                             {:open "B" :fret 0}
                             {:open "E" :fret 0}]
        insufficient-position [{:open "E" :fret 0}
                               {:open "B" :fret 0}
                               {:open "E" :fret 0}
                               {:open "B" :fret 0}
                               {:open "E" :fret 0}
                               {:open "B" :fret 0}]]
    (is (sufficient? e-chord sufficient-position))
    (is (not (sufficient? e-chord insufficient-position)))))

(deftest root-position?-test
  (let [e-chord {:root "E" :tonality "Major"}
        root-position [{:open "E" :fret 0}
                       {:open "A" :fret 2}
                       {:open "D" :fret 2}
                       {:open "G" :fret 1}
                       {:open "B" :fret 0}
                       {:open "E" :fret 0}]
        second-inversion [{:open "E" :fret nil}
                          {:open "A" :fret 2}
                          {:open "D" :fret 2}
                          {:open "G" :fret 1}
                          {:open "B" :fret 0}
                          {:open "E" :fret 0}]]
    (is (root-position? e-chord root-position))
    (is (not (root-position? e-chord second-inversion)))))

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (map :name results)
        expected (map name-for-chord the-major-and-minor-chords)]
    (is (= (set expected) (set chords)))))
