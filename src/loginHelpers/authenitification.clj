(ns loginHelpers.authenitification
  (:require
   [sessions.sessionhelper :as session-helper]
   [sqlQueryExecutor.sqlqueryhelper :refer [getLoginUser registerUser]]
   [renderinghelpers.htmlparser :refer [renderHtml]]
   [buddy.hashers :as hashers]
   [config.core :refer [env]]
   [validators.registrationValidator :refer [check-registration-validity]]
   [validators.loginValidator :refer [check-login-validity]]))


(defn try-login-user [username password {session :session}]
   (let [loggedUser (getLoginUser username)]
     (if (or (nil? loggedUser) (= false (:valid (hashers/verify password (:password loggedUser))))) (renderHtml "login.html" {:error "User with provided credentials was not found in database."}) (session-helper/set-user loggedUser :session))))

(defn loginUser [username password] (let [loggingError (check-login-validity username password)] (if-not (nil? loggingError) (renderHtml "login.html" loggingError) (try-login-user username password :session))))

(defn userRegistration [username password mail firstName lastName address] 
  (-> (try
    (let [loggingError (check-registration-validity username password firstName lastName mail address)]
      (if-not (nil? loggingError) (renderHtml "registration.html" loggingError) (registerUser username (hashers/derive password) mail firstName lastName address)))
      (catch Exception e (let [serverError {:serverError "Server error"}] (renderHtml "registration.html"  {:serverError e}))))))