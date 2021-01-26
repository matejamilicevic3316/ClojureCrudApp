(ns shophelpers.universalhelpers)

(defn get-count-range [products] (range (int (Math/ceil (/ (count products) 9))) (+ 1 (Math/ceil (/ (count products) 9)))))
