
Help
====

Uses [clj-http](https://github.com/dakrone/clj-http) to send request;
has all of [specter](https://github.com/nathanmarz/specter) required;
has `clojure.string` as `str`, 
    `cheshire.core` as `json`, 
    `clojure.data.xml` as `xml` required.

`@config` or `(default)`: 

  show current config

`(default k1..kn v)`:

  Set an value in config. (e.g. `(default :headers "Token" "sometoken")`

`(cd <path>)` or `(cd <old> <new>)`:

  Change current working URL; If absolute replace the current one.  If it's a
  single `-`, then go back to the previous URL. If starting with `~` use the
  `base-url` as home.  Otherwise go down deeper.  If two params are passed, any
  occurence of `<old>` in the current URL will be replaced with `<new>`.

`(request <:get|:post|...> <?path> {...})`:

  Send a request with the method kw via `clj-http` to the current URL (with
  optional `(cd ?path)`) with the map as request.  The current defaults from
  the config get merged with it.  `GET`, `POST`, `PUT`, `DELETE` are shortcuts
  for `request (:get|:post|:put|:delete)`.

`(xml [...])`, `(json ...)`:

  Helper to generate a XML or JSON body.
