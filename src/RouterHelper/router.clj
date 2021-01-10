(ns routerhelper.router
(:require [compojure.core :refer [defroutes GET POST]]
          [compojure.route :refer [not-found]]
          [renderinghelpers.htmlparser :refer [renderHtml]]
          [loginHelpers.authenitification :refer [loginUser userRegistration]]
          [sessions.sessionhelper :as session]
          [shophelpers.shophandler :as shop-handler]))

(defroutes app
(GET "/login" [] (renderHtml "login.html" nil))
(GET "/registration" [] (renderHtml "registration.html" nil))
(POST "/login" [] (fn [_] (let [username (:username (:params _))
                                password (:password (:params _))] (loginUser username password))))
(GET "/home" [] (session/logged-in (fn [_] (renderHtml "home.html" (shop-handler/get-products-from-db nil))) "/login"))
(GET "/home/:productpage" [] (session/logged-in (fn [_] (renderHtml "home.html" (shop-handler/get-products-from-db (:productpage (:path-params _))))) "/login"))
(POST "/registration" [] (fn [_] (let [username (:username (:params _))
                                       password (:password (:params _))
                                       mail (:mail (:params _))
                                       firstName (:firstName (:params _))
                                       lastName (:lastName (:params _))
                                       ] (userRegistration username password mail firstName lastName))))
(GET "/producttypes" {params :query-params}  (session/logged-in (fn [_](renderHtml "producttype.html"  (shop-handler/get-product-type (get params "producttypeid")))) "/login"))
(GET "/pendingmail" [] (renderHtml "pendingmail.html" nil))
(not-found "<h1>Page not found</h1>")
)