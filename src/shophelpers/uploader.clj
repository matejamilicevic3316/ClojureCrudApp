(ns shophelpers.uploader
  (:require [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn upload-handler [{:keys [filename tempfile]}]
  (io/copy (io/file tempfile) (io/file "resources" "public" "images" filename))
  (io/delete-file (.getAbsolutePath tempfile))
  (ok {:status :ok}))
