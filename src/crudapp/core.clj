(ns crudapp.core
  (:gen-class)
  (:require [server.serverrunner :refer [runServer]])
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (runServer)
)