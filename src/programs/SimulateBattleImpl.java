package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;

import java.util.List;

public class SimulateBattleImpl {
    private Army friendlyArmy;
    private Army opposingArmy;

    public SimulateBattleImpl(Army friendlyArmy, Army opposingArmy) {
        this.friendlyArmy = friendlyArmy;
        this.opposingArmy = opposingArmy;
    }

    // Метод для старта битвы
    //Алгоритмическая сложность: O(m + n), где m — это число юнитов в первой армии, а n — это число юнитов во второй армии
    public void startBattle() {
        // Цикл продолжается, пока обе армии живы
        while (isArmyAlive(friendlyArmy) && isArmyAlive(opposingArmy)) {
            executeAttackPhase(friendlyArmy, opposingArmy);
            executeAttackPhase(opposingArmy, friendlyArmy);  // Теперь противник атакует
        }
    }

    // Проверяет, есть ли живые юниты в армии
    private boolean isArmyAlive(Army army) {
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }

    // Выполняет фазу атаки для данной армии
    //Алгоритмическая сложность: O(m), где m — число юнитов в атакующей армии
    private void executeAttackPhase(Army attackingArmy, Army defendingArmy) {
        for (Unit attacker : attackingArmy.getUnits()) {
            if (attacker.isAlive()) {
                Unit target = getRandomTarget(defendingArmy);
                if (target != null) {
                    attack(attacker, target);
                }
            }
        }
    }

    // Получение случайной цели среди врагов
    private Unit getRandomTarget(Army enemyArmy) {
        List<Unit> livingTargets = enemyArmy.getUnits().stream()
                .filter(Unit::isAlive)
                .toList();

        // Если нет подходящих целей, возвращаем null
        return livingTargets.isEmpty() ? null : livingTargets.get((int) (Math.random() * livingTargets.size()));
    }

    // Метод атаки одного юнита на другого
    private void attack(Unit attacker, Unit target) {
        int damageDealt = attacker.getBaseAttack(); // Здесь можно учесть бонусы
        target.setHealth(target.getHealth() - damageDealt);

        // Проверка на смерть цели
        if (target.getHealth() <= 0) {
            target.setAlive(false);
        }
    }
}


