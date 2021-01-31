(ns validators.baseValidator
   (:require
   [sqlQueryExecutor.sqlqueryhelper :refer [check-if-exists]]))

(defn checkIfAnyErrorExist [errorObject] (some #{true} (for [i (keys errorObject)] (if-not(nil? (i errorObject)) true false))))

(defn is-valid [regex value] (if (= (re-matches regex value) value) true false))

(defn checkIsAvailable [table field value] (check-if-exists table field value))

(defn check-multiple-validity [value regex value-to-compare-with] (if (is-valid regex value) (if (= nil (checkIsAvailable "USERS" value-to-compare-with value)) nil (str value-to-compare-with " is already in use"))  (str value-to-compare-with " not in valid format.")))