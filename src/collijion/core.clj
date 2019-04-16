(ns collijion.core
  (:import [javafx.animation KeyFrame
                             KeyValue
                             Timeline]
           [javafx.application Application]
           [javafx.scene Group
            Scene]
           [javafx.scene.shape Circle]
           [javafx.scene.paint Color]
           [javafx.stage Stage]
           [javafx.util Duration]))

(defn -start
  [this ^Stage stage]
  (let [circle (doto (Circle. 50.0 135.0 50.0) (.setFill Color/RED))
        group (Group. [circle])
        kv (KeyValue. (.translateXProperty circle) 400)
        anim (Timeline. 60.0 (into-array
                              KeyFrame
                              [(KeyFrame.
                                (Duration. 500.0)
                                (into-array KeyValue [kv]))]))
        scene (Scene. group 700 600)]
    (doto anim
      (.setCycleCount 20)
      (.setAutoReverse true)
      (.play))
    (doto stage
      (.setTitle "Drawing a Circle")
      (.setScene scene)
      (.show))))

(defn -main
  [& args]
  (Application/launch collijion.core args))

(proxy [Application][]
  (start [this ^Stage stage] (-start this stage))
  (main [& args] (-main args)))
