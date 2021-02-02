(ns shophelpers.productshandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]
            [shophelpers.universalhelpers :as helpers]
            [shophelpers.uploader :as uploader]
            [validators.productsValidator :refer [check-product-validity]]))


(defn get-products-from-db [page] (let [product-types (query/get-product-types nil)]{:products (query/get-products-pagination page 3 "productcount") :product-types product-types :product-typer-for-slider (take 3 product-types)}))

(defn get-all-products-paginate [page] (let [products (query/get-products-pagination page 9 "products.name")]{:products products  :page-count  (helpers/get-page-count page) }))

(defn get-product-info-and-types [id] {:product (if (nil? id) nil (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))) :product-types (query/get-product-types nil)})


(defn search-products [page producttypeid keyword] (let [products (query/search-products-db (or page "1") (or producttypeid 0) keyword)] 
                                                     {:products products :page-count 
                                                      (helpers/get-page-count page) 
                                                      :keywordvalue keyword :producttypeidvalue producttypeid}))

(defn add-or-update-product [product] 
  (if-not (clojure.string/blank? (:filename (:image product))) (uploader/upload-handler (:image product)))
  (query/add-or-insert-product (if (clojure.string/blank? (:filename (:image product)))
                   (assoc product :imageid nil) (assoc product :imageid (query/add-image-return-id (:filename (:image product))))))(response/redirect "/admin/products")) 

(defn handle-product [product] (helpers/handle-validation-process (check-product-validity product) add-or-update-product product "admin-products-editor.html" :product {:product-types (query/get-product-types nil)})) 




(defn get-product-info [id] {:product (let [product (query/get-product id)] (if (> (count product) 0) (nth product 0) nil))})
