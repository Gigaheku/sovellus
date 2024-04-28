package com.harjoitus.sovellus.config;

import com.harjoitus.sovellus.model.Genre;
import com.harjoitus.sovellus.model.Movie;
import com.harjoitus.sovellus.model.Role;
import com.harjoitus.sovellus.model.User;
import com.harjoitus.sovellus.repository.GenreRepository;
import com.harjoitus.sovellus.repository.MovieRepository;
import com.harjoitus.sovellus.repository.RoleRepository;
import com.harjoitus.sovellus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Initialize roles and users
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_VISITOR");
        createUserIfNotFound("admin", "admin", "ROLE_ADMIN");
        createUserIfNotFound("user", "user", "ROLE_USER");

        // Initialize genres
        Genre drama = createGenreIfNotFound("Drama");
        Genre action = createGenreIfNotFound("Action");
        Genre romance = createGenreIfNotFound("Romance");

        // Initialize movies
        createMovieIfNotFound("The Shawshank Redemption", "Frank Darabont", 1994, "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.", drama);
        createMovieIfNotFound("The Godfather", "Francis Ford Coppola", 1972, "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", drama, action);
        createMovieIfNotFound("Titanic", "James Cameron", 1997, "A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.", drama, romance);
    }

    private void createRoleIfNotFound(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }
    }

    private void createUserIfNotFound(String username, String password, String roleName) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User(username, passwordEncoder.encode(password));
            Role role = roleRepository.findByName(roleName);
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    private Genre createGenreIfNotFound(String name) {
        Genre genre = genreRepository.findByName(name);
        if (genre == null) {
            genre = new Genre(name);
            genreRepository.save(genre);
        }
        return genre;
    }

    private void createMovieIfNotFound(String title, String director, int releaseYear, String description, Genre... genres) {
        List<Movie> movies = movieRepository.findByTitleContaining(title);
        if (movies.isEmpty()) {
            Movie movie = new Movie(title, director, releaseYear, description, new HashSet<>(Arrays.asList(genres)));
            movieRepository.save(movie);
        }
    }
}

