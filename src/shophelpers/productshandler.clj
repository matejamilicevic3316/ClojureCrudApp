(ns shophelpers.productshandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [renderinghelpers.htmlparser :refer [renderHtml]]
            [shophelpers.universalhelpers :as helpers]
            [shophelpers.uploader :as uploader]
            [validators.productsValidator :refer [check-product-validity]]))


(defn get-products-from-db [id] {:products (query/get-products-pagination id 3 "productcount") :product-types (query/get-product-types)})

(defn get-all-products-paginate [page] (let [products (query/get-products-pagination page 9 "products.name")]{:products products  :page-count  (helpers/get-page-count page) }))


(defn get-product-info-and-types [id] {:product (if (nil? id) nil (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))) :product-types (query/get-product-types)})


(defn search-products [page producttypeid keyword] (let [products (query/search-products-db (or page 1) (or producttypeid 0) keyword)] 
                                                     {:products products :page-count 
                                                      (helpers/get-page-count page) 
                                                      :keywordvalue keyword :producttypeidvalue producttypeid}))

(defn add-or-update-product [product] 
  (if-not (clojure.string/blank? (:filename (:image product))) (uploader/upload-handler (:image product)))
  (query/add-or-insert-product (if (clojure.string/blank? (:filename (:image product)))
                   (assoc product :imageid nil) (assoc product :imageid (query/add-image-return-id (:filename (:image product))))))(response/redirect "/admin/products")) 

(defn handle-product [product] (let [validationError (check-product-validity product)] (if (nil? validationError) (add-or-update-product product) (renderHtml "admin-products-editor.html" (assoc validationError :product product :product-types (query/get-product-types)))))) 




(defn get-product-info [id] {:product (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))})
