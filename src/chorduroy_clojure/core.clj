(ns chorduroy-clojure.core
  (:require [chorduroy-clojure.position :refer [playable?]]))

(def strings ["Sixth" "Fifth" "Fourth" "Third" "Second" "First"])
(def the-chromatic-scale
  ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(def intervals
  {"Major" [0 4 7]
   "Minor" [0 3 7]
   "Minor 7th" [0 3 7 10]
   "Dominant 7th" [0 4 7 10]
   "Major 7th" [0 4 7 11]})

(defn name-for-chord
  [chord]
  (let [{:keys [root tonality]} chord]
    (str root " " tonality)))

(defn walk-scale
  [start degrees]
  (when-not (nil? degrees)
    (let [start-index (.indexOf the-chromatic-scale start)
          sum (+ start-index degrees)
          index (if (< sum 12) sum (- sum 12))]
      (get the-chromatic-scale index))))

(def the-diatonic-chords
  (vec (for [root the-chromatic-scale
             tonality ["Major" "Minor" "Minor 7th" "Dominant 7th" "Major 7th"]]
         {:root root :tonality tonality :notes (map (partial walk-scale root) (get intervals tonality))} )))

(defn identify
  [position tuning]
  (let [notes (set (remove nil? (map walk-scale tuning position)))]
    (first (filter #(= notes (set (:notes %))) the-diatonic-chords))))

(defn harmonize
  [chord]
  (let [{:keys [root tonality]} chord
        major-second (walk-scale root 2)
        minor-third (walk-scale root 3)
        major-third (walk-scale root 4)
        augmented-fourth (walk-scale root 5)
        fifth (walk-scale root 7)
        major-sixth (walk-scale root 9)
        minor-seventh (walk-scale root 10)
        major-seventh (walk-scale root 11)]
    (case tonality
      "Major" #{root major-third fifth}
      "Minor" #{root minor-third fifth}
      "Major 6th" #{root major-third major-sixth}
      "Minor 7th" #{root minor-third fifth minor-seventh}
      "Dominant 7th" #{root major-third fifth minor-seventh}
      "Major 7th" #{root major-third fifth major-seventh}
      "Mystic Chord" #{root augmented-fourth minor-seventh major-third major-sixth major-second})))
