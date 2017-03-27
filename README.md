      _ \  ____|  ___|__ __|   _ \  ____|  _ \  |     
     |   | __|  \___ \   |    |   | __|   |   | |     
     __ <  |          |  |    __ <  |     ___/  |     
    _| \_\_____|_____/  _|   _| \_\_____|_|    _____| 


Clojure REPL that resembles a shell in regard the current working directory
being an URL and allows sending request.

Ideally started with:

    rlwrap -m -M .clj -C rest-repl java -jar target/rest-repl-*-standalone.jar

Command line params:

    rest-repl [options...] [url]
    
      -i, --init script.clj  Run the given file before the first prompt
      -h, --help

On the prompt call `(help)` for the functions available (also
[here](resources/help.md))


![screenshot](doc/restrepl.png)


Examples
--------

[How to use the REST-REPL for generative testing with `clojure.spec`](doc/testing.md)


Uses
----

- [clj-http](https://github.com/dakrone/clj-http) to handle HTTP
- [cheshire](https://github.com/dakrone/cheshire) to handle JSON
- [data.xml](https://github.com/clojure/data.xml) to handle XML
- [specter](https://github.com/nathanmarz/specter) injected
  automatically to handle nested data
- [puget](https://github.com/greglook/puget) for colored output

