package javlib.utils;

public class UrlFormatter
{

    public static String getIndexStarsUrl( char index, int page )
    {
        return String.format( JavConfig.indexStarsFt, index, page );
    }

    public static String getStarMoviesUrl( String starCode, int page )
    {
        return String.format( JavConfig.starMoviesFt, starCode, page );
    }

}
