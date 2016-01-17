all:

run:
	lein uberjar
	rlwrap -m -M .clj java -jar target/rest-repl-*-standalone.jar
