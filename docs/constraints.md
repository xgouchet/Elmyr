## Constraints

Most primitive type can be built using a single, simple constraint. Although you can also use finer control, those constraints can help create quickly simple values. 

Most of those are self explanatory



### IntConstraints 

 - **ANY** : can return any possible int;
 - **TINY** : can return an int between 0 and 32;
 - **SMALL** : can return an int between 0 and 256;
 - **BIG** : can return an int greater or equal to 65’536;
 - **HUGE** : can return an int greater or equal to 16’777’216;
 - **POSITIVE** : can return any int greater or equal to 0;
 - **POSITIVE_STRICT** : can return any int greater than 0;
 - **NEGATIVE** : can return any int less or equal to 0;
 - **NEGATIVE_STRICT** : can return any int less than 0;


### FloatConstraints 

 - **ANY** : can return any possible float;
 - **POSITIVE** : can return any float greater or equal to 0;
 - **POSITIVE_STRICT** : can return any float greater than 0;
 - **NEGATIVE** : can return any float less or equal to 0;
 - **NEGATIVE_STRICT** : can return float int less than 0;


### CharConstraints 

 - **ANY** : can return any possible char;
 - **ASCII** : can return any char from the ASCII table;
 - **ASCII_EXTENDED** : can return any char from the extended ASCII table;
 - **HEXADECIMAL** : can return any hexadecimal character;
 - **ALPHA** : can return any alphabetical (roman) character;
 - **ALPHA_NUM** : can return any alphabetical (roman) or numerical (arabic) character;
 - **NUMERICAL** : can return any numerical (arabic) character;
 - **WHITESPACE** : can return any whitespace character;
 - **NON_HEXADECIMAL** : can return any but an hexadecimal character;
 - **NON_ALPHA** : can return any but an alphabetical (roman) character;
 - **NON_ALPHA_NUM** : can return any but an alphabetical (roman) or numerical (arabic) character;
 - **NON_NUMERICAL** : can return any but a numerical (arabic) character;
 - **NON_WHITESPACE** : can return any but a whitespace character;

### StringConstraints 

 - **ANY** : can return any possible string;
 - **WORD** : returns a word-like string;
 - **LIPSUM** : returns a _Lorem Ipsum_ kind of sentence;
 - **HEXADECIMAL** : returns an hexadecimal string;
 - **NUMERICAL** : returns a numerical string;
 - **URL** : returns an URL-like string;
 - **EMAIL** : returns an e-mail-like string;
 - **PATH** : returns a path string, matching the current platform format;
 - **PATH_LINUX** : returns a path string, matching the Linux platform format;
 - **PATH_WINDOWS** : returns a path string, matching the Windows platform format;
 - **PATH_MACOS** : returns a path string, matching the MacOs platform format;
