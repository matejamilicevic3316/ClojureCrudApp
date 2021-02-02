(ns validators.loginValidator
  (:require
   [validators.baseValidator :refer [is-valid checkIfAnyErrorExist check-multiple-validity]]))

(def usernameRegex #"^(?=[a-zA-Z0-9._]{6,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")
(def passwordRegex #"^(?=[a-zA-Z0-9._]{6,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")

(defn check-login-validity [username password] 
  (let [loggingError {:usernameError (if (is-valid usernameRegex username) nil "Username not in valid format.") 
                      :passwordError (if (is-valid passwordRegex password) nil "Password not in valid format.")} ] (if (= true (checkIfAnyErrorExist loggingError)) loggingError nil)))