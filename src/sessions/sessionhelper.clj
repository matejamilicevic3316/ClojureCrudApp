(ns sessions.sessionhelper
  (:require
   [ring.util.response :refer [redirect]]))

(defn is-admin [handler redirectRoute]
  (fn [request] 
    (if-not (= true (:isadmin (:user (:session request)))) (redirect redirectRoute) (handler request))))

(defn clear-session [{session :session}]
  (-> (redirect "/login")
      (assoc :session (dissoc session :user :cart))))

(defn logged-in [handler redirectRoute]
  (fn [request] 
    (if-not (:user (:session request)) (redirect redirectRoute) (handler request))))

(defn set-user [user {session :session}]
  (-> (redirect "/home")
      (assoc :session (assoc session :user user)))) 

(defn get-user-data-from-session [request]
  {:user (:user (:session request))}
  )