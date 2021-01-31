(ns validators.producttypesValidator
  (:require
   [validators.baseValidator :refer [is-valid checkIfAnyErrorExist check-multiple-validity]]))

(def name-regex #"^[\w\n\s\.\,]{5,50}$")
(def description-regex #"^[\w\n\s\.\,]{10,1000}$")

(defn check-producttype-validity [{:keys [description name id image]}] 
  (let [producttypeError {:nameError (if (is-valid name-regex name) nil "Name must not contain ',\",; and must be between 5 and 50 long") 
                      :descriptionError (if (is-valid description-regex description) nil "Description must not contain ',\",; and must be between 10 and 1000 long")
                      :imageError (if (nil? id) (if (clojure.string/blank? (:filename image)) "Image is required.") nil)
                      } ] (if (= true (checkIfAnyErrorExist producttypeError)) producttypeError nil)))
