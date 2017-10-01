(ns chorduroy-clojure.filterer)
(require '[chorduroy-clojure.core :refer :all])

(defn frets-from
  [position]
  (map :fret position))

(defn- well-clustered?
  [frets]
  (let [clusters (partition-by nil? frets)]
    (< (count clusters) 3)))

(defn min-max-fret
  [frets]
  (let [fretted (filter fretted? frets)]
    (if (empty? fretted)
      [0 0]
      [(apply min fretted) (apply max fretted)])))

(defn fretted?
  [fret]
  (and (not (nil? fret))
       (not (zero? fret))))

(defn count-fingers
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
  (let [frets (frets-from position)]
    (and (well-clustered? frets)
         (reachable? frets))))

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
  (and (playable? position)
       (sufficient? chord position)
       (root-position? chord position)))
