(ns renderinghelpers.htmlparser
  (:require 
   [selmer.parser :as parser]))

(defn renderHtml [htmlpage object] (parser/render-file (str "html/" htmlpage) object))