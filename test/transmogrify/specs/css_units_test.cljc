(ns transmogrify.specs.css-units-test
  (:require [transmogrify.css.units :as css-units]
            [transmogrify.specs.css-spec :as css-spec]
            [clojure.test :refer :all]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.spec.alpha :as s]))

(def number-of-tests 100)

(def number-gen (gen/one-of [gen/int gen/double]))

(def absolute-unit-gen
  (gen/one-of
    [(gen/return :cm)
     (gen/return :mm)
     (gen/return :Q)
     (gen/return :in)
     (gen/return :pc)
     (gen/return :pt)
     (gen/return :px)]))

(def relative-unit-gen
  (gen/one-of
    [(gen/return :em)
     (gen/return :ex)
     (gen/return :cap)
     (gen/return :ch)
     (gen/return :rem)
     (gen/return :ic)
     (gen/return :lh)
     (gen/return :rlh)
     (gen/return :vh)
     (gen/return :vw)
     (gen/return :vi)
     (gen/return :vb)
     (gen/return :vmin)
     (gen/return :vmax)]))

(def angular-unit-gen
  (gen/one-of
    [(gen/return :deg)
     (gen/return :grad)
     (gen/return :rad)
     (gen/return :turn)]))

(def time-unit-gen
  (gen/one-of
    [(gen/return :s)
     (gen/return :ms)]))

(def frequency-unit-gen
  (gen/one-of
    [(gen/return :Hz)
     (gen/return :kHz)]))

(def resolution-unit-gen
  (gen/one-of
    [(gen/return :dpi)
     (gen/return :dpcm)
     (gen/return :dppx)]))

(def unit-gen
  (gen/one-of
    [absolute-unit-gen
     relative-unit-gen
     angular-unit-gen
     time-unit-gen
     frequency-unit-gen
     resolution-unit-gen]))

(defspec css-unit-convert-fn-absolute-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit absolute-unit-gen)
            unit-to absolute-unit-gen]
           (s/valid? :transmogrify.specs.css-spec/units (#'css-units/convert input unit-to))))

#_(defspec css-unit-convert-fn-relative-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit relative-unit-gen)
            unit-to relative-unit-gen]
           (is Error (#'css-units/convert input unit-to))))

;; FIXME Circular values required?
;; issues are due to the mixing of ints and doubles as the magnitude
#_(defspec css-unit-convert-fn-angular-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit angular-unit-gen)
            unit-to angular-unit-gen]
           (s/valid? :transmogrify.specs.css-spec/units (#'css-units/convert input unit-to))))

(defspec css-unit-convert-fn-time-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude gen/int
                                :unit time-unit-gen)
            unit-to time-unit-gen]
           (s/valid? :transmogrify.specs.css-spec/units (#'css-units/convert input unit-to))))

;; fails with issues due to 0
#_(defspec css-unit-convert-fn-frequency-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude gen/pos-int
                                :unit frequency-unit-gen)
            unit-to frequency-unit-gen]
           (s/valid? :transmogrify.specs.css-spec/units (#'css-units/convert input unit-to))))

;; fails with a 0
#_(defspec css-unit-convert-fn-resolution-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude gen/pos-int
                                :unit resolution-unit-gen)
            unit-to resolution-unit-gen]
           (s/valid? :transmogrify.specs.css-spec/units (#'css-units/convert input unit-to))))

#_(defspec css-unit-convert-fn-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit unit-gen)
            unit-to unit-gen]
           (s/valid? :transmogrify.specs.css-spec/units (#'css-units/convert input unit-to))))