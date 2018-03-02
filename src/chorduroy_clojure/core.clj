(ns chorduroy-clojure.core)

(def strings ["Sixth" "Fifth" "Fourth" "Third" "Second" "First"])
(def the-chromatic-scale
  ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(def the-diatonic-chords
  (vec (for [root the-chromatic-scale
             tonality ["Major" "Minor" "Major 6th" "Minor 7th" "Dominant 7th" "Major 7th" "Mystic Chord"]]
         {:root root :tonality tonality} )))

(defn- well-clustered?
  [frets]
  (let [clusters (partition-by nil? frets)]
    (< (count clusters) 3)))

(defn- fretted?
  [fret]
  (and (not (nil? fret))
       (not (zero? fret))))

(defn- min-max-fret
  [frets]
  (let [fretted (filter fretted? frets)]
    (if (empty? fretted)
      [0 0]
      [(apply min fretted) (apply max fretted)])))

(defn- count-fingers
  [frets]
  (let [fretted (filter fretted? frets)
        [min max] (min-max-fret frets)]
    (if (some #{0} frets)
      (count fretted)
      (+ 1 (count (remove #(= min %) fretted))))))

(defn- reachable?
  [frets]
  (let [[min max] (min-max-fret frets)
        reach (- max min)]
    (and (< reach 4)
         (< (count-fingers frets) 5))))

(defn playable?
  [position]
  (and (well-clustered? position)
       (reachable? position)))

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

(defn in-harmony?
  [note chord]
  (let [notes (harmonize chord)]
    (some #{note} notes)))

(defn position-to-chart
  [position]
  (->> position
       (map #(str "--" (or (:fret %) "x") "--"))
       reverse
       (interpose "\n")
       (apply str)))
