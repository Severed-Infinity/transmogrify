(ns transmogrify.specs.css-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [spec-tools
             [core :as st-c]
             [data-spec :as st-ds]
             [spec :as spec]]))

;;; TODO move all s/form, s/exercise, s/conform, s/explain to testing file
(s/def ::css-wide-keywords (st-ds/spec ::css-wide-keywords (s/spec #{:initial :inherit :unset :revert})))
(s/form ::css-wide-keywords)
(s/exercise ::css-wide-keywords)

;;;;;; UNITS ;;;;;;;;;
;; https://drafts.csswg.org/css-values-3/
;; FIXME more work needs done on percentage
(s/def ::magnitude spec/double?)
(s/def ::unit #{:%})

(s/form ::magnitude)
(s/exercise ::magnitude)
(s/form ::unit)
(s/exercise ::unit)

;; FIXME look at this
;;#_(:vector (s/cat :magnitude spec/double? :unit #{:%}))
;;#_(:map {:magnitude spec/double? :unit (s/spec #{:per})})
;;; FIXME code coverage drop occurs here
;;; FIXME possible fix would be a conformer/generator that for string has a number between 0-100 conjoined with %
;;; FIXME issue seems to be with the map branch - needs to cover namespaced keys too, unit is the issue?
(s/def ::percentage
  (st-ds/spec
    ::percentage
    (s/or
      :string (s/and spec/string? #(re-matches #"(\d+|\d+[.]\d+)%" %))
      :map (s/keys :req-un [::magnitude ::unit]))))

;; Removing conform and explain-data causes a massive code coverage drop
;; Exercise doesn't work 100% of the time, must affect code coverage
(s/form ::percentage)
(s/exercise ::percentage)
(s/conform ::percentage "100%")
(s/conform ::percentage "65.45%")
(s/conform ::percentage "65.45")
(s/conform ::percentage "15")
(s/conform ::percentage 15)
;;; FIXME map format is lacking code coverage
(s/conform ::percentage {::magnitude 16.0 ::unit :%})
(s/conform ::percentage {::magnitude 62 ::unit :%})
(s/conform ::percentage {:magnitude 16.0 :unit :%})
(s/conform ::percentage {::magnitude 16.0 :unit :%})
(s/conform ::percentage {:magnitude 16.0 ::unit :%})
(s/conform ::percentage {::magnitude 16.0})
(s/conform ::percentage {::unit :%})
(s/conform ::percentage [16.0 :%])
(s/explain-data ::percentage {::magnitude 16.0 ::unit :%})
(s/explain-data ::percentage {::magnitude 62 ::unit :%})
(s/explain-data ::percentage {:magnitude 16.0 :unit :%})
(s/explain-data ::percentage {::magnitude 16.0 :unit :%})
(s/explain-data ::percentage {:magnitude 16.0 ::unit :%})
(s/explain-data ::percentage {::magnitude 16.0})
(s/explain-data ::percentage {::unit :%})
(s/explain-data ::percentage [16.0 :%])
(s/explain-data ::percentage [:magnitude 16.0 :unit :%])
(s/conform ::percentage {:magnitude -73 :unit :%})
(s/explain-data ::percentage {:magnitude 16 :unit :%})
(s/explain-data ::percentage {::magnitude 16.0 ::unit :%})

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

(s/form ::em)
(s/exercise ::em)
(s/form ::ex)
(s/exercise ::ex)
(s/form ::ch)
(s/exercise ::ch)
(s/form ::rem)
(s/exercise ::rem)
(s/form ::vh)
(s/exercise ::vh)
(s/form ::vw)
(s/exercise ::vw)
(s/form ::vmin)
(s/exercise ::vmin)
(s/form ::vmax)
(s/exercise ::vmax)

;;; ABSOLUTE
(s/def ::cm (st-ds/spec ::cm {:magnitude spec/double? :unit (s/spec #{:cm})}))
(s/def ::mm (st-ds/spec ::mm {:magnitude spec/double? :unit (s/spec #{:mm})}))
(s/def ::q (st-ds/spec ::q {:magnitude spec/double? :unit (s/spec #{:q})}))
(s/def ::in (st-ds/spec ::in {:magnitude spec/double? :unit (s/spec #{:in})}))
(s/def ::pc (st-ds/spec ::pc {:magnitude spec/double? :unit (s/spec #{:pc})}))
(s/def ::pt (st-ds/spec ::pt {:magnitude spec/double? :unit (s/spec #{:pt})}))
(s/def ::px (st-ds/spec ::px {:magnitude spec/double? :unit (s/spec #{:px})}))

(s/form ::cm)
(s/exercise ::cm)
(s/form ::mm)
(s/exercise ::mm)
(s/form ::q)
(s/exercise ::q)
(s/form ::in)
(s/exercise ::in)
(s/form ::pc)
(s/exercise ::pc)
(s/form ::pt)
(s/exercise ::pt)
(s/form ::px)
(s/exercise ::px)

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

(s/form ::deg)
(s/exercise ::deg)
(s/conform ::deg {:magnitude 15 :unit :deg})

(s/form ::grad)
(s/exercise ::grad)
(s/conform ::grad {:magnitude 300 :unit :grad})

(s/form ::rad)
(s/exercise ::rad)
(s/conform ::rad {:magnitude 5.4 :unit :rad})

(s/form ::turn)
(s/exercise ::turn)
(s/conform ::turn {:magnitude 0.45 :unit :turn})

;;; DURATION
(s/def ::s (st-ds/spec ::s {:magnitude spec/int? :unit (s/spec #{:s})}))
(s/def ::ms (st-ds/spec ::ms {:magnitude spec/int? :unit (s/spec #{:ms})}))

(s/form ::s)
(s/exercise ::s)
(s/form ::ms)
(s/exercise ::ms)

;;; FREQUENCY
(s/def ::hz (st-ds/spec ::hz {:magnitude spec/pos-int? :unit (s/spec #{:hz})}))
(s/def ::khz (st-ds/spec ::khz {:magnitude spec/pos-int? :unit (s/spec #{:khz})}))

(s/form ::hz)
(s/exercise ::hz)
(s/form ::khz)
(s/exercise ::khz)

;;; RESOLUTION
(s/def ::dpi (st-ds/spec ::dpi {:magnitude spec/pos-int? :unit (s/spec #{:dpi})}))
(s/def ::dpcm (st-ds/spec ::dpcm {:magnitude spec/pos-int? :unit (s/spec #{:dpcm})}))
(s/def ::dppx (st-ds/spec ::dppx {:magnitude spec/pos-int? :unit (s/spec #{:dppx})}))

(s/form ::dpi)
(s/exercise ::dpi)
(s/form ::dpcm)
(s/exercise ::dpcm)
(s/form ::dppx)
(s/exercise ::dppx)

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

(s/form ::relative-distance-units)
(s/exercise ::relative-distance-units)
(s/form ::absolute-distance-units)
(s/exercise ::absolute-distance-units)
(s/form ::distance-units)
(s/exercise ::distance-units)
(s/form ::duration-units)
(s/exercise ::duration-units)
(s/form ::frequency-units)
(s/exercise ::frequency-units)
(s/form ::resolution-units)
(s/exercise ::resolution-units)
(s/form ::angular-units)
(s/exercise ::angular-units)

;;; FONT SPECIFIC UNITS
(s/def ::absolute-size
  (st-ds/spec ::absolute-size (s/spec #{:xx-small :x-small :small :medium :large :x-large :xx-large})))
(s/def ::relative-size (st-ds/spec ::relative-size (s/spec #{:larger :smaller})))

(s/form ::absolute-size)
(s/exercise ::absolute-size)
(s/form ::relative-size)
(s/exercise ::relative-size)

;;; PROPERTIES
;;; FONT
(s/def ::named spec/string?)
(s/def ::generic
  (st-ds/spec
    ::generic
    (s/spec #{:serif :sans-serif :cursive :fantasy :monospace :system-ui :emoji :math :fangsong})))

(s/form ::named)
(s/exercise ::named)
(s/form ::generic)
(s/exercise ::generic)

(s/def ::font-family
  (st-ds/spec
    ::font-family
    [(s/or :named ::named :generic ::generic :global ::css-wide-keywords)]))

(s/form ::font-family)
(s/exercise ::font-family)
(s/conform ::font-family [:serif :cursive "helvetica"])

;;; small code coverage drop - fixed with a simple = test
(defn multiple-of-100? [n] (zero? (mod n 100)))
;;FIXME possibly going to cause naming conflict later
;;TODO number keywords https://drafts.csswg.org/css-fonts-4/#valdef-font-weight-number
;;; small code coverage drop - fixed with s/conform
(s/def ::weight-number (st-ds/spec ::number (s/and (s/int-in 0 1000) multiple-of-100?)))
(s/def ::weight-value (st-ds/spec ::value (s/spec #{:normal :bold :bolder :lighter})))

;; FIXME move to testing file and do proper testing
(true? (multiple-of-100? 100))
(false? (multiple-of-100? -100))

(s/form ::weight-number)
(s/exercise ::weight-number)
(s/conform ::weight-number 800)

(s/form ::weight-value)
(s/exercise ::weight-value)

(s/def ::font-weight
  (st-ds/spec
    ::font-weight
    (s/or
      :value ::weight-value
      :number ::weight-number)
    {:default :normal}))

(s/form ::font-weight)
(s/exercise ::font-weight)
(s/conform ::font-weight 100)

(s/def ::stretch-value
  (st-ds/spec
    ::stretch-value
    (s/spec
      #{:normal :ultra-condensed :extra-condensed :condensed
        :semi-condensed :semi-expanded :expanded :extra-expanded :ultra-expanded})))

(s/form ::stretch-value)
(s/exercise ::stretch-value)

;;; FIXME code coverage affected by percentage
(s/def ::font-stretch
  (st-ds/spec
    ::font-stretch
    (s/or
      :value ::stretch-value
      :percentage ::percentage)))

(s/form ::font-stretch)
(s/exercise ::font-stretch)
(s/conform ::font-stretch "100%")

;; FIXME decide one whether to keep oblique with angle or not
(s/def ::font-style
  (st-ds/spec
    ::font-style
    (s/or
      :value (s/spec #{:normal :italic :oblique})
      :oblique-angle (s/cat :value (s/spec #{:oblique}) :angle ::angular-units))))

(s/form ::font-style)
(s/exercise ::font-style)
(s/conform ::font-style [:oblique {:magnitude 10 :unit :deg}])
(s/explain-data ::font-style [:oblique {:magnitude 10 :unit :deg}])


;;; FIXME code coverage affected by percentage
(s/def ::font-size
  (st-ds/spec
    ::font-size
    (s/or
      :absolute ::absolute-size
      :relative ::relative-size
      :length ::distance-units
      :percentage ::percentage)))

(s/form ::font-size)
(s/exercise ::font-size)
(s/conform ::font-size {:magnitude 16.0 :unit :px})
(s/explain-data ::font-size {:magnitude 16.0 :unit :%})

;;; FIXME code coverage affected by percentage
(s/def ::font-min-size
  (st-ds/spec
    ::font-min-size
    (s/or
      :absolute ::absolute-size
      :relative ::relative-size
      :length ::distance-units
      :percentage ::percentage)))

(s/form ::font-min-size)
(s/exercise ::font-min-size)
(s/conform ::font-min-size :small)

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

(s/form ::font-max-size)
(s/exercise ::font-max-size)
(s/conform ::font-max-size :infinity)

(s/def ::font-size-adjust
  (st-ds/spec
    ::font-size-adjust
    (s/or
      :none (s/spec #{:none})
      :number spec/number?)))

(s/form ::font-size-adjust)
(s/exercise ::font-size-adjust)
(s/conform ::font-size-adjust :none)

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

(s/form ::font)
(s/exercise ::font)
(s/conform ::font [{:magnitude 16.0 :unit :em} ["helvetica"]])
(s/conform ::font [:normal 400 :normal {:magnitude 16.0 :unit :px} ["helvetica"]])
(s/explain-data ::font [{:magnitude 16.0 :unit :em} ["helvetica"]])

(s/def ::font-synthesis
  (st-ds/spec
    ::font-synthesis
    (s/or
      :none #{:none}
      :vector [(s/spec #{:weight :small-caps :style})])))

(s/form ::font-synthesis)
(s/exercise ::font-synthesis)
#_(s/conform ::font-synthesis)

(s/def ::properties
  (st-ds/spec
    ::properties
    {(st-ds/opt :font-family)   ::font-family
     (st-ds/opt :font-weight)   ::font-weight
     (st-ds/opt :font-stretch)  ::font-stretch
     (st-ds/opt :font-style)    ::font-style
     (st-ds/opt :font-size)     ::font-size
     (st-ds/opt :font-min-size) ::font-min-size
     (st-ds/opt :font-max-size) ::font-max-size
     (st-ds/opt :font)          ::font}))

(s/form ::properties)
(s/exercise ::properties)
(st-c/conform
  ::properties
  {:font-family   ["helvetica" :serif]
   :font-weight   :normal
   :font-stretch  :normal
   :font-style    :normal
   :font-size     {:magnitude 16.0 :unit :px}
   :font-min-size {:magnitude 12.0 :unit :px}
   :font-max-size {:magnitude 20.0 :unit :px}
   :font          [:normal 400 :normal {:magnitude 16.0 :unit :px} ["helvetica"]]})