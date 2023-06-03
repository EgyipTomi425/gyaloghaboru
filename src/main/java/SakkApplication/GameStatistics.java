package sakkApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Játékosok statisztikai elemzésére készült osztály.
 */
public class GameStatistics 
{
    private ArrayList<GameResult> gameResults;

    /**
     * A feldolgozáshoz szükséges játékokat kell megadni.
     * @param gameResults játékokat tároló ArrayList.
     */
    public GameStatistics(ArrayList<GameResult> gameResults) 
    {
        this.gameResults = gameResults;
    }

    /**
     * Statisztikát készít játékosonként a játékokról
     * @return JatekosStatisztika ArrayList, ami az eredményeket tárolja.
     */
    public ArrayList<JatekosStatisztika> getGameStatistics() 
    {
        ArrayList<String> players = gameResults.stream()
                .flatMap(result -> Stream.of(result.getVilagos(), result.getSotet()))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> gyozelmiArany = players.stream()
                .map(player -> calculateWinRatio(player))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> lejatszottPartik = players.stream()
                .map(player -> calculatePlayedGames(player))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Double> atlagosLepesszam = players.stream()
                .map(player -> calculateAverageWinMoveCount(player))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<JatekosStatisztika> gameStatistics = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) 
        {
            JatekosStatisztika entry = new JatekosStatisztika(players.get(i), gyozelmiArany.get(i).toString(),
                    lejatszottPartik.get(i), atlagosLepesszam.get(i).toString());
            gameStatistics.add(entry);
        }

        return gameStatistics;
    }

    private int calculateWinRatio(String player) 
    {
        long totalGames = gameResults.stream()
                .filter(result -> result.getVilagos().equals(player) || result.getSotet().equals(player))
                .count();

        long winGames = gameResults.stream()
                .filter(result -> result.getGyoztesNev().equals(player))
                .count();

        if (totalGames == 0) 
        {
            return 0;
        }

        return (int) ((winGames * 100) / totalGames);
    }

    private int calculatePlayedGames(String player) 
    {
        return (int) gameResults.stream()
                .filter(result -> result.getVilagos().equals(player) || result.getSotet().equals(player))
                .count();
    }

    private double calculateAverageWinMoveCount(String player) 
    {
        return gameResults.stream()
            .filter(result -> {return result.getGyoztesNev().equalsIgnoreCase(player);})
            .mapToDouble(result -> {return result.getLepesek();})
            .average()
            .orElse(1000);
    }
}
