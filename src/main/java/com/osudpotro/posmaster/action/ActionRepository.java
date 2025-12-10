package com.osudpotro.posmaster.action;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByName(String name);

    boolean existsByName(String name);

    @Query("""
                SELECT a, ura
                FROM Action a
                LEFT JOIN UiResourceAction ura
                    ON ura.action.id = a.id
                    AND ura.uiResource.id = :resourceId order by a.id ASC
            """)
    List<Object[]> findActionsWithUiResourceChecked(
            @Param("resourceId") Long resourceId);
}
