(ns crudapp.serverrunner
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [routerhelper.router :refer [app]]
            [config.core :refer [env]]))

(defn runServer []
  (run-jetty (wrap-defaults app (assoc-in site-defaults 
                                          [:security :anti-forgery] false)) {:port (:port env)})
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (runServer)
)