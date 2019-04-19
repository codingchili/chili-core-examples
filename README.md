# chili-core-examples [![Build Status](https://travis-ci.org/codingchili/chili-core-examples.svg?branch=master)](https://travis-ci.org/codingchili/chili-core-examples)

An example highscore service using chili-core.


# Building
To build the jar, run
```console
./gradlew build
```

This runs the tests and creates a jar-file under `build/libs/`.

Start the jar with,
```console
$ java -jar chili-core-samples.jar
STARTUP [14:48:53.861] service.start   [SystemContext  ] name=com.codingchili.highscore.Service
STARTUP [14:48:54.279] listener.start  [HighscoreContext] handler=HighscoreHandler@api port :8080
```

# Testing

To retrieve the current highscore listing,
```console
curl http://localhost:8080/api/list
```

To update the highscore list,
```console
curl http://localhost:8080/api/update -d '{"player": "robin", "score": 500}'
```


---
Find other projects that use chili-core here: [topics/chili-core](https://github.com/topics/chili-core).