package com.example.sapper.controller;

import com.example.sapper.schemas.request.GameTurnRequest;
import com.example.sapper.schemas.request.NewGameRequest;
import com.example.sapper.schemas.response.ErrorResponse;
import com.example.sapper.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    @PostMapping("/new")
    public ResponseEntity<?> createNewGame(@RequestBody NewGameRequest request) {
        try {
            return ResponseEntity.ok(gameService.createNewGame(request));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/turn")
    public ResponseEntity<?> makeTurn(@RequestBody GameTurnRequest request) {
        try {
            return ResponseEntity.ok(gameService.makeTurn(request));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
