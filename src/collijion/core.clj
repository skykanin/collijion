(ns collijion.core
  (:require [cljfx.api :as fx]
            [collijion.planet])
  (:import [collijion.planet Planet]))

(def renderer
  (fx/create-renderer))

(defn root [{:keys [planets]}]
  {:fx/type :stage
   :showing true
   :title "gravity"
   :width 800
   :height 800
   :scene {:fx/type :scene
           :root {:fx/type :group
                  :children (into []
                             (for [{:keys [x y r]} planets]
                               {:fx/type :circle
                                :center-x x
                                :center-y y
                                :radius r
                                :fill :black}))}}})

(defn -main [& args]
  "Entrypoint for the program"
  (loop [planets [(Planet. 999999 60 400 400 [0 0])
                  (Planet. 100 10 700 50 [-5 10])
                  (Planet. 100 10 100 750 [5 -10])
                  (Planet. 100 10 700 400 [-5 -3])
                  (Planet. 100 10 100 400 [5 3])]]
    (renderer {:fx/type root :planets planets})
    (println (:v (last planets)))
    (Thread/sleep 100)
    (recur (collijion.planet/update-planets planets))))
