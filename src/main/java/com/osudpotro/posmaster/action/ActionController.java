package com.osudpotro.posmaster.action;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/actions")
public class ActionController {
    private final ActionService actionService;

    //    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @GetMapping
    public List<ActionDto> getAllActions() {
        return actionService.getAllActions();
    }

    @GetMapping("/checked/{id}")
    public List<ActionsWithUiResource> getActionsWithUiResourceChecked(@PathVariable Long id) {
          return actionService.getActionsWithUiResourceChecked(id);
    }

    //    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @GetMapping("/{id}")
    public ActionDto getAction(@PathVariable Long id) {
        return actionService.getAction(id);
    }

    @PostMapping
    public ResponseEntity<ActionDto> createAction(@Valid @RequestBody ActionCreateRequest request, UriComponentsBuilder uriBuilder) {
        var actionDto = actionService.createAction(request);
        var uri = uriBuilder.path("/actions/{id}").buildAndExpand(actionDto.getId()).toUri();
        return ResponseEntity.created(uri).body(actionDto);
    }

    @PutMapping("/{id}")
    public ActionDto updateAction(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateActionRequest request) {
        return actionService.updateAction(id, request);
    }

    @DeleteMapping("/{id}")
    public ActionDto deleteAction(
            @PathVariable(name = "id") Long id) {
        return actionService.deleteAction(id);
    }

    @GetMapping("/{id}/deactivate")
    public ActionDto deactivateAction(
            @PathVariable(name = "id") Long id) {
        return actionService.deactivateAction(id);
    }

    @GetMapping("/{id}/activate")
    public ActionDto activateAction(
            @PathVariable(name = "id") Long id) {
        return actionService.activateAction(id);
    }

    @ExceptionHandler(DuplicateActionException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAction(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(ActionNotFoundException.class)
    public ResponseEntity<Void> handleActionNotFound() {
        return ResponseEntity.notFound().build();
    }
}
