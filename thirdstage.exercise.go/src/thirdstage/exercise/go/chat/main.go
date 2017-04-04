package main

import (
	"log"
	"net/http"
	"path/filepath"
	"sync"
	"text/template"
	"github.com/gorilla/websocket"
)

type templateHandler struct {
	once     sync.Once
	filename string
	templ    *template.Template
}

func (t *templateHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	t.once.Do(func() {
		t.templ = template.Must(template.ParseFiles(filepath.Join("templates", t.filename)))
	})

	t.templ.Execute(w, r)

}

type room struct {
	forward chan []byte
}

type client struct {
	socket *websocket.Conn
	send chan []byte
	room *room
}

func main() {
	http.Handle("/", &templateHandler{filename: "chat.html"})

	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal("ListenAndServe:", err)
	}
}
