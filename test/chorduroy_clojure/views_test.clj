(ns chorduroy-clojure.views-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.views :refer :all]))

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
