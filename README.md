# ![Unicode](http://www.unicode.org/webscripts/logo60s2.gif) Unilex

Unilex is a new subproject of Unicode CLDR.
The purpose is to provide a central repository of lexical data: information about words in different languages.
For each language, the goal is to collect [properties](PROPERTIES.md) such as
[word frequency](PROPERTIES.md#word-frequency),
[pronunciation](PROPERTIES.md#pronunciation),
[hyphenation](PROPERTIES.md#hyphenation), and eventually [part of
speech](PROPERTIES.md#part-of-speech) and other linguistic
information. Such data can be useful for building spell checkers, text-to-speech synthesis systems,
and a variety of other services that require language processing.

As a central repository, it can serve as a known location where software developers look to find lexical data, and a place where native language speakers can contribute data that can then be used to improve support for their language.
To flesh out the support in different languages, the we are soliciting contributions of lexical data for different languages.
That data is contributed as machine-readable list of words and their properties, as tab-separated plaintext files. For more about the current formats, see [Unilex Properties](https://github.com/unicode-org/unilex/blob/master/PROPERTIES.md)

Because the project has just started, there is a limited amount of data.
Please
[create an issue](https://github.com/unicode-org/unilex/issues) if you
can contribute lexical data for a language.

Eventually, we plan releasing lexical data for the world’s languages
as part of the
[Unicode Common Locale Data Repository](http://cldr.unicode.org/).
 The final format has has not been decided yet: one option could be to
format everything into XML files according to the [Lexical Markup
Framework](http://www.lexicalmarkupframework.org/); or we might define
some new, possibly JSON-based format; or maybe we’ll just publish the
data in its present form of tab-separated plaintext files.

### Copyright & Licenses

Copyright © 2017-2024 Unicode, Inc. Unicode and the Unicode Logo are registered trademarks of Unicode, Inc. in the United States and other countries.

A CLA is required to contribute to this project - please refer to the [CONTRIBUTING.md](https://github.com/unicode-org/.github/blob/main/.github/CONTRIBUTING.md) file (or start a Pull Request) for more information.

The contents of this repository are governed by the Unicode [Terms of Use](https://www.unicode.org/copyright.html) and are released under [LICENSE](./LICENSE).
