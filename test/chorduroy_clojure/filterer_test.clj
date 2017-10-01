(ns chorduroy-clojure.filterer-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.filterer :refer :all]))

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
