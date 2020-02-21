(ns dict-data-builder.trie-test
  (:require [clj-thamil.format :as fmt]
            [clojure.test :refer :all]
            [clojure.zip :as z]
            [dict-data-builder.io :as io]
            [dict-data-builder.trie :as t :refer :all]))

(let [lines1 ["அகப்பட்ட	4662"
              "அகப்பட்டு	4662"]]
  (deftest trie-operations
    (let [trie1 (io/lines->phoneme-freq-trie lines1)
          tz1 (t/trie-zipper trie1)
          tz2 (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்"])
          tz2a (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "அ"])
          tz2b (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "உ"])]
      (testing "making a trie zipper"
        (is (= tz1 (-> tz1 z/root t/trie-zipper)))
        (is (= tz1 (-> tz2 z/root t/trie-zipper))))
      (testing "descending a trie zipper"
        (is (not (nil? (t/descend-trie-zipper tz1 ["அ"]))))
        (is (nil? (t/descend-trie-zipper tz1 ["இ"])))
        (is (not (nil? tz2)))
        (is (not (nil? tz2a)))
        (is (nil? (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "ஆ" "ல்"])))
        (testing "contains-sub-path (convenience fn)"
          (is (t/contains-sub-path tz1 ["அ"]))
          (is (not (t/contains-sub-path tz1 ["இ"])))
          (is (not (t/contains-sub-path tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "ஆ" "ல்"])))))
      (testing "trie zipper sibling vals"
        (is (= ["அ" "உ"] (t/sibling-trie-vals tz2a)))
        (is (= ["அ" "உ"] (t/sibling-trie-vals tz2b))))
      (testing "trie zipper parent path"
        (is (= ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்"] (t/parent-path tz2)))
        (is (= ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "அ"] (t/parent-path tz2a)))
        (is (= ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "உ"] (t/parent-path tz2b)))
        (let [words-map {"ஆறு" nil
                   "ஆறினான்" nil
                   "ஆற்றுக்கு" nil}
              words-as-phonemes-map (reduce-kv #(assoc %1 (fmt/str->phonemes %2) %3) {} words-map)
              trie (fmt/make-trie words-as-phonemes-map)
              tz1 (t/trie-zipper trie)
              tz2 (t/descend-trie-zipper tz1 ["ஆ" "ற்" "உ"])]
          (is (empty? (t/parent-path tz1)))
          (is (= ["ஆ" "ற்" "உ"] (t/parent-path tz2)))))
      (testing "matching a terminal subpath in a trie zipper"
        (let [tz1-paths1 [["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "அ"]
                          ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "உ"]]
              tz1-paths2 (conj tz1-paths1
                               ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "ஆ" "ல்"])]
          (is (t/contains-terminal-sub-paths tz1 tz1-paths1))
          (is (not (t/contains-terminal-sub-paths tz1 tz1-paths2))))
        (let [tz2-paths1 [["அ"]
                          ["உ"]]
              tz2-paths2 (conj tz2-paths1
                               ["ஆல்"])]
          (is (t/contains-terminal-sub-paths tz2 tz2-paths1))
          (is (not (t/contains-terminal-sub-paths tz2 tz2-paths2))))
        (testing "a nil at the end of a path in terminal subpath check is okay"
          (let [tz1-paths1-with-nil [["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "அ" nil]
                                     ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "உ" nil]]
                tz1-paths2-with-nil (conj tz1-paths1-with-nil
                                          ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "ஆ" "ல்"])]
            (is (t/contains-terminal-sub-paths tz1 tz1-paths1-with-nil))
            (is (not (t/contains-terminal-sub-paths tz1 tz1-paths2-with-nil))))))
      (testing "zipper-at-root-pos"
        (let [trie1 (io/lines->phoneme-freq-trie lines1)
              tz1 (t/trie-zipper trie1)
              tz2 (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்"])
              tz2a (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "அ"])]
          (is (not= tz1 tz2))
          (is (= tz1 (t/zipper-at-root-pos tz2)))
          (is (not= tz2 tz2a))
          (is (= tz1 (t/zipper-at-root-pos tz2a)))))
      (testing "is-at-terminus"
        (let [trie1 (io/lines->phoneme-freq-trie lines1)
              tz1 (t/trie-zipper trie1)
              tz2 (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்"])
              tz2a (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ப்" "ப்" "அ" "ட்" "ட்" "அ"])]
          (is (= false (t/is-at-terminus tz1)))
          (is (= false (t/is-at-terminus tz2)))
          (is (= true (t/is-at-terminus tz2a))))))))
