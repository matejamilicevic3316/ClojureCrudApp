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
(GET "/admin" [] (session/is-admin (fn [_] (renderHtml "admin.html" nil)) "/404"))
(GET "/admin/producttypes" [] (session/is-admin (fn [_] (renderHtml "admin-producttypes.html" (shop-handler/get-all-product-types))) "/404"))
(GET "/admin/products" {params :query-params} (session/is-admin (renderHtml "admin-products.html" (shop-handler/get-all-products-paginate (get params "page"))) "/404"))
(GET "/admin/users" [] (session/is-admin (renderHtml "admin-users.html" nil) "/404"))
(GET "/admin/orders" [] (session/is-admin (renderHtml "admin-orders.html" nil) "/404"))
(GET "/delete" {params :query-params} (session/is-admin (fn [_] (shop-handler/delete (get params "table") (get params "id"))) "/404"))
(GET "/editproducttype" {params :query-params} (session/is-admin (fn [_] (renderHtml "admin-producttypes-editor.html" (if-not (nil? (get params "id")) (shop-handler/get-product-type (get params "id")) nil))) "/404"))
(POST "/editproducttypes" [] (session/is-admin (fn [_] (shop-handler/handle-product-type {:id (:id (:params _)) :description (:description (:params _)) :name (:name (:params _)) :image (:file (:params _))})) "/404"))
(not-found "<h1>Page not found</h1>")
)