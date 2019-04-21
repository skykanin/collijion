(ns collijion.core
  (:require [cljfx.api :as fx]))

(def renderer
  (fx/create-renderer))

(defn root [{:keys [y]}]
  {:fx/type :stage
   :showing true
   :title "some example"
   :width 500
   :height 800
   :scene {:fx/type :scene
           :root {:fx/type :group
                  :children [{:fx/type :circle
                              :center-x 250
                              :center-y y
                              :radius 50
                              :fill :blue}
                             {:fx/type :circle
                              :center-x 250
                              :center-y 400
                              :radius 50
                              :fill :red}]}}})
                                         
(defn -main [& args]
  (loop [y 699
         v 15
         g 0.2]
    (when-not (>= y 750)
      (renderer {:fx/type root :y y})
      (Thread/sleep 30)
      (recur (- y v) (- v g) g))))
