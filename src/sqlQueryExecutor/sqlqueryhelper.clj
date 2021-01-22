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

(defn search-products-db [keyword page productypeid] (executeQuery (str "SELECT products.id as productid,products.name,products.description,src,alt,producttypes.name as producttypename,price FROM products INNER JOIN images 
on products.imageid = images.id INNER JOIN producttypes on products.producttypeid = producttypes.id WHERE producttypes.name like  '%" keyword "%' OR products.name like '%" keyword "%' OR producttypes.id = " productypeid " OFFSET 9 * (" page "-1) LIMIT 9")))

(defn add-order-with-id [userid] (executeInsert (str "INSERT INTO orders (ordertime, userid) VALUES (NOW()," userid ")")) (:maxid (nth (executeQuery "SELECT MAX(id) as maxid from orders") 0))); SELECT MAX(id) from USERS;")))

(defn add-order-articles [order-id cart-data] (doseq [product (vec cart-data)] (if-not (= nil product) (executeInsert (str "INSERT INTO orders_product(orderid, productid, qty, productprice) VALUES (" order-id ", " (:productid product) "," (:qty product) ", " (:price product) ")"))())))
