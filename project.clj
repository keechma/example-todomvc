(defproject keechma-todomvc "0.1.0-SNAPSHOT"
  :description "TodoMVC implemented in Keechma"
  :url "http://github.com/keechma/keechma-todomvc"
  :license {:name "MIT"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"]
                 [org.clojure/core.async "0.7.559"
                  :exclusions [org.clojure/tools.reader]]
                 [reagent "0.9.1"]
                 [keechma "0.3.14"
                  :exclusions [cljsjs/react-with-addons
                               cljsjs/react-dom
                               cljsjs/react-dom-server]]
                 [keechma/entitydb "0.1.6"]]

  :plugins [[lein-figwheel "0.5.19"]
            [lein-cljsbuild "1.1.7"
             :exclusions [[org.clojure/clojure]]]
            [lein-marginalia "0.9.1"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild
  {:builds
   [{:id "dev"
     :source-paths ["src"]
     :figwheel {:on-jsload "keechma-todomvc.app/restart!"}
     :compiler {:main                 keechma-todomvc.app
                :optimizations        :none
                :output-to            "resources/public/js/app.js"
                :output-dir           "resources/public/js/dev"
                :asset-path           "js/dev"
                :source-map-timestamp true}}
    ;; This next build is an compressed minified build for
    ;; production. You can build this with:
    ;; lein cljsbuild once min
    {:id "min"
     :source-paths ["src"]
     :compiler {:main            keechma-todomvc.app
                :optimizations   :advanced
                :output-to       "resources/public/js/app.js"
                :output-dir      "resources/public/js/min"
                :elide-asserts   true
                :closure-defines {goog.DEBUG false}
                :pretty-print    false}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             })
