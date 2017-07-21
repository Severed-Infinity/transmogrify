(ns transmogrify.specs.css-spec-test
  (:require [transmogrify.specs.css-spec :as css-spec]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as s-gen]
            [clojure.test :as test]
            [clojure.test.check :as tc]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer [defspec]]))

(test/run-tests)

;; FIXME I am an idiot, I should be testing with s/valid? and not s/conform
;; TODO property base testing

(def pos-double (gen/fmap (fn [n] (if (pos? n) n 0.1)) gen/double))

(defspec css-spec-magnitude-key-generative-test
         10000
         (prop/for-all
           [d pos-double]
           (s/valid? ::css-spec/magnitude d)))

(defspec css-spec-unit-key-generative-test
         10000
         (prop/for-all
           [per-sym (gen/return :%)]
           (s/valid? ::css-spec/unit per-sym)))

(def percentage-as-a-string
  (gen/fmap
    (fn [[n1 n2]] (str n1 "." n2 "%"))
    (gen/tuple gen/pos-int gen/pos-int)))

(defspec css-spec-percentage-unit-string-generative-test
         10000
         (prop/for-all
           [per-str percentage-as-a-string]
           (s/valid? ::css-spec/percentage per-str)))

(defspec css-spec-percentage-unit-map-generative-test
         10000
         (prop/for-all
           [m (s/gen ::css-spec/magnitude)
            u (s/gen ::css-spec/unit)]
           (s/valid? ::css-spec/percentage {:magnitude m :unit u})))

(defspec css-spec-font-family-generative-test
         10000
         (prop/for-all
           [inputs (gen/one-of [(gen/tuple gen/string)
                                (gen/tuple (s/gen ::css-spec/generic))
                                (gen/tuple (s/gen ::css-spec/css-wide-keywords))
                                (gen/tuple gen/string (s/gen ::css-spec/generic))
                                (gen/tuple gen/string (s/gen ::css-spec/css-wide-keywords))
                                (gen/tuple (s/gen ::css-spec/css-wide-keywords) (s/gen ::css-spec/generic))
                                (gen/tuple gen/string (s/gen ::css-spec/generic) (s/gen ::css-spec/css-wide-keywords))])]
           (s/valid? ::css-spec/font-family inputs)))

;; FIXME only produces 400 99% of the time and 100 the others
(def multiple-of-100-less-then-1000
  (gen/fmap
    (fn [n] (* n 100))
    (gen/large-integer* {:min 1 :max 9})))

(defspec css-spec-weight-number-generative-test
         10000
         (prop/for-all
           [n multiple-of-100-less-then-1000]
           (s/valid? ::css-spec/weight-number n)))

#_(gen/sample multiple-of-100-less-then-1000)
#_(gen/sample (s/gen ::css-spec/weight-number))

(defspec css-spec-weight-value-generative-test
         10000
         (prop/for-all
           [val (gen/one-of [(gen/return :normal) (gen/return :bold) (gen/return :bolder) (gen/return :lighter)])]
           (s/valid? ::css-spec/weight-value val)))

(defspec css-spec-font-weight-generative-test
         10000
         (prop/for-all
           [inputs (gen/one-of [(s/gen ::css-spec/weight-number) (s/gen ::css-spec/weight-value)])]
           (s/valid? ::css-spec/font-weight inputs)))

(defspec css-spec-properties-map-generative-test
         10000
         (prop/for-all
           [f-family (s/gen ::css-spec/font-family)
            f-weight (s/gen ::css-spec/font-weight)
            f-stretch (s/gen ::css-spec/font-stretch)
            f-style (s/gen ::css-spec/font-style)
            f-size (s/gen ::css-spec/font-size)
            f-min-size (s/gen ::css-spec/font-min-size)
            f-max-size (s/gen ::css-spec/font-max-size)
            f-font (s/gen ::css-spec/font)
            f-synthesis (s/gen ::css-spec/font-synthesis)]
           (s/valid?
             ::css-spec/properties
             {:font-family    f-family
              :font-weight    f-weight
              :font-stretch   f-stretch
              :font-style     f-style
              :font-size      f-size
              :font-min-size  f-min-size
              :font-max-size  f-max-size
              :font           f-font
              :font-synthesis f-synthesis})))


(test/deftest css-spec-unit-tests
  (test/testing "css unit specs"
    (test/testing "-> percentage units"
      (test/is (s/valid? ::css-spec/magnitude 10.5)) "only doubles allowed"
      (test/is (not (s/valid? ::css-spec/magnitude 50)) "anything but a double should be invalid")
      (test/is (s/valid? ::css-spec/unit :%)) "only the percentage sign (:%) as a keyword allowed"
      (test/is (not (s/valid? ::css-spec/unit :percentage)) "anything but :% should be invalid")

      (test/is (not (s/valid? ::css-spec/percentage "100")) "should be invalid for not having % at the end")
      (test/is (s/valid? ::css-spec/percentage "50.5%")
               "should be valid so long as its a double followed by % is at the end")
      (test/is (not (s/valid? ::css-spec/percentage "invalid%"))
               "even though % is at the end it should be invalid for not having a number before it")
      (test/is (s/valid? ::css-spec/percentage {:magnitude 16.0 :unit :%})
               "map format should be valid")
      (test/is (not (s/valid? ::css-spec/percentage {::css-spec/magnitude 75.76 ::css-spec/unit :%}))
               "namespaced map format should be invalid")
      (test/is (not (s/valid? ::css-spec/percentage [16.0 :%])) "anything else should be invalid"))

    (test/testing "-> distance"
      (test/testing "relative units"
        (test/is (s/valid? ::css-spec/em {:magnitude 16.0 :unit :em}))
        (test/is (s/valid? ::css-spec/ex {:magnitude 16.0 :unit :ex}))
        (test/is (s/valid? ::css-spec/ch {:magnitude 16.0 :unit :ch}))
        (test/is (s/valid? ::css-spec/rem {:magnitude 16.0 :unit :rem}))
        (test/is (s/valid? ::css-spec/vh {:magnitude 16.0 :unit :vh}))
        (test/is (s/valid? ::css-spec/vw {:magnitude 16.0 :unit :vw}))
        (test/is (s/valid? ::css-spec/vmin {:magnitude 16.0 :unit :vmin}))
        (test/is (s/valid? ::css-spec/vmax {:magnitude 16.0 :unit :vmax})))

      (test/testing "absolute units"
        (test/is (s/valid? ::css-spec/cm {:magnitude 16.0 :unit :cm}))
        (test/is (s/valid? ::css-spec/mm {:magnitude 16.0 :unit :mm}))
        (test/is (s/valid? ::css-spec/q {:magnitude 16.0 :unit :q}))
        (test/is (s/valid? ::css-spec/in {:magnitude 16.0 :unit :in}))
        (test/is (s/valid? ::css-spec/pc {:magnitude 16.0 :unit :pc}))
        (test/is (s/valid? ::css-spec/pt {:magnitude 16.0 :unit :pt}))
        (test/is (s/valid? ::css-spec/px {:magnitude 16.0 :unit :px}))))

    (test/testing "-> angular units"
      (test/is (s/valid? ::css-spec/deg {:magnitude 240 :unit :deg})
               "magnitude should be between 0 and 360 (inclusive)")
      (test/is (not (s/valid? ::css-spec/deg {:magnitude -14 :unit :deg}))
               "anything outside of 0 and 360 should be invalid")
      (test/is (s/valid? ::css-spec/grad {:magnitude 126 :unit :grad})
               "magnitude should be between 0 and 400 (inclusive)")
      (test/is (not (s/valid? ::css-spec/grad {:magnitude 644 :unit :grad}))
               "anything outside of 0 and 300 should be invalid")
      (test/is (s/valid? ::css-spec/rad {:magnitude 6.2 :unit :rad})
               "should be between 0 and 2pi")
      (test/is (not (s/valid? ::css-spec/rad {:magnitude 65.89 :unit :rad}))
               "anything outside of  0 and 2pi(inclusive) should be invalid")
      (test/is (s/valid? ::css-spec/turn {:magnitude 0.2 :unit :turn})
               "should be between 0 and 1 (inclusive)")
      (test/is (not (s/valid? ::css-spec/turn {:magnitude -0.3 :unit :turn}))
               "anything outside of  0 and 2pi should be invalid"))

    (test/testing "-> duration units"
      (test/is (s/valid? ::css-spec/s {:magnitude 78 :unit :s}))
      (test/is (s/valid? ::css-spec/ms {:magnitude 2301 :unit :ms})))

    (test/testing "-> frequency units"
      (test/is (s/valid? ::css-spec/hz {:magnitude 120 :unit :hz}))
      (test/is (s/valid? ::css-spec/khz {:magnitude 10 :unit :khz})))

    (test/testing "-> resolution units"
      (test/is (s/valid? ::css-spec/dpi {:magnitude 72 :unit :dpi}))
      (test/is (not (s/valid? ::css-spec/dpi {:magnitude -2 :unit :dpi})) "negative magnitude should be invalid")
      (test/is (s/valid? ::css-spec/dpcm {:magnitude 12 :unit :dpcm}))
      (test/is (not (s/valid? ::css-spec/dpcm {:magnitude -43 :unit :dpcm})) "negative magnitude should be invalid")
      (test/is (s/valid? ::css-spec/dppx {:magnitude 89 :unit :dppx}))
      (test/is (not (s/valid? ::css-spec/dppx {:magnitude -23 :unit :dppx})) "negative magnitude should be invalid")))

  (test/testing "-> unit groups"))


(test/deftest css-spec-property-tests
  (test/testing "css font specs"
    (test/testing "-> font family spec"
      ;; TODO Sub specs
      ;; FIXME proper output comparison
      (test/is (s/valid? ::css-spec/font-family ["helvetica"])) "coll of a single font as a string"
      (test/is (s/valid? ::css-spec/font-family [:serif])) "coll of a single font from generics"
      (test/is (s/valid? ::css-spec/font-family ["helvetica" :serif])) "coll of multiple mixed font options"
      (test/is (not (s/valid? ::css-spec/font-family "helvetica")) "string font should be invalid")
      (test/is (not (s/valid? ::css-spec/font-family :serif)) "keyword generic font should be invalid"))

    (test/testing "-> font weight spec"
      #_(test/testing "-> testing css-spec/multiple-of-100?"
          (test/is (true? (css-spec/multiple-of-100? 800)) "positive 100's, should be true")
          (test/is (true? (css-spec/multiple-of-100? 300)) "positive 100's, should be true")
          (test/is (true? (css-spec/multiple-of-100? -200)) "negative 100's should be true")
          (test/is (false? (css-spec/multiple-of-100? 90)) "not a multiple of 100, should be false")
          (test/is (false? (css-spec/multiple-of-100? 1110)) "not a multiple of 100, should be false")
          (test/is (false? (css-spec/multiple-of-100? -330)))) "not a multiple of 100, should be false"

      (test/testing "-> testing weight number"
        (test/is (s/valid? ::css-spec/weight-number 400))
        (test/is (s/valid? ::css-spec/weight-number 300))
        (test/is (not (s/valid? ::css-spec/weight-number -100)))
        (test/is (not (s/valid? ::css-spec/weight-number 1200)))
        (test/is (not (s/valid? ::css-spec/weight-number 50)))))

    (test/testing "-> properties map of just font specs"
      (test/is
        (s/valid?
          ::css-spec/properties
          {:font-family  ["helvetica" :serif]
           :font-weight  :normal
           :font-stretch :normal
           :font-style   :normal
           :font-size    {:magnitude 16.0 :unit :px}})))))