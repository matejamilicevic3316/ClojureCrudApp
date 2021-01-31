(ns shophelpers.ordershandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [sessions.sessionhelper :refer [get-user-data-from-session]]
            [sessions.cartsessionhelper :refer [get-cart-data-from-session empty-cart]]
            [ring.util.response :as response]
            [shophelpers.universalhelpers :refer [get-count-range]]
            [ring.util.http-response :refer [ok]]
            [shophelpers.carthandler :as carthandler]))

(defn order [_] (let [order-id (query/add-order-with-id (:id (:user (get-user-data-from-session _))))] (query/add-order-articles order-id (carthandler/filter-cart-data (get-cart-data-from-session _))))(empty-cart _))

(defn get-orders [page] (let [orders (query/get-orders-pagination page 9)] {:orders orders :page-count (range 1 (+ 1 (int (Math/ceil (/ (query/get-count-orders) 9)))))}))

(defn change-order-status [obj] (query/set-is-order-finished (:id obj) (:isfinished obj))(response/redirect "/admin/orders"))

