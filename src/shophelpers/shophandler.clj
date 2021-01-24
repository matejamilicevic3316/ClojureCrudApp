(ns shophelpers.shophandler
  (:require [sqlQueryExecutor.sqlqueryhelper :refer [get-all-products-from-db get-products-pagination add-image-return-id add-or-insert-product-type delete-by-id-and-table get-product-types get-product-type-from-db get-product search-products-db add-order-with-id add-order-articles]]
            [sessions.sessionhelper :refer [add-product-to-cart-session get-cart-data-from-session get-user-data-from-session]]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn get-cart-product-count [id cart] (count (filter #(= (:productid %) id) cart)))

(defn filter-cart-data [cart] (for [product (distinct (vec cart)) 
                                    :let [cart-line 
                                        (assoc product :qty 
                                            (get-cart-product-count (:productid product) cart) :totalPrice (* (get-cart-product-count (:productid product) cart) (:price product)))]] cart-line))

(defn get-products-from-db [id] {:products (get-products-pagination id 3) :product-types (get-product-types)})

(defn get-all-products-paginate [page] {:products (get-products-pagination page 9)})

(defn get-product-type [id] {:product-type (get-product-type-from-db id)})

(defn get-product-info [id] {:product (let [product (get-product id)] (if (> (count product) 0) (nth product 0) nil))})

(defn add-to-cart [id _] (add-product-to-cart-session (get-product id) _))

(defn get-cart-data [_] (let [cart-data (filter-cart-data (get-cart-data-from-session _))] {:items cart-data :totalPrice (reduce #(+ %1 (* (:qty %2) (:price %2))) 0 cart-data)}))

(defn get-product-count [products] (range (int (Math/ceil (/ (count products) 9))) (+ 1 (Math/ceil (/ (count products) 9)))))

(defn search-products [keyword page producttypeid] (let [products (search-products-db keyword (or page 1) (or producttypeid 0))] {:products products :page-count (get-product-count products) :keyword keyword}))

(defn order [_] (let [order-id (add-order-with-id (:id (get-user-data-from-session _)))] (add-order-articles order-id (filter-cart-data (get-cart-data-from-session _))) (response/redirect "/home")))

(defn delete [table id] (delete-by-id-and-table table id)(response/redirect "/admin/producttypes"))

(defn get-all-product-types [] {:product-types (get-product-types)})

(defn upload-handler [{:keys [filename tempfile]}]
  (io/copy (io/file tempfile) (io/file "resources" "public" "images" filename))
  (io/delete-file (.getAbsolutePath tempfile))
  (ok {:status :ok}))

(defn handle-product-type [product-type] (upload-handler (:image product-type))
  (add-or-insert-product-type (if (= nil (:image product-type))
                   (assoc product-type :imageid nil)(assoc product-type :imageid (add-image-return-id (:filename (:image product-type))))))(response/redirect "/admin/producttypes"))