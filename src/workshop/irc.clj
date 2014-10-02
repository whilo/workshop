(ns workshop.irc
  (:require [irclj.core :as irc]
            [clojure.pprint :refer [pprint]]))


;; real secure(!) bot at https://github.com/hiredman/clojurebot

(def log (atom []))

@log

(def my-nick (str "eval" (rand-int 100)))

my-nick


(defn- pp-str [edn] ;; HACK pprint, find proper way?
  (let [out (java.io.StringWriter.)]
    (binding [*out* out]
      (pprint edn)
      (map second (re-seq #"(.+)\n" (.toString out))))))


(declare connection)
(defn ^:dynamic privmsg [ref m]
  (let [[[_ msg]] (re-seq (re-pattern (str "\\s*" my-nick ".?\\s*(.+)"))
                          (:text m))]
    (when (and (:text m) (:nick m)) (swap! log conj [(:nick m) [(:text m)]]))
    (when (and (not= my-nick (:nick m)) msg)
      (let [res (pp-str (try (-> msg read-string eval)
                          (catch Exception e (str e))))]
        (doseq [line res] (irc/reply connection m line))
        (swap! log conj [my-nick res])))))

(defn start []
  (def connection (irc/connect "irc.freenode.net"
                               7000
                               my-nick
                               :ssl? true
                               :callbacks {:privmsg #'privmsg}))
  (irc/join connection "##rzl-demo")
  (irc/message connection "##rzl-demo" "hello world!"))


(comment
  (start)
  (irc/kill connection)

  (irc/part connection "#raumzeitlabor" :message "bye"))
