(ns chorduroy-clojure.filterer
  (:use [chorduroy-clojure.core])
  (:require [chorduroy-clojure.position :refer [playable?]]))

(defn frets-from
  [position]
  (map :fret position))

(defn get-position-notes
  [position]
  (->> position
       (remove #(nil? (:fret %)))
       (map #(walk-scale (:open %) (:fret %)))
       set))

(defn sufficient?
  [chord position]
  (let [chord-notes (harmonize chord)
        position-notes (get-position-notes position)]
    (every? position-notes chord-notes)))


(defn root-position?
  [chord position]
  (let [{root :root} chord
        base (->> position
                  (remove #(nil? (:fret %)))
                  first
                  (#(walk-scale (:open %) (:fret %))))]
    (= root base)))

(defn eligible?
  [chord position]
  (and (playable? (frets-from position))
       (sufficient? chord position)
       (root-position? chord position)))
