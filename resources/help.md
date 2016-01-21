
Help
====

Uses [clj-http](https://github.com/dakrone/clj-http) to send request;
Has all of [specter](https://github.com/nathanmarz/specter) required.

`@config`: 

  show current config

`(default k1..kn v)`:

  Set an value in config.

`(cd <path>)`:

  Change current working URL; If absolute replace the current one.  If
  starting with `~` use the `base-url` as home.  Otherwise go down
  deeper.

`(request <:get|:post|...> <?path> {...})`:

  Send a request with the method kw via `clj-http` to the current URL
  (if optional `(cd ?path)` with the map as request.  The current
  defaults get merged with it.
