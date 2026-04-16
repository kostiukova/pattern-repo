package ua.edu.chnu.combined;

import java.util.*;

// Інтерфейс для структурних патернів
interface Building {
    void build();
}

// Єдиний клас Castle, що підтримує всі патерни
class Castle implements Building, Cloneable {
    private String name;
    private int towers;
    private String material;

    // Конструктор для Creational патернів (Builder, Factory тощо)
    public Castle(String name, int towers, String material) {
        this.name = name;
        this.towers = towers;
        this.material = material;
    }

    // Конструктор для Structural патернів (Proxy, Adapter тощо)
    public Castle(String name) {
        this(name, 1, "Stone");
    }

    @Override
    public void build() {
        System.out.println("Castle Structure: " + name + " built with " + material);
    }

    // Виправлений патерн Prototype
    @Override
    public Castle clone() {
        try {
            return (Castle) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Castle(this.name, this.towers, this.material);
        }
    }

    @Override
    public String toString() {
        return "Castle{name='" + name + "', towers=" + towers + ", material='" + material + "'}";
    }

    // Геттери для Bridge/Flyweight
    public String getName() { return name; }
}

// 1. ПОРОДЖУВАЛЬНІ ПАТЕРНИ (CREATIONAL)

// Singleton
class KingdomRegistry {
    private static KingdomRegistry instance;
    private KingdomRegistry() {}
    public static KingdomRegistry getInstance() {
        if (instance == null) instance = new KingdomRegistry();
        return instance;
    }
    public void log(String msg) { System.out.println("[SINGLETON LOG]: " + msg); }
}

// Factory Method
abstract class CastleDeveloper {
    public abstract Castle createCastle();
}
class WoodCastleDeveloper extends CastleDeveloper {
    @Override
    public Castle createCastle() { return new Castle("Wooden Outpost", 1, "Oak"); }
}

// Abstract Factory
interface StyleFactory {
    Castle createStyleCastle();
}
class MedievalStyleFactory implements StyleFactory {
    @Override
    public Castle createStyleCastle() { return new Castle("Medieval Keep", 4, "Granite"); }
}

// Builder
class CastleBuilder {
    private String name;
    private int towers;
    private String material;
    public CastleBuilder setName(String n) { this.name = n; return this; }
    public CastleBuilder setTowers(int t) { this.towers = t; return this; }
    public CastleBuilder setMaterial(String m) { this.material = m; return this; }
    public Castle build() { return new Castle(name, towers, material); }
}

// Object Pool
class CastlePool {
    private Queue<Castle> pool = new LinkedList<>();
    public Castle acquire() {
        return pool.isEmpty() ? new Castle("Pooled Tent", 0, "Canvas") : pool.poll();
    }
    public void release(Castle c) { pool.offer(c); }
}

// 2. СТРУКТУРНІ ПАТЕРНИ (STRUCTURAL)

// Adapter
class OldSystemFence {
    public void buildOldFence() { System.out.println("Adapter: Old wooden fence erected."); }
}
class FenceAdapter implements Building {
    private OldSystemFence oldSystem;
    public FenceAdapter(OldSystemFence s) { this.oldSystem = s; }
    @Override
    public void build() { oldSystem.buildOldFence(); }
}

// Composite
class FortressComplex implements Building {
    private List<Building> components = new ArrayList<>();
    public void addComponent(Building b) { components.add(b); }
    @Override
    public void build() {
        System.out.println("Composite: Building a complex fortress...");
        for (Building b : components) b.build();
    }
}

// Proxy
class SecureCastleProxy implements Building {
    private Castle realCastle;
    private boolean isKing;
    public SecureCastleProxy(String name, boolean isKing) {
        this.realCastle = new Castle(name);
        this.isKing = isKing;
    }
    @Override
    public void build() {
        if (isKing) {
            System.out.print("Proxy (Access Granted): ");
            realCastle.build();
        } else {
            System.out.println("Proxy (Access Denied): Only King can build this!");
        }
    }
}

// Flyweight
class MaterialFlyweightFactory {
    private static final Map<String, String> cache = new HashMap<>();
    public static String getSharedMaterial(String type) {
        if (!cache.containsKey(type)) cache.put(type, "Shared_" + type);
        return cache.get(type);
    }
}

// Facade
class KingdomFacade {
    public void fastBuild() {
        System.out.println("Facade: Initializing full kingdom build...");
        new Castle("Capital").build();
        new OldSystemFence().buildOldFence();
    }
}

// Bridge
interface Color { void apply(); }
class RedColor implements Color { public void apply() { System.out.println(" with Red Flags."); } }

abstract class BriddedCastle {
    protected Color color;
    public BriddedCastle(Color c) { this.color = c; }
    abstract void draw();
}
class HighCastle extends BriddedCastle {
    public HighCastle(Color c) { super(c); }
    @Override void draw() { System.out.print("Bridge: High Castle"); color.apply(); }
}

// Decorator
abstract class BuildingDecorator implements Building {
    protected Building target;
    public BuildingDecorator(Building b) { this.target = b; }
    @Override public void build() { target.build(); }
}
class MoatDecorator extends BuildingDecorator {
    public MoatDecorator(Building b) { super(b); }
    @Override
    public void build() {
        super.build();
        System.out.println("Decorator: + Deep Moat added for defense.");
    }
}

// 3. MAIN - ДЕМОНСТРАЦІЯ ВСІХ ПАТЕРНІВ
public class Main {
    public static void main(String[] args) {
        System.out.println("=== GROUP 1: CREATIONAL PATTERNS (6) ===\n");

        // 1. Singleton
        KingdomRegistry.getInstance().log("System initialized.");

        // 2. Factory Method
        Castle wood = new WoodCastleDeveloper().createCastle();
        System.out.println("Factory Method: " + wood);

        // 3. Abstract Factory
        Castle medieval = new MedievalStyleFactory().createStyleCastle();
        System.out.println("Abstract Factory: " + medieval);

        // 4. Builder
        Castle custom = new CastleBuilder().setName("Sky Fortress").setTowers(10).setMaterial("Glass").build();
        System.out.println("Builder: " + custom);

        // 5. Prototype
        Castle clone = custom.clone();
        System.out.println("Prototype: " + clone);

        // 6. Object Pool
        CastlePool pool = new CastlePool();
        Castle pooled = pool.acquire();
        System.out.println("Object Pool: " + pooled);

        System.out.println("\n=== GROUP 2: STRUCTURAL PATTERNS (7) ===\n");

        // 1. Adapter
        Building adaptedFence = new FenceAdapter(new OldSystemFence());
        adaptedFence.build();

        // 2. Composite
        FortressComplex complex = new FortressComplex();
        complex.addComponent(new Castle("North Tower"));
        complex.addComponent(new Castle("South Tower"));
        complex.build();

        // 3. Proxy
        Building proxySecure = new SecureCastleProxy("Hidden Vault", false);
        proxySecure.build();

        // 4. Flyweight
        String sharedMat = MaterialFlyweightFactory.getSharedMaterial("Marble");
        System.out.println("Flyweight Material: " + sharedMat);

        // 5. Facade
        new KingdomFacade().fastBuild();

        // 6. Bridge
        new HighCastle(new RedColor()).draw();

        // 7. Decorator
        Building basic = new Castle("Standard Castle");
        Building decorated = new MoatDecorator(basic);
        decorated.build();

        System.out.println("\n=== ALL PATTERNS COMPLETED SUCCESSFULLY ===");
    }
}