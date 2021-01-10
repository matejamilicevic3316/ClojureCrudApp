(defproject crudapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"
            
            }
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.0"]
                 [ring/ring-defaults "0.3.2"]
                 [metosin/ring-http-response "0.9.1"]
                 [compojure "1.6.2"]
                 [yogthos/config "1.1.7"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.postgresql/postgresql "42.2.18"]
                 [selmer/selmer "1.12.29"]
                 [ring-cors "0.1.13"]
                 [ring-basic-authentication "1.1.0"]
                 [buddy/buddy-hashers "1.7.0"]
                 [com.draines/postal "2.0.4"]
                 [crypto-password "0.2.1"]]
  :main ^:skip-aot crudapp.core
  :clean-targets ^{:protect false}
  {:http-server-root "public"
   :css-dirs ["resources/css"]
   :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})