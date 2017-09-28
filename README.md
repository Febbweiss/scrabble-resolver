# scrabble-resolver
This project builds language resolvers to resolve Scrabble enigmas.

## Usage

```
usage: Main [-b <source>] [-h] -l <language> [-max <max>] [-min <min>] [-r <letters>]
 -b,--build <source>      Build a new dictionary from the source file (a
                          Linux words file)
 -h,--help                Display help message
 -l,--lang <language>     Language (in [english, french])
 -max,--max <max>         Maximum word length (default : 7)
 -min,--min <min>         Minimum word length (default : 3)
 -r,--resolve <letters>   Find words with the given letters
 ```

This project can be used to provide data to game such as [Word Fighters](https://github.com/Febbweiss/wordfighters).

## Building a resolver

This project comes with 2 resolvers :
* English (British)
* French

To generate new ones, just use a file with one word per line (such as Linux  _[words](https://en.wikipedia.org/wiki/Words_(Unix))_) and run the Main providing the following parameters :
* _--build_ followed by the path to the file to parse
* _--lang_ to provide the language

You can configure words the will be manage by the resolver using the _--min_ and _--max_ parameters. Only words with length between _--min_ and _--max_ will be manage.

The resolver will be generated in the _src/main/resource/resolvers_ folder. To use it, rebuild the project :
```
mvn clean package
```

## Resolving enigmas

You can generate and resolve engima just using the _--lang_ parameter. A enigma will be generated and resolved using default parameters. You can custom resolution using the _--min_ and _--max_ parameters.

You can provide a letter suite to resolve using the _--resolve_ parameter, ie :
```
$> Main -l english -r achilst
Resolver in english loaded in 388ms.
Letters : achilst
Words between 3 and 7 letters : 
	with 5 letters (14): [chats, chits, clash, hails, halts, hilts, latch, laths, saith, shalt, staci, tails, thais, tisha]
	with 4 letters (35): [acts, ails, alit, cali, cash, cast, cats, chat, chit, hail, hals, halt, hats, hilt, hits, itch, lash, last, lath, lats, lisa, list, sail, salt, scat, shat, shit, silt, slat, slit, tail, talc, thai, this, tics]
	with 3 letters (23): [act, ail, ali, ash, ats, cal, cat, chi, hal, has, hat, his, hit, ila, its, lit, sac, sal, sat, sic, sit, tia, tic]

```

## Licences

Copyright (c) 2017 Fabrice ECAILLE aka Febbweiss

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
