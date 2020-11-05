(ns chorduroy-clojure.core
  (:require [clojure.set]))

(def strings ["Sixth" "Fifth" "Fourth" "Third" "Second" "First"])
(def the-chromatic-scale
  ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(def the-chromatic-scale-index
  (zipmap the-chromatic-scale (range)))

(def intervals
  {"Major" {:required [0 4 7]}
   "Major 6th" {:required [0 4 7 9]}
   "Major 6th + 9" {:required [0 4 7 9 14]}
   "Major 6th + #11" {:required [0 4 7 9 18] :optional [14]}
   "Major 7th" {:required [0 4 7 11]}
   "Major 7th + 9" {:required [0 4 7 11 14]}
   "Major 7th + #11" {:required [0 4 7 11 18] :optional [14]}
   "Major 7th + 13" {:required [0 4 7 11 21] :optional [14 18]}
   "Major 9th" {:required [0 4 7 14] :optional [11]}
   "Major 11th" {:required [0 4 7 17] :optional [11 14]}
   "Major 13th" {:required [0 4 7 21] :optional [11 14 17]}

   "Minor" {:required [0 3 7]}
   "Minor 6th" {:required [0 3 7 9]}
   "Minor 6th + 9" {:required [0 3 7 9 14]}
   "Minor 6th + 11" {:required [0 3 7 9 17] :optional [14]}
   "Minor 6th + #11" {:required [0 3 7 9 18] :optional [14]}
   "Minor 6th + ♮7" {:required [0 3 7 9 11]}
   "Minor 7th" {:required [0 3 7 10]}
   "Minor 7th + b9" {:required [0 3 7 10 13]}
   "Minor 7th + 9" {:required [0 3 7 10 14]}
   "Minor 7th + 11" {:required [0 3 7 10 17] :optional [14]}
   "Minor 7th (b5)" {:required [0 3 6 10]}
   "Minor 7th (b5) + b9" {:required [0 3 6 10 13]}
   "Minor 7th (b5) + 9" {:required [0 3 6 10 14]}
   "Minor 7th (b5) + 11" {:required [0 3 6 10 17] :optional [14]}
   "Minor 7th (b5) + #5" {:required [0 3 6 8 10]}
   "Minor 9th" {:required [0 3 7 14] :optional [10]}
   "Minor 11th" {:required [0 3 7 17] :optional [10 14]}
   "Minor 13th" {:required [0 3 7 20] :optional [10 14 17]}

   "Augmented 7th" {:required [0 4 8 10]}
   "Augmented Major 7th" {:required [0 4 8 11]}
   "Augmented Major 7th + 9" {:required [0 4 8 11 14]}
   "Augmented Major 7th + #11" {:required [0 4 8 11 18] :optional [14]}
   "Diminished 7th" {:required [0 3 6 9]}
   "Diminished 7th + 9" {:required [0 3 6 9 14]}
   "Diminished 7th + 11" {:required [0 3 6 9 17] :optional [14]}
   "Diminished 7th + #5" {:required [0 3 6 8 9]}
   "Diminished 7th + ♮7" {:required [0 3 6 9 11]}
   "Dominant 7th" {:required [0 4 7 10]}
   "Dominant 7th + b9" {:required [0 4 7 10 13]}
   "Dominant 7th + 9" {:required [0 4 7 10 14]}
   "Dominant 7th + #9" {:required [0 4 7 10 15]}
   "Dominant 7th + #11" {:required [0 4 7 10 18] :optional [14]}
   "Dominant 7th + b13" {:required [0 4 7 10 20] :optional [14 18]}
   "Dominant 7th + 13" {:required [0 4 7 10 21] :optional [14 18]}
   "Dominant 7th (b5)" {:required [0 4 6 10]}
   "Dominant 7th (b5) + b9" {:required [0 4 6 10 13]}
   "Dominant 7th (b5) + 9" {:required [0 4 6 10 14]}
   "Dominant 7th (b5) + #9" {:required [0 4 6 10 15]}
   "Dominant 7th (b5) + b13" {:required [0 4 6 10 20] :optional [14]}
   "Dominant 7th (+5)" {:required [0 4 8 10]}
   "Dominant 7th (+5) + b9" {:required [0 4 8 10 13]}
   "Dominant 7th (+5) + 9" {:required [0 4 8 10 14]}
   "Dominant 7th (+5) + #9" {:required [0 4 8 10 15]}
   "Dominant 7th (+5) + #11" {:required [0 4 8 10 18] :optional [14]}
   "Minor/Major 7th" {:required [0 3 7 11]}
   "Minor/Major 7th + 9" {:required [0 3 7 11 14]}
   "Minor/Major 7th + 11" {:required [0 3 7 11 17] :optional [14]}
   "Minor/Major 7th + #11" {:required [0 3 7 11 18] :optional [14]}
   "Half-Diminished 7th" {:required [0 3 6 10]}

   "Mystic Chord" {:required [0 6 10 16 21 26]}})

(defn name-for-chord
  [chord]
  (let [{:keys [root tonality]} chord]
    (str root " " tonality)))

(defn walk-scale
  [start degrees]
  (when-not (nil? degrees)
    (let [start-index (the-chromatic-scale-index start)
          sum (+ start-index degrees)
          index (mod sum 12)]
      (get the-chromatic-scale index))))

(def the-diatonic-chords
  (group-by :root
            (vec (for [root the-chromatic-scale
                       [tonality {:keys [required optional], :or {optional []}}] intervals]
                   (let [map-fn (partial walk-scale root)
                         required (set (map map-fn required))
                         optional (set (map map-fn optional))
                         permitted (clojure.set/union required optional)]
                     {:root root :tonality tonality :required required :permitted permitted})))))

(defn identify
  [position tuning]
  (let [position-notes (remove nil? (map walk-scale tuning position))
        position-root (first position-notes)
        notes (set position-notes)
        candidates (the-diatonic-chords position-root)
        identifying-fn (fn [{:keys [required permitted]}]
                         (and (clojure.set/subset? required notes)
                              (clojure.set/subset? notes permitted)))]
    (some #(when (identifying-fn %) %) candidates)))
