package com.osudpotro.posmaster.action;

import com.osudpotro.posmaster.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ActionService {
    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;
    private final AuthService authService;
    public List<ActionDto> getAllActions() {
        return actionRepository.findAll()
                .stream()
                .map(actionMapper::toDto)
                .toList();
    }
    public ActionDto createAction(ActionCreateRequest request) {
        if(actionRepository.existsByName(request.getName())){
            throw new DuplicateActionException();
        }
        var user = authService.getCurrentUser();
        var action=actionMapper.toEntity(request);
        action.setCreatedBy(user);
        actionRepository.save(action);
        return actionMapper.toDto(action);
    }
    public ActionDto updateAction(Long actionId, UpdateActionRequest request) {
        var action = actionRepository.findById(actionId).orElseThrow(ActionNotFoundException::new);
        var user = authService.getCurrentUser();
        actionMapper.update(request, action);
        action.setUpdatedBy(user);
        actionRepository.save(action);
        return actionMapper.toDto(action);
    }
    public ActionDto getAction(Long actionId) {
        var action = actionRepository.findById(actionId).orElseThrow(ActionNotFoundException::new);
        return actionMapper.toDto(action);
    }
    public ActionDto deactivateAction(Long actionId) {
        var action = actionRepository.findById(actionId).orElseThrow(ActionNotFoundException::new);
        var user = authService.getCurrentUser();
        action.setStatus(2);
        action.setUpdatedBy(user);
        actionRepository.save(action);
        return actionMapper.toDto(action);
    }
    public ActionDto activateAction(Long actionId) {
        var action = actionRepository.findById(actionId).orElseThrow(ActionNotFoundException::new);
        var user = authService.getCurrentUser();
        action.setStatus(1);
        action.setUpdatedBy(user);
        actionRepository.save(action);
        return actionMapper.toDto(action);
    }
    public ActionDto deleteAction(Long actionId) {
        var action = actionRepository.findById(actionId).orElseThrow(ActionNotFoundException::new);
        var user = authService.getCurrentUser();
        action.setStatus(3);
        action.setUpdatedBy(user);
        actionRepository.save(action);
        return actionMapper.toDto(action);
    }
}
