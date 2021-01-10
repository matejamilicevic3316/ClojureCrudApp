(ns validators.registrationValidator
  (:require
   [validators.baseValidator :refer [is-valid checkIfAnyErrorExist check-multiple-validity]]))

(def usernameRegex #"^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")
(def passwordRegex #"^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")
(def firstNameRegex #"^[A-Z][a-z]*$")
(def lastNameRegex #"^[A-Z][a-z]*$")
(def mailRegex #"^\S+@\S+\.\S+$")

(defn check-registration-validity [username password firstName lastName mail] 
  (let [loggingError {:usernameError (check-multiple-validity username usernameRegex "Username")
                        :passwordError (if (is-valid passwordRegex password) nil "Password not in valid format.")
                        :firstNameError (if (is-valid firstNameRegex firstName) nil "First name not in valid format.")
                        :lastNameError (if (is-valid  lastNameRegex lastName) nil "Last name not in valid format.")
                        :mailError (check-multiple-validity mail mailRegex "Mail")}] (if (= true (checkIfAnyErrorExist loggingError)) loggingError nil)))