# Introduction to dict-data-builder

## Conjugation Examples

### Examples Setup

```clj
(ns dict-data-builder.conjugation)

(def செய் (map->Verb {:root "செய்" :verb-class "1a"}))

(def மாள் (map->Verb {:root "மாள்" :verb-class "1b"}))

(def செல் (map->Verb {:root "செல்" :verb-class "1c"}))

(def சோர் (map->Verb {:root "சோர்" :verb-class "2"}))

(def ஊற்று (map->Verb {:root "ஊற்று" :verb-class "3"}))

(def போடு (map->Verb {:root "போடு" :verb-class "4"}))

(def எடு (map->Verb {:root "எடு" :verb-class "6"}))

(def கற (map->Verb {:root "கற" :verb-class "7"}))

(def கேள் (map->Verb {:root "கேள்" :verb-class "5"}))

(def வில் (map->Verb {:root "வில்" :verb-class "5"}))

(def தின் (map->Verb {:root "தின்" :verb-class "5"}))

(def உண் (map->Verb {:root "உண்" :verb-class "5"}))

(def போ (map->Verb {:root "போ" :verb-class "3"}))

(def சொல் (map->Verb {:root "சொல்" :verb-class "3"}))

(def ஆ (map->Verb {:root "ஆ" :verb-class "3"}))
```

### Past Tense Examples

```clj
(->> (past-tense-conjugations எடு) 
     (map cjg-pprint-str)
     (run! println))
```

### Present Tense Examples

```clj
(->> (present-tense-conjugations எடு) 
     (map cjg-pprint-str)
     (run! println))
```

### Future Tense Examples

```clj
(->> (future-tense-conjugations எடு) 
     (map cjg-pprint-str)
     (run! println))
```

## Phoneme Decomposition and Comparison

Create fns to give 3rd person singular honorifc for past, present, and future

```clj
(def அவர் (get data/PRONOUNS "அவர்"))

(defn past-அவர் [verb] (past-tense-conjugations verb அவர்))

(defn present-அவர் [verb] (present-tense-conjugations verb அவர்))

(defn future-அவர் [verb] (future-tense-conjugations verb அவர்))
```

Create a fn that returns together past, present, and future of 3rd person singular honorific

```clj
(def past-present-future (juxt past-அவர்
                               present-அவர்
                               future-அவர்))

(past-present-future எடு)
```

Just get one form of conjugation per tense

```clj
(->> (past-present-future எடு)
     (map first))
```

Now split up into phonemes

```clj
(->> (past-present-future எடு)
     (map first)
     (map மொழி/தொடை->ஒலியன்கள்))
```

Anglicize the phonemes.  But note how this is a non-problem if we used an alphabet script.

```clj
(require '[clj-thamil.format.convert :as cvt])

(->> (past-present-future எடு)
     (map first)
     (map மொழி/தொடை->ஒலியன்கள்)
     (map (partial map cvt/தமிழ்->romanized)))
```
