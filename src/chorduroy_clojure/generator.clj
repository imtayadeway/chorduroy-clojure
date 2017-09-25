(ns chorduroy-clojure.generator)

(def the-chromatic-scale
  ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(def the-major-and-minor-chords
  (set (for [root the-chromatic-scale
             tonality ["Major" "Minor"]]
         {:root root :tonality tonality} )))

(defn name-for-chord
  [chord]
  (let [{:keys [root tonality]} chord]
    (str root " " tonality)))

(defn walk-scale
  [start degrees]
  (let [start-index (.indexOf the-chromatic-scale start)
        sum (+ start-index degrees)
        index (if (< sum 12) sum (- sum 12))]
    (get the-chromatic-scale index)))

(defn harmonize
  [chord]
  (let [{:keys [root tonality]} chord
        third-degrees (case tonality "Minor" 3 "Major" 4)
        third (walk-scale root third-degrees)
        fifth (walk-scale root 7)]
    #{root third fifth}))

(defn in-harmony?
  [note chord]
  (let [notes (harmonize chord)]
    (some #{note} notes)))

(defn first-fret-in-harmony
  ([chord open]
   (first-fret-in-harmony chord open 0))
  ([chord open fret]
   (let [note (walk-scale open fret)]
     (if (in-harmony? note chord)
       fret
       (recur chord open (inc fret))))))

(defn positions-for-chord
  [chord tuning]
  [(map (partial first-fret-in-harmony chord) tuning)])

(defn generate
  [tuning]
  (map (fn [chord] {:name (name-for-chord chord) :positions (positions-for-chord chord tuning)})
       the-major-and-minor-chords))
