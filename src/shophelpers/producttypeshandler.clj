(ns shophelpers.producttypeshandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [renderinghelpers.htmlparser :refer [renderHtml]]
            [shophelpers.uploader :as uploader]
            [validators.producttypesValidator :refer [check-producttype-validity]]))

(defn get-product-type [id] {:product-type (query/get-product-type-from-db id)})

(defn get-all-product-types [] {:product-types (query/get-product-types)})

(defn add-or-update-product-type [product-type] (if-not (clojure.string/blank? (:filename (:image product-type))) (uploader/upload-handler (:image product-type)))
  (query/add-or-insert-product-type (if (clojure.string/blank? (:filename (:image product-type)))
                   (assoc product-type :imageid nil)(assoc product-type :imageid (query/add-image-return-id (:filename (:image product-type))))))(response/redirect "/admin/producttypes")) 

(defn handle-product-type [product-type] (let [validationError (check-producttype-validity product-type)] (if (nil? validationError) (add-or-update-product-type product-type) (renderHtml "admin-producttypes-editor.html" (assoc validationError :product-type product-type))))) 
