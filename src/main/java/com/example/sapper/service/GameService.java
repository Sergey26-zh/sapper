package com.example.sapper.service;

import com.example.sapper.entity.Cell;
import com.example.sapper.entity.Game;
import com.example.sapper.repository.GameRepository;
import com.example.sapper.schemas.request.GameTurnRequest;
import com.example.sapper.schemas.request.NewGameRequest;
import com.example.sapper.schemas.response.GameInfoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    /**
     * Создание новой игры
     *
     * @param request настройки новой игры {@link NewGameRequest}
     * @return информация об игре {@link GameInfoResponse}
     */
    @Transactional
    public GameInfoResponse createNewGame(NewGameRequest request) {
        validateNewGameRequest(request);

        Game game = initializeNewGame(request);
        game.setCells(initializeField(game));

        gameRepository.save(game);

        return mapToGameInfoResponse(game);
    }

    /**
     * Метод для обработки хода
     *
     * @param request данные о ходе игрока {@link GameTurnRequest}
     * @return информация об игре {@link GameInfoResponse}
     */
    @Transactional
    public GameInfoResponse makeTurn(GameTurnRequest request) {
        Game game = findGameById(request.getGameId());
        validateTurn(game, request);

        Cell cell = findCell(game, request.getRow(), request.getCol());

        if (cell.getValue().equals("M")) {
            game.setCompleted(true);
            revealAllMines(game);
        } else {
            revealCell(game, cell);
            if (isGameCompleted(game)) {
                game.setCompleted(true);
                revealAllMines(game);
            }
        }

        gameRepository.save(game);
        return mapToGameInfoResponse(game);
    }

    private void validateNewGameRequest(NewGameRequest request) {
        if (request.getWidth() > 30) {
            throw new IllegalArgumentException("Ширина не должна превышать 30.");
        }

        if (request.getHeight() > 30) {
            throw new IllegalArgumentException("Высота не должна превышать 30.");
        }

        if (request.getMinesCount() >= request.getWidth() * request.getHeight()) {
            throw new IllegalArgumentException("Количество мин должно быть меньше, чем общее количество ячеек.");
        }
    }

    private Game initializeNewGame(NewGameRequest request) {
        Game game = new Game();
        game.setWidth(request.getWidth());
        game.setHeight(request.getHeight());
        game.setMinesCount(request.getMinesCount());
        game.setCompleted(false);
        return game;
    }

    private List<Cell> initializeField(Game game) {
        int width = game.getWidth();
        int height = game.getHeight();
        int minesCount = game.getMinesCount();

        List<Cell> cells = createEmptyField(game, width, height);
        placeMines(cells, minesCount);
        calculateAdjacentMines(cells);

        return cells;
    }

    private List<Cell> createEmptyField(Game game, int width, int height) {
        List<Cell> cells = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = new Cell();
                cell.setGame(game);
                cell.setRow(row);
                cell.setCol(col);
                cell.setValue(" ");
                cells.add(cell);
            }
        }
        return cells;
    }

    private void placeMines(List<Cell> cells, int minesCount) {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < minesCount) {
            int index = random.nextInt(cells.size());
            Cell cell = cells.get(index);
            if (!cell.getValue().equals("M")) {
                cell.setValue("M");
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines(List<Cell> cells) {
        cells.stream()
                .filter(cell -> !cell.getValue().equals("M"))
                .forEach(cell -> {
                    int minesAround = countMinesAround(cell, cells);
                    if (minesAround > 0) {
                        cell.setValue(String.valueOf(minesAround));
                    }
                });
    }

    private int countMinesAround(Cell cell, List<Cell> cells) {
        return (int) cells.stream()
                .filter(c -> isAdjacent(cell, c))
                .filter(c -> c.getValue().equals("M"))
                .count();
    }

    private boolean isAdjacent(Cell cell, Cell adjacentCell) {
        int rowDiff = Math.abs(cell.getRow() - adjacentCell.getRow());
        int colDiff = Math.abs(cell.getCol() - adjacentCell.getCol());
        return (rowDiff <= 1 && colDiff <= 1) && !(rowDiff == 0 && colDiff == 0);
    }

    private void validateTurn(Game game, GameTurnRequest request) {
        if (game.isCompleted()) {
            throw new RuntimeException("Игра уже завершена.");
        }

        int row = request.getRow();
        int col = request.getCol();
        if (row < 0 || row >= game.getHeight() || col < 0 || col >= game.getWidth()) {
            throw new IllegalArgumentException("Некорректные координаты ячейки.");
        }
    }

    private Game findGameById(UUID gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Игра не найдена."));
    }

    private Cell findCell(Game game, int row, int col) {
        return game.getCells().stream()
                .filter(c -> c.getRow() == row && c.getCol() == col)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ячейка не найдена."));
    }

    private void revealCell(Game game, Cell cell) {
        if (!cell.getValue().equals(" ")) return;

        int minesAround = countMinesAround(cell, game.getCells());
        cell.setValue(minesAround == 0 ? "0" : String.valueOf(minesAround));

        if (minesAround == 0) {
            revealAdjacentCells(game, cell);
        }
    }

    private void revealAdjacentCells(Game game, Cell cell) {
        game.getCells().stream()
                .filter(c -> isAdjacent(cell, c))
                .filter(c -> c.getValue().equals(" "))
                .forEach(c -> revealCell(game, c));
    }

    private boolean isGameCompleted(Game game) {
        return game.getCells().stream()
                .noneMatch(cell -> cell.getValue().equals(" "));
    }

    private void revealAllMines(Game game) {
        game.getCells().stream()
                .filter(cell -> cell.getValue().equals("M"))
                .forEach(cell -> cell.setValue("X"));
    }

    private GameInfoResponse mapToGameInfoResponse(Game game) {
        GameInfoResponse response = new GameInfoResponse();
        response.setGameId(game.getId());
        response.setWidth(game.getWidth());
        response.setHeight(game.getHeight());
        response.setMinesCount(game.getMinesCount());
        response.setCompleted(game.isCompleted());
        response.setField(constructField(game));

        return response;
    }

    private List<List<String>> constructField(Game game) {
        Map<String, String> cellMap = game.getCells().stream()
                .collect(Collectors.toMap(cell -> cell.getRow() + "," + cell.getCol(), Cell::getValue));

        List<List<String>> field = new ArrayList<>();
        for (int row = 0; row < game.getHeight(); row++) {
            List<String> rowCells = new ArrayList<>();
            for (int col = 0; col < game.getWidth(); col++) {
                rowCells.add(cellMap.getOrDefault(row + "," + col, " "));
            }
            field.add(rowCells);
        }
        return field;
    }
}