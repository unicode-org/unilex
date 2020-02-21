# dict-data-builder

This project is designed to read the word frequencies data in Unilex for Tamil (`ta` language code) as input and derive a set of basic part-of-speech (POS) tagged word stems as output.

Another goal of the project is to ensure that the output POS-tagged word lists (and the code used to generate them) are open-sourced under a permissive license with less restrictions, enabling the data to be used in more contexts.  The Unicode license is similar to MIT/BSD/Apache2/EPL/etc., but see [Unicode license FAQ](http://www.unicode.org/faq/unicode_license.html) for more information.

## Usage

The project is designed to be built and run with the [Leiningen build tool](https://leiningen.org/).

Running `lein run` currently produces the following usage output:

```
Usage:
    input-ta-only     cat ta.txt | lein run input-ta-no-eng > ta-no-eng.txt
    verbs1            cat ta-no-eng.txt | lein run verbs1 > verbs1.txt
    verbs2            cat verbs1.txt | lein run verbs2 > verbs2.txt
    input-no-verbs    lein run input-no-verbs ta-no-eng.txt verbs2.txt > no-verbs.txt
    nouns1            cat no-verbs.txt | lein run nouns1 > nouns1.txt
    nouns2            cat nouns1.txt | lein run nouns2 > nouns2.txt
```

The commands should be run in order to create a pipeline of processing.

### `input-ta-only`
* reads data from standard input produced as "word frequencies" in Unilex (`cat ta.txt`), where "word" is more accurately described as a string token, and frequencies are based on the source corpus
  * note: ta.txt can be downloaded from the Unilex [frequency data directory](https://github.com/unicode-org/unilex/tree/master/data/frequency)
* removes any header lines or any "words" containing non-Tamil characters, and most non-Tamil characters are English
* prints the filtered output to standard output (`ta-no-eng.txt`)

### `verbs1`
* reads Tamil-only words from standard input (`cat ta-no-eng.txt`)
* inserts each word as a sequence of phonemes into a prefix tree, iterates through the tree, and identifies verbs & their verb classes based on conjugation patterns
  * verb conjugation logic based on Tamil grammar lessons at [LearnTamil.com](https://www.learntamil.com/)
* prints verbs (verb root + verb class) to standard output (`verbs1.txt`) in EDN format

### `verbs2`
* reads in Tamil verbs from standard input as produced from `verbs1` command in EDN format (`cat verbs1.txt`)
* does stemming on verbs by creating a map that associates verbs and their transformed versions
* prints stemmed verbs to standard output (`verbs2.txt`) in EDN format

### `input-no-verbs`
* takes 2 arguments: the original filtered words list file (`ta-no-eng.txt`) and the stemmed verbs file (`verbs2.txt`)
* removes all the verbs from the original words
* prints the remaining words to standard output (`no-verbs.txt`) as quoted strings (EDN format for strings)

### `nouns1`
* reads in words without verbs from standard input (`cat no-verbs.txt`)
* inserts each word as a sequence of phonemes into a prefix tree, iterates through the tree, and identifies nouns based on rules for common suffixes and case suffixes for nouns (a.k.a. "inflections")
  * suffix and case suffix logic (சந்தி "sandhi" - to meet) based on Tamil grammar lessons at [LearnTamil.com](https://www.learntamil.com/)
* prints nouns to standard output (`nouns1.txt`) in EDN format

### `nouns2`
* reads in Tamil nouns from standard input as produced from `nouns` command in EDN format (`cat nouns1.txt`)
* does stemming on nouns by creating a map that associates nouns and their inflected versions
* prints nouns to standard output (`nouns2.txt`) as quoted strings (EDN format for strings)

## Details

Verb output contains the verb root and the verb class.  The verb classes are defined based on the original Dr. Graul's classification of Tamil verbs.  Verb class information is included both for enabling clear guidance for conjugation as well as disambiguation (often for intransitive/transitive pairs like சேர்<sup>2</sup> / சேர்<sup>6</sup> and வளர்<sup>2</sup> / வளர்<sup>6</sup>, but also for unrelated verbs like படு<sup>4</sup> / படு<sup>6</sup>).

The phoneme prefix tree used for verb and noun inference was converted into a zipper in order to allow for easy up/down/left/right navigation.  Code that provides the conversion of the trie into a zipper, and convenience utility funtions based on the trie zipper, were added in `trie.clj`.

The logic for stemming with the knowledge of "derived verbs" and "inflected nouns" is similar.  The map of derivations/inflections (derived verb -> original verb; inflected noun -> original noun) can be thought of as representing the edges of a tree in which each node points to its parent, where the parent node is the original word and the child node is the transformed/inflected version of the parent.  The iterations of the stemming code are like taking each of the verbs one step up its respective tree until it hits its root.  It might be possible to make this more efficient by stemming each verb fully before moving on to the next.

When determining the verbs, a list of false positives was hard-coded in order to clear the hurdle of noun/adjective pairs that fit the verb conjugation rules, irregular verbs, and similar errors.  Similar work was done to filter the output of noun stemming (partly due to incomplete removal of AvP and verbal adjectives and verbal nouns).  The hope is that one of the many benefits of having dictionary data is that future stemming effort can be made easier and quicker after the establishment of this lexical dataset.  Future work could test that assumption.

## License

Distributed under the Unicode license, same as Unilex.  The Unicode license is similar to MIT.  See [Unicode license FAQ](http://www.unicode.org/faq/unicode_license.html) for more information.
