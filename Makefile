all:

clean:
	lein clean

uberjar: clean
	lein uberjar

run: uberjar
	rlwrap -m -M .clj java -jar target/rest-repl-*-standalone.jar
