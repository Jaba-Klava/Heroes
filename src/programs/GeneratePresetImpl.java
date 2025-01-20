package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneratePresetImpl {
    private static final String[] UNIT_TYPES = {"archer", "spear", "swordsman", "cavalry"};
    private static final String[] ATTACK_TYPES = {"ranged", "melee"};
    private Random randomGenerator;

    public GeneratePresetImpl() {
        this.randomGenerator = new Random();
    }

    // Метод для генерации случайной армии с заданным количеством юнитов
    // Алгоритмическая сложность: O(n), n — это количество юнитов, которые нужно сгенерировать
    public Army generateRandomArmy(int unitCount) {
        List<Unit> generatedUnits = new ArrayList<>();

        for (int i = 0; i < unitCount; i++) {
            String unitName = "Unit" + (i + 1);
            String unitType = UNIT_TYPES[randomGenerator.nextInt(UNIT_TYPES.length)];
            int unitHealth = randomGenerator.nextInt(100) + 1;
            int unitBaseAttack = randomGenerator.nextInt(20) + 1;
            int unitCost = randomGenerator.nextInt(50) + 1;
            String unitAttackType = ATTACK_TYPES[randomGenerator.nextInt(ATTACK_TYPES.length)];

            // Создаем карты бонусов (можно настроить по желанию)
            Map<String, Double> attackBonuses = new HashMap<>();
            Map<String, Double> defenceBonuses = new HashMap<>();
            attackBonuses.put("defaultAttackBonus", 0.0);  // Пример
            defenceBonuses.put("defaultDefenseBonus", 0.0); // Пример

            int unitXCoordinate = randomGenerator.nextInt(10); // Примерный диапазон координат
            int unitYCoordinate = randomGenerator.nextInt(10); // Примерный диапазон координат

            // Создаем юнит с его характеристиками
            Unit unit = new Unit(unitName, unitType, unitHealth, unitBaseAttack, unitCost,
                    unitAttackType, attackBonuses, defenceBonuses, unitXCoordinate, unitYCoordinate);
            generatedUnits.add(unit);
        }

        // Возвращаем новую армию со случайно сгенерированными юнитами
        return new Army(generatedUnits);
    }
}
