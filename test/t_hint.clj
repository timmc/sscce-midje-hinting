(ns t-hint
  (:require [rx.lang.clojure.core :as rx]
            [midje.sweet :refer :all]))

(defn dump
  [o]
  (-> o .toList .toBlocking .first seq vec))

;; rx.Observable/from is overloaded as taking either Object or
;; Iterable (among others.) Calling the first with a list creates an
;; Observable with one element (a list) and calling the other results
;; in an Observable with as many items as were in the list. The below
;; test shows that under certain circumstances, that very important
;; hinting disappears.

(def top-form
  (fn [v] (rx.Observable/from ^Iterable (repeat v v))))

(let [let-form (fn [v] (rx.Observable/from ^Iterable (repeat v v)))]
  (facts "keep-hint"
    (let [facts-let-form (fn [v] (rx.Observable/from ^Iterable (repeat v v)))
          arg-helper (fn [^Iterable s] (rx.Observable/from s))
          facts-let-arg (fn [v] (arg-helper (repeat v v)))]
      ;; f defined at top level
      (dump (top-form 3)) => [3 3 3]
      ;; f defined in let around facts
      (dump (let-form 3)) => [3 3 3]
      ;; f defined in let inside facts
      (dump (facts-let-form 3)) => [3 3 3] ;; Fails! Emits [(3 3 3)]
      ;; f defined in let inside facts, but hint is on arglist
      (dump (facts-let-arg 3)) => [3 3 3]
      ;; f is inline
      (dump ((fn [v] (rx.Observable/from ^Iterable (repeat v v))) 3))
      => [3 3 3])))
