(ns chorduroy-clojure.filterer-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.filterer :refer :all]))

(deftest sufficient?-test
  (let [e-chord {:root "E" :tonality "Major"}]
    (is (sufficient? e-chord ["E" "B" "E" "G#/Ab" "B" "E"]))
    (is (not (sufficient? e-chord ["E" "B" "E" nil nil nil])))))

(deftest root-position?-test
  (let [e-chord {:root "E" :tonality "Major"}]
    (is (root-position? e-chord ["E" "B" "E" "G#/Ab" "B" "E"]))
    (is (not (root-position? e-chord [nil "B" "E" "G#/Ab" "B" "E"])))))
