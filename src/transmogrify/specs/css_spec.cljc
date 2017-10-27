(ns transmogrify.specs.css-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as s-gen]
            [clojure.test.check.generators :as gen]
            [spec-tools
             [core :as st-c]
             [data-spec :as st-ds]
             [spec :as spec]]))

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

;;; FIXME issue seems to be with the map branch - needs to cover namespaced keys too, unit is the issue?
;; NOTE percentage cannot be negative as < 0 or 0, 0.00000001 is acceptable
(s/def ::percentage
  (st-ds/spec
    ::percentage
    (s/or
      :string (s/with-gen
                (s/and spec/string? #(re-matches #"(\d+|\d+[.]\d+)%" %))
                #(gen/fmap
                   (fn [[n1 n2]] (str n1 "." n2 "%"))
                   (gen/tuple gen/pos-int gen/pos-int)))
      ;; FIXME ISSUE coverage is only affected by namespaced keys
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
(s/def ::cap any?)
(s/def ::ic any?)
(s/def ::lh any?)
(s/def ::rlh any?)
(s/def ::vi any?)
(s/def ::vb any?)

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
(s/def ::rad (st-ds/spec ::rad {:magnitude (s/and spec/number? #(< 0 % 6.2831853072)) :unit (s/spec #{:rad})}))
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
(s/def ::relative-distance-units (s/or :em ::em :ex ::ex :ch ::ch :rem ::rem :vh ::vh :vw ::vw :vmin ::vmin :vmax ::vmax))
(s/def ::absolute-distance-units (s/or :cm ::cm :mm ::mm :q ::q :in ::in :pc ::pc :pt ::pt :px ::px))
(s/def ::distance-units (s/or :relative ::relative-distance-units :absolute ::absolute-distance-units))
(s/def ::duration-units (s/or :s ::s :ms ::ms))
(s/def ::angular-units (s/or :deg ::deg :grad ::grad :rad ::rad :turn ::turn))
(s/def ::frequency-units (s/or :hz ::hz :khz ::khz))
(s/def ::resolution-units (s/or :dpi ::dpi :dpcm ::dpcm :dppx ::dppx))

;;; FONT SPECIFIC UNITS
(s/def ::absolute-size (st-ds/spec ::absolute-size (s/spec #{:xx-small :x-small :small :medium :large :x-large :xx-large})))
(s/def ::relative-size (st-ds/spec ::relative-size (s/spec #{:larger :smaller})))

;;; PROPERTIES

;; TODO REMAINDER OF FONTS
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

;;FIXME possibly going to cause naming conflict later
;;TODO number keywords https://drafts.csswg.org/css-fonts-4/#valdef-font-weight-number
(s/def ::weight-number (st-ds/spec ::number (s/spec #{100 200 300 400 500 600 700 800 900})))
(s/def ::weight-value (st-ds/spec ::value (s/spec #{:normal :bold :bolder :lighter})))

(s/def ::font-weight
  (st-ds/spec
    ::font-weight
    (s/or
      :value ::weight-value
      :number ::weight-number)))

(s/def ::stretch-value
  (st-ds/spec
    ::stretch-value
    (s/spec
      #{:normal :ultra-condensed :extra-condensed :condensed
        :semi-condensed :semi-expanded :expanded :extra-expanded :ultra-expanded})))

(s/def ::font-stretch
  (st-ds/spec
    ::font-stretch
    (s/or
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

(s/def ::font-size
  (st-ds/spec
    ::font-size
    (s/or
      :absolute ::absolute-size
      :relative ::relative-size
      :length ::distance-units
      :percentage ::percentage)))

(s/def ::font-min-size
  (st-ds/spec
    ::font-min-size
    (s/or
      :absolute ::absolute-size
      :relative ::relative-size
      :length ::distance-units
      :percentage ::percentage)))

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

;; FIXME string has some strict conditions to be met https://www.w3.org/TR/css-fonts-3/#feature-tag-value
(s/def ::feature-tag-value (s/cat :string string? :value (s/? (s/or :int int? :key #{:on :off}))))
(s/def ::font-feature-settings
  (st-ds/spec ::font-feature-settings
              (s/or :normal (s/spec #{:normal})
                    :feature-tag-value ::feature-tag-value)))
(s/def ::font-kerning (st-ds/spec ::font-kerning (s/spec #{:auto :normal :none})))
;; FIXME string has some strict conditions https://www.w3.org/TR/css-fonts-3/#propdef-font-language-override
(s/def ::font-language-override (st-ds/spec ::font-language-override (s/or :normal (s/spec #{:normal}) :string string?)))

;; TODO @font-face & @font-feature-values rules

;; TODO font variant
;; FONT VARIANT
(s/def ::font-variant any?)
(s/def ::font-variant-alternatives any?)
(s/def ::font-variant-caps
  (st-ds/spec ::font-variant-caps
              (s/spec #{:normal :small-caps :all-small-caps :petite-caps :all-petite-caps :unicase :titling-caps})))
(s/def ::font-variant-east-asian any?)
(s/def ::font-variant-ligatures any?)

(s/def ::numeric-figure-values #{:lining-numbers :oldstyle-nums})
(s/def ::numeric-spacing-values #{:proportional-nums :tabular-nums})
(s/def ::numeric-fraction-values #{:diagonal-fractions :stack-fractions})
(s/def ::numeric-other-values #{:ordinal :slashed-zero})
(s/def ::font-variant-numeric
  (st-ds/spec ::font-variant-numeric
              (s/or :normal (s/spec #{:normal})
                    :numeric-values (s/+ (s/or :figure ::numeric-figure-values
                                               :spacing ::numeric-spacing-values
                                               :fraction ::numeric-fraction-values
                                               :other ::numeric-other-values)))))

(s/def ::font-variant-position (st-ds/spec ::font-variant-position (s/spec #{:normal :sub :super})))

;; TODO colour and background
;; COLOUR
;; TODO leave till last?
(s/def ::color any?)
(s/def ::opacity any?)

;; BACKGROUND
(s/def ::background-color any?)
(s/def ::background-image any?)
(s/def ::background-repeat any?)
(s/def ::background-attachment any?)
(s/def ::background-position any?)
(s/def ::background-blend-mode any?)
(s/def ::background-clip any?)
(s/def ::background-origin any?)
(s/def ::background-size any?)

;; FIXME lot's to add to background property
(s/def ::background (s/cat :color ::background-color
                           :image ::background-image
                           :repeat ::background-repeat
                           :attachment ::background-attachment
                           :position ::background-position))

;; TODO border
;; BORDER
(s/def ::border-top-width any?)
(s/def ::border-right-width any?)
(s/def ::border-bottom-width any?)
(s/def ::border-left-width any?)
(s/def ::border-width any?)

(s/def ::border-top-color any?)
(s/def ::border-right-color any?)
(s/def ::border-bottom-color any?)
(s/def ::border-left-color any?)
(s/def ::border-color any?)

(s/def ::border-top-right-radius any?)
(s/def ::border-top-left-radius any?)
(s/def ::border-bottom-right-radius any?)
(s/def ::border-bottom-left-radius any?)
(s/def ::border-radius any?)

(s/def ::border-top-style any?)
(s/def ::border-right-style any?)
(s/def ::border-bottom-style any?)
(s/def ::border-left-style any?)
(s/def ::border-style any?)

(s/def ::border-image-outset any?)
(s/def ::border-image-repeat any?)
(s/def ::border-image-slice any?)
(s/def ::border-image-source any?)
(s/def ::border-image-width any?)
(s/def ::border-image any?)

(s/def ::border-top any?)
(s/def ::border-right any?)
(s/def ::border-bottom any?)
(s/def ::border-left any?)
(s/def ::border any?)

(s/def ::box-decoration-break any?)
(s/def ::box-shadow any?)

;; TODO box
;; BOX
(s/def ::margin-top any?)
(s/def ::margin-right any?)
(s/def ::margin-bottom any?)
(s/def ::margin-left any?)
(s/def ::margin any?)

(s/def ::padding-top any?)
(s/def ::padding-right any?)
(s/def ::padding-bottom any?)
(s/def ::padding-left any?)
(s/def ::padding any?)

(s/def ::top any?)
(s/def ::right any?)
(s/def ::bottom any?)
(s/def ::left any?)

(s/def ::max-height any?)
(s/def ::min-height any?)
(s/def ::max-width any?)
(s/def ::min-width any?)
(s/def ::width any?)
(s/def ::height any?)

(s/def ::overflow any?)
(s/def ::overflow-x any?)
(s/def ::overflow-y any?)

(s/def ::visibility any?)
#_(s/def ::vertical-align any?)
(s/def ::z-index any?)
(s/def ::float any?)
(s/def ::clear any?)
(s/def ::clip any?)

;; TODO flexible box
;; FLEXIBLE BOX
(s/def ::align-content any?)
(s/def ::align-items any?)
(s/def ::align-self any?)

(s/def ::flex-basis any?)
(s/def ::flex-direction any?)
(s/def ::flex-flow any?)
(s/def ::flex-grow any?)
(s/def ::flex-shrink any?)
(s/def ::flex-wrap any?)
(s/def ::flex any?)
(s/def ::flex-item-align any?)
(s/def ::flex-line-pack any?)
(s/def ::flex-order any?)
(s/def ::flex-pack any?)

(s/def ::justify-content any?)
(s/def ::order any?)

;; TODO grid layout
;; GRID LAYOUT
(s/def ::grid any?)
(s/def ::grid-area any?)
(s/def ::grid-auto-columns any?)
(s/def ::grid-auto-flow any?)
(s/def ::grid-auto-position any?)
(s/def ::grid-auto-rows any?)
(s/def ::grid-column any?)
(s/def ::grid-column-start any?)
(s/def ::grid-column-end any?)
(s/def ::grid-row any?)
(s/def ::grid-row-start any?)
(s/def ::grid-row-end any?)
(s/def ::grid-template any?)
(s/def ::grid-template-areas any?)
(s/def ::grid-template-rows any?)
(s/def ::grid-template-columns any?)

;; TODO text
;; TEXT
(s/def ::word-spacing any?)
(s/def ::letter-spacing any?)
#_(s/def ::text-decoration any?)
;; FIXME duplicate of vertical align, decide which group it belongs too
(s/def ::vertical-align any?)
(s/def ::text-transform any?)
(s/def ::text-align any?)
(s/def ::text-indent any?)
(s/def ::line-height any?)
(s/def ::hanging-punctuation any?)
(s/def ::hyphens any?)
(s/def ::line-break any?)
(s/def ::overflow-wrap any?)
(s/def ::tab-size any?)
(s/def ::text-align-last any?)
(s/def ::text-combine-upright any?)
(s/def ::text-justify any?)
(s/def ::white-space any?)
(s/def ::word-break any?)
(s/def ::word-spacing any?)
(s/def ::word-wrap any?)
(s/def ::text-space-collapse any?)

;; TODO text decoration
;; TEXT DECORATION
(s/def ::text-decoration any?)
(s/def ::text-decoration-color any?)
(s/def ::text-decoration-line any?)
(s/def ::text-decoration-style any?)
(s/def ::text-shadow any?)
(s/def ::text-underline-position any?)
(s/def ::text-decoration-skip any?)
(s/def ::text-emphasis any?)
(s/def ::text-emphasis-color any?)
(s/def ::text-emphasis-style any?)
(s/def ::text-emphasis-position any?)

;; TODO writing modes
;; WRITING MODES
(s/def ::direction any?)
(s/def ::text-orientation any?)
#_(s/def ::text-combine-upright any?)
(s/def ::unicode-bidi any?)
(s/def ::user-select any?)
(s/def ::writing-mode any?)

;; TODO table
;; TABLE
(s/def ::border-collapse any?)
(s/def ::border-spacing any?)
(s/def ::caption-side any?)
(s/def ::empty-cell any?)
(s/def ::table-layout any?)

;; TODO lists and counters
;; LISTS & COUNTERS
(s/def ::counter-increment any?)
(s/def ::counter-reset any?)
(s/def ::list-style any?)
(s/def ::list-style-type any?)
(s/def ::list-style-image any?)
(s/def ::list-style-position any?)

;; TODO animation
;; ANIMATION
(s/def ::animation any?)
(s/def ::animation-delay any?)
(s/def ::animation-direction any?)
(s/def ::animation-duration any?)
(s/def ::animation-fill-mode any?)
(s/def ::animation-iteration-count any?)
(s/def ::animation-name any?)
(s/def ::animation-play-state any?)
(s/def ::animation-timing-function any?)
;; TODO @keyframes rule

;; TODO transform
;; TRANSFORM
(s/def ::backface-visibility any?)
(s/def ::perspective any?)
(s/def ::perspective-origin any?)
(s/def ::transform any?)
(s/def ::transform-origin any?)
(s/def ::transform-style any?)

;; TODO transitions
;; TRANSITIONS
(s/def ::transition any?)
(s/def ::transition-property any?)
(s/def ::transition-duration any?)
(s/def ::transition-timing-function any?)
(s/def ::transition-delay any?)

;; TODO basic user interface
;; BASIC USER INTERFACE
(s/def ::box-sizing any?)
(s/def ::display any?)
(s/def ::content any?)
(s/def ::cursor any?)
(s/def ::ime-mode any?)
(s/def ::nav-down any?)
(s/def ::nav-up any?)
(s/def ::nav-left any?)
(s/def ::nav-right any?)
(s/def ::nav-index any?)
(s/def ::outline any?)
(s/def ::outline-color any?)
(s/def ::outline-offset any?)
(s/def ::outline-style any?)
(s/def ::outline-width any?)
(s/def ::resize any?)
(s/def ::text-overflow any?)

;; TODO multi-column layout
;; MULTI-COLUMN LAYOUT
(s/def ::break-after any?)
(s/def ::break-before any?)
(s/def ::break-inside any?)
(s/def ::column-count any?)
(s/def ::column-fill any?)
(s/def ::column-gap any?)
(s/def ::column-rule any?)
(s/def ::column-rule-color any?)
(s/def ::column-rule-width any?)
(s/def ::column-span any?)
(s/def ::column-width any?)
(s/def ::columns any?)

;; TODO paged media
;; PAGED MEDIA
(s/def ::windows any?)
(s/def ::orphans any?)
(s/def ::page-break-after any?)
(s/def ::page-break-before any?)
(s/def ::page-break-inside any?)

(s/def ::marks any?)
(s/def ::quotes any?)

;; TODO filter effects
;; FILTER EFFECTS
(s/def ::filter any?)

;; TODO image values and replaced content
;; IMAGE VALUES & REPLACED CONTENT
(s/def ::image-orientation any?)
(s/def ::image-rendering any?)
(s/def ::image-resolution any?)
(s/def ::object-fit any?)
(s/def ::object-position any?)

;; TODO masking
;; MASKING
(s/def ::mask any?)
(s/def ::mask-type any?)

;; TODO speech
;; SPEECH
(s/def ::azimuth any?)
(s/def ::cue any?)
(s/def ::cue-after any?)
(s/def ::cue-before any?)
(s/def ::elevation any?)
(s/def ::mark any?)
(s/def ::mark-after any?)
(s/def ::mark-before any?)
(s/def ::pause any?)
(s/def ::pause-after any?)
(s/def ::pause-before any?)
(s/def ::phonemes any?)
(s/def ::play-duration any?)
(s/def ::rest any?)
(s/def ::rest-after any?)
(s/def ::rest-before any?)
(s/def ::richness any?)
(s/def ::speak any?)
(s/def ::speak-header any?)
(s/def ::speak-numeral any?)
(s/def ::speak-punctuation any?)
(s/def ::speech-rate any?)
(s/def ::stress any?)
(s/def ::voice-balance any?)
(s/def ::voice-duration any?)
(s/def ::voice-family any?)
(s/def ::voice-pitch any?)
(s/def ::voice-pitch-range any?)
(s/def ::voice-rate any?)
(s/def ::voice-stress any?)
(s/def ::voice-volume any?)
(s/def ::volume any?)

;; TODO marquee
;; MARQUEE
(s/def ::marquee-direction any?)
(s/def ::marquee-play-count any?)
(s/def ::marquee-speed any?)
(s/def ::marquee-style any?)

;; FIXME complete properties
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
