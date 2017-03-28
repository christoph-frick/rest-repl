@Grapes([
		@Grab('io.ratpack:ratpack-groovy:1.4.5'),
		@Grab('org.slf4j:slf4j-simple:1.7.21')
])
import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.fromJson
import static ratpack.jackson.Jackson.json

class User {
	String username
	String password
}

ratpack {
	handlers {
		post() {
			render(parse(fromJson(User)).map{ User u ->
				if (u.username && u.password) {
					// artificial error
					assert u.password.size() < 17
					return json(u)
				} else {
					response.status(400)
					return json(error: "Missing arguments")
				}
			})
		}
	}
}
