(ns shophelpers.productshandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [shophelpers.universalhelpers :as helpers]
            [shophelpers.uploader :as uploader]))


(defn get-products-from-db [id] {:products (query/get-products-pagination id 3) :product-types (query/get-product-types)})

(defn get-all-products-paginate [page] (let [products (query/get-products-pagination page 9)]{:products products  :page-count (helpers/get-count-range products)}))


(defn get-product-info-and-types [id] {:product (if (nil? id) nil (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))) :product-types (query/get-product-types)})


(defn search-products [keyword page producttypeid] (let [products (query/search-products-db keyword (or page 1) (or producttypeid 0))] {:products products :page-count (helpers/get-count-range products) :keyword keyword}))



(defn handle-product [product]  (if-not (clojure.string/blank? (:filename (:image product))) (uploader/upload-handler (:image product)))
  (query/add-or-insert-product (if (clojure.string/blank? (:filename (:image product)))
                   (assoc product :imageid nil) (assoc product :imageid (query/add-image-return-id (:filename (:image product))))))(response/redirect "/admin/products"))


(defn get-product-info [id] {:product (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))})
