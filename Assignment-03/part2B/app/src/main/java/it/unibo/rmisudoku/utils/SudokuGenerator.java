package it.unibo.rmisudoku.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import it.unibo.rmisudoku.model.CellState;

public class SudokuGenerator {
    private static final int GRID_SIZE = 9;
    private Grid<CellState> sudoku;
    private Grid<CellState> solution;
    
    public Grid<CellState> getSudoku() {
        return this.sudoku;
    }

    public Grid<CellState> getSolution() {
        return this.solution;
    }

    public SudokuGenerator() throws IOException {
        String url = "https://sudoku-api.vercel.app/api/dosuku";
        URL obj;
        try {
            obj = new URI(url).toURL();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                throw new IOException(
                    "Sudoku generation API returned error "
                    + String.valueOf(responseCode)
                );
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray grid = jsonObject
                .getJSONObject("newboard")
                .getJSONArray("grids")
                .getJSONObject(0)
                .getJSONArray("value");
            JSONArray solutionGrid = jsonObject
                .getJSONObject("newboard")
                .getJSONArray("grids")
                .getJSONObject(0)
                .getJSONArray("solution");

            this.sudoku = new Grid<>(GRID_SIZE, GRID_SIZE);
            this.solution = new Grid<>(GRID_SIZE, GRID_SIZE);
            for (int i = 0; i < GRID_SIZE; i++) {
                List<Object> list = grid.getJSONArray(i).toList();
                for (int j = 0; j < GRID_SIZE; j++) {
                    this.sudoku.setElement(
                        new Coords(i, j),
                        new CellState(
                            (int) list.get(j),
                            ((int) list.get(j) == 0)
                        )
                    );
                }

                List<Object> solutionList = solutionGrid.getJSONArray(i)
                    .toList();
                for (int j = 0; j < GRID_SIZE; j++) {
                    this.solution.setElement(
                        new Coords(i, j),
                        new CellState(
                            (int) solutionList.get(j),
                            ((int) solutionList.get(j) == 0)
                        )
                    );
                }
            }
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
