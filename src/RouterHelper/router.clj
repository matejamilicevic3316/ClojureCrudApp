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
(GET "/home" {params :query-params}  (session/logged-in (fn [_] (renderHtml "home.html" (shop-handler/get-products-from-db (get params "page")))) "/login"))
(POST "/registration" [] (fn [_] (let [username (:username (:params _))
                                       password (:password (:params _))
                                       mail (:mail (:params _))
                                       firstName (:firstName (:params _))
                                       lastName (:lastName (:params _))
                                       ] (userRegistration username password mail firstName lastName))))
(GET "/producttypes" {params :query-params}  (session/logged-in (fn [_](renderHtml "producttype.html"  (shop-handler/get-product-type (get params "producttypeid")))) "/login"))
(GET "/product" {params :query-params} (session/logged-in (fn [_] (renderHtml "product.html" (shop-handler/get-product-info (get params "productid")))) "/login"))
(GET "/add" {params :query-params} (session/logged-in (fn [_] (shop-handler/add-to-cart (get params "productId") _)) "/login"))
(GET "/cart" [] (session/logged-in (fn [_] (renderHtml "cart.html" (shop-handler/get-cart-data _))) "/login"))
(GET "/search" {params :query-params} (session/logged-in (fn [_] (renderHtml "search.html" (shop-handler/search-products (get params "keyword") (get params "page") (get params "producttypeid")))) "/login"))
(POST "/logout" [] (session/logged-in (fn [_] (session/clear-session :session)) "/login"))
(POST "/order" [] (session/logged-in (fn [_] (shop-handler/order _)) "/login"))
(not-found "<h1>Page not found</h1>")
)