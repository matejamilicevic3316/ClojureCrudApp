(ns shophelpers.producttypeshandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [shophelpers.universalhelpers :refer [handle-validation-process]]
            [shophelpers.uploader :as uploader]
            [validators.producttypesValidator :refer [check-producttype-validity]]))

(defn get-product-type [id] {:product-type (query/get-product-type-from-db id)})

(defn get-all-product-types [] {:product-types (query/get-product-types nil)})

(defn add-or-update-product-type [product-type] (if-not (clojure.string/blank? (:filename (:image product-type))) (uploader/upload-handler (:image product-type)))
  (query/add-or-insert-product-type (if (clojure.string/blank? (:filename (:image product-type)))
                   (assoc product-type :imageid nil)(assoc product-type :imageid (query/add-image-return-id (:filename (:image product-type))))))(response/redirect "/admin/producttypes")) 

(defn handle-product-type [product-type] (handle-validation-process (check-producttype-validity product-type) add-or-update-product-type  product-type "admin-producttypes-editor.html" :product-type nil))
