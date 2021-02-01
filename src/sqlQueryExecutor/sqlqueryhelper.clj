(ns sqlQueryExecutor.sqlqueryhelper
  (:require [clojure.java.jdbc :as sql]
            [config.core :refer [env]]
            [ring.util.response :refer [redirect]]))

(def db (:conn env))

(defn executeQuery [query] (sql/query db query))
(defn executeSql [query] (sql/execute! db query))

(defn getLoginUser [username]
  (let [loggedUser (executeQuery (str "SELECT * FROM USERS WHERE USERNAME = '" username "'"))] (if (> (count loggedUser) 0) (nth loggedUser 0) nil)))

(defn registerUser [username password mail firstName lastName address]
  (executeSql (str "INSERT INTO users(username, password, firstname, lastname, mail, isactive, isadmin, address) Values ('" username "','" password "','" firstName "','" lastName "','" mail "',false,false,'" address "')"))
  (redirect "/login")
  )

(defn get-count [table additional-string] (:count (nth (executeQuery (str "SELECT COUNT (*) as count FROM " table " " additional-string)) 0)))

(defn get-count-orders [] (count (executeQuery (str "SELECT DISTINCT orderid FROM orders INNER JOIN orders_product on orders.id = orders_product.productid Where (isFinished = false AND orders.id in (SELECT productid from orders_product))"))))

(defn get-product [id] (executeQuery (str "SELECT products.id as productid,price,products.name,products.description,src,alt,producttypes.name as producttypename FROM products INNER JOIN images 
on products.imageid = images.id INNER JOIN producttypes on products.producttypeid = producttypes.id
WHERE products.id = " id)))

(defn get-products-pagination [page offset orderby] (executeQuery (str "SELECT products.id as productid,products.name,products.description,src,alt,producttypes.name as producttypename,COUNT(orders_product.productid) as productcount ,price FROM products INNER JOIN images 
on products.imageid = images.id INNER JOIN producttypes on products.producttypeid = producttypes.id left outer join orders_product ON orders_product.productid = products.id  
                                                                       GROUP BY products.id,products.name,products.description,src,alt,producttypes.name,price ORDER BY " orderby " DESC OFFSET " offset " * ("(if (= nil page) 1 page) "-1) LIMIT " offset)))

(defn get-product-types [count] (executeQuery (str "SELECT producttypes.id,producttypes.name,producttypes.description, count(products.id) as productcount,src,alt FROM producttypes 
LEFT join products on producttypes.id = products.producttypeid inner join images on images.id = producttypes.imgid 
GROUP BY producttypes.name,src,alt,producttypes.id,producttypes.description " (if (nil? count) "" "LIMIT 3"))))

(defn get-product-type-from-db [id] (let [single-item (executeQuery (str "SELECT * FROM producttypes where Id = " id))] (if (> (count single-item) 0) (nth single-item 0) nil)))

(defn check-if-exists [table field value] (let [object (executeQuery (str "SELECT * FROM " table " WHERE " field " = "(if (number? value) "" "'") value (if (number? value) "" "'")))] 
                                            (if (> (count object) 0) (nth object 0) nil)))

(defn search-products-db [page productypeid keyword] (executeQuery (str "SELECT products.id as productid,products.name,products.description,src,alt,producttypes.name as producttypename,price FROM products INNER JOIN images 
on products.imageid = images.id INNER JOIN producttypes on products.producttypeid = producttypes.id WHERE producttypes.id = " productypeid " " (if (nil? keyword) "" (str "OR products.name like '%" keyword "%' OR producttypes.name like '%" keyword "%'")) " OFFSET 9 * (" page "-1) LIMIT 9")))

(defn add-order-with-id [userid] (executeSql (str "INSERT INTO orders (ordertime, userid) VALUES (NOW()," userid ")")) (:maxid (nth (executeQuery "SELECT MAX(id) as maxid from orders") 0))); SELECT MAX(id) from USERS;")))

(defn add-order-articles [order-id cart-data] (doseq [product (vec cart-data)] (if-not (= nil product) (executeSql (str "INSERT INTO orders_product(orderid, productid, qty, productprice) VALUES (" order-id ", " (:productid product) "," (:qty product) ", " (:price product) ")"))())))

(defn delete-by-id-and-table [table id] (executeSql (str "DELETE FROM " table " WHERE id = " id)))

(defn add-or-insert-product-type [product-type] (if (= nil (:id product-type))(executeSql (str "INSERT INTO producttypes(name,description,imgid) VALUES ('" (:name product-type) "', '" (:description product-type) "'," (:imageid product-type) ")"))
                                     (executeSql (str "UPDATE producttypes SET name = '" (:name product-type) "', description = '" (:description product-type) (if (nil? (:imageid product-type)) "' " (str "', imgid = " (:imageid product-type))) " WHERE id = " (:id product-type)))))

(defn add-or-insert-product [product] (if (= nil (:productid product))(executeSql (str "INSERT INTO products(name,description,producttypeid,imageid,price) VALUES ('" (:name product) "', '" (:description product) "'," (:producttypeid product) "," (:imageid product) ", " (:price product) ")"))
                                     (executeSql (str "UPDATE products SET name = '" (:name product) "', description = '" (:description product) "'" (if (nil? (:imageid product)) " " (str ", imageid = " (:imageid product))) " , producttypeid = " (:producttypeid product) ", price = "(:price product)" WHERE id = " (:productid product)))))

(defn add-image-return-id [image] (executeSql (str "INSERT INTO Images(src,alt) VALUES ('" image "', '" image "')"))(:maxid (nth (executeQuery "SELECT max(id) as maxid FROM Images") 0)))

(defn get-users-pagination [page offset] (executeQuery (str "SELECT * FROM users ORDER BY username OFFSET " offset " * ("(if (= nil page) 1 page) "-1) LIMIT " offset)))

(defn set-is-user-admin [id isadmin] (executeSql (str "UPDATE USERS SET isadmin = " (not (boolean (Boolean/valueOf isadmin))) " WHERE id = " id)))

(defn get-orders-pagination [page offset] (executeQuery (str "SELECT orders.*,SUM(qty * productprice) FROM ORDERS INNER JOIN ORDERs_product ON orders.id = ORDERs_product.orderid WHERE isfinished = false
GROUP BY orders.id OFFSET " offset " * ("(if (= nil page) 1 page) "-1) LIMIT " offset)))

(defn update-user [id firstname lastname password address] (executeSql (str "UPDATE USERS SET firstname = '" firstname "', lastname = '" lastname (if (nil? password) "" (str "', password =  '" password )) "', address = '" address "' WHERE id = '" id "'")))

(defn set-is-order-finished [id isfinished] (executeSql (str "UPDATE ORDERS SET isfinished = " (not (boolean (Boolean/valueOf isfinished))) " WHERE id = " id)))