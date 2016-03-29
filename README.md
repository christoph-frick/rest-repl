      _ \  ____|  ___|__ __|   _ \  ____|  _ \  |     
     |   | __|  \___ \   |    |   | __|   |   | |     
     __ <  |          |  |    __ <  |     ___/  |     
    _| \_\_____|_____/  _|   _| \_\_____|_|    _____| 


Clojure REPL that resembles a shell in regard the current working directory
being an URL and allows sending request.

Ideally started with:

    rlwrap -m -M .clj -C rest-repl java -jar target/rest-repl-*-standalone.jar

[See the help](resources/help.md)

![screenshot](doc/restrepl.png)

Uses:

- [clj-http](https://github.com/dakrone/clj-http) to handle HTTP
- [cheshire](https://github.com/dakrone/cheshire) to handle JSON
- [data.xml](https://github.com/clojure/data.xml) to handle XML
- [specter](https://github.com/nathanmarz/specter) injected
  automatically to handle nested data
- [puget](https://github.com/greglook/puget) for colored output

