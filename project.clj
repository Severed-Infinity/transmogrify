(defproject transmogrify "0.0.1-SNAPSHOT"
  :description "Transform in a surprising or magical manner, 
                to change in appearance or form, especially strangely or grotesquely; transform.
                
                Given a nested (vector) datastructure transform it into the given language provided it 
                matches the language spec provided and the transformation algorithm"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [org.clojure/spec.alpha "0.1.143"]
                 [org.clojure/core.specs.alpha "0.1.24"]
                 [metosin/spec-tools "0.5.1"]]
  :plugins [[lein-cloverage "1.0.10"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.10.0-alpha2"]
                                  [cloverage/cloverage "1.0.9"]]}}
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]
  :resource-paths ["resources"]
  :test-paths ["test"]
  :target-path "target/%s"
  #_:aot #_[transmogrify.core]
  #_:main #_transmogrify.core)
