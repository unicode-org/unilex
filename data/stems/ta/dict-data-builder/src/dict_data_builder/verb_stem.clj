(ns dict-data-builder.verb-stem
  (:require [clj-thamil.format :as fmt]
            [clj-thamil.மொழியியல் :as மொழி]
            [clojure.string :as string]
            [dict-data-builder.conjugation :as cjg]
            [dict-data-builder.data :as data :refer [map->Verb map->PronounVerbConjugation]])
  (:import dict_data_builder.data.Verb))

;; purpose of this namespace is to reduce the set of inferred verbs to the
;; true verb roots (verb stems) and remove the transformed verbs

(defn verb->derivation-map
  "Return a map in which the derived verbs are keys pointing to the root verb that produced them, given an input root verb"
  [verb]
  ;; example: verb = எடு
  (let [transformed-verbs [(-> verb cjg/reflexive-verb) ;; எடுத்துக்கொள்
                           (-> verb
                               cjg/reflexive-verb
                               ;; Hack to ensure that the -கொள் becomes -கொள்ள
                               ;; when creating infinitive within cjg/passive-verb.
                               ;; Don't yet know how to programmatically detect when
                               ;; long word should double the final consonant b/c
                               ;; it is a compound with a short word verb as the
                               ;; last part of the compound.
                               (update-in [:root] மொழி/சந்தி "ள்")
                               cjg/passive-verb) ;; எடுத்துக்கொள்ளப்படு
                           (-> verb cjg/completion-விடு-verb) ;; எடுத்துவிடு
                           (-> verb cjg/perfect-tense-verb) ;; எடுத்திரு
                           (-> verb cjg/continuous-tense-verb) ;; எடுத்துக்கொண்டிரு
                           (-> verb cjg/passive-verb) ;; எடுக்கப்படு
                           (-> verb
                               cjg/passive-verb
                               cjg/perfect-tense-verb) ;; எடுக்கப்பட்டிரு
                           (-> verb
                               cjg/inf-கூடு-verb) ;; எடுக்ககூடு
                           (-> verb
                               cjg/inf-வேண்டு-verb) ;; எடுக்கவேண்டு
                           ]
        map-entries (for [transf-verb transformed-verbs]
                      [transf-verb verb])
        derivation-map (into {} map-entries)]
    derivation-map))

(defn verbs->transformed-verb-derivation-map
  "A map that connects a transformed verb to the un-transformed version"
  [verbs]
  (let [derivation-maps (map verb->derivation-map verbs)
        derivation-map (reduce merge derivation-maps)]
    derivation-map))

(defn remove-derivations-from-verbs
  "Perform one iteration of removing derived verbs from the input verb set"
  [verbs]
  (let [derivation-map (verbs->transformed-verb-derivation-map verbs)]
    (loop [verbs-set (into #{} verbs)
           derivation-entries (seq derivation-map)]
      (if (empty? derivation-entries)
        verbs-set
        (let [derivation-entry (first derivation-entries)
              [derived-verb orig-verb] derivation-entry]
          (if-not (contains? verbs-set derived-verb)
            (recur verbs-set (rest derivation-entries))
            (let [new-verbs-set (-> verbs-set
                                    (disj derived-verb)
                                    (conj orig-verb))
                  new-derivation-entries (rest derivation-entries)]
              (recur new-verbs-set new-derivation-entries))))))))

(defn verbs->stems
  "Perform iterations of remote-derivations-from-verbs until all verbs are reduced to their original stems"
  [verbs]
  (loop [curr-verbs verbs]
    (let [new-curr-verbs (remove-derivations-from-verbs curr-verbs)]
      (if (= (count new-curr-verbs)
             (count curr-verbs))
        new-curr-verbs
        (recur new-curr-verbs)))))
