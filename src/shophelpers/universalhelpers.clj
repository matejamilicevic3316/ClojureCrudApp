(ns shophelpers.universalhelpers
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]))

(defn get-count-range [products] (range (int (Math/ceil (/ (count products) 9))) (+ 1 (Math/ceil (/ (count products) 9)))))

(defn delete [table id] (query/delete-by-id-and-table table id)(response/redirect "/admin/producttypes"))