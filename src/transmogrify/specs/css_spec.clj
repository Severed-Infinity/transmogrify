(ns transmogrify.specs.css-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as s-gen]
            [clojure.test.check.generators :as gen]
            [spec-tools
             [core :as st-c]
             [data-spec :as st-ds]
             [spec :as spec]]))

;;; TODO move all s/form, s/exercise, s/conform, s/explain to testing file
(s/def ::css-wide-keywords (st-ds/spec ::css-wide-keywords (s/spec #{:initial :inherit :unset :revert})))

;;;;;; UNITS ;;;;;;;;;
;; https://drafts.csswg.org/css-values-3/
;; FIXME more work needs done on percentage
(defn pos-double? [x] (and (double? x) (pos? x)))

;;requires custom generator
(s/def ::magnitude
  (s/with-gen
    pos-double?
    #(gen/fmap (fn [n] (if (pos? n) n 0.1)) gen/double)))
(s/def ::unit #{:%})

;;; FIXME code coverage drop occurs here
;;; FIXME possible fix would be a conformer/generator that for string has a number between 0-100 conjoined with %
;;; FIXME issue seems to be with the map branch - needs to cover namespaced keys too, unit is the issue?
;; NOTE percentage cannot be negative as < 0 or 0, 0.00000001 is acceptable
(s/def ::percentage
  (st-ds/spec
    ::percentage
    (s/or
      :string (s/and spec/string? #(re-matches #"(\d+|\d+[.]\d+)%" %))
      :map (s/keys :req-un [::magnitude ::unit]))))

;;; DISTANCES
;;; RELATIVE
(s/def ::em (st-ds/spec ::em {:magnitude spec/double? :unit (s/spec #{:em})}))
(s/def ::ex (st-ds/spec ::ex {:magnitude spec/double? :unit (s/spec #{:ex})}))
(s/def ::ch (st-ds/spec ::ch {:magnitude spec/double? :unit (s/spec #{:ch})}))
(s/def ::rem (st-ds/spec ::rem {:magnitude spec/double? :unit (s/spec #{:rem})}))
(s/def ::vh (st-ds/spec ::vh {:magnitude spec/double? :unit (s/spec #{:vh})}))
(s/def ::vw (st-ds/spec ::vw {:magnitude spec/double? :unit (s/spec #{:vw})}))
(s/def ::vmin (st-ds/spec ::vmin {:magnitude spec/double? :unit (s/spec #{:vmin})}))
(s/def ::vmax (st-ds/spec ::vmax {:magnitude spec/double? :unit (s/spec #{:vmax})}))

;;; ABSOLUTE
(s/def ::cm (st-ds/spec ::cm {:magnitude spec/double? :unit (s/spec #{:cm})}))
(s/def ::mm (st-ds/spec ::mm {:magnitude spec/double? :unit (s/spec #{:mm})}))
(s/def ::q (st-ds/spec ::q {:magnitude spec/double? :unit (s/spec #{:q})}))
(s/def ::in (st-ds/spec ::in {:magnitude spec/double? :unit (s/spec #{:in})}))
(s/def ::pc (st-ds/spec ::pc {:magnitude spec/double? :unit (s/spec #{:pc})}))
(s/def ::pt (st-ds/spec ::pt {:magnitude spec/double? :unit (s/spec #{:pt})}))
(s/def ::px (st-ds/spec ::px {:magnitude spec/double? :unit (s/spec #{:px})}))

;;; ANGULAR
;;; small code coverage drop - fixed with s/conform
(s/def ::deg (st-ds/spec ::deg {:magnitude (s/int-in 0 361) :unit (s/spec #{:deg})}))
;;; small code coverage drop - fixed with s/conform
(s/def ::grad (st-ds/spec ::grad {:magnitude (s/int-in 0 401) :unit (s/spec #{:grad})}))
;; FIXME spec/double? -> 0-2pi
;; FIXME spec/double? issue with whole ints
(s/def ::rad (st-ds/spec ::rad {:magnitude (s/and spec/double? #(< 0 % 6.2831853072)) :unit (s/spec #{:rad})}))
;; FIXME the range is a bit strange
(s/def ::turn (st-ds/spec ::turn {:magnitude (s/and spec/number? #(< 0 % 1.01)) :unit (s/spec #{:turn})}))

;;; DURATION
(s/def ::s (st-ds/spec ::s {:magnitude spec/int? :unit (s/spec #{:s})}))
(s/def ::ms (st-ds/spec ::ms {:magnitude spec/int? :unit (s/spec #{:ms})}))

;;; FREQUENCY
(s/def ::hz (st-ds/spec ::hz {:magnitude spec/pos-int? :unit (s/spec #{:hz})}))
(s/def ::khz (st-ds/spec ::khz {:magnitude spec/pos-int? :unit (s/spec #{:khz})}))

;;; RESOLUTION
(s/def ::dpi (st-ds/spec ::dpi {:magnitude spec/pos-int? :unit (s/spec #{:dpi})}))
(s/def ::dpcm (st-ds/spec ::dpcm {:magnitude spec/pos-int? :unit (s/spec #{:dpcm})}))
(s/def ::dppx (st-ds/spec ::dppx {:magnitude spec/pos-int? :unit (s/spec #{:dppx})}))

;;; UNIT GROUPS
;; FIXME distance vs length naming
(s/def ::relative-distance-units
  (s/or :em ::em :ex ::ex :ch ::ch :rem ::rem :vh ::vh :vw ::vw :vmin ::vmin :vmax ::vmax))
(s/def ::absolute-distance-units (s/or :cm ::cm :mm ::mm :q ::q :in ::in :pc ::pc :pt ::pt :px ::px))
(s/def ::distance-units (s/or :relative ::relative-distance-units :absolute ::absolute-distance-units))
(s/def ::duration-units (s/or :s ::s :ms ::ms))
(s/def ::angular-units (s/or :deg ::deg :grad ::grad :rad ::rad :turn ::turn))
(s/def ::frequency-units (s/or :hz ::hz :khz ::khz))
(s/def ::resolution-units (s/or :dpi ::dpi :dpcm ::dpcm :dppx ::dppx))

;;; FONT SPECIFIC UNITS
(s/def ::absolute-size
  (st-ds/spec ::absolute-size (s/spec #{:xx-small :x-small :small :medium :large :x-large :xx-large})))
(s/def ::relative-size (st-ds/spec ::relative-size (s/spec #{:larger :smaller})))

;;; PROPERTIES
;;; FONT
(s/def ::named spec/string?)
(s/def ::generic
  (st-ds/spec
    ::generic
    (s/spec #{:serif :sans-serif :cursive :fantasy :monospace :system-ui :emoji :math :fangsong})))

(s/def ::font-family
  (st-ds/spec
    ::font-family
    [(s/or :named ::named :generic ::generic :global ::css-wide-keywords)]))

;;; small code coverage drop - fixed with a simple = test
(defn multiple-of-100? [n] (zero? (mod n 100)))
;;FIXME possibly going to cause naming conflict later
;;TODO number keywords https://drafts.csswg.org/css-fonts-4/#valdef-font-weight-number
;;; small code coverage drop - fixed with s/conform
(s/def ::weight-number (st-ds/spec ::number (s/and (s/int-in 0 1000) multiple-of-100?)))
(s/def ::weight-value (st-ds/spec ::value (s/spec #{:normal :bold :bolder :lighter})))

(s/def ::font-weight
  (st-ds/spec
    ::font-weight
    (s/or
      :value ::weight-value
      :number ::weight-number)
    {:default :normal}))

(s/def ::stretch-value
  (st-ds/spec
    ::stretch-value
    (s/spec
      #{:normal :ultra-condensed :extra-condensed :condensed
        :semi-condensed :semi-expanded :expanded :extra-expanded :ultra-expanded})))

;;; FIXME code coverage affected by percentage
(s/def ::font-stretch
  (st-ds/spec
    ::font-stretch
    (s/or
      ;;FIXME requires globals
      :value ::stretch-value
      :global ::css-wide-keywords
      ;;NOTE percentage is a css4 spec only thing
      :percentage ::percentage)))

;; FIXME decide one whether to keep oblique with angle or not
(s/def ::font-style
  (st-ds/spec
    ::font-style
    (s/or
      :value (s/spec #{:normal :italic :oblique})
      :oblique-angle (s/cat :value (s/spec #{:oblique}) :angle ::angular-units))))

;;; FIXME code coverage affected by percentage
(s/def ::font-size
  (st-ds/spec
    ::font-size
    (s/or
      :absolute ::absolute-size
      :relative ::relative-size
      :length ::distance-units
      :percentage ::percentage)))

;;; FIXME code coverage affected by percentage
(s/def ::font-min-size
  (st-ds/spec
    ::font-min-size
    (s/or
      :absolute ::absolute-size
      :relative ::relative-size
      :length ::distance-units
      :percentage ::percentage)))

;;; FIXME code coverage affect by percentage
(s/def ::font-max-size
  (st-ds/spec
    ::font-max-size
    (s/or
      :absolute ::absolute-size
      :relative ::relative-size
      :length ::distance-units
      :percentage ::percentage
      :infinity (s/spec #{:infinity}))))

(s/def ::font-size-adjust
  (st-ds/spec
    ::font-size-adjust
    (s/or
      :none (s/spec #{:none})
      :number spec/number?)))

;;; FIXME code coverage of the rest is affected by percentage
;;; FIXME font is mostly likely going to lack code coverage due to optionals
(s/def ::font
  (st-ds/spec
    ::font
    (s/or
      :font-vector (s/cat
                     :font-style (s/? ::font-style)
                     ;; TODO font-variant, come back to this when it is done
                     #_(:font-variant (s/? ::font-variant))
                     :font-weight (s/? ::font-weight)
                     :font-stretch (s/? ::font-stretch)
                     :font-size ::font-size
                     ;; TODO line-height, come back to this when it is done
                     #_(:line-height (s/? ::line-height))
                     :font-family ::font-family)
      :system-fonts (s/spec #{:caption :icon :menu :message-box :small-caption :status-bar}))))

(s/def ::font-synthesis
  (st-ds/spec
    ::font-synthesis
    (s/or
      :none #{:none}
      :vector (s/coll-of (s/spec #{:weight :small-caps :style})))))

(s/def ::properties
  (st-ds/spec
    ::properties
    {(st-ds/opt :font-family)    ::font-family
     (st-ds/opt :font-weight)    ::font-weight
     (st-ds/opt :font-stretch)   ::font-stretch
     (st-ds/opt :font-style)     ::font-style
     (st-ds/opt :font-size)      ::font-size
     (st-ds/opt :font-min-size)  ::font-min-size
     (st-ds/opt :font-max-size)  ::font-max-size
     (st-ds/opt :font)           ::font
     (st-ds/opt :font-synthesis) ::font-synthesis}))