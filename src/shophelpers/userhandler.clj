(ns shophelpers.userhandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [sessions.sessionhelper :refer [get-user-data-from-session]]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [buddy.hashers :as hashers]
            [shophelpers.universalhelpers :as helpers]))

(defn get-users [page] (let [users (query/get-users-pagination page 9)]{:users users :page-count (helpers/get-count-range users)}))

(defn changerole [obj] (query/set-is-user-admin (:id obj) (:isadmin obj))(response/redirect "/admin/users"))

(defn update-user [obj _] (query/update-user (:id (:user (get-user-data-from-session _))) (:firstname obj) (:lastname obj) (:username obj) (:mail obj) (if (or (nil? (:password obj)) (clojure.string/blank? (:password obj))) nil (hashers/derive (:password obj))))(response/redirect "/editprofile"))
