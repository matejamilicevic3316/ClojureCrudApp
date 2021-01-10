(ns sessions.sessionhelper
  (:require
   [ring.util.response :refer [response redirect]]))

(defn remove-user [{session :session}]
  (-> (response "User removed")
      (assoc :session (dissoc session :user))))

(defn clear-session []
  (-> (response "Session cleared")
      (dissoc :session)))

(defn logged-in [handler redirectRoute]
  (fn [request] 
    (if-not (:user (:session request)) (redirect redirectRoute) (handler request))))

(defn set-user [user {session :session}]
  (-> (redirect "/home")
      (assoc :session (assoc session :user user))))

(defn getUser [request]
  (:user (:session request)))