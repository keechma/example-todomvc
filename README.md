# example-todomvc

TodoMVC application example for the [Keechma framework](http://github.com/keechma/keechma).

Read the [annotated source](http://keechma.com/annotated/todomvc.html).

## Setup

Make sure you have [Leiningen](http://leiningen.org/) installed.

Clone the repo:

```
$ git clone https://github.com/keechma/example-todomvc.git
$ cd example-todomvc
```

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL.

## Build and view docs

From within the `example-todomvc` directory:

```
$ lein marg $(find src -type f | sort)
$ open docs/uberdoc.html
```

## References

- [Keechma Guides](https://keechma.com/guides/)
- [Creating Reagent Components](https://github.com/reagent-project/reagent/blob/master/doc/CreatingReagentComponents.md)

## License

Copyright © 2020 Mihael Konjevic

Distributed under the MIT License.
