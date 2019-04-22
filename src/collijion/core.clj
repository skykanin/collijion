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
  (loop [planets [(Planet. 100000 50 400 400 [0 0])
                  (Planet. 1000 20 700 50 [-5 10])]]
    (renderer {:fx/type root :planets planets})
    #_(println (:v (last planets)))
    (Thread/sleep 100)
    (recur (collijion.planet/update-planets planets))))
