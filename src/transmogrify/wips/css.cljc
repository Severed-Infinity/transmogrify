(ns transmogrify.wips.css
  (:require [transmogrify.css.units :as units]))

(def unit units/unit)

(defn css
  "core function for clojure to css - and to files?"
  [data]
  data)

#_(css [:body :h1 {:font-size (unit "37.795275591px" :cm)}])
