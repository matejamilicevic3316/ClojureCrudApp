(ns shophelpers.shophandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [sessions.sessionhelper :refer [add-product-to-cart-session get-cart-data-from-session get-user-data-from-session]]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn get-cart-product-count [id cart] (count (filter #(= (:productid %) id) cart)))

(defn filter-cart-data [cart] (for [product (distinct (vec cart)) 
                                    :let [cart-line 
                                        (assoc product :qty 
                                            (get-cart-product-count (:productid product) cart) :totalPrice (* (get-cart-product-count (:productid product) cart) (:price product)))]] cart-line))

(defn get-product-count [products] (range (int (Math/ceil (/ (count products) 9))) (+ 1 (Math/ceil (/ (count products) 9)))))

(defn get-products-from-db [id] {:products (query/get-products-pagination id 3) :product-types (query/get-product-types)})

(defn get-all-products-paginate [page] (let [products (query/get-products-pagination page 9)]{:products products  :page-count (get-product-count products)}))

(defn get-product-type [id] {:product-type (query/get-product-type-from-db id)})

(defn get-product-info [id] {:product (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))})

(defn get-product-info-and-types [id] {:product (if (nil? id) nil (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))) :product-types (query/get-product-types)})

(defn add-to-cart [id _] (add-product-to-cart-session (query/get-product id) _))

(defn get-cart-data [_] (let [cart-data (filter-cart-data (get-cart-data-from-session _))] {:items cart-data :totalPrice (reduce #(+ %1 (* (:qty %2) (:price %2))) 0 cart-data)}))


(defn search-products [keyword page producttypeid] (let [products (query/search-products-db keyword (or page 1) (or producttypeid 0))] {:products products :page-count (get-product-count products) :keyword keyword}))

(defn order [_] (let [order-id (query/add-order-with-id (:id (:user (get-user-data-from-session _))))] (query/add-order-articles order-id (filter-cart-data (get-cart-data-from-session _))) (response/redirect "/home")))

(defn delete [table id] (query/delete-by-id-and-table table id)(response/redirect "/admin/producttypes"))

(defn get-all-product-types [] {:product-types (query/get-product-types)})

(defn upload-handler [{:keys [filename tempfile]}]
  (io/copy (io/file tempfile) (io/file "resources" "public" "images" filename))
  (io/delete-file (.getAbsolutePath tempfile))
  (ok {:status :ok}))

(defn handle-product-type [product-type] (if-not (clojure.string/blank? (:filename (:image product-type))) (upload-handler (:image product-type)))
  (query/add-or-insert-product-type (if (clojure.string/blank? (:filename (:image product-type)))
                   (assoc product-type :imageid nil)(assoc product-type :imageid (query/add-image-return-id (:filename (:image product-type))))))(response/redirect "/admin/producttypes")) 

(defn handle-product [product]  (if-not (clojure.string/blank? (:filename (:image product))) (upload-handler (:image product)))
  (query/add-or-insert-product (if (clojure.string/blank? (:filename (:image product)))
                   (assoc product :imageid nil) (assoc product :imageid (query/add-image-return-id (:filename (:image product))))))(response/redirect "/admin/products"))

(defn get-users [page] (let [users (query/get-users-pagination page 9)]{:users users :page-count (get-product-count users)}))

(defn changerole [obj] (query/set-is-user-admin (:id obj) (:isadmin obj))(response/redirect "/admin/users"))

(defn get-orders [page] (let [orders (query/get-orders-pagination page 9)]{:orders orders :page-count (get-product-count orders)}))

(defn change-order-status [obj] (query/set-is-order-finished (:id obj) (:isfinished obj))(response/redirect "/admin/orders"))