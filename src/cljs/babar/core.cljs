(ns babar.core
 (:require-macros [cljs.core.async.macros :refer [go]])
 (:require [cljs-http.client :as http]
           [cljs.core.async :refer [<!]]
           #_ [ajax.core :refer [GET]]
           [reagent.core :as reagent]
           [babar.html :as html]
   ))

;;;---------------------------------------------------------------------------
;;; Vars
;;;---------------------------------------------------------------------------

(defonce app-state
  (reagent/atom {}))

;;;---------------------------------------------------------------------------
;; Initialize App
;;;---------------------------------------------------------------------------

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")
    ))

;;;---------------------------------------------------------------------------
;;; Routes
;;;---------------------------------------------------------------------------

(defn vertex-route [graph-name]
  (str "/vertices/" graph-name))

;;;----------------------------------------------------------------------------
;;; Links

;;;---------------------------------------------------------------------------
;;; Graphs Page
;;;---------------------------------------------------------------------------


(def graph-keys '(:link :vertex_count :edge_count))

;;----------------------------------------------------------------------------

(defn graph-headers []
  (zipmap graph-keys '("Graph Name" "Vertex Count" "Edge Count")))

;;----------------------------------------------------------------------------

;; Generate a graph href items

(defn generate-graph-hrefs [maps]
  (map #(assoc % :href (vertex-route (:name %))) maps))

(def graphs-mock-data
  [{:name "Elephant" :vertex_count  18941 :edge_count 1491032}
   {:name "Art" :vertex_count 21001 :edge_count 1525596}
   {:name "Biology" :vertex_count 16421 :edge_count 2025148}
   {:name "Geography" :vertex_count 15939 :edge_count 1391879}
   {:name "History" :vertex_count 23181 :edge_count  2272900}
   {:name "Literature" :vertex_count 19623 :edge_count 1704489}
   {:name "Mathematics" :vertex_count 15706 :edge_count  1193512}
   {:name "Science" :vertex_count 23509 :edge_count 1717632}])
    

;;----------------------------------------------------------------------------

(defn get-graph-entries []
  #_
  (go (let [response #_
            (<! (http/get "http://localhost:3000/graphs"))
            (<! (http/get "https://api.github.com/users"
                                ;; parameters
                                 {:with-credentials? false
                                  :query-params {"since" 135}}))]
        ;;(prn response)
        ;;(prn (:status response))
        ;;(prn (:body response))
        (:body response)))
  graphs-mock-data)

;;----------------------------------------------------------------------------

(defn graphs-page []
  (let [graph-entries (get-graph-entries)]
    (prn graph-entries)
    [:div ;; (html/layout-css "Graphs Page"
     [:h1 "Knowledge Graphs"]
     [:section
      [:div {:id "content"}]
      (html/generate-table
       `(~(graph-headers)
         ~@(html/generate-links (generate-graph-hrefs graph-entries) :name :href))
       graph-keys)
      ]
     [:section
      [:div {:id "bottom-navigation"}
       [:a {:id "navrow-bottom" :href "/Test"} "Images"]
       [:a {:id "navrow-bottom" :href "/Test"} "Tests"]]
      ]]
     ))

;;;---------------------------------------------------------------------------
;; Home Page
;;;---------------------------------------------------------------------------

(defn page [ratom]
  (graphs-page))

;;;---------------------------------------------------------------------------

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

;;;---------------------------------------------------------------------------
(defn ^:export main []
  (dev-setup)
  (reload))

;;;----------------------------------------------------------------------------
;;; End of File
;;;----------------------------------------------------------------------------
