(ns workshop.web
  (:use [hiccup.page :only [html5]])
  (:require [workshop.irc :refer [log]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [compojure.handler :refer [site]]
            [org.httpkit.server :refer [run-server]]))

(defn print-msg [[nick msg]]
  [:div
   [:span {:style "color:red;"} (str nick ":")]
   [:span (apply str (interleave msg (repeat "<br/>")))]])

(defn print-log [log]
  (map print-msg log))

(defroutes irc-log
  (GET "/" [] (html5
                [:body [:script "setTimeout(function(){window.location.reload()},2000)"]
                 [:h3 "Eval-Bot Log"]
                 (print-log @log)]))
  (not-found "Ooops, nothing here..."))


(comment
  (def server (run-server (site #'irc-log) {:port 54321}))
  (server))
