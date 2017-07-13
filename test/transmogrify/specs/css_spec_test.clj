(ns transmogrify.specs.css-spec-test
  (:require [transmogrify.specs.css-spec :as css-spec]
            [clojure.spec.alpha :as s]
            [clojure.test :as test]))

(test/testing "css font specs"
  (test/is
    (not=
      ::s/invalid
      (s/conform
        ::css-spec/properties
        {:font-family  ["helvetica" :serif]
         :font-weight  :normal
         :font-stretch :normal
         :font-style :normal
         :font-size {:magnitude 16.0 :unit :px}}))))