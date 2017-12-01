(defproject babar "0.1.0-SNAPSHOT"
  :dependencies [;; Clojure & CLJS
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async "0.3.443"  exclusions [org.clojure/tools.reader]]
                ;; Web server
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [compojure "1.4.0"]
                 [ring-server "0.4.0"]
                 ;; Json
                 [cheshire "5.5.0"]
                 ;; Templating
                 [hiccup "1.0.5"]
                 ;; Environment variables from project.clj
                 [environ "1.0.1"]
                 ;; FE Libraries
                 [reagent "0.6.1"]
                 [reagent-utils "0.1.7"] [cljs-http "0.1.43"]
                ;; Routing and HTML5 pushstate
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.7"
                  :exclusions [org.clojure/tools.reader]]
                 ;; DB libraries
                 [org.clojure/java.jdbc "0.3.2"]
                 [postgresql "9.1-901.jdbc4"]
                 ]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"  "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :clean-targets ^{:protect false} ["resources/public/js"
                                    "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  ;; Standalone mock server
  :main babar.server
  :ring {:handler      babar.handler/app
         :uberwar-name "babar.war"}


  :profiles
  {:repl
   ;; In the latest CIDER, cider-jack-in-clojurescript "injects" those
   ;; dependencies in the project, so we don't need to explicitly
   ;; reference them....
   ;; :dependencies [[org.clojure/tools.nrepl "0.2.12"]]
   ;; :plugins [[cider/cider-nrepl "0.11.0-SNAPSHOT"]]
   {:dependencies [[com.cemerick/piggieback "0.2.1"]
                   [figwheel-sidecar "0.5.2"]]
    :repl-options
    {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}

   :dev
   {:dependencies [[com.cemerick/piggieback "0.2.1"]
                   [figwheel-sidecar "0.5.8"]]
    :source-paths ["src/cljs"]
    :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
    :plugins      [[lein-figwheel "0.5.10"]]
    :env          {;; This is how the client will resolve xhr calls to a
                   ;; specific backend:
                   :api-server-root
                   ;; Local API server
                   "http://localhost:3000"
                   :dev true}
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "babar.core/reload"}
     :compiler     {:main                 babar.core
                    :optimizations        :none
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/dev"
                    :asset-path           "js/dev"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            babar.core
                    :optimizations   :advanced
                    :output-to       "resources/public/js/app.js"
                    :output-dir      "resources/public/js/min"
                    :elide-asserts   true
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    ]})
