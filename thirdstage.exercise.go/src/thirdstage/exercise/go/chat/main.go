//For complete code, refer https://github.com/matryer/goblueprints/blob/master/chapter1/chat/main.go
package main

import (
	"log"
	"net/http"
	"path/filepath"
	"sync"
	"text/template"
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

func main() {
	r := newRoom()

	http.Handle("/", &templateHanlder{filename : "chat.html"})
	http.Handle("/room", r)

	go r.run()

	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal("ListenAndServe:", err)
	}
}
