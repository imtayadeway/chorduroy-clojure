(ns chorduroy-clojure.core)

(def strings ["Sixth" "Fifth" "Fourth" "Third" "Second" "First"])
(def the-chromatic-scale
  ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(def intervals
  {"Major" {:required [0 4 7]}
   "Minor" {:required [0 3 7]}
   "Minor 7th" {:required [0 3 7 10]}
   "Minor 9th" {:required [0 3 7 14] :optional [10]}
   "Minor 11th" {:required [0 3 7 17] :optional [10 14]}
   "Minor 13th" {:required [0 3 7 20] :optional [10 14 17]}
   "Dominant 7th" {:required [0 4 7 10]}
   "Major 7th" {:required [0 4 7 11]}
   "Major 9th" {:required [0 4 7 14] :optional [11]}
   "Major 11th" {:required [0 4 7 17] :optional [11 14]}
   "Major 13th" {:required [0 4 7 21] :optional [11 14 17]}
   "Mystic Chord" {:required [0 6 10 16 21 26]}})

(defn name-for-chord
  [chord]
  (let [{:keys [root tonality]} chord]
    (str root " " tonality)))

(defn walk-scale
  [start degrees]
  (when-not (nil? degrees)
    (let [start-index (.indexOf the-chromatic-scale start)
          sum (+ start-index degrees)
          index (mod sum 12)]
      (get the-chromatic-scale index))))

(def the-diatonic-chords
  (vec (for [root the-chromatic-scale
             [tonality {:keys [required optional], :or {optional []}}] intervals]
         (let [map-fn (partial walk-scale root)
               required (set (map map-fn required))
               optional (set (map map-fn optional))]
           {:root root :tonality tonality :required required :optional optional}))))

(defn identify
  [position tuning]
  (let [position-notes (remove nil? (map walk-scale tuning position))
        position-root (first position-notes)
        notes (set position-notes)
        identifying-fn (fn [{:keys [root required optional]}]
                         (and (= root position-root)
                              (clojure.set/subset? required notes)
                              (clojure.set/subset? notes (clojure.set/union required optional))))
        candidates (filter identifying-fn the-diatonic-chords)]
    (first candidates)))
