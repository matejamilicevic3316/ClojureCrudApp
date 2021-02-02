(ns validators.uservalidator
  (:require
   [validators.baseValidator :refer [is-valid checkIfAnyErrorExist]]))

(def passwordRegex #"^(?=[a-zA-Z0-9._]{6,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")
(def firstNameRegex #"^[A-Z][a-z]*$")
(def lastNameRegex #"^[A-Z][a-z]*$")
(def addressRegex #"^[\w\n\s\.\,]{5,100}$")

(defn check-edit-profile [{:keys [password firstname lastname address]}] 
  (let [loggingError { :passwordError (if (clojure.string/blank? password) nil (if (is-valid passwordRegex password) nil "Password not in valid format."))
                      :firstNameError (if (is-valid firstNameRegex firstname) nil "First name not in valid format.")
                      :lastNameError (if (is-valid  lastNameRegex lastname) nil "Last name not in valid format.")
                      :addressError (if (is-valid  addressRegex address) nil "Address not in valid format (5 to 100 length, only letters and numbers).")}] (if (= true (checkIfAnyErrorExist loggingError)) loggingError nil)))
