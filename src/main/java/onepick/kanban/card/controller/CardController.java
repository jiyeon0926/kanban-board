package onepick.kanban.card.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.service.CardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
}