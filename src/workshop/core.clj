;; STEPS
;; - quickly introduce bot/web concept
;; - nolen's cljs examples, primitives, simple functions, vars / state management, host interop
;; - quickly explain this code-base

;; Ausblick:
;; - languages as libraries: core.match, core.logic (sem-logic), core.async, core.typed, transducers
;; - clojurescript, clojure-cljr (...)
;; - datomic database
;; - examples of clojure apps
;;   - Daily Mail, whole backend, 11.34 million visitors 08/14
;;   - Amazon
;;   - Heroku
;;   - Akamai
;;   - robotic company repl robot
;;   - Twitter analytics (BackType)
;;   - groupon
;;   - prismatic smart news feed
;;   https://www.quora.com/Whos-using-Clojure-in-production
;;
;; - Deutsches Refernzbuch http://denkspuren.blogspot.de/2013/04/freies-clojure-buch-funktionale.html
;; - Clojure user group


;; Most parts of this tutorial are taken from https://github.com/swannodette/lt-cljs-tutorial
;; and adjusted for Clojure/Java. Thanks to David Nolen for the nice tutorial.

(ns workshop.core
  (:require [clojure.string :as string]))

;; :require is how you can import functionality from a different Clojure namespace into
;; the current one. Here we are requiring `clojure.string` and giving it an
;; alias. We could write the following:

(clojure.string/blank? "")

;; But that's really verbose compared to:

(string/blank? "")




;; Comments
;; ----------------------------------------------------------------------------

;; There are three ways to create comments in Clojure. The first way is
;; by preceding a line with a semi-colon, just like the lines you are reading
;; now.

;; The second way is by preceding a form with `#_`. This causes Clojure
;; to skip the evaluation of only the form immediately following, without
;; affecting the evaluation of the surrounding forms.

;; Try to reveal the secret message below:

(str "The secret word is " #_(string/reverse "tpircSerujolC"))

;; Finally, you can also create a comment using the `comment` macro. One common
;; technique is to use the `comment` macro to include code to be evaluated in a
;; REPL, but which you do not normally want to be included in the compiled
;; source.

;; For example, try placing your cursor after the last `)` below and type
;; Command-ENTER:

(comment

  (string/upper-case "This is only a test...")

  )

;; The `comment` macro makes the whole form return `nil`. Now go back and
;; highlight just the middle line, then type Command-ENTER. In this way
;; you can include code samples or quick tests in-line with the rest of
;; your code.


;; Definitions
;; ----------------------------------------------------------------------------

;; Once you have a namespace you can start creating top level definitions in
;; that namespace.

;; You can define a top level with `def`.

(def x 1)

x

;; You can also refer to top level definitions by fully qualifying them.

workshop.core/x

;; This means top levels can never be shadowed by locals and function
;; parameters.

(let [x 2]
  x)



;; One way to define a function is like this.

(def y (fn [] 1))

(y)


;; Defining functions in Clojure is common enough that `defn` sugar is
;; provided and idiomatic.

(defn z [] 1)

(z)


;; Literal data types
;; ----------------------------------------------------------------------------

;; Clojure comes out of the box with the usual useful data literals.

;; Booleans

(def a-boolean true)

;; Strings

(def a-string "Hello!")

;; Numbers

(def a-number 1)


;; Function literals
;; ----------------------------------------------------------------------------

;; Clojure also supports a short hand function literal which is useful
;; You can use the % and %N placeholders to represent function arguments.

;; You should not abuse the function literal notation as it degrades readability
;; outside of simple cases. It is nice for simple functional cases such as
;; the following.

(map (fn [n] (* n 2)) [1 2 3 4 5])

(map #(* % 2) [1 2 3 4 5])


;; Constructing a type
;; ----------------------------------------------------------------------------

;; Of course some JavaScript data types you will want to create with
;; constructor.

;; (java.util.Date.) is equivalent to new Date().

(def a-date (java.util.Date.))

a-date

;; Note the above returns an `#inst` data literal.

(def another-date #inst "2014-01-15")

another-date

;; Handy


;; Java Interop
;; ----------------------------------------------------------------------------

;; In Clojure we can extend namespaces on the fly (not in cljs), because we have eval.
(import '[java.util UUID])

(require '[clojure.data :refer [diff]])

(diff [1 2 3] [1 2 3])

;; Static member access happens with /.
(UUID/randomUUID)

(def ^:dynamic *id-fn* #(UUID/randomUUID))

(def counter (atom 0))

(binding [*id-fn* (fn [] (swap! counter inc))]
  (*id-fn*))

;; Method access through the dot-notation.
(.hashCode (UUID/randomUUID))

(. a-date getTime)



;; Clojure data types
;; ============================================================================

;; Unless there is a good reason you should generally write your Clojure
;; programs with Clojure data types. They have many advantages over
;; Java data types - they present a uniform API and they are immutable.

;; Vectors
;; ----------------------------------------------------------------------------

;; Instead of arrays Clojure programmers use persistent vectors, they are
;; like arrays - they support efficient random access, efficient update
;; and efficient addition to the end.

(def a-vector [1 2 3 4 5])

;; We can get the length of a vector in constant time via `count`.

(count a-vector)

;; We can add an element to the end.

(conj a-vector 6)

;; Note this does not mutate the array! `a-vector` will be left unchanged.

a-vector

;; Hallelujah!

;; We can access any element in a vector with `nth`. The following will
;; return the second element.

(nth ["foo" "bar" "baz"] 1)

;; Surprisingly vectors can be treated as functions. This is actually
;; a very useful property for associative data structures to have as
;; we'll see below with sets.

(["foo" "bar" "baz"] 0)


;; Maps
;; ----------------------------------------------------------------------------

;; Along with vectors maps are the most common data type in Clojure.
;; Map usage is analogous to the usage of Object in JavaScript, but
;; Clojure maps are immutable and considerably more flexible.

;; Let's define a simple map. Note `:foo` is a Clojure keyword.
;; Clojure programmers generally do not use strings for keys.

(def a-map {:foo "bar", :baz "woz"})

;; We can get the number of key-value pairs in constant time.

(count a-map)

;; We can access a particular value for a key with `get`.

(get a-map :foo)

;; We can add a new key-value pair with `assoc`.

(assoc a-map :noz "goz")

;; Again a-map is unchanged!

a-map

;; We can remove a key value pair with `dissoc`.

(dissoc a-map :foo)

;; Again a-map is unchanged!

a-map

;; Like vectors maps can act like functions.

(a-map :foo)

;; However Clojure keywords themselves can act like functions and the
;; following is more idiomatic.

(:foo a-map)

;; We can check if a map contains a key, with `contains?`.

(contains? a-map :foo)

;; We can get all the keys in a map with `keys`.

(keys a-map)

;; And all of the values with `vals`.

(vals a-map)

;; There are many cool ways to create maps.

(zipmap [:foo :bar :baz] [1 2 3])

(hash-map :foo 1 :bar 2 :baz 3)

(apply hash-map [:foo 1 :bar 2 :baz 3])

(into {} [[:foo 1] [:bar 2] [:baz 3]])

;; Clojure maps support complex keys.

(def complex-map {[1 2] :one-two [3 4] :three-four})

(get complex-map [3 4])


;; Keyword digression
;; ----------------------------------------------------------------------------

;; Let's take a moment to digress about keywords as they are so ubiquitous
;; in Clojure code.

(identity :foo)

;; If you add an additional preceding colon you'll get namespaced keyword.

(identity ::foo)

;; What good is this for? It allows you to put data into collections without
;; fear of namespace clashes without the tedium of manual namespacing them
;; in your source.

(identity {:user/foo ::foo})

;; Namespaced keywords are essential to Light Table's modularity.


;; Sets
;; ----------------------------------------------------------------------------

;; Clojure also supports sets.

(def a-set #{:cat :dog :bird})

;; `:cat` is already in `a-set`, so it will be unchanged.

(conj a-set :cat)

;; But `:zebra` isn't.

(conj a-set :zebra)

;; If you haven't guessed already, `conj` is a "polymorphic" function that adds
;; an item to a collection. This is some of the uniformity we alluded to
;; earlier.

;; `contains?` works on sets just like they do on maps.

(contains? a-set :cat)

;; Like vectors and maps, sets can also act as functions. If the argument
;; exists in the set it will be returned, otherwise the set will return nil.

(#{:cat :dog :bird} :cat)

;; This is powerful when combined with conditionals.

(defn check [x]
  (if (#{:cat :dog :bird} x)
    :valid
    :invalid))

(check :cat)
(check :zebra)

;; Lists
;; ----------------------------------------------------------------------------

;; A less common Clojure data structure is lists. This may be surprising
;; as Clojure is a Lisp, but maps, vectors and sets are the goto for most
;; applications. Still lists are sometimes useful.

(def a-list '(:foo :bar :baz))


;; Equality
;; ============================================================================

;; Clojure has a much simpler notion of equality than what is present
;; in other languages. In Clojure equality is always deep equality.

(= {:foo "bar" :baz "woz"} {:foo "bar" :baz "woz"})

;; Maps are not ordered.

(= {:foo "bar" :baz "woz"} {:baz "woz" :foo "bar"})

;; For sequential collections equality just works.

(= [1 2 3] '(1 2 3))

;; It is possible to check whether two things are represented by the same thing
;; in memory with `identical?`.

(def my-vec [1 2 3])
(def your-vec [1 2 3])

(identical? my-vec your-vec)


;; Control
;; ============================================================================

;; In order to write useful programs we need to be able to be able to express
;; control. Clojure provides the usual control constructs, however
;; truth-y and false-y values are not the same as in Java so it's worth
;; reviewing.


;; if
;; ----------------------------------------------------------------------------

;; 0 is not a false-y value.

(if 0
  "Zero is not false-y"
  "Yuck")

;; Nor is the empty string.

(if ""
  "An empty string is not false-y"
  "Yuck")

;; The only false-y values in Clojure are `nil` and `false`.

;; cond
;; ----------------------------------------------------------------------------

;; Nesting if tends to be noisy and hard to read so Clojure provides
;; a `cond` macro to deal with this.

(cond
  nil "Not going to return this"
  false "Nope not going to return this either"
  :else "Default case")


;; loop/recur
;; ----------------------------------------------------------------------------

;; The most primitive looping construct in Clojure is loop/recur. Most
;; of the iteration constructs are defined in terms of it. Using loop/recur is
;; usually considered bad style if a reasonable functional solution via
;; map/filter/reduce or a list comprehension is possible.

(loop [i 0 ret []]
  (if (< i 10)
    (recur (inc i) (conj ret i))
    ret))

(defn fib [x]
  (if (and (= x 0) (= x 1)) 1
    (recur (- x 1))))

(fib 1)


;; Moar functions
;; ============================================================================

;; Functions are the essence of any significant Clojure program so
;; we will dive into features that are unique to Clojure functions that
;; might be unfamiliar.

;; Here is a simple function that takes two arguments and adds them.

(defn foo1 [a b]
  (+ a b))

(foo1 1 2)

;; Functions can have multiple arities.

(defn foo2
  ([a b] (+ a b))
  ([a b c] (* a b c)))

(foo2 3 4)
(foo2 3 4 5)

;; Multiple arities can be used to supply default values.

(defn defaults
  ([x] (defaults x :default))
  ([x y] [x y]))

(defaults :explicit)
(defaults :explicit1 :explicit2)

;; Functions support rest arguments.

(defn foo3 [a b & d]
  [a b d])

(foo3 1 2)
(foo3 1 2 3 4)

;; You can apply functions.

(apply + [1 2 3 4 5])


;; Scoping
;; ============================================================================

;; Clojure has lexical scoping. In Clojure functions parameters and let binding
;; locals are not mutable!

(def some-x 1)

(let [some-x 2]
  some-x)

some-x


(let [fns (loop [i 0 ret []]
            (if (< i 10)
              (recur (inc i) (conj ret (fn [] i)))
              ret))]
  (map #(%) fns))


;; Exceptions
;; ============================================================================

(throw (IllegalArgumentException. "ERRRORZ everywhere!"))

;; not mentioned
;; - protocols
;; - multimethods
;; - inheritance
