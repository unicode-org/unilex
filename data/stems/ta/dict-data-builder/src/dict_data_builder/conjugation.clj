(ns dict-data-builder.conjugation
  (:require [clj-thamil.format :as fmt]
            [clj-thamil.மொழியியல் :as மொழி]
            [clojure.string :as string]
            [dict-data-builder.data :as data :refer [map->Verb map->PronounVerbConjugation]]
            [dict-data-builder.verb :as v])
  (:import dict_data_builder.data.Verb))

(def அது (get data/PRONOUNS "அது"))

(def அவை (get data/PRONOUNS "அவை"))

;;
;; verb conjugation
;;

;; past-stem

(defn past-stem
  [verb]
  (let [{:keys [root verb-class]} verb
        letters (மொழி/தொடை->எழுத்துகள் root)
        phonemes (மொழி/தொடை->ஒலியன்கள் root)]
    (cond
      (data/CLASS-5-VERBS root) (-> (data/CLASS-5-VERBS root)
                                    :past-stem)
      (data/IRREGULAR-VERBS root) (-> (data/IRREGULAR-VERBS root)
                                      :past-stem)
      (= "1a" verb-class) (மொழி/சந்தி root "த்")
      (= "1b" verb-class) (-> letters
                              butlast
                              (concat ["ண்ட்"])
                              ((partial apply str)))
      (= "1c" verb-class) (-> letters
                              butlast
                              (concat ["ன்ற்"])
                              ((partial apply str)))
      (= "2" verb-class) (மொழி/சந்தி root "ந்த்")
      (= "3" verb-class) (மொழி/சந்தி root "இன்")
      (= "4" verb-class) (-> phonemes
                             butlast
                             ((fn [phonemes] (concat phonemes [(last phonemes)])))
                             fmt/phonemes->str)
      (= "6" verb-class) (மொழி/சந்தி root "த்த்")
      (= "7" verb-class) (மொழி/சந்தி root "ந்த்"))))


;; present-stem

(defn present-stem-weak-verb
  [verb]
  (மொழி/சந்தி (:root verb) "கிற்"))

(defn present-stem-strong-verb
  [verb]
  (மொழி/சந்தி (:root verb) "க்கிற்"))

(defn present-stem
  [verb]
  (let [{:keys [root verb-class]} verb]
    (cond
      (data/CLASS-5-VERBS root) (-> (data/CLASS-5-VERBS root)
                                    :present-stem)
      (data/IRREGULAR-VERBS root) (-> (data/IRREGULAR-VERBS root)
                                      :present-stem)
      (data/WEAK-VERB-CLASSES verb-class) (present-stem-weak-verb verb)
      (data/STRONG-VERB-CLASSES verb-class) (present-stem-strong-verb verb))))

(defn present-stem->alt-stem
  [present-stem]
  (let [letters (மொழி/தொடை->எழுத்துகள் present-stem)
        new-letters (-> (->> letters
                             (drop-last 2))
                        (concat ["கி" "ன்" "ற்"]))
        new-stem (apply str new-letters)]
    new-stem))

;; future-stem

(defn future-stem-weak-verb
  [verb]
  (மொழி/சந்தி (:root verb) "வ்"))

(defn future-stem-strong-verb
  [verb]
  (மொழி/சந்தி (:root verb) "ப்ப்"))

(defn future-stem
  [verb]
  (let [{:keys [root verb-class]} verb]
    (cond
      (data/CLASS-5-VERBS root) (-> (data/CLASS-5-VERBS root)
                                    :future-stem)
      (data/IRREGULAR-VERBS root) (-> (data/IRREGULAR-VERBS root)
                                      :future-stem)
      (data/WEAK-VERB-CLASSES verb-class) (future-stem-weak-verb verb)
      (data/STRONG-VERB-CLASSES verb-class) (future-stem-strong-verb verb))))

;; infinitive

(defn infinitive-weak-verb
  [verb]
  (let [root (:root verb)
        letters (மொழி/தொடை->எழுத்துகள் root)
        phonemes (மொழி/தொடை->ஒலியன்கள் root)]
    (if-not (and (= 2 (count letters))
                 (every? மொழி/குறிலா? letters))
      (மொழி/சந்தி root "அ")
      (let [new-phonemes (-> phonemes
                             butlast
                             (concat ["அ"]))
            new-word (fmt/phonemes->str new-phonemes)]
        new-word))))

(defn infinitive-strong-verb
  [verb]
  (let [root (:root verb)]
    (மொழி/சந்தி root "க்க")))

(defn infinitive
  [verb]
  (let [root (:root verb)
        verb-class (:verb-class verb)]
    (cond
      (data/CLASS-5-VERBS root) (-> (data/CLASS-5-VERBS root)
                                    :infinitive)
      (data/IRREGULAR-VERBS root) (-> (data/IRREGULAR-VERBS root)
                                      :infinitive)
      (data/WEAK-VERB-CLASSES verb-class) (infinitive-weak-verb verb)
      (data/STRONG-VERB-CLASSES verb-class) (infinitive-strong-verb verb))))

;; AvP

(defn avp
  [verb]
  (let [{:keys [root verb-class]} verb
        past-stem (past-stem verb)]
    (cond
      ;; We could compute Class 5 AvP from the infinitive, just like
      ;; other non-class 3 verbs.  But some irregular verbs (ex: சொல்)
      ;; has an AvP unrelated to its past stem, and we record data for
      ;; class 5 and irregular verbs in the same format in case the
      ;; consistency is useful later.
      (data/CLASS-5-VERBS root) (-> (data/CLASS-5-VERBS root)
                                    :avp)
      (data/IRREGULAR-VERBS root) (-> (data/IRREGULAR-VERBS root)
                                      :avp)
      (= "3" verb-class) (மொழி/சந்தி root "இ")
      :else (மொழி/சந்தி past-stem "உ")))) 

(defn negative-avp
  [verb]
  (if (= verb (map->Verb {:root "இரு" :verb-class "7"}))
    "இல்லாமல்"
    (let [inf (infinitive verb)
          phonemes (மொழி/தொடை->ஒலியன்கள் inf)
          new-phonemes (-> phonemes
                           butlast
                           (concat ["ஆ" "ம்" "அ" "ல்"]))
          new-word (fmt/phonemes->str new-phonemes)]
      new-word)))

(defn negative-verb-conjugation-past-present
  [verb]
  (let [inf (infinitive verb)
        negative-verb (மொழி/சந்தி inf "இல்லை")]
    #{negative-verb}))

(defn negative-verb-future-stem
  [verb]
  (let [inf (infinitive verb)
        negative-verb (மொழி/சந்தி inf "மாட்ட்")]
    negative-verb))

(defn conditional
  [verb]
  (மொழி/சந்தி (past-stem verb) "ஆல்"))

(defn negative-conditional
  [verb]
  (-> (infinitive verb)
      மொழி/தொடை->ஒலியன்கள்
      butlast
      (concat ["ஆ" "வ்" "இ" "ட்" "ட்" "ஆ" "ல்"])
      fmt/phonemes->str))

;; verb transformations

(defn perfect-tense-verb
  [verb]
  (let [root (:root verb)
        verb-class (:verb-class verb)
        avp (avp verb)
        new-root (மொழி/சந்தி avp "இரு")
        new-verb (-> (assoc verb :root new-root :verb-class "7")
                     data/keep-default-verb-keys)]
    new-verb))

(defn completion-விடு-verb
  [verb]
  (let [root (:root verb)
        verb-class (:verb-class verb)
        avp (avp verb)
        new-root (str avp "விடு")
        new-verb (-> (assoc verb :root new-root :verb-class "4")
                     data/keep-default-verb-keys)]
    new-verb))

(defn reflexive-verb
  [verb]
  (let [root (:root verb)
        verb-class (:verb-class verb)
        avp (avp verb)
        extra-strong-letter-doubling-verb-info-list #{(data/IRREGULAR-VERBS "போ")
                                                      (data/IRREGULAR-VERBS "சொல்")
                                                      (data/IRREGULAR-VERBS "சா")
                                                      (data/IRREGULAR-VERBS "ஆ")
                                                      (data/CLASS-5-VERBS "கேள்")
                                                      (data/CLASS-5-VERBS "மீள்")
                                                      (data/CLASS-5-VERBS "தோல்")
                                                      (data/CLASS-5-VERBS "வில்")}
        extra-strong-letter-doubling-verbs (->> extra-strong-letter-doubling-verb-info-list
                                                (map data/keep-default-verb-keys)
                                                (into #{}))
        new-root (if (or (extra-strong-letter-doubling-verbs (data/keep-default-verb-keys verb))
                         (#{"3" "4" "6"} verb-class))
                   (str avp "க்கொள்")
                   (str avp "கொள்"))
        new-verb (-> (assoc verb :root new-root :verb-class "1b")
                     data/keep-default-verb-keys)]
    new-verb))

(defn continuous-tense-verb
  [verb]
  (let [reflexive-verb (reflexive-verb verb)
        root (:root reflexive-verb)
        letters (மொழி/தொடை->எழுத்துகள் root)
        new-letters (-> letters
                        butlast
                        (concat ["ண்" "டி" "ரு"]))
        new-root (apply str new-letters)
        new-verb (-> (assoc verb :root new-root :verb-class "7")
                     data/keep-default-verb-keys)]
    new-verb))

(defn passive-verb
  [verb]
  (let [inf (infinitive verb)
        new-root (மொழி/சந்தி inf "ப்படு")
        new-verb (-> (assoc verb :root new-root :verb-class "4")
                     data/keep-default-verb-keys)]
    new-verb))

(defn inf-கூடு-verb
  [verb]
  (let [inf (infinitive verb)
        new-root (மொழி/சந்தி inf "க்கூடு")
        new-verb (-> (assoc verb :root new-root :verb-class "3")
                     data/keep-default-verb-keys)]
    new-verb))

(defn inf-வேண்டு-verb
  [verb]
  (let [inf (infinitive verb)
        new-root (மொழி/சந்தி inf "வேண்டு")
        new-verb (-> (assoc verb :root new-root :verb-class "3")
                     data/keep-default-verb-keys)]
    new-verb))

;; everything above but for அது, அவை

(defn present-conjugation-அவை
  [verb]
  (let [present-stem (present-stem verb)
        letters (மொழி/தொடை->எழுத்துகள் present-stem)
        present-cjg-அவை (->> letters
                             (drop-last 2)
                             (#(concat % ["கி" "ன்" "ற" "ன"]))
                             ((partial apply str)))]
    #{present-cjg-அவை}))

(defn future-conjugation-அது-அவை
  [verb]
  (let [inf (infinitive verb)
        phonemes (மொழி/தொடை->ஒலியன்கள் inf)
        new-phonemes (->> phonemes
                          butlast
                          (#(concat % ["உ" "ம்"])))
        future-cjg-word (fmt/phonemes->str new-phonemes)]
    #{future-cjg-word}))

(defn negative-conjugation-future-அது-அவை
  [verb]
  (let [inf (infinitive verb)
        phonemes (மொழி/தொடை->ஒலியன்கள் inf)
        new-phonemes (->> phonemes
                          butlast
                          (#(concat % ["ஆ" "து"])))
        future-cjg-word (fmt/phonemes->str new-phonemes)]
    #{future-cjg-word}))

(defn past-conjugation-அது
  [verb]
  (let [{:keys [root verb-class]} verb
        போ (-> (data/IRREGULAR-VERBS "போ")
               (select-keys [:root :verb-class])
               (map->Verb))
        சொல் (-> (data/IRREGULAR-VERBS "சொல்")
               (select-keys [:root :verb-class])
               (map->Verb))
        ஆ (-> (data/IRREGULAR-VERBS "ஆ")
              (select-keys [:root :verb-class])
              (map->Verb))]
    (cond
      (= போ verb) #{"போயிற்று" "போனது"}
      (= சொல் verb) #{"சொல்லிற்று" "சொன்னது"}
      (= ஆ verb) #{"ஆயிற்று" "ஆனது"}
      (= "3" verb-class) #{(மொழி/சந்தி root "இற்று")
                           (மொழி/சந்தி root "இயது")}
      :else #{(மொழி/சந்தி (past-stem verb) "அது")})))

(defn past-conjugation-அவை
  [verb]
  (let [{:keys [root verb-class]} verb
        போ (-> (data/IRREGULAR-VERBS "போ")
               (select-keys [:root :verb-class])
               (map->Verb))
        சொல் (-> (data/IRREGULAR-VERBS "சொல்")
               (select-keys [:root :verb-class])
               (map->Verb))
        ஆ (-> (data/IRREGULAR-VERBS "ஆ")
              (select-keys [:root :verb-class])
              (map->Verb))]
    (cond
      (= போ verb) #{"போயின"}
      (= சொல் verb) #{"சொல்லின"}
      (= ஆ verb) #{"ஆயின"}
      (= "3" verb-class) #{(மொழி/சந்தி root "இன")}
      :else #{(மொழி/சந்தி (past-stem verb) "அன")})))

;; Conjugations

(defn cjg-pprint-str
  [conjugation]
  (str (-> conjugation :pronoun :pronoun)
       " + "
       (-> conjugation :verb :root)
       " "
       (-> conjugation :verb :verb-class)
       " = "
       (-> conjugation :conjugation)
       (str "   (" (:desc conjugation) ")")))

(defn past-tense-conjugations
  ([verb]
   (let [conjugations (for [pronoun (vals data/PRONOUNS)]
                        (let [conjugation-strs (past-tense-conjugations verb pronoun)
                              desc (string/join " || " [(:meaning pronoun)
                                                        "past tense"
                                                        (str (:root verb) " " (:verb-class verb))])]
                          (for [conjugation-str conjugation-strs]
                            (let [conjugation-map {:pronoun pronoun
                                                   :verb verb
                                                   :conjugation conjugation-str
                                                   :desc desc}
                                  conjugation (map->PronounVerbConjugation conjugation-map)]
                              conjugation))))
         flattened-conjugations (flatten conjugations)]
     flattened-conjugations))
  ([verb pronoun]
   (cond
     (= அது pronoun) (past-conjugation-அது verb)
     (= அவை pronoun) (past-conjugation-அவை verb)
     :else (let [stem (past-stem verb)
                 pronoun-id (:id pronoun)
                 endings (data/VERB-ENDINGS pronoun-id)
                 ending-strs (map :ending endings)
                 conjugations (map (partial மொழி/சந்தி stem) ending-strs)]
             conjugations))))

(defn present-tense-conjugations
  "must handle கிற்/கின்ற் and க்கிற்/க்கின்ற்"
  ([verb]
   (let [conjugations (for [pronoun (vals data/PRONOUNS)]
                        (let [conjugation-strs (present-tense-conjugations verb pronoun)
                              desc (string/join " || " [(:meaning pronoun)
                                                        "present tense"
                                                        (str (:root verb) " " (:verb-class verb))])]
                          (for [conjugation-str conjugation-strs]
                            (let [conjugation-map {:pronoun pronoun
                                                   :verb verb
                                                   :conjugation conjugation-str
                                                   :desc desc}
                                  conjugation (map->PronounVerbConjugation conjugation-map)]
                              conjugation))))
         flattened-conjugations (flatten conjugations)]
     flattened-conjugations))
  ([verb pronoun]
   (cond
     (= அவை pronoun) (present-conjugation-அவை verb)
     :else (let [canonical-stem (present-stem verb)
                 alt-stem (present-stem->alt-stem canonical-stem)
                 stems [canonical-stem alt-stem]
                 pronoun-id (:id pronoun)
                 endings (data/VERB-ENDINGS pronoun-id)
                 ending-strs (map :ending endings)
                 conjugations (->> (for [stem stems]
                                     (map (partial மொழி/சந்தி stem) ending-strs))
                                   flatten)]
             conjugations))))

(defn future-tense-conjugations
  ([verb]
   (let [conjugations (for [pronoun (vals data/PRONOUNS)]
                        (let [conjugation-strs (future-tense-conjugations verb pronoun)
                              desc (string/join " || " [(:meaning pronoun)
                                                        "future tense"
                                                        (str (:root verb) " " (:verb-class verb))])]
                          (for [conjugation-str conjugation-strs]
                            (let [conjugation-map {:pronoun pronoun
                                                   :verb verb
                                                   :conjugation conjugation-str
                                                   :desc desc}
                                  conjugation (map->PronounVerbConjugation conjugation-map)]
                              conjugation))))
         flattened-conjugations (flatten conjugations)]
     flattened-conjugations))
  ([verb pronoun]
   (cond
     (#{அது அவை} pronoun) (future-conjugation-அது-அவை verb)
     :else (let [stem (future-stem verb)
                 pronoun-id (:id pronoun)
                 endings (data/VERB-ENDINGS pronoun-id)
                 ending-strs (map :ending endings)
                 conjugations (map (partial மொழி/சந்தி stem) ending-strs)]
             conjugations))))

(defn negative-future-conjugations
  ([verb]
   (let [conjugations (for [pronoun (vals data/PRONOUNS)]
                        (let [conjugation-strs (negative-future-conjugations verb pronoun)
                              desc (string/join " || " [(:meaning pronoun)
                                                        "negative future tense"
                                                        (str (:root verb) " " (:verb-class verb))])]
                          (for [conjugation-str conjugation-strs]
                            (let [conjugation-map {:pronoun pronoun
                                                   :verb verb
                                                   :conjugation conjugation-str
                                                   :desc desc}
                                  conjugation (map->PronounVerbConjugation conjugation-map)]
                              conjugation))))
         flattened-conjugations (flatten conjugations)]
     flattened-conjugations))
  ([verb pronoun]
   (cond
     (#{அது அவை} pronoun) (negative-conjugation-future-அது-அவை verb)
     :else (let [stem (negative-verb-future-stem verb)
                 pronoun-id (:id pronoun)
                 endings (data/VERB-ENDINGS pronoun-id)
                 ending-strs (map :ending endings)
                 conjugations (map (partial மொழி/சந்தி stem) ending-strs)]
             conjugations))))

;;
;; all conjugations
;;


(defn all-conjugations-no-verb-transform
  [verb]
  (let [single-inflection-cjg-fn-descs [[infinitive "infinitive"]
                                        [avp "adverbial participle"]
                                        [negative-avp "negative adverbial participle"]
                                        [negative-verb-conjugation-past-present "negative past tense"]
                                        [negative-verb-conjugation-past-present "negative present tense"]
                                        [conditional "conditional"]
                                        [negative-conditional "negative conditional"]]
        multi-cjg-fns [past-tense-conjugations
                       present-tense-conjugations
                       future-tense-conjugations
                       negative-future-conjugations]]
    (letfn [(single-inflection-cjgs [cjg-fn-desc verb]
              (for [pronoun (vals data/PRONOUNS)]
                (let [[cjg-fn desc] cjg-fn-desc
                      conjugation-str (cjg-fn verb)
                      desc (string/join " || " [(:meaning pronoun)
                                                desc
                                                (str (:root verb) " " (:verb-class verb))])
                      conjugation-map {:pronoun pronoun
                                       :verb verb
                                       :conjugation conjugation-str
                                       :desc desc}
                      conjugation (map->PronounVerbConjugation conjugation-map)]
                  conjugation)))]            
      (let [single-infl-cjgs (flatten
                              (for [cjg-fn-desc single-inflection-cjg-fn-descs]
                                (single-inflection-cjgs cjg-fn-desc verb)))
            multi-infl-cjgs (flatten
                             (for [cjg-fn multi-cjg-fns]
                               (cjg-fn verb)))
            all-cjgs (concat single-infl-cjgs
                             multi-infl-cjgs)]
        all-cjgs))))

(defn verb-with-transformed-verbs
  [verb]
  (let [verb-transformers [perfect-tense-verb
                           completion-விடு-verb
                           reflexive-verb
                           continuous-tense-verb
                           passive-verb]
        transformed-verbs (map #(% verb) verb-transformers)
        all-forms (into #{} (concat [verb] transformed-verbs))]
    all-forms))
                    
(defn all-conjugations-with-verb-transform
  [verb] 
  (let [all-verb-forms (verb-with-transformed-verbs verb) 
        all-cjgs (into #{} (flatten (for [verb-form all-verb-forms]
                                      (all-conjugations-no-verb-transform verb-form))))]
    all-cjgs))
