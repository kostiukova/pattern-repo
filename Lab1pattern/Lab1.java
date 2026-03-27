package ua.edu.chnu.creational;

import java.util.*;


//1. ОСНОВНА СУТНІСТЬ 
class Castle {
    private String name;
    private int towers;
    private String material;

    // Конструктор доступний тільки всередині пакету (для патернів)
    protected Castle(String name, int towers, String material) {
        this.name = name;
        this.towers = towers;
        this.material = material;
    }

    // Prototype Pattern: копіювання об'єкта
    public Castle clone() {
        return new Castle(this.name, this.towers, this.material);
    }

    @Override
    public String toString() {
        return "Castle{Назва='" + name + "', Веж=" + towers + ", Матеріал='" + material + "'}";
    }
}

// 2. SINGLETON (Одинак)
class KingdomRegistry {
    private static KingdomRegistry instance;
    private List<String> history = new ArrayList<>();

    private KingdomRegistry() {}

    public static KingdomRegistry getInstance() {
        if (instance == null) instance = new KingdomRegistry();
        return instance;
    }

    public void log(String msg) { history.add(msg); System.out.println("LOG: " + msg); }
}

// 3. FACTORY METHOD (Фабричний метод)
abstract class CastleDeveloper {
    public abstract Castle createCastle();
}

class WoodCastleDeveloper extends CastleDeveloper {
    public Castle createCastle() { return new Castle("Дерев'яний форт", 1, "Дуб"); }
}

// 4. ABSTRACT FACTORY (Абстрактна фабрика)
interface AgeFactory {
    Castle createCastle();
}

class MedievalFactory implements AgeFactory {
    public Castle createCastle() { return new Castle("Середньовічна цитадель", 4, "Камінь"); }
}

// 5. BUILDER (Будівельник)
class CastleBuilder {
    private String name = "Default";
    private int towers = 1;
    private String material = "Dirt";

    public CastleBuilder setName(String name) { this.name = name; return this; }
    public CastleBuilder setTowers(int towers) { this.towers = towers; return this; }
    public CastleBuilder setMaterial(String material) { this.material = material; return this; }

    public Castle build() { return new Castle(name, towers, material); }
}

// 6. OBJECT POOL (Пул об'єктів)
class CastlePool {
    private Queue<Castle> pool = new LinkedList<>();

    public Castle acquire() {
        if (pool.isEmpty()) return new Castle("Тимчасовий намет", 0, "Тканина");
        return pool.poll();
    }

    public void release(Castle c) { pool.offer(c); }
}

// ГОЛОВНИЙ КЛАС ДЛЯ ДЕМОНСТРАЦІЇ (Main)
public class Lab1 {
    public static void main(String[] args) {
        System.out.println("=== Лабораторна робота №1: Породжувальні патерни ===\n");

        // 1. Singleton
        KingdomRegistry registry = KingdomRegistry.getInstance();
        registry.log("Запуск королівства...");

        // 2. Factory Method
        CastleDeveloper dev = new WoodCastleDeveloper();
        Castle woodCastle = dev.createCastle();
        System.out.println("Factory Method: " + woodCastle);

        // 3. Abstract Factory
        AgeFactory ageFactory = new MedievalFactory();
        Castle stoneCastle = ageFactory.createCastle();
        System.out.println("Abstract Factory: " + stoneCastle);

        // 4. Builder
        Castle customCastle = new CastleBuilder()
                .setName("Золотий Палац")
                .setTowers(20)
                .setMaterial("Золото")
                .build();
        System.out.println("Builder: " + customCastle);

        // 5. Prototype
        Castle clonedCastle = customCastle.clone();
        System.out.println("Prototype (Clone): " + clonedCastle);

        // 6. Object Pool
        CastlePool pool = new CastlePool();
        Castle pooled = pool.acquire();
        System.out.println("Object Pool (Acquire): " + pooled);
        pool.release(pooled);

        registry.log("Всі патерни продемонстровано!");
    }
}