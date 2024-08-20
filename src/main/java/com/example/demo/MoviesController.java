package com.example.demo;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoviesController {
    ArrayList<Movie> movies = new ArrayList<Movie>();

    MoviesController() {
        movies.add(new Movie(1, "The Godfather", "Francis Ford Coppola", "Crime", 1972));
        movies.add(new Movie(2, "The Shawshank Redemption", "Frank Darabont", "Drama", 1994));
        movies.add(new Movie(3, "Pokemon", "Ash Ketchup", "Anime", 2023));
    }

    @GetMapping("/movies")
    public ResponseEntity<ArrayList<Movie>> getMovie() {
        // generate array of movies and return all movies
        return ResponseEntity.ok(movies);
    }

    // add a new movie to the list
    @PostMapping("/movies")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        // request body json object

        movies.add(movie);
        return ResponseEntity.ok(movie);
    }

    // get movie from id
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") int movieId) {
        Movie movie = null;
        for (Movie m : movies) {
            if (m.getId() == movieId) {
                movie = m;
                break;
            }
        }
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }
}
