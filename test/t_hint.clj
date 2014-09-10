(ns t-hint
  (:require [rx.lang.clojure.core :as rx]
            [midje.sweet :refer :all]))

(defn force-all
  [o]
  (-> o .toList .toBlocking .first seq vec))

(def top-form
  (fn [v] (rx.Observable/from ^Iterable (repeat v v))))

(let [let-form (fn [v] (rx.Observable/from ^Iterable (repeat v v)))]
  (facts "keep-hint"
    (let [facts-let-form (fn [v] (rx.Observable/from ^Iterable (repeat v v)))
          arg-helper (fn [^Iterable s] (rx.Observable/from s))
          facts-let-arg (fn [v] (arg-helper (repeat v v)))]
      ;; f defined at top level
      (force-all (top-form 3)) => [3 3 3]
      ;; f defined in let around facts
      (force-all (let-form 3)) => [3 3 3]
      ;; f defined in let inside facts
      (force-all (facts-let-form 3)) => [3 3 3]
      ;; f defined in let inside facts, but hint is on arglist
      (force-all (facts-let-arg 3)) => [3 3 3])))
