(ns transmogrify.specs.css-spec-test
  (:require [transmogrify.specs.css-spec :as css-spec]
            [clojure.spec.alpha :as s]
            [clojure.test :as test]))

(test/run-tests)

(test/deftest css-spec-property-tests
  (test/testing "css font specs"
    (test/testing "-> font family spec"
      ;; TODO Sub specs
      ;; FIXME proper output comparison
      (test/is (not= ::s/invalid (s/conform ::css-spec/font-family ["helvetica"])) "coll of a single font as a string")
      (test/is (not= ::s/invalid (s/conform ::css-spec/font-family [:serif])) "coll of a single font from generics")
      (test/is (not= ::s/invalid (s/conform ::css-spec/font-family ["helvetica" :serif])) "coll of multiple mixed font options")
      (test/is (= ::s/invalid (s/conform ::css-spec/font-family "helvetica")) "string font should be invalid")
      (test/is (= ::s/invalid (s/conform ::css-spec/font-family :serif)) "keyword generic font should be invalid"))

    (test/testing "-> font weight spec"
      (test/testing "-> testing css-spec/multiple-of-100?"
        (test/is (true? (css-spec/multiple-of-100? 800)) "positive 100's, should be true")
        (test/is (true? (css-spec/multiple-of-100? 300)) "positive 100's, should be true")
        (test/is (true? (css-spec/multiple-of-100? -200)) "negative 100's should be true")
        (test/is (false? (css-spec/multiple-of-100? 90)) "not a multiple of 100, should be false")
        (test/is (false? (css-spec/multiple-of-100? 1110)) "not a multiple of 100, should be false")
        (test/is (false? (css-spec/multiple-of-100? -330)))) "not a multiple of 100, should be false")

    (test/testing "-> properties map of just font specs"
      (test/is
        (not=
          ::s/invalid
          (s/conform
            ::css-spec/properties
            {:font-family  ["helvetica" :serif]
             :font-weight  :normal
             :font-stretch :normal
             :font-style   :normal
             :font-size    {:magnitude 16.0 :unit :px}}))))))