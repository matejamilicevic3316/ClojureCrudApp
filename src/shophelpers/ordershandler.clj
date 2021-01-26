(ns shophelpers.ordershandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [sessions.sessionhelper :refer [get-cart-data-from-session get-user-data-from-session]]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]))

(defn order [_] (let [order-id (query/add-order-with-id (:id (:user (get-user-data-from-session _))))] (query/add-order-articles order-id (filter-cart-data (get-cart-data-from-session _))) (response/redirect "/home")))

(defn get-orders [page] (let [orders (query/get-orders-pagination page 9)]{:orders orders :page-count (get-product-count orders)}))

(defn change-order-status [obj] (query/set-is-order-finished (:id obj) (:isfinished obj))(response/redirect "/admin/orders"))

