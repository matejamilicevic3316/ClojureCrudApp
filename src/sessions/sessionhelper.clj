(ns sessions.sessionhelper
  (:require
   [ring.util.response :refer [response redirect]]))

(defn remove-user [{session :session}]
  (-> (response "User removed")
      (assoc :session (dissoc session :user))))

(defn clear-session [{session :session}]
  (-> (redirect "/login")
      (assoc :session (dissoc session :user :cart))))

(defn logged-in [handler redirectRoute]
  (fn [request] 
    (if-not (:user (:session request)) (redirect redirectRoute) (handler request))))

(defn set-user [user {session :session}]
  (-> (redirect "/home")
      (assoc :session (assoc session :user user))))

(defn initialize-cart-session [product old-session] (-> (redirect "/cart") (assoc :session (assoc old-session :cart product))))

(defn add-to-cart-session [product old-session] (-> (redirect "/cart") (assoc :session (assoc old-session :cart (lazy-cat (:cart old-session) product)))))

(defn add-product-to-cart-session [product _]
  (let [old-session (:session _)] (if (> (count (:cart old-session)) 0) (add-to-cart-session product old-session) (initialize-cart-session product old-session)))
)

(defn get-cart-data-from-session [request]
  (:cart (:session request))
  )

(defn get-user-data-from-session [request]
  (:user (:session request))
  )