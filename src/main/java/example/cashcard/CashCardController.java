package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;
    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        }
        return ResponseEntity.notFound().build();
//        return cashCardOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    /*
    We were able to add UriComponentsBuilder ucb as a method argument to this
    POST handler method and it was automatically passed in.
    How so? It was injected from our now-familiar friend,
    Spring's IoC Container. Thanks, Spring Web!
     */
    @PostMapping
    ResponseEntity<Void> createCashCard(@RequestBody CashCard cashCard, UriComponentsBuilder uriComponentsBuilder) {
        CashCard savedCashCard = cashCardRepository.save(cashCard);
        URI locationOfCashCard = uriComponentsBuilder
                .path("cashcards/{requestedId}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locationOfCashCard).build();
    }
}
