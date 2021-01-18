(ns sqlQueryExecutor.sqlqueryhelper
  (:require [clojure.java.jdbc :as sql]
            [config.core :refer [env]]
            [ring.util.response :refer [redirect]]))

(def db (:conn env))

(defn executeQuery [query] (sql/query db query))
(defn executeInsert [query] (sql/execute! db query))

(defn getLoginUser [username]
  (let [loggedUser (executeQuery (str "SELECT * FROM USERS WHERE USERNAME = '" username "'"))] (if (> (count loggedUser) 0) (nth loggedUser 0) nil)))

(defn registerUser [username password mail firstName lastName]
  (executeInsert (str "INSERT INTO users(username, password, firstname, lastname, mail, isactive, isadmin) Values ('" username "','" password "','" firstName "','" lastName "','" mail "',false,true)"))
  (redirect "/login")
  )

(defn get-product [id] (executeQuery (str "SELECT products.id as productid,price,products.name,products.description,src,alt,producttypes.name as producttypename FROM products INNER JOIN images 
on products.imageid = images.id INNER JOIN producttypes on products.producttypeid = producttypes.id
WHERE products.id = " id)))

(defn get-products-pagination [page] (executeQuery (str "SELECT products.id as productid,products.name,products.description,src,alt,producttypes.name as producttypename,price FROM products INNER JOIN images 
on products.imageid = images.id INNER JOIN producttypes on products.producttypeid = producttypes.id OFFSET 3 * ("(if (= nil page) 1 page) "-1) LIMIT 3")))

(defn get-product-types [] (executeQuery "SELECT producttypes.id,producttypes.name, count(products.id) as productcount,src,alt FROM producttypes 
inner join products on producttypes.id = products.producttypeid inner join images on images.id = producttypes.imgid 
GROUP BY producttypes.name,src,alt,producttypes.id"))

(defn get-product-type-from-db [id] (let [single-item (executeQuery (str "SELECT * FROM producttypes where Id = " id))] (if (> (count single-item) 0) (nth single-item 0) nil)))

(defn check-if-exists [field value] (let [loggedUser (executeQuery (str "SELECT * FROM USERS WHERE " field " = '" value "'"))] (if (> (count loggedUser) 0) (nth loggedUser 0) nil)))