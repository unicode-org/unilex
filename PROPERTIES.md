# Unilex Properties

All files in [data](data/) are tab-separated plaintext files in
UTF-8 encoding. The file names are [Unicode language
identfiers](http://unicode.org/reports/tr35/#Unicode_language_identifier)
in IETF BCP47 syntax; for example, `vec-u-sd-itpd.txt` contains
data for the Venetian language as used in the Padua subdivision of Italy.


## Word frequency

In [frequency](data/frequency/), we collect data how often each word
form appears per billion tokens in a language corpus. (Our corpora are
actually much smaller than a billion tokens, but their size varies a lot
depending on the language; so we scale the numbers to a hypothetical
total of one billion tokens per language). Currently, we use Google’s
[Corpus Crawler](https://github.com/googlei18n/corpuscrawler) project
to build language corpora from we’re computing word frequencies, but
we’re open to accepting other contributions.


## Pronunciation

In [pronunciation](data/pronunciation/), we’ll collect phonemic transcriptions
of every word form to the
[International Phonetic Alphabet](https://en.wikipedia.org/wiki/International_Phonetic_Alphabet).
