(ns shophelpers.universalhelpers
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]
            [renderinghelpers.htmlparser :refer [renderHtml]]))

(defn get-count-range [products] (range (int (Math/ceil (/ (count products) 9))) (+ 1 (Math/ceil (/ (count products) 9)))))

(defn delete [table id] (query/delete-by-id-and-table table id)(response/redirect "/admin/producttypes"))

(defn basic-insert-handler [insert-handler validator page-to-render] (fn [request] (let [error (validator request)] (if (nil? error) (insert-handler request) (renderHtml page-to-render {:validationErrors error})))))