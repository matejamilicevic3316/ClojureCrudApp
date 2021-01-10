(ns shophelpers.shophandler
  (:require [sqlQueryExecutor.sqlqueryhelper :refer [get-products get-product-types get-product-type-from-db]]))

(defn get-products-from-db [id] {:products (get-products id) :product-types (get-product-types)})

(defn get-product-type [id] {:product-type (get-product-type-from-db id)})