package it.unibo.rmisudoku.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import it.unibo.rmisudoku.model.CellState;

public class SudokuGenerator {
    private List<List<CellState>> sudoku;
    private List<List<CellState>> solution;
    
    public List<List<CellState>> getSudoku() {
        return this.sudoku;
    }

    public List<List<CellState>> getSolution() {
        return this.solution;
    }

    public SudokuGenerator() {
        String url = "https://sudoku-api.vercel.app/api/dosuku";
        URL obj;
        try {
            obj = new URI(url).toURL();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Sudoku generation API returned error " + String.valueOf(responseCode));
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

            this.sudoku = new ArrayList<>();
            this.solution = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                List<Object> list = grid.getJSONArray(i).toList();
                List<CellState> cellStateList = new ArrayList<>();
                list.forEach(element -> {
                    cellStateList.add(new CellState((int) element));
                });
                this.sudoku.add(cellStateList);

                List<Object> solutionList = solutionGrid.getJSONArray(i).toList();
                List<CellState> cellStateSolutionList = new ArrayList<>();
                solutionList.forEach(element -> {
                    cellStateSolutionList.add(new CellState((int) element));
                });
                this.solution.add(cellStateSolutionList);
            }
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
