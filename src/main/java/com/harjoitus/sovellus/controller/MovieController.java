package com.harjoitus.sovellus.controller;

import com.harjoitus.sovellus.model.Movie;
import com.harjoitus.sovellus.service.MovieService;
import com.harjoitus.sovellus.service.OmdbApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private OmdbApiService omdbApiService;

    @GetMapping
    public String listMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movies/add";
    }

    @PostMapping
    public String saveMovie(@ModelAttribute Movie movie) {
        Map<String, Object> details = omdbApiService.getMovieDetails(movie.getTitle());
if (details != null && !details.isEmpty()) {
    movie.setDescription((String) details.get("Plot"));
    movie.setDirector((String) details.get("Director"));
   
    String yearString = (String) details.get("Year");
    if (yearString != null && !yearString.isEmpty()) {
        try {
            movie.setReleaseYear(Integer.parseInt(yearString));
        } catch (NumberFormatException e) {
          
            logger.error("Could not parse year from OMDb API response: " + yearString, e);
        }
    }
}

        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Movie> movie = movieService.getMovieById(id);
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            return "movies/edit";
        } else {
            return "redirect:/movies";
        }
    }

    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute Movie movie) {
        movie.setId(id);
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}
