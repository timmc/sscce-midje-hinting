(defproject sscce-midje-hint "0.1.0-SNAPSHOT"
  :description "SSCCE for bug in type-hinting in midje facts"
  :url "https://github.com/timmc/sscce-midje-hinting"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.netflix.rxjava/rxjava-clojure "0.19.6"
                  :exclusions [org.clojure/clojure]]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :plugins [[lein-midje "3.1.3"]]}})
