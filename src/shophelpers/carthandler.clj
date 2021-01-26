(ns shophelpers.carthandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [sessions.sessionhelper :refer [add-product-to-cart-session get-cart-data-from-session]]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]))

(defn get-cart-product-count [id cart] (count (filter #(= (:productid %) id) cart)))

(defn filter-cart-data [cart] (for [product (distinct (vec cart)) 
                                    :let [cart-line 
                                        (assoc product :qty 
                                            (get-cart-product-count (:productid product) cart) :totalPrice (* (get-cart-product-count (:productid product) cart) (:price product)))]] cart-line))


(defn add-to-cart [id _] (add-product-to-cart-session (query/get-product id) _))

(defn get-cart-data [_] (let [cart-data (filter-cart-data (get-cart-data-from-session _))] {:items cart-data :totalPrice (reduce #(+ %1 (* (:qty %2) (:price %2))) 0 cart-data)}))

