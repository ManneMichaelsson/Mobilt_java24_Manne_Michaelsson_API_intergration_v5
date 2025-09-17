# Android Movie App

Detta är en android-app som är skriven i **Kotlin** och som använder **The Movie Database (TMDB)**.  
Api:n används för att söka efter filmer, se listor på populära filmer och visa detaljerad information om filmer. 

---

## Endpoints
- Populära filmer:  https://api.themoviedb.org/3/movie/popular?api_key=$apiKey
- Sök film:  https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query
- Detaljerad information om film:  https://api.themoviedb.org/3/movie/$movieId?api_key=$apiKey


---

##  Teknisk information
Applikationen använder biblioteket **Volley** för att göra nätverksanrop.  
JSON-svaret tolkas manuellt till en lista av objektet `Movie`.  
Denna listan skickas senare vidare till `MovieAdapter` som sedan uppdaterar `RecyclerView:en` för att visa resultatet för användaren.  

För att hämta bilder från URL som kommer från databasen används biblioteket **Glide**. 

---

## Struktur
Appen är byggd på en struktur där **MainActivity** är värd.  
Navigationen mellan appens fragments (`StartFragment`, `MovieListFragment` etc…) hanteras av Androids **Navigationskomponent** via en `nav_graph.xml`.  
