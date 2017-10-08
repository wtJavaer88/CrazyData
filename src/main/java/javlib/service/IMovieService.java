package javlib.service;

import javlib.entity.JMovie;

import org.jsoup.nodes.Document;

public interface IMovieService
{
    JMovie getMovieDetail( Document doc );
}
