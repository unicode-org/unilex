(ns dict-data-builder.inflection
  (:require [clj-thamil.மொழியியல் :as மொழி]))

(defn accusative
  [noun]
  (மொழி/வேற்றுமை noun "ஐ"))

(defn dative
  [noun]
  (மொழி/வேற்றுமை noun "உக்கு"))

(defn sociative
  [noun]
  #{(மொழி/வேற்றுமை noun "உடன்")
    (மொழி/வேற்றுமை noun "ஓடு")})

(defn locative
  [noun]
  #{(மொழி/வேற்றுமை noun "இல்")})

(defn ablative
  [noun]
  #{(மொழி/வேற்றுமை noun "இலிருந்து")})

(defn possessive
  [noun]
  #{(மொழி/வேற்றுமை noun "உடைய")
    (மொழி/வேற்றுமை noun "இன்")})

(defn locative-human
  [noun]
  #{(மொழி/வேற்றுமை noun "இடம்")})

(defn instrumental
  [noun]
  (மொழி/வேற்றுமை noun "ஆல்"))

(defn conjunction
  [noun]
  (மொழி/சந்தி noun "உம்"))

(defn adjective
  [noun]
  (மொழி/சந்தி noun "ஆன"))

(defn adverb
  [noun]
  (மொழி/சந்தி noun "ஆக"))

(defn ordinal
  [noun]
  #{(மொழி/சந்தி noun "ஆவது")
    (மொழி/சந்தி noun "ஆம்")})

(defn indefinite-every
  [noun]
  (மொழி/சந்தி noun "உம்"))

(defn indefinite-some
  [noun]
  (மொழி/சந்தி noun "ஓ"))

(defn indefinite-any
  [noun]
  (மொழி/சந்தி noun "ஆவது"))

(def inflection-fns
  #{accusative
    dative
    sociative
    locative
    ablative
    possessive
    locative-human
    instrumental
    conjunction
    adjective
    adverb
    மொழி/பன்மை ;; plural
    (comp accusative மொழி/பன்மை)
    (comp dative மொழி/பன்மை)
    (comp sociative மொழி/பன்மை)
    (comp locative மொழி/பன்மை)
    (comp ablative மொழி/பன்மை)
    (comp possessive மொழி/பன்மை)
    (comp locative-human மொழி/பன்மை)
    (comp instrumental மொழி/பன்மை)
    (comp conjunction மொழி/பன்மை)
    (comp adjective மொழி/பன்மை)
    (comp adverb மொழி/பன்மை)
    indefinite-every
    indefinite-some
    indefinite-any})

(defn all-inflections
  [noun]
  (let [inflections (->> ((apply juxt inflection-fns) noun)
                         (map (fn [s] (if (string? s) [s] s)))
                         (apply concat))
        unique-inflections (-> (into #{} inflections)
                               (disj noun))]
    unique-inflections))
