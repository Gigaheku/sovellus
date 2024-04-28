package com.harjoitus.sovellus.repository;

import com.harjoitus.sovellus.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByDirectorContaining(String director);
    
    List<Movie> findByTitleContaining(String title);
}
