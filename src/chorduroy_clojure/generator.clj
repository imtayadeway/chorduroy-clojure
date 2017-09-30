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

(defn frets-in-harmony
  [chord open]
  (->> (range 11)
       (filter #(in-harmony? (walk-scale open %) chord))
       (#(conj % nil))))

(defn positions-for-chord
  [chord tuning]
  (for [sixth (frets-in-harmony chord (get tuning 0))
        fifth (frets-in-harmony chord (get tuning 1))
        fourth (frets-in-harmony chord (get tuning 2))
        third (frets-in-harmony chord (get tuning 3))
        second (frets-in-harmony chord (get tuning 4))
        first (frets-in-harmony chord (get tuning 5))]
    [sixth fifth fourth third second first]))

(defn playable?
  [position]
  (let [clusters (partition-by nil? position)
        fretted (remove #(or (nil? %) (zero? %)) position)
        max (if (empty? fretted) 0 (apply max fretted))
        min (if (empty? fretted) 0 (apply min fretted))
        reach (- max min)]
    (and (< (count clusters) 3)
         (< reach 4))))

(defn- generate-row
  [tuning chord]
  (let [name (name-for-chord chord)
        positions (filter playable? (positions-for-chord chord tuning))]
    {:name name :positions positions}))

(defn generate
  [tuning]
  (map (partial generate-row tuning) the-major-and-minor-chords))
