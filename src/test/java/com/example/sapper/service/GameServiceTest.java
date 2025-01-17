package com.example.sapper.service;

import com.example.sapper.entity.Game;
import com.example.sapper.repository.GameRepository;
import com.example.sapper.schemas.request.NewGameRequest;
import com.example.sapper.schemas.response.GameInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        game = new Game();
        game.setId(UUID.randomUUID());
        game.setWidth(5);
        game.setHeight(5);
        game.setMinesCount(5);
        game.setCompleted(false);

        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));
    }

    @Test
    void createNewGame_shouldReturnGameInfoResponse() {
        NewGameRequest request = new NewGameRequest();
        request.setWidth(5);
        request.setHeight(5);

        when(gameRepository.save(any(Game.class))).thenReturn(game);

        GameInfoResponse response = gameService.createNewGame(request);

        assertEquals(game.getWidth(), response.getWidth());
        assertEquals(game.getHeight(), response.getHeight());
    }

    @Test
    void createNewGame_invalidHeight() {
        NewGameRequest request = new NewGameRequest();
        request.setWidth(5);
        request.setHeight(31);

        when(gameRepository.save(any(Game.class))).thenReturn(game);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameService.createNewGame(request));

        assertEquals("Высота не должна превышать 30.", exception.getMessage());
    }


}
