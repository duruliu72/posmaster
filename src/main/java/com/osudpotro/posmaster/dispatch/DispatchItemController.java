package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/dispatch-items")
public class DispatchItemController {
//    @Autowired
//    private DispatchItemService dispatchItemService;
//
//    @DeleteMapping("/{id}/delete-item")
//    public DispatchItemDto deleteEntity(
//            @PathVariable(name = "id") Long id) {
//        return dispatchItemService.deleteEntity(id);
//    }
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Map<String, String>> handleEntityNotFound(Exception ex) {
//        return ResponseEntity.badRequest().body(
//                Map.of("error", ex.getMessage())
//        );
//    }
}
