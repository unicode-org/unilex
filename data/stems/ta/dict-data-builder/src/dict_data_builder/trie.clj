(ns dict-data-builder.trie
  (:require [clj-thamil.format :as fmt]
            [clojure.set :as set]
            [clojure.zip :as z]))

;; TODO: parameterize sorting fns in clj-thamil by the letter seq

;;
;; hacking re-def'ing of letter seq
;;

(def letters [["ஃ" "அ" "ஆ" "இ" "ஈ" "உ" "ஊ" "எ" "ஏ" "ஐ" "ஒ" "ஓ" "ஔ"]
              ["க்" "க" "கா" "கி" "கீ" "கு" "கூ" "கெ" "கே" "கை" "கொ" "கோ" "கௌ"]
              ["ங்" "ங" "ஙா" "ஙி" "ஙீ" "ஙு" "ஙூ" "ஙெ" "ஙே" "ஙை" "ஙொ" "ஙோ" "ஙௌ"]
              ["ச்" "ச" "சா" "சி" "சீ" "சு" "சூ" "செ" "சே" "சை" "சொ" "சோ" "சௌ"]
              ["ஞ்" "ஞ" "ஞா" "ஞி" "ஞீ" "ஞு" "ஞூ" "ஞெ" "ஞே" "ஞை" "ஞொ" "ஞோ" "ஞௌ"]
              ["ட்" "ட" "டா" "டி" "டீ" "டு" "டூ" "டெ" "டே" "டை" "டொ" "டோ" "டௌ"]
              ["ண்" "ண" "ணா" "ணி" "ணீ" "ணு" "ணூ" "ணெ" "ணே" "ணை" "ணொ" "ணோ" "ணௌ"]
              ["த்" "த" "தா" "தி" "தீ" "து" "தூ" "தெ" "தே" "தை" "தொ" "தோ" "தௌ"]
              ["ந்" "ந" "நா" "நி" "நீ" "நு" "நூ" "நெ" "நே" "நை" "நொ" "நோ" "நௌ"]
              ["ப்" "ப" "பா" "பி" "பீ" "பு" "பூ" "பெ" "பே" "பை" "பொ" "போ" "பௌ"]
              ["ம்" "ம" "மா" "மி" "மீ" "மு" "மூ" "மெ" "மே" "மை" "மொ" "மோ" "மௌ"]
              ["ய்" "ய" "யா" "யி" "யீ" "யு" "யூ" "யெ" "யே" "யை" "யொ" "யோ" "யௌ"]
              ["ர்" "ர" "ரா" "ரி" "ரீ" "ரு" "ரூ" "ரெ" "ரே" "ரை" "ரொ" "ரோ" "ரௌ"]
              ["ல்" "ல" "லா" "லி" "லீ" "லு" "லூ" "லெ" "லே" "லை" "லொ" "லோ" "லௌ"]
              ["வ்" "வ" "வா" "வி" "வீ" "வு" "வூ" "வெ" "வே" "வை" "வொ" "வோ" "வௌ"]
              ["ழ்" "ழ" "ழா" "ழி" "ழீ" "ழு" "ழூ" "ழெ" "ழே" "ழை" "ழொ" "ழோ" "ழௌ"]
              ["ள்" "ள" "ளா" "ளி" "ளீ" "ளு" "ளூ" "ளெ" "ளே" "ளை" "ளொ" "ளோ" "ளௌ"]
              ["ற்" "ற" "றா" "றி" "றீ" "று" "றூ" "றெ" "றே" "றை" "றொ" "றோ" "றௌ"]
              ["ன்" "ன" "னா" "னி" "னீ" "னு" "னூ" "னெ" "னே" "னை" "னொ" "னோ" "னௌ"]])

(def letters-plus-grantha
  (concat fmt/letters 
          [["ஜ்" "ஜ" "ஜா" "ஜி" "ஜீ" "ஜு" "ஜூ" "ஜெ" "ஜே" "ஜை" "ஜொ" "ஜோ" "ஜௌ"]
           ["ஷ்" "ஷ" "ஷா" "ஷி" "ஷீ" "ஷு" "ஷூ" "ஷெ" "ஷே" "ஷை" "ஷொ" "ஷோ" "ஷௌ"]
           ["ஸ்" "ஸ" "ஸா" "ஸி" "ஸீ" "ஸு" "ஸூ" "ஸெ" "ஸே" "ஸை" "ஸொ" "ஸோ" "ஸௌ"]
           ["ஹ்" "ஹ" "ஹா" "ஹி" "ஹீ" "ஹு" "ஹூ" "ஹெ" "ஹே" "ஹை" "ஹொ" "ஹோ" "ஹௌ"]
           ["க்ஷ்" "க்ஷ" "க்ஷா" "க்ஷி" "க்ஷீ" "க்ஷு" "க்ஷூ" "க்ஷெ" "க்ஷே" "க்ஷை" "க்ஷொ" "க்ஷோ" "க்ஷௌ"]
           ["ஶ்ரீ"]]))

(def vowels
  (let [vowel-row (first letters)]
    (concat (rest vowel-row) [(first vowel-row)])))

(def c-cv-letters (rest letters-plus-grantha))

;; (def consonants (map first c-cv-letters))

(def ^{:private false
       :doc "a flattened seq of all தமிழ் letters in lexicographical (alphabetical) order -- put anohter way, in the order of அகர முதல் னரக இறுவாய் as the 2500 yr old grammatical compendium தொல்காப்பியம் states in its outset"}
  letter-seq (flatten (concat vowels c-cv-letters)))

(def ^{:doc "a map where the key is a தமிழ் letter, and the value is a number indicating its relative position in sort order"}
  sort-map (zipmap letter-seq (range)))

(defn letter-before?
  "a 2-arg predicate indicating whether the first string comes before the second string, but assuming that each string will only represent individual letters"
  [s1 s2]
  (cond (and (nil? s1) (nil? s2)) true
        (and (nil? (get sort-map s1)) (nil? (get sort-map s2))) (boolean (neg? (compare s1 s2)))
        (nil? (get sort-map s1)) true
        (nil? (get sort-map s2)) false
        :else (< (get sort-map s1) (get sort-map s2))))

(def ^{:doc "a comparator for strings that represent a single letter that respects தமிழ் alphabetical order"}
  letter-comp (comparator letter-before?))

;;
;; trie (clj-thamil style nested map) zipper fns 
;;


(defrecord TrieZipperNode [parent-val subtree-map])


(defn is-branch?
  "One of the input fns for creating a zipper for a trie"
  [node]
  (let [{:keys [parent-val subtree-map]} node
        [p m] [parent-val subtree-map]]
    (and m
         (map? m)
         ;;(not= [nil] (keys m))
         )))

(defn get-children
  "One of the input fns for creating a zipper for a trie."
  [node] 
  (let [{:keys [parent-val subtree-map]} node
        [p m] [parent-val subtree-map]
        entries (seq m)
        sorted-entries (sort-by (comp sort-map first) entries)
        sorted-entry-maps (for [entry sorted-entries]
                            (let [[p m] entry]
                              (map->TrieZipperNode {:parent-val p
                                                    :subtree-map m})))
        result-vec (vec sorted-entry-maps)]
    result-vec))

(defn make-node
  "One of the input fns for creating a zipper for a trie."
  [node new-children]
  (let [{:keys [parent-val subtree-map]} node
        new-children-map-entries (for [nc new-children]
                                   [(:parent-val nc)
                                    (:subtree-map nc)])
        new-subtree-map (apply merge new-children-map-entries)]
    (map->TrieZipperNode {:parent-val parent-val
                          :subtree-map new-subtree-map})))

(defn trie-zipper
  "Create a zipper for a trie."
  [trie-map]
  (let [init-node (if (set/subset? #{:parent-val :subtree-map} (-> trie-map keys set))
                    trie-map
                    (map->TrieZipperNode {:parent-val nil
                                          :subtree-map trie-map}))]
    (z/zipper is-branch? get-children make-node init-node)))


(defn descend-trie-zipper
  "Given a trie zipper a sub-path (seq of elems of the trie), attempt to descend in the zipper according to the sub-path.  Return the zipper after descent, or nil if unsuccesful.  sub-path may include a nil value at the end to represent terminal nil value in the trie as an attempt to match the end of a path, although no guarantees made that this will always work."
  [z sub-path]
  (if (empty? sub-path)
    z
    (if-not (z/branch? z)
      nil
      (let [elem (first sub-path)
            child-elems (->> z z/children (map :parent-val) set)]
        (when (contains? child-elems elem)
          (loop [child-pointer (z/down z)]
            (if (= elem (-> child-pointer z/node :parent-val))
              (descend-trie-zipper child-pointer (rest sub-path))
              (recur (z/right child-pointer)))))))))

(defn contains-sub-path
  "A convenience function that takes a trie zipper and sub-path and returns on whether the sub-path exists (regardless of whether it ends at a leaf)."
  [z sub-path]
  (not (nil? (descend-trie-zipper z sub-path))))

(defn sibling-trie-vals
  "For the current node, get a the parent-vals of the sibling nodes. Seq contains current node."
  [z]
  (->> z
       z/up
       z/children
       (map :parent-val)
       vec))

(defn parent-path
  "Given a trie zipper, return the seq (not including the initial sentinel nil value) from the root to the parent, inclusive.  Parent = parent-val in the current node."
  [z]
  (let [parent-val (:parent-val (z/node z))
        path-to-parent-exclusive (->> (z/path z)
                                      (map :parent-val)
                                      (drop 1) ;; drop the intial sentinel nil
                                               ;; alternatively: (drop 1)
                                      vec)
        path-to-parent-inclusive (if (zero? (count (z/path z)))
                                   []
                                   (conj path-to-parent-exclusive parent-val))]
    path-to-parent-inclusive))

(defn contains-terminal-sub-paths
  "Given a trie zipper and a seq of sub-paths, return whether
all of the sub-paths exist as the paths to terminal nodes"
  [z sub-paths]
  (let [sub-paths-with-terminal-nil (for [p sub-paths]
                                      (if (= nil (last p))
                                        p
                                        (-> p
                                            vec
                                            (conj nil))))
        all-paths-match (every? (partial descend-trie-zipper z) sub-paths-with-terminal-nil)]
    all-paths-match))

(defn zipper-at-root-pos
  "Returns the zipper after moving up to the root position, but unlike clojure.zip/root, provides the zipper and not just the value attached to it"
  [z]
  (->> (iterate z/up z)
       (take-while identity)
       last))

(defn is-at-terminus
  "Returns whether the zipper is at a leaf node position, regardless of whether it also may contain sub-paths"
  [z]
  (boolean
   (contains-terminal-sub-paths z [[nil]])))
