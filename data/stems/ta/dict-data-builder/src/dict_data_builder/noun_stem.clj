(ns dict-data-builder.noun-stem
  (:require [dict-data-builder.inflection :as infl]))

(defn noun->derivation-map
  [noun]
  (let [transformed-nouns (infl/all-inflections noun)
        map-entries (for [transf-noun transformed-nouns]
                      [transf-noun noun])
        derivation-map (into {} map-entries)]
    derivation-map))

(defn nouns->transformed-noun-derivation-map
  [nouns]
  (let [derivation-maps (map noun->derivation-map nouns)
        derivation-map (reduce merge derivation-maps)]
    derivation-map))

(defn remove-derivations-from-nouns
  [nouns]
  (let [derivation-map (nouns->transformed-noun-derivation-map nouns)]
    (loop [nouns-set (into #{} nouns)
           derivation-entries (seq derivation-map)]
      (if (empty? derivation-entries)
        nouns-set
        (let [derivation-entry (first derivation-entries)
              [derived-noun orig-noun] derivation-entry]
          (if-not (contains? nouns-set derived-noun)
            (recur nouns-set (rest derivation-entries))
            (let [new-nouns-set (-> nouns-set
                                    (disj derived-noun)
                                    (conj orig-noun))
                  new-derivation-entries (rest derivation-entries)]
              (recur new-nouns-set new-derivation-entries))))))))

(defn nouns->stems
  [nouns]
  (loop [curr-nouns nouns]
    (let [new-curr-nouns (remove-derivations-from-nouns curr-nouns)]
      (if (= (count new-curr-nouns)
             (count curr-nouns))
        new-curr-nouns
        (recur new-curr-nouns)))))
