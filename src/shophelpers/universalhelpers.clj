(ns shophelpers.universalhelpers
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [ring.util.response :as response]
            [renderinghelpers.htmlparser :refer [renderHtml]]
            [sessions.sessionhelper :as session]))

(defn prepare-object-for-rendering [obj _] (assoc obj :isadmin (:isadmin (:user (session/get-user-data-from-session _)))))

(defn delete [table id] (query/delete-by-id-and-table table id)(response/redirect "/admin/producttypes"))

(defn get-page-count [page] (if (> (Long/parseLong (or page "1")) 1) (range (- (Long/parseLong page) 1) (+ (Long/parseLong page) 2)) (range 1 4)))
