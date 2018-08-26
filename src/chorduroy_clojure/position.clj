(ns chorduroy-clojure.position)

(defn- well-clustered?
  [position]
  (let [clusters (partition-by nil? position)]
    (< (count clusters) 3)))

(defn- fretted?
  [fret]
  (and (not (nil? fret))
       (not (zero? fret))))

(defn- min-max-fret
  [position]
  (let [fretted (filter fretted? position)]
    (if (empty? fretted)
      [0 0]
      [(apply min fretted) (apply max fretted)])))

(defn- count-fingers
  [position]
  (let [fretted (filter fretted? position)
        [min max] (min-max-fret position)]
    (if (some #{0} position)
      (count fretted)
      (+ 1 (count (remove #(= min %) fretted))))))

(defn- reachable?
  [position]
  (let [[min max] (min-max-fret position)
        reach (- max min)]
    (and (< reach 4)
         (< (count-fingers position) 5))))

(defn playable?
  [position]
  (and (well-clustered? position)
       (reachable? position)))

(defn to-chart
  [position]
  (->> position
       (map #(str "--" (or % "x") "--"))
       reverse
       (interpose "\n")
       (apply str)))
