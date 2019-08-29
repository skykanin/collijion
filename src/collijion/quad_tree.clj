(ns collijion.quad-tree)

(defrecord Boundary [x y s])

(defrecord QuadTree
           [boundary max-points
            points divided?])

(defn make-qtree
  "Instanciates an empty quad tree"
  ([boundary]
   (QuadTree. boundary 10 [] false))
  ([boundary max-points]
   (QuadTree. boundary max-points [] false)))

(defn- in-boundary?
  "Returns true if point is in boundary"
  [{:keys [x y s]} {px :x py :y}]
  (let [half-s (/ s 2)]
    (and (>= px (- x half-s)) (<= px (+ x half-s))
         (>= py (- y half-s)) (<= py (+ y half-s)))))

(defn- create-quadrant
  "Creates a quadtrant with the correct bounds"
  [_ coord {:keys [x y s]} max-points]
  (let [half-s (/ s 2)
        quart-s (/ s 4)
        create-bound
        #(case %
           :nw (Boundary. (- x quart-s) (- y quart-s) half-s)
           :ne (Boundary. (+ x quart-s) (- y quart-s) half-s)
           :sw (Boundary. (- x quart-s) (+ y quart-s) half-s)
           :se (Boundary. (+ x quart-s) (+ y quart-s) half-s))]
    (QuadTree. (create-bound coord) max-points [] false)))

(defn- partition-qtree
  "Partitions a quad tree if not already divided"
  [{:keys [boundary divided? max-points] :as qtree}]
  (if divided? qtree
      (-> qtree
          (update :nw create-quadrant :nw boundary max-points)
          (update :ne create-quadrant :ne boundary max-points)
          (update :sw create-quadrant :sw boundary max-points)
          (update :se create-quadrant :se boundary max-points)
          (assoc :divided? true))))

(defn- find-leaf-path
  "Finds correct path for point insertion to a
  quad tree. Returns a path vector"
  [point qt]
  (loop [point point {:keys [divided? nw ne sw se] :as qtree} qt path []]
    (cond
      (not divided?) (conj path :points)
      (in-boundary? (:boundary nw) point)
      (if divided? (recur point (:nw qtree) (conj path :nw)) path)
      (in-boundary? (:boundary ne) point)
      (if divided? (recur point (:ne qtree) (conj path :ne)) path)
      (in-boundary? (:boundary sw) point)
      (if divided? (recur point (:sw qtree) (conj path :sw)) path)
      (in-boundary? (:boundary se) point)
      (if divided? (recur point (:se qtree) (conj path :se)) path)
      (not (in-boundary? (:boundary qtree) point)) (println "Point not in bounds"))))

(defn- move-points-down
  "Moves points in a quad tree down a notch
  to the four new sub trees."
  [{d :divided? :as qtree}]
  (if-not d
    (println "Error: This tree is a leaf node")
    (loop [{:keys [nw ne sw se] :as m} qtree]
      (let [point (first (:points m))
            update-fn (fn [qt k]
                        (-> qt
                            (update :points (comp vec (partial drop 1)))
                            (update-in [k :points] #(conj % point))))]
        (cond
          (empty? (:points m)) m
          (in-boundary? (:boundary nw) point) (recur (update-fn m :nw))
          (in-boundary? (:boundary ne) point) (recur (update-fn m :ne))
          (in-boundary? (:boundary sw) point) (recur (update-fn m :sw))
          (in-boundary? (:boundary se) point) (recur (update-fn m :se)))))))

(defn- update-grav
  "Calculate and update a tree node's average position and mass"
  [{:keys [points] :as qt-node}]
  (let [sum-points (fn [acc val]
                     {:x (+ (:x acc) (:x val))
                      :y (+ (:y acc) (:y val))
                      :m (+ (:m acc) (:m val))})
        len (count points)
        average (comp
                 (fn [{:keys [x y m]}]
                   {:x (/ x len) :y (/ y len) :m (/ m len)})
                 (partial reduce sum-points {:x 0 :y 0 :m 0}))]
    (if (zero? len) qt-node
        (assoc qt-node :average (average points)))))

;; TODO: Implement a mapping function for the quad tree

(defn- insert
  "Returns an updated quad tree with an inserted point"
  [{:keys [boundary divided? points max-points] :as qtree} point]
  (cond
    (and (in-boundary? boundary point) (> max-points (count points)))
    (-> qtree
        (update :points #(conj % point))
        update-grav)
    (and (in-boundary? boundary point) (= max-points (count points)) divided?)
    (-> qtree
        (update-in (find-leaf-path point qtree) #(conj % point))
        move-points-down
        update-grav)
    (and (in-boundary? boundary point) (= max-points (count points)) (not divided?))
    (insert (partition-qtree qtree) point)
    :else (println "Point not in bounds")))
