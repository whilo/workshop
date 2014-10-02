(defproject workshop "0.1.0-SNAPSHOT"
  :description "A workshop repository to demonstrate Clojure."
  :url "https://github.com/ghubber/workshop"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 ;; irc bot
                 [irclj "0.5.0-alpha4"]
                 ;; web-frontend
                 [ring "1.3.1"]
                 [compojure "1.1.9"]
                 [http-kit "2.1.19"]])
