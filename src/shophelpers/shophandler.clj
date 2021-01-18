(ns shophelpers.shophandler
  (:require [sqlQueryExecutor.sqlqueryhelper :refer [get-products-pagination get-product-types get-product-type-from-db get-product]]
            [sessions.sessionhelper :refer [add-product-to-cart-session get-cart-data-from-session]]))

(defn get-product-count [id cart] (count (filter #(= (:productid %) id) cart)))

(defn filter-cart-data [cart] (for [product (distinct (vec cart)) 
                                    :let [cart-line 
                                        (assoc product :qty 
                                            (get-product-count (:productid product) cart) :totalPrice (* (get-product-count (:productid product) cart) (:price product)))]] cart-line))

(defn get-products-from-db [id] {:products (get-products-pagination id) :product-types (get-product-types)})

(defn get-product-type [id] {:product-type (get-product-type-from-db id)})

(defn get-product-info [id] {:product (let [product (get-product id)] (if (> (count product) 0) (nth product 0) nil))})

(defn add-to-cart [id _] (add-product-to-cart-session (get-product id) _))

(defn get-cart-data [_] (let [cart-data (filter-cart-data (get-cart-data-from-session _))] {:items cart-data :totalPrice (reduce #(+ %1 (* (:qty %2) (:price %2))) 0 cart-data)}))
