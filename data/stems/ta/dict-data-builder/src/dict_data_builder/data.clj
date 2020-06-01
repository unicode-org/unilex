(ns dict-data-builder.data
  (:require [clj-thamil.format :as fmt])
  (:use [flatland.ordered.map]))

;;
;; data definitions
;;

(defrecord Verb [^String root ^String verb-class])

(defrecord Pronoun [^String id ^String pronoun, ^String meaning])

(defrecord VerbEnding [^String ending, ^Pronoun pronoun])

(defrecord PronounVerbConjugation [^Pronoun pronoun, ^Verb verb, ^String conjugation, ^String desc])

;;
;; constants
;;


(def ^{:doc "map of pronoun key strings to the full Pronoun records"}
  PRONOUNS
  (let [pronouns [(map->Pronoun {:id "நான்" :pronoun "நான்" :meaning "1st person singular"})
                  (map->Pronoun {:id "நீ" :pronoun "நீ" :meaning "2nd person singular"})
                  (map->Pronoun {:id "நீர்" :pronoun "நீர்" :meaning "2nd person singular, honorific"})
                  (map->Pronoun {:id "நீங்கள்2PSH" :pronoun "நீங்கள்" :meaning "2nd person singular, honorific"})
                  (map->Pronoun {:id "அவன்" :pronoun "அவன்" :meaning "3rd person singular, male"})
                  (map->Pronoun {:id "அவள்" :pronoun "அவள்" :meaning "3rd person singular, female"})
                  (map->Pronoun {:id "அவர்" :pronoun "அவர்" :meaning "3rd person singular, honoric"})
                  (map->Pronoun {:id "அது" :pronoun "அது" :meaning "3rd person singular, inanimate"})
                  (map->Pronoun {:id "நாம்" :pronoun "நாம்" :meaning "1st person plural, inclusive of speaker"})
                  (map->Pronoun {:id "நாங்கள்" :pronoun "நாங்கள்" :meaning "1st person plural, exclusive of speaker"})
                  (map->Pronoun {:id "நீங்கள்2PP" :pronoun "நீங்கள்" :meaning "2nd person plural"})
                  (map->Pronoun {:id "அவர்கள்" :pronoun "அவர்கள்" :meaning "3rd person animate plural"})
                  (map->Pronoun {:id "அவை" :pronoun "அவை" :meaning "3rd person inanimate plural"})]
        pronouns-map (into (ordered-map) (map #(vector (:id %) %) pronouns))]
    pronouns-map))


(def ^{:doc "a map of a pronoun key string to the full VerbEnding"}
  VERB-ENDINGS
  (let [verb-endings [(map->VerbEnding {:ending "ஏன்"
                                        :pronoun (get PRONOUNS "நான்")})
                      (map->VerbEnding {:ending "ஆய்"
                                        :pronoun (get PRONOUNS "நீ")})
                      (map->VerbEnding {:ending "ஈர்"
                                        :pronoun (get PRONOUNS "நீர்")})
                      (map->VerbEnding {:ending "ஈர்கள்"
                                        :pronoun (get PRONOUNS "நீங்கள்2PSH")})
                      (map->VerbEnding {:ending "ஆன்"
                                        :pronoun (get PRONOUNS "அவன்")})
                      (map->VerbEnding {:ending "ஆள்"
                                        :pronoun (get PRONOUNS "அவள்")})
                      (map->VerbEnding {:ending "ஆர்"
                                        :pronoun (get PRONOUNS "அவர்")})
                      (map->VerbEnding {:ending "அது"
                                        :pronoun (get PRONOUNS "அது")})
                      (map->VerbEnding {:ending "ஓம்"
                                        :pronoun (get PRONOUNS "நாம்")})
                      (map->VerbEnding {:ending "ஓம்"
                                        :pronoun (get PRONOUNS "நாங்கள்")})
                      (map->VerbEnding {:ending "ஈர்கள்"
                                        :pronoun (get PRONOUNS "நீங்கள்2PP")})
                      (map->VerbEnding {:ending "ஆர்கள்"
                                        :pronoun (get PRONOUNS "அவர்கள்")})   
                      ;; we can add more, but they have less usage (ex: newspaper only,
                      ;; Old Tamil/Middle Tamil)
                      ]
        ;; if we ever add the alternate endings used elsewhere / in
        ;; the past, then we should allow for that by making the
        ;; values of this map a collection, not a scalar
        verb-endings-map (group-by (comp :id :pronoun) verb-endings)]
    verb-endings-map)) 

(def WEAK-VERB-CLASSES
  #{"1a" "1b" "1c" "2" "3" "4"})

(def STRONG-VERB-CLASSES
  #{"6" "7"})

(def IRREGULAR-VERBS
  (let [irregular-verbs #{{:root "வா" :verb-class "2" :infinitive "வர" :past-stem "வந்த்" :avp "வந்து" :present-stem "வருகிற்" :future-stem "வருவ்"}
                          {:root "போ" :verb-class "3" :infinitive "போக" :past-stem "போன்" :avp "போய்" :present-stem "போகிற்" :future-stem "போவ்"}
                          {:root "சொல்" :verb-class "3" :infinitive "சொல்ல" :past-stem "சொன்ன்" :avp "சொல்லி" :present-stem "சொல்கிற்" :future-stem "சொல்வ்"}
                          {:root "சா" :verb-class "1a" :infinitive "சாக" :past-stem "செத்த்" :avp "செத்து" :present-stem "சாகிற்" :future-stem "சாவ்"}
                          {:root "ஆ" :verb-class "3" :infinitive "ஆக" :past-stem "ஆன்" :avp "ஆய்" :present-stem "ஆகிற்" :future-stem "ஆவ்"}
                          {:root "வே" :verb-class "2" :infinitive "வேக" :past-stem "வெந்த்" :avp "வெந்து" :present-stem "வேகிற்" :future-stem "வேவ்"}}
        irregular-verbs-map (into {} (map #(vector (:root %) %) irregular-verbs))]
    irregular-verbs-map))

(def CLASS-5-VERBS
  (let [class-5-verbs #{{:root "கேள்" :verb-class "5" :infinitive "கேட்க" :avp "கேட்டு" :past-stem "கேட்ட்" :present-stem "கேட்கிற்" :future-stem "கேட்ப்"}
                        {:root "மீள்" :verb-class "5" :infinitive "மீட்க" :avp "மீட்டு" :past-stem "மீட்ட்" :present-stem "மீட்கிற்" :future-stem "மீட்ப்"}
                        {:root "நில்" :verb-class "5" :infinitive "நிற்க" :avp "நின்று" :past-stem "நின்ற்" :present-stem "நிற்கிற்" :future-stem "நிற்ப்"}
                        {:root "வில்" :verb-class "5" :infinitive "விற்க" :avp "விற்று" :past-stem "விற்ற்" :present-stem "விற்கிற்" :future-stem "விற்ப்"}
                        {:root "ஏல்" :verb-class "5" :infinitive "ஏற்க" :avp "ஏற்று" :past-stem "ஏற்ற்" :present-stem "ஏற்கிற்" :future-stem "ஏற்ப்"}
                        {:root "தோல்" :verb-class "5" :infinitive "தோற்க" :avp "தோற்று" :past-stem "தோற்ற்" :present-stem "தோற்கிற்" :future-stem "தோற்ப்"}
                        {:root "தின்" :verb-class "5" :infinitive "தின்ன" :avp "தின்று" :past-stem "தின்ற்" :present-stem "தின்கிற்" :future-stem "தின்ப்"}
                        {:root "உண்" :verb-class "5" :infinitive "உண்ண" :avp "உண்டு" :past-stem "உண்ட்" :present-stem "உண்கிற்" :future-stem "உண்ப்"}
                        {:root "என்" :verb-class "5" :infinitive "என" :avp "என்று" :past-stem "என்ற்" :present-stem "என்கிற்" :future-stem "என்ப்"}
                        {:root "காண்" :verb-class "5" :infinitive "காண" :avp "கண்டு" :past-stem "கண்ட்" :present-stem "காண்கிற்" :future-stem "காண்ப்"}}
        class-5-verbs-map (into {} (map #(vector (:root %) %) class-5-verbs))]
    class-5-verbs-map))

;;
;; verb data fns
;;

(def verb-keys (->> (Verb/getBasis)
                    (map keyword)))

(defn keep-default-verb-keys
  "Return a verb val that only has the keys in the Verb record"
  [verb]
  (-> verb
      (select-keys verb-keys)
      map->Verb))


(def VERB-COMPARE-KEYS verb-keys)

(defn verb-equals?
  [v1 v2]
  (= (select-keys v1 VERB-COMPARE-KEYS)
     (select-keys v2 VERB-COMPARE-KEYS)))

(defn verb-before?
  [v1 v2]
  (let [root1 (:root v1)
        root2 (:root v2)
        class1 (:verb-class v1)
        class2 (:verb-class v2)]
    (boolean (or (fmt/word-before? root1 root2)
                 (and (= root1 root2)
                      (fmt/word-before? class1 class2))))))

(def verb-comparator (comparator verb-before?))
