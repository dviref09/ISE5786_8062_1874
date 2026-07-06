package renderer;

import java.io.FileWriter;
import java.io.IOException;

import geometries.impl.Geometries;
import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.BeamLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Test for minip2
 * @author Dvir Farkash
 */
@TestMethodOrder(OrderAnnotation.class)
public class Minip2Tests {
    
    /**
     * Path to the output benchmark measurement log file.
     */
    private static final String LOG_FILE = "measurements/MP2_measurements.txt";
    
    /**
     * Target image width and height resolution to thoroughly stress test the engine.
     */
    private static final int RESOLUTION = 1000;
    
    // =========================================================================
    // --- 1. No Acceleration Improvements ---
    // =========================================================================
    
    /**
     * Test 01: Baseline rendering with zero acceleration and single-threaded execution.
     */
    @Test
    @Order(8)
    void test01_NoAccel_NoMT() {
        runBenchmark("01. No Accel / No MT", false, false, false, false);
    }
    
    /**
     * Test 02: Baseline rendering with zero acceleration utilizing multi-core processing.
     */
    @Test
    @Order(7)
    void test02_NoAccel_MT() {
        runBenchmark("02. No Accel / With MT", true, false, false, false);
    }
    
    /**
     * Test 03: Flat scene execution accelerated only via Bounding Box (CBR) testing, single-threaded.
     */
    @Test
    @Order(6)
    void test03_CBR_NoMT() {
        runBenchmark("03. CBR Only / No MT", false, true, false, false);
    }
    
    /**
     * Test 04: Flat scene execution accelerated via Bounding Box (CBR) testing with multi-threading.
     */
    @Test
    @Order(5)
    void test04_CBR_MT() {
        runBenchmark("04. CBR Only / With MT", true, true, false, false);
    }
    
    /**
     * Test 05: Hierarchical scene graph constructed manually, using CBR, single-threaded.
     */
    @Test
    @Order(4)
    void test05_ManualBVH_NoMT() {
        runBenchmark("05. Manual BVH / No MT", false, true, true, false);
    }
    
    /**
     * Test 06: Hierarchical scene graph constructed manually, using CBR with multi-threading.
     */
    @Test
    @Order(3)
    void test06_ManualBVH_MT() {
        runBenchmark("06. Manual BVH / With MT", true, true, true, false);
    }
    
    /**
     * Test 07: Unstructured scene optimized via automated binary BVH tree construction, single-threaded.
     */
    @Test
    @Order(2)
    void test07_AutoBVH_NoMT() {
        runBenchmark("07. Auto BVH / No MT", false, true, false, true);
    }
    
    /**
     * Test 08: Unstructured scene optimized via automated binary BVH tree construction with multi-threading.
     */
    @Test
    @Order(1)
    void test08_AutoBVH_MT() {
        runBenchmark("08. Auto BVH / With MT", true, true, false, true);
    }
    
    /**
     * Test 09: Hierarchical scene graph constructed manually, without acceleration, single-threaded.
     */
    @Test
    @Order(9)
    void test09_ManualHierarchy_NoMT() {
        runBenchmark("09. Manual Hierarchy / No MT", false, false, true, false);
    }
    
    /**
     * Test 10: Hierarchical scene graph constructed manually, without acceleration, with multi-threading.
     */
    @Test
    @Order(10)
    void test10_ManualHierarchy_MT() {
        runBenchmark("10. Manual Hierarchy / With MT", true, false, true, false);
    }
    
    /**
     * Test 11: Unstructured scene optimized via automatic hierarchy construction, single-threaded.
     */
    @Test
    @Order(11)
    void test11_AutoHierarchy_NoMT() {
        runBenchmark("11. Auto Hierarchy / No MT", false, false, false, true);
    }
    
    /**
     * Test 12: Unstructured scene optimized via automatic hierarchy construction, with multi-threading.
     */
    @Test
    @Order(12)
    void test12_AutoHierarchy_MT() {
        runBenchmark("12. Auto Hierarchy / With MT", true, false, false, true);
    }
    
    // =========================================================================
    // --- Core Benchmark Execution Engine ---
    // =========================================================================
    
    /**
     * Prepares the scene infrastructure, isolates the rendering execution loop, triggers image generation, and writes
     * execution records to the benchmark text file. * @param configName Label identifying the active testing
     * configuration.
     * @param useMT Flag to activate multi-threaded rendering pipelines.
     * @param useCBR Flag to activate basic cloud bounding box intersections.
     * @param useManualBVH Flag to partition the helix structures into custom group hierarchies.
     * @param useAutoBVH Flag to trigger automatic spatial hierarchy partitioning.
     */
    private void runBenchmark(String configName, boolean useMT, boolean useCBR, boolean useManualBVH,
                              boolean useAutoBVH) {
        // 1. Instantiating the underlying Scene state (Creation overhead is explicitly excluded from timing)
        Scene scene = new Scene("Crystal DNA Helix")
                .setBackground(new Color(4, 6, 10))
                .setAmbientLight(new AmbientLight(new Color(10, 12, 16)));
        
        buildDnaScene(scene, useManualBVH);
        
        // 2. Camera Engineering and View Plane Mapping Configuration
        Camera.Builder cameraBuilder = new Camera.Builder()
                .setLocation(new Point(0, 320, 1580))
                .setDirection(new Point(0, 160, 0))
                .setVpSize(400, 400)
                .setVpDistance(1250)
                .setResolution(RESOLUTION, RESOLUTION)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setDebugPrint(1)
                .setSSRays(3);
        
        // Injecting the respective engine acceleration state variables based on target parameters
        if (useMT)
            cameraBuilder.setMultithreading(-1);
        if (useCBR)
            cameraBuilder.enableCBR();
        if (useAutoBVH)
            cameraBuilder.enableBVH();
        
        Camera camera = cameraBuilder.build();
        
        // 3. Performance Isolation Metric Window
        System.out.println("Executing Performance Test: " + configName);
        long startTime = System.currentTimeMillis();
        
        camera.renderImage(); // Core ray tracing process loop
        
        long renderTime = System.currentTimeMillis() - startTime;
        camera.writeToImage("Minip2Test");
        
        // 4. File I/O Persistence Logging
        logPerformance(configName, renderTime);
    }
    
    // =========================================================================
    // --- Procedural Scene Modeling ---
    // =========================================================================
    
    /**
     * Procedurally populates the 3D world space. Generates a complex architectural double-helix totaling exactly 901
     * primitive bodies (1 Plane + 150 pairs * 6 shapes). * Light positions and area sizes are explicitly configured to
     * project wide, distinct, highly observable soft shadows across the floor plane. * @param scene The target
     * active scene
     * object instance.
     * @param manualHierarchy When true, organizes structural elements into a 3-level tree hierarchy.
     */
    private void buildDnaScene(Scene scene, boolean manualHierarchy) {
        // --- High-Performance Physically Based Material Definitions ---
        // Mirror-like reflective ocean bed providing a clear canvas for casting soft penumbras
        Material oceanMat = new Material().setKD(0.4).setKS(0.6).setShininess(100).setKR(0.4);
        // Deeply refractive transparent crystal compound forming the spiraling strands
        Material backboneMat = new Material().setKD(0.1).setKS(0.75).setShininess(250).setKT(0.7).setKR(0.15);
        // Semi-translucent material creating dense geometric structures for crisp shadow creation
        Material bridgeMat = new Material().setKD(0.45).setKS(0.4).setShininess(80).setKT(0.15).setKR(0.1);
        
        Geometries root = scene.geometries;
        
        // 1. The Ocean Canvas (1 Infinite Plane Base) -> Added to Root (Level 1)
        root.add(new Plane(new Point(0, 0, 0), new Vector(0, 1, 0))
                .setMaterial(oceanMat)
                .setEmission(new Color(2, 4, 8)));
        
        // Allocating structural composite nodes if 3-Level Manual Hierarchy optimizations are active (Stage 2-B)
        Geometries helixRoot = manualHierarchy ? new Geometries() : root;
        
        // Level 2 Sub-containers: 5 Macro-Sectors
        Geometries[] macroSectors = null;
        // Level 3 Sub-containers: 15 Micro-Groups total (3 per Macro-Sector)
        Geometries[][] microGroups = null;
        
        if (manualHierarchy) {
            macroSectors = new Geometries[5];
            microGroups = new Geometries[5][3];
            for (int m = 0; m < 5; m++) {
                macroSectors[m] = new Geometries();
                for (int g = 0; g < 3; g++) {
                    microGroups[m][g] = new Geometries();
                }
            }
        }
        
        // 2. DNA Structural Generation Loop (150 Struts = 900 Distinct Geometric Entities)
        int totalBasePairs = 150;
        double radius = 42;
        
        for (int i = 0; i < totalBasePairs; i++) {
            double y = 15 + (i * 2.6); // Spatial elevation mapping
            double angle = i * (Math.PI / 12); // Circular helix rotation angle
            
            // Outer Helix Spherical Strands (Refractive Compounds)
            Point p1 = new Point(Math.cos(angle) * radius, y, Math.sin(angle) * radius);
            Point p2 = new Point(Math.cos(angle + Math.PI) * radius, y, Math.sin(angle + Math.PI) * radius);
            
            Sphere s1 = (Sphere) new Sphere(p1, 3.8).setMaterial(backboneMat).setEmission(new Color(0, 8, 25));
            Sphere s2 = (Sphere) new Sphere(p2, 3.8).setMaterial(backboneMat).setEmission(new Color(25, 6, 0));
            
            // Central Base-Pair Bridges (4 Triangles generating intricate crystalline shadow patterns)
            Point top = new Point(0, y + 1.2, 0);
            Point bot = new Point(0, y - 1.2, 0);
            Point right = new Point(Math.cos(angle + Math.PI / 2) * 6, y, Math.sin(angle + Math.PI / 2) * 6);
            Point left = new Point(Math.cos(angle - Math.PI / 2) * 6, y, Math.sin(angle - Math.PI / 2) * 6);
            
            Color bridgeGlow = new Color((i * 3) % 200, 40, 200 - ((i * 3) % 200));
            
            Triangle t1 = (Triangle) new Triangle(p1, top, right).setMaterial(bridgeMat).setEmission(bridgeGlow);
            Triangle t2 = (Triangle) new Triangle(p1, bot, right).setMaterial(bridgeMat);
            Triangle t3 = (Triangle) new Triangle(p2, top, left).setMaterial(bridgeMat).setEmission(bridgeGlow);
            Triangle t4 = (Triangle) new Triangle(p2, bot, left).setMaterial(bridgeMat);
            
            // Channeling structural insertion based on structural hierarchy strategies
            if (manualHierarchy) {
                int macroIndex = i / 30; // Separates into blocks of 30 pairs (0-4)
                int microIndex = (i % 30) / 10; // Sub-separates into blocks of 10 pairs (0-2)
                microGroups[macroIndex][microIndex].add(s1, s2, t1, t2, t3, t4);
            } else {
                root.add(s1, s2, t1, t2, t3, t4);
            }
        }
        
        // Assembling and stitching the 3-Level Manual hierarchy tree
        if (manualHierarchy) {
            for (int m = 0; m < 5; m++) {
                for (int g = 0; g < 3; g++) {
                    // Level 3 embedded directly inside Level 2
                    macroSectors[m].add(microGroups[m][g]);
                }
                // Level 2 embedded inside the main Helix structural composite node
                helixRoot.add(macroSectors[m]);
            }
            // Add Helix Composite root to main Scene Root composite (Level 1)
            root.add(helixRoot);
        }
        
        // --- 3. High-Emphasis Soft Shadow Light Source Architecture ---
        
        // A. Directional Light: Balanced midnight blue background fill
        scene.lights.add(new DirectionalLight(new Color(10, 15, 25), new Vector(1, -1, 0.5)));
        
        // B. Point Light 1: Low warm orange source with maximized width to generate extended soft penumbras
        scene.lights.add(new PointLight(new Color(255, 90, 30), new Point(-280, 40, -40))
                .setKl(0.002).setKq(0.00005)
                .setWidth(25).setHeight(25)); // Wide area array for pronounced soft shadow bleeding
        
        // C. Point Light 2: Elevated cold blue source producing overlapping penumbra intersections
        scene.lights.add(new PointLight(new Color(30, 90, 255), new Point(80, 360, 40))
                .setKl(0.002).setKq(0.00005)
                .setWidth(20).setHeight(20)); // Large source area creates wide blurred shadows
        
        // D. Spot Light: High-intensity side-angle spotlight projecting elongated, dramatic structural shadow outlines
        scene.lights.add(new SpotLight(new Color(160, 510, 160), new Point(180, 220, 100), new Vector(-1.2, -0.6, -0.8))
                .setKl(0.0008).setKq(0.00002)
                .setWidth(25).setHeight(25)); // Drastically expanded to stretch soft shadows onto the ocean surface
        
        // E. Narrow Beam (Stage 7 Flashlight Bonus): Concentrated white beam piercing downwards through the central
        // helix axis
        scene.lights.add(new BeamLight(new Color(765, 765, 765), new Point(0, 440, 0), new Vector(0, -1, 0))
                .setKl(0.0001)
                .setKq(0.00001)
                .setNarrowBeam(8) // Tight angle constriction
                .setWidth(10)
                .setHeight(10)); // Medium area ensures the high-contrast central shadows remain soft at the edges
    }
    
    /**
     * Appends processing measurements safely into the persistent log file. * @param config Name of the tested
     * configuration.
     * @param renderTimeMillis Time elapsed during the isolated camera execution step.
     */
    private void logPerformance(String config, long renderTimeMillis) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(String.format("Config: %-30s | Render Time: %7.3f sec\n",
                    config, renderTimeMillis / 1000.0));
        } catch (IOException ignore) {
        }
    }
}