(ns transmogrify.wips.sample-transducer-ideas
  (:require [clojure.string :as str]))
(comment
  (defn template-combiner
    ;; no input/empty base type, exit type/condition?
    ([] nil)
    ;; single of it's type, returns the single type
    ([type] type)
    ;; type and an extra, combine at this stage, combine-> template-reducer?
    ;; combine seems to call out to some java base version of the function
    ([type extra] (combine type extra))
    ;; type and multiple extras, pass to self with recur?
    ([type extra & more] (recur template-reducer (template-reducer type extra) more)))

  (defn template-transducer [xf]
    (fn
      ;; SET-UP
      ([] (xf))
      ;; PROCESS
      ([result input] (xf result input))
      ([result input & inputs] (xf result input inputs))
      ;; COMPLETE
      ([result] (xf result))))

  ;; transduce with the identity transform is equivalent to reduce,
  ;; in the following way:
  (transduce identity f sample)
  (f (reduce f (f) sample))

  ;; For example, we can define a reducing function and then use it:
  (defn conj-second
    ([]
     [])
    ([result]
     result)
    ([result [x y]]
     (conj result y)))

  (def sample [[1 :a] [2 :b] [3 :c]])

  (transduce identity conj-second sample)
  ;;=>[:a :b :c]
  (conj-second (reduce conj-second (conj-second) sample))
  ;;=>[:a :b :c]

  ;; Let's prove the point with printing:
  (defn conj-second
    ([]
     (println "0") [])
    ([result]
     (println "1") result)
    ([result [x y]]
     (println "2") (conj result y)))

  ;; Then the following both print 0 2 2 2 1
  (transduce identity conj-second sample)
  (conj-second (reduce conj-second (conj-second) sample)))

(comment (fn ^:static conj
           ([] [])
           ([coll] coll)
           ([coll x] (clojure.lang.RT/conj coll x))
           ([coll x & xs]
            (if xs
              (recur (clojure.lang.RT/conj coll x) (first xs) (next xs))
              (clojure.lang.RT/conj coll x))))
         (fn +
           ([] 0)
           ([x] (cast Number x))
           ([x y] (. clojure.lang.Numbers (add x y)))
           ([x y & more]
            (reduce1 + (+ x y) more))))


;;-> current state of html transformer

;;-> needs s specialised str fn as the reducer
;; it's looking like spec will come in handy here
(defn- html->str
  ([] "")

  ([element]
   (println "single" element
            #_(str element))
   (let [[tag & content :as norm] element
         named-tag (name tag)]

     (str "<" named-tag
          ">" #_(transduce (map html->str) str content) (apply html->str content)
          "</" named-tag ">")))

  ([element ele2]
   (println "double e1" element "e2" ele2)
   (str (html->str element) " " (html->str ele2)))

  ([element ele2 & more]
   (println "multiple e1" element "e2" ele2 "more" more)
   (if more
     (recur (html->str element ele2) (first more) (next more))
     (str element ele2))))

;; -> input is the same as accumulator
(defn html
  ([xf]
   (fn rf
     ([] (xf))
     ([acc] (println "acc1" acc) (xf acc))
     ([acc input
       #_(reduce
           (fn [a i]
             (xf a i))
           acc input)]
      (println "acc2" acc "input" input)
      (xf acc (html->str input)))))
  #_([data
      (html->str data)]))


;;xf-> transformer?
(def xf html)
;;-> combiner same as reducer?
(def combiner str)
;; transducer expects a collection of what ever you are working with
(def page1 [[:html]])
(def page2 [[:html [:body]]])
(def page3 [[:head] [:body]])
;;reducer expects a single item of what ever you are working with
(def page1-1 [:html [:head [:title]] [:body]])
(def page2-2 [:html [:head [:title]] [:body [:p [:h1]]]])
(def page3-3 [:html [:body [:section]]])
#_(html->str page1-1)
#_(html->str page2-2)
#_(html->str page3-3)
#_(((html) str) page1-1)
#_(html->str (reduce html->str (html->str) page1))

;; reducer ->'what goes here'->'str possibly'
#_(transduce xf combiner page1)
#_(transduce xf combiner page2)
#_(transduce xf combiner page3)
