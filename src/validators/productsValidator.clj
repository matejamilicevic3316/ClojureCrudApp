(ns validators.productsValidator
  (:require
   [validators.baseValidator :refer [is-valid checkIfAnyErrorExist checkIsAvailable]]))

(def name-regex #"^[\w\n\s\.\,]{5,50}$")
(def description-regex #"^[\w\n\s\.\,]{10,1000}$")
(def price-regex #"^[0-9]\d*$")


(defn check-product-validity [{:keys [description name productid image price producttypeid]}] 
  (let [producttypeError {:nameError (if (is-valid name-regex name) nil "Name must not contain ',\",; and must be between 5 and 50 long") 
                      :descriptionError (if (is-valid description-regex description) nil "Description must not contain ',\",; and must be between 10 and 1000 long")
                      :imageError (if (nil? productid) (if (clojure.string/blank? (:filename image)) "Image is required.") nil)
                      :priceError (if (is-valid price-regex price) nil "Price must be positive number" )
                      :productTypeError (if (nil? (checkIsAvailable "producttypes" "Id" (or producttypeid 0))) "Product id doesnt exist" nil)
                      } ] (if (= true (checkIfAnyErrorExist producttypeError)) producttypeError nil)))
