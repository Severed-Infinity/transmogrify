(ns transmogrify.internal
  (:require [transmogrify.specs.html_spec :as html]
            [clojure.spec.alpha :as spec]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]
            [clojure.pprint :as pp]))


(comment (defmulti transducible-multimethod
           (fn [xf]
             (fn
               ([] "init form" (xf))
               ([result] "completion form" (xf result))
               ([result init] "reducing/process form" (xf result init))))))

;;TODO heavy cleanup
(defmulti transmogrifier 
  "Self contained alorgithm implementations for translating 
  from clojure (vector) datastructures to the respective language
  
  Some potential options
  -allow-custom (bool): allows for custom tags (particularly javafx extensions)
  -allow-any (bool): same as the above? no checking/validation?
  -validate (bool): skip clojure.spec step?"
  (fn [language _] language))


;; ADD TO HTML MULTIMETHOD
(defn- html-styles->str [styles]
  (str/join (map (fn [[k v]] (str (name k) ":" v ";")) styles)))

(defn- html-attrs->str [attrs]
  (str/join
    (map
      (fn [[k v]]
        (str
          (str/lower-case (name k)) 
          "=\""
          (cond
            (map? v) (html-styles->str v)
            (sequential? v) (str/join " " v)
            :else v)
          "\" "))
      attrs)))
  
(html-attrs->str {:class "test me"
                  :style {:bg "#fff"}})
(html-attrs->str {})

(defn- normalise-html
  ;;TODO include id's and classes e.g. div#content.container.black-bg
  [element]
  (let [[tag & content] element
        attrs (first content)]
    (if (map? attrs)
      [tag attrs (next content)]
      [tag {} content])))

(normalise-html [:html [:head [:title]] [:body [:img] [:p]]])

(defn- void-tag?
  [tag]
  (let [tag-type (spec/or :void ::html/void-elements)           
        conformed-tag (spec/conform tag-type tag)]
    (if (spec/valid? tag-type tag)
      (case (first conformed-tag)
        :void true
        :unescapable false)
      false)))

(void-tag? :script)
(void-tag? :base)
(void-tag? :p)
(void-tag? :img)

(defn- html->str
  ;;TODO apply spec rules about tags and order
  ;;do loop/recur some how
  ;;FIXME needs to be changed to a transducible context to keep vector traversal
  [element]
  (let [[tag attrs content :as norm] (normalise-html element)
        named-tag (name tag)]
    (str "<" named-tag (html-attrs->str attrs)
      (if (void-tag? tag)
        "/>"
        (str ">" (str/join (map html->str content))
          "</" named-tag ">")))))

(html->str [:base])
(html->str [:html])

(html->str [:html {} [:head [:title {}] [:body {} [:img {} ] [:p]]]])

(html->str [:html [:head [:title]]])

(stest/instrument)








