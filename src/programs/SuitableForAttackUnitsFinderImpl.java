package programs;


import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;

import java.util.List;
import java.util.stream.Collectors;

public class SuitableForAttackUnitsFinderImpl {

    // Метод для поиска юнитов, которые могут атаковать
    // Алгоритмическая сложность: O(n)
    public List<Unit> findSuitableUnits(Army army) {
        return army.getUnits().stream()
                .filter(Unit::isAlive) // Только живые юниты
                .collect(Collectors.toList());
    }
}
