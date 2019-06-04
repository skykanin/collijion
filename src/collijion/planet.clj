(ns collijion.planet
  (:import [clojure.lang PersistentVector]))

(def G 6.67264E-11)
#_(def G 1E-5)

(defrecord Planet [m r x y ^PersistentVector v])

(defn add
  "Adds together an arbitrary amount of vectors"
  [& colls]
  (reduce (partial mapv +) colls))

(defn sub
  "Subtracts an arbitrary amount of vectors"
  [& colls]
  (reduce (partial mapv -) colls))

(defn neg
  "Negate vector"
  [[x y]]
  [(- x) (- y)])

(defn mult
  "Multiplies a vector by a number"
  [v n]
  (mapv #(* % n) v))

(defn div
  "Divides a vector by a number"
  [v n]
  (when-not (= n 0)
   (mapv #(/ % n) v)))

(defn magnitude
  "Calculates the magnitude of a vector"
  [[x y]]
  (Math/sqrt (+ (Math/pow x 2) (Math/pow y 2))))

(defn unit
  "Calculates the unit vector of a vector"
  [v]
  (let [m (magnitude v)]
    (if (= m (double 0))
      [0 0]
      (div v m))))

(defn grav-vec
  "Calculates the attraction force between two
  bodies using Newton's law of universal gravitation.
  Force applied on object 2 exerted by object 1."
  [{x1 :x y1 :y m1 :m :as p1} {x2 :x y2 :y m2 :m :as p2}]
  (let [diff (sub [x2 y2] [x1 y1])
        dist (magnitude diff) ; distance between the two bodies
        unit (unit diff) ; unit vector (direction only)
        const (/ (* G m1 m2)
                 (Math/pow dist 2))]
    (mult unit const)))

(defn gen-f-vec
  "Calculate all force vectors for all objects in
  a vector"
  [planets]
  (partition (dec (count planets))
   (for [p1 planets p2 planets
         :when (not= p1 p2)]
     (grav-vec p1 p2)))) ;check this application

(defn apply-forces
  "Apply all the force vectors to the planets"
  ([planets forces]
   (let [sum-forces (fn [old fl m]
                      (as-> fl n
                        (reduce add n)
                        (div n m)
                        (add old n)))
         zip (fn [p fl]
               (update p :v sum-forces fl (:m p)))]
     (map zip planets forces))))

(defn apply-speed
  "Applies the new velocity to the planets"
  [planets]
  (let [move (fn [{[vx vy] :v :as p}]
               (-> p
                (update :x + vx)
                (update :y + vy)))]
    (map move planets)))

(defn update-planets
  "Update all planets velocity vectors
  and coordinates"
  [planets]
  (apply-speed (apply-forces planets (gen-f-vec planets))))
