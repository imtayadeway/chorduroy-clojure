(ns chorduroy-clojure.filterer
  (:use [chorduroy-clojure.core]))

(defn sufficient?
  [chord position-notes]
  (let [chord-notes (harmonize chord)]
    (every? (set position-notes) chord-notes)))

(defn root-position?
  [chord position-notes]
  (let [{root :root} chord
        base (->> position-notes (remove nil?) first)]
    (= root base)))

(defn eligible?
  [chord position-notes]
  (and (sufficient? chord position-notes)
       (root-position? chord position-notes)))
