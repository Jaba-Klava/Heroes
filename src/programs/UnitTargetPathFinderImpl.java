package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;

import java.util.*;

public class UnitTargetPathFinderImpl {
    private static final int WIDTH = 27; // Ширина поля
    private static final int HEIGHT = 21; // Высота поля
    private static final int[][] DIRECTIONS = getDirections();// Направления движения

    // Метод для получения направлений
    private static int[][] getDirections() {
        return new int[][] {
                {1, 0},   // Вниз
                {-1, 0},  // Вверх
                {0, 1},   // Вправо
                {0, -1}   // Влево
        };
    }

    // Метод находит путь к цели, используя алгоритм поиска по ширине (BFS, Breadth-First Search)
    //Поэтому сложность будет O (m*n), где n - ширина, m - высота.
    public List<Edge> findPath(Unit unit, int targetX, int targetY, List<Unit> existingUnitList) {
        // Инициализация необходимых структур данных
        int[][] distances = initializeDistanceArray();
        boolean[][] visitedCells = new boolean[WIDTH][HEIGHT];
        Edge[][] previousEdges = new Edge[WIDTH][HEIGHT];
        Set<String> occupiedPositions = collectOccupiedCells(existingUnitList, unit);
        PriorityQueue<EdgeDistance> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));

        setInitialPoint(unit, distances, priorityQueue); // Устанавливаем начальную точку

        // Выполняем алгоритм поиска пути
        while (!priorityQueue.isEmpty()) {
            EdgeDistance currentEdge = priorityQueue.poll();  // Извлечение ячейки с минимальным расстоянием

            // Если ячейка уже посещена, переходим к следующей
            if (visitedCells[currentEdge.getX()][currentEdge.getY()]) continue;
            visitedCells[currentEdge.getX()][currentEdge.getY()] = true;

            if (isTargetReached(currentEdge, targetX, targetY)) {
                break;
            }

            // Исследуем соседние ячейки
            exploreNeighbors(currentEdge, occupiedPositions, distances, previousEdges, priorityQueue);
        }

        return constructPath(previousEdges, unit, targetX, targetY);
    }

    // Метод инициализирует массив расстояний
    private int[][] initializeDistanceArray() {
        int[][] distances = new int[WIDTH][HEIGHT];
        // Устанавливаем начальные значения в максимальное значение
        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        return distances;
    }

    // Метод собирает занятые клетки
    private Set<String> collectOccupiedCells(List<Unit> existingUnitList, Unit unit) {
        Set<String> occupiedPositions = new HashSet<>();

        // Добавляем занятые клетки в набор
        for (Unit existingUnit : existingUnitList) {
            if (existingUnit.isAlive() && existingUnit != unit) {
                occupiedPositions.add(existingUnit.getxCoordinate() + "," + existingUnit.getyCoordinate());
            }
        }
        return occupiedPositions;
    }

    // Метод устанавливает начальную точку
    private void setInitialPoint(Unit unit, int[][] distances, PriorityQueue<EdgeDistance> priorityQueue) {
        int startX = unit.getxCoordinate();
        int startY = unit.getyCoordinate();
        distances[startX][startY] = 0;
        priorityQueue.add(new EdgeDistance(startX, startY, 0));
    }

    // Метод проверяет, достигнута ли целевая точка
    private boolean isTargetReached(EdgeDistance currentEdge, int targetX, int targetY) {
        return currentEdge.getX() == targetX && currentEdge.getY() == targetY;
    }

    // Метод исследует соседние ячейки
    private void exploreNeighbors(EdgeDistance currentEdge, Set<String> occupiedPositions, int[][] distances, Edge[][] previousEdges, PriorityQueue<EdgeDistance> priorityQueue) {
        for (int[] direction : DIRECTIONS) { // Проходим по всем направлениям
            int neighborX = currentEdge.getX() + direction[0];
            int neighborY = currentEdge.getY() + direction[1];

            // Проверяем доступность клетки
            if (isCellAvailable(neighborX, neighborY, occupiedPositions)) {
                int newDistance = distances[currentEdge.getX()][currentEdge.getY()] + 1;

                // Если новое расстояние меньше текущего, обновляем
                if (newDistance < distances[neighborX][neighborY]) {
                    distances[neighborX][neighborY] = newDistance; // Обновляем расстояние
                    previousEdges[neighborX][neighborY] = new Edge(currentEdge.getX(), currentEdge.getY()); // Запоминаем предыдущую ячейку
                    priorityQueue.add(new EdgeDistance(neighborX, neighborY, newDistance)); // Добавляем соседнюю ячейку в очередь
                }
            }
        }
    }

    // Проверка доступности клетки
    private boolean isCellAvailable(int x, int y, Set<String> occupiedPositions) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && !occupiedPositions.contains(x + "," + y);
    }

    // Метод для построения пути
    private List<Edge> constructPath(Edge[][] previousEdges, Unit unit, int targetX, int targetY) {
        List<Edge> path = new ArrayList<>();
        int currentX = targetX;
        int currentY = targetY;

        // Составляем путь, начиная с целевой ячейки
        while (previousEdges[currentX][currentY] != null) {
            path.add(new Edge(currentX, currentY));
            Edge previousEdge = previousEdges[currentX][currentY];
            currentX = previousEdge.getX();
            currentY = previousEdge.getY();
        }
        Collections.reverse(path); // Переворачиваем путь, чтобы он был от начальной до целевой точки
        return path; // Возвращаем построенный путь
    }

    // Публичный метод для доступа к пути
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        return findPath(attackUnit, targetUnit.getxCoordinate(), targetUnit.getyCoordinate(), existingUnitList);
    } // Запускаем поиск пути
}
