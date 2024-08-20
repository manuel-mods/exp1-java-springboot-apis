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
        movies.add(new Movie(1, "The Godfather", "Francis Ford Coppola", "Crime", 1972,
                "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son."));
        movies.add(new Movie(2, "The Shawshank Redemption", "Frank Darabont", "Drama", 1994, "Two imprisoned"));
        movies.add(new Movie(3, "Pokemon", "Ash Ketchup", "Anime", 2023, "Gotta catch them all"));
        movies.add(new Movie(4, "The Dark Knight", "Christopher Nolan", "Action", 2008,
                "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept"));
        movies.add(new Movie(5, "The Lord of the Rings: The Return of the King", "Peter Jackson", "Fantasy", 2003,
                "Gandalf and Aragorn lead the World"));

    }

    @GetMapping("/movies")
    public ResponseEntity<ArrayList<Movie>> getMovie() {
        // generate array of movies and return all movies
        return ResponseEntity.ok(movies);
    }

    // add a new movie to the list
    @PostMapping("/movies")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        // validate if all fields are filled
        if (movie.getId() == 0 || movie.getName() == null || movie.getTitle() == null || movie.getCategory() == null
                || movie.getYear() == 0 || movie.getSynopsis() == null) {

            return ResponseEntity.badRequest().build();
        }

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
