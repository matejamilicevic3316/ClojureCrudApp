(ns sessions.cartsessionhelper
  (:require
   [ring.util.response :refer [redirect]]))

(defn initialize-cart-session [product old-session] (-> (redirect "/cart") (assoc :session (assoc old-session :cart product))))

(defn add-to-cart-session [product old-session] (-> (redirect "/cart") (assoc :session (assoc old-session :cart (lazy-cat (:cart old-session) product)))))

(defn add-product-to-cart-session [product _]
  (let [old-session (:session _)] (if (> (count (:cart old-session)) 0) (add-to-cart-session product old-session) (initialize-cart-session product old-session)))
)

(defn empty-cart [_]
  (-> (redirect "/home")
      (assoc :session (dissoc (:session _) :cart))))

(defn get-cart-data-from-session [request]
  (:cart (:session request)))