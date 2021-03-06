(ns routerhelper.router
(:require [compojure.core :refer [defroutes GET POST]]
          [compojure.route :refer [not-found]]
          [renderinghelpers.htmlparser :refer [renderHtml]]
          [loginHelpers.authenitification :refer [loginUser userRegistration]]
          [sessions.sessionhelper :as session]
          [shophelpers.userhandler :as user-handler]
          [shophelpers.carthandler :as cart-handler]
          [shophelpers.productshandler :as product-handler]
          [shophelpers.ordershandler :as order-handler]
          [shophelpers.producttypeshandler :as product-type-handler]
          [shophelpers.universalhelpers :as universal]
          [ring.util.response :refer [redirect]]
          ))

(defroutes app
(GET "/login" [] (renderHtml "login.html" nil))
(GET "/registration" [] (renderHtml "registration.html" nil))
(POST "/login" [] (fn [_] (let [username (:username (:params _))
                                password (:password (:params _))] (loginUser username password))))
(GET "/home" {params :query-params}  (session/logged-in (fn [_] (renderHtml "home.html" (universal/prepare-object-for-rendering (product-handler/get-products-from-db (get params "page")) _))) "/login"))
(POST "/registration" [] (fn [_] (let [username (:username (:params _))
                                       password (:password (:params _))
                                       mail (:mail (:params _))
                                       firstName (:firstName (:params _))
                                       lastName (:lastName (:params _))
                                       address (:address (:params _))
                                       ] (userRegistration username password mail firstName lastName address))))
(GET "/producttypes" {params :query-params}  (session/logged-in (fn [_](renderHtml "producttype.html"  (universal/prepare-object-for-rendering (product-type-handler/get-product-type (get params "producttypeid")) _))) "/login"))
(GET "/product" {params :query-params} (session/logged-in (fn [_] (renderHtml "product.html" (universal/prepare-object-for-rendering (product-handler/get-product-info (get params "productid"))  _))) "/login"))
(GET "/add" {params :query-params} (session/logged-in (fn [_] (cart-handler/add-to-cart (get params "productId") _)) "/login"))
(GET "/cart" [] (session/logged-in (fn [_] (renderHtml "cart.html" (universal/prepare-object-for-rendering (cart-handler/get-cart-data _) _))) "/login"))
(GET "/search" {params :query-params} (session/logged-in (fn [_] (renderHtml "search.html" (universal/prepare-object-for-rendering (product-handler/search-products (get params "page") (get params "producttypeid") (get params "keyword")) _))) "/login"))
(POST "/logout" [] (session/logged-in (fn [_] (session/clear-session :session)) "/login"))
(POST "/order" [] (session/logged-in (fn [_] (order-handler/order _)) "/login"))
(GET "/admin" [] (session/is-admin (fn [_] (renderHtml "admin.html" (universal/prepare-object-for-rendering nil _))) "/404"))
(GET "/admin/producttypes" [] (session/is-admin (fn [_] (renderHtml "admin-producttypes.html"  (universal/prepare-object-for-rendering (product-type-handler/get-all-product-types) _))) "/404"))
(GET "/admin/products" {params :query-params} (session/is-admin (fn [_] (renderHtml "admin-products.html" (universal/prepare-object-for-rendering (product-handler/get-all-products-paginate (get params "page")) _))) "/404"))
(GET "/admin/users" {params :query-params} (session/is-admin (fn [_] (renderHtml "admin-users.html" (universal/prepare-object-for-rendering (user-handler/get-users (get params "page")) _))) "/404"))
(GET "/admin/orders" {params :query-params} (session/is-admin (fn [_] (renderHtml "admin-orders.html" (universal/prepare-object-for-rendering (order-handler/get-orders (get params "page")) _ ))) "/404"))
(GET "/delete" {params :query-params} (session/is-admin (fn [_] (universal/delete (get params "table") (get params "id"))) "/404"))
(GET "/editproducttype" {params :query-params} (session/is-admin (fn [_] (renderHtml "admin-producttypes-editor.html" (universal/prepare-object-for-rendering (if-not (nil? (get params "id")) (product-type-handler/get-product-type (get params "id")) nil) _))) "/404"))
(POST "/editproducttypes" {params :query-params} (session/is-admin (fn [_] (product-type-handler/handle-product-type {:id (get params "id") :description (:description (:params _)) :name (:name (:params _)) :image (:file (:params _))})) "/404"))
(GET "/editproduct" {params :query-params} (session/is-admin (fn [_] (renderHtml "admin-products-editor.html" (universal/prepare-object-for-rendering (product-handler/get-product-info-and-types (get params "id")) _))) "/404"))
(POST "/editproduct" {params :query-params} (session/is-admin (fn [_] (product-handler/handle-product {:productid (get params "productid") :description (:description (:params _)) :name (:name (:params _)) :producttypeid (:producttypeid (:params _)) :image (:file (:params _)) :price (:price (:params _))})) "/404"))
(POST "/changeuserrole" {params :query-params} (session/is-admin (fn [_] (user-handler/changerole {:id (get params "id") :isadmin (:isadmin (:params _))})) "/404"))
(POST "/changeorderstatus" {params :query-params} (session/is-admin (fn [_] (order-handler/change-order-status {:id (get params "id") :isfinished (:isfinished (:params _))})) "/404"))
(GET "/editprofile" [] (session/logged-in (fn [_] (renderHtml "editprofile.html" (universal/prepare-object-for-rendering (session/get-user-data-from-session _) _))) "/404"))
(POST "/edituser" [] (session/logged-in (fn [_] (user-handler/update-user {:firstname (:firstname (:params _)) :lastname (:lastname (:params _)) :password (:password (:params _)) :address (:address (:params _))} _)) "/404"))
(GET "/" [] (session/logged-in (fn [_] (redirect "/home")) "/login" ))
(not-found "<h1>Page not found</h1>")
)