(ns shophelpers.userhandler
  (:require [sqlQueryExecutor.sqlqueryhelper :as query]
            [sessions.sessionhelper :refer [get-user-data-from-session]]
            [ring.util.response :as response]
            [ring.util.http-response :refer [ok]]
            [renderinghelpers.htmlparser :refer [renderHtml]]
            [buddy.hashers :as hashers]
            [shophelpers.universalhelpers :as helpers]
            [validators.uservalidator :refer [check-edit-profile]]))

(defn get-users [page] (let [users (query/get-users-pagination page 9)]{:users users :page-count (helpers/get-page-count page)}))

(defn changerole [obj] (query/set-is-user-admin (:id obj) (:isadmin obj))(response/redirect "/admin/users"))

(defn handle-update-user [obj _] (query/update-user (:id (:user (get-user-data-from-session _))) (:firstname obj) (:lastname obj) (if (or (nil? (:password obj)) (clojure.string/blank? (:password obj))) nil (hashers/derive (:password obj))) (:address obj))(response/redirect "/editprofile"))

(defn update-user [obj _] (let [validationError (check-edit-profile obj)] (if (nil? validationError) (handle-update-user obj _) (renderHtml "editprofile.html" (assoc validationError :user obj))))) 
