# Crudapp store

This project is created for purpose of exam from "Alati i metode softverskog inzenjerstva".


This project is projected to be online store. 
Users can register themselves on this web application. After registration user doesn't have admin permissions.
After login phase, user is being redirected to home page, where he can find products and also to search for products by keyword or filter them by provided product types.
On search page, we have 9 products per page with provided pagination. After searching for products, user can add product to cart.
In right top corner is button "My cart" where user can check his cart and order it if he wants to.
If user is granted as admin, in standard layout in top left corner is button "Admin page".
There, user can go to admin page of product types, products, orders and users.
For users, admin can only change role, from admin to not admin and reverse.
For orders, admin can only mark order as finished.
For product types, admin can preview product types, edit, add or delete them.
For products, admin can preview products, edit, add or delete them.

# References

https://weavejester.github.io/compojure/compojure.core.html,
https://github.com/yogthos/Selmer,
https://reports.aashe.org/media/secure/905/submission-5036/web-development-with-clojure-second-edition_b2_0.pdf

# How to run

To run this project, Leiningen and mysqlserver are needed to be installed.
By running script which stands in root of project, clojure.sql in mysql server (workbench), database is going to be created.
Also one admin user is going to be created with username "administrator12" and password "password12".
Application can be runned from root folder with CMD command "lein run".


# License

Copyright © 2021
Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
