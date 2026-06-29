package renderer;

import java.io.FileWriter;
import java.io.IOException;

import geometries.api.Intersectable;
import geometries.impl.Geometries;
import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
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
 * Integration and performance testing for Mini-Project 2 (MP02).
 * Splitting the original cityscape scene into multiple specific test configurations
 * to benchmark combinations of Multithreading, CBR (Cloud Bound Region / Bounding Boxes),
 * and manual hierarchical scene partitioning.
 * * Results are written to a performance log file.
 * * @author Software Engineering Expert
 */
@TestMethodOrder(OrderAnnotation.class)
public class Minip2Tests {
    
    private static final String LOG_FILE = "measurements/MP2_measurements.txt";
    
    /**
     * Test 1: Baseline performance. Single-threaded, NO CBR, Flat Scene Structure.
     */
    @Test
    @Order(1)
    public void testCityscape_SingleThread_NoCBR_Flat() {
        runTestConfiguration("Single-Threaded / No CBR / Flat Scene", false, false, false);
    }
    
    /**
     * Test 2: Adding Multithreading only. Multi-threaded, NO CBR, Flat Scene Structure.
     */
    @Test
    @Order(2)
    public void testCityscape_MultiThread_NoCBR_Flat() {
        runTestConfiguration("Multi-Threaded / No CBR / Flat Scene", true, false, false);
    }
    
    /**
     * Test 3: Adding CBR to single thread. Single-threaded, WITH CBR, Flat Scene Structure.
     */
    @Test
    @Order(3)
    public void testCityscape_SingleThread_WithCBR_Flat() {
        runTestConfiguration("Single-Threaded / With CBR / Flat Scene", false, true, false);
    }
    
    /**
     * Test 4: Combining Multithreading and CBR. Multi-threaded, WITH CBR, Flat Scene Structure.
     */
    @Test
    @Order(4)
    public void testCityscape_MultiThread_WithCBR_Flat() {
        runTestConfiguration("Multi-Threaded / With CBR / Flat Scene", true, true, false);
    }
    
    /**
     * Test 5: Manual Hierarchical Partitioning with CBR. Single-threaded, WITH CBR, Hierarchical Scene Tree.
     */
    @Test
    @Order(5)
    public void testCityscape_SingleThread_WithCBR_ManualHierarchy() {
        runTestConfiguration("Single-Threaded / With CBR / Manual Hierarchy Tree", false, true, true);
    }
    
    /**
     * Test 6: Ultimate Optimization. Multi-threaded, WITH CBR, Hierarchical Scene Tree.
     */
    @Test
    @Order(6)
    public void testCityscape_MultiThread_WithCBR_ManualHierarchy() {
        runTestConfiguration("Multi-Threaded / With CBR / Manual Hierarchy Tree", true, true, true);
    }
    
    /**
     * Core runner method that builds the scene dynamically based on flags, renders it,
     * measures execution time, and writes the benchmark analytics to a file.
     */
    private void runTestConfiguration(String testName, boolean useMultithreading, boolean useCBR,
                                      boolean useManualHierarchy) {
        // 1. Initialize Scene
        Scene scene = new Scene("Futuristic Cityscape over a Mirror Lake")
                .setBackground(new Color(10, 15, 30))
                .setAmbientLight(new AmbientLight(new Color(20, 20, 35)));
        
        // Configure CBR/BVH globally on the scene if supported by your engine's setCBR/setBVH method
        // scene.setCBR(useCBR);
        
        // 2. Define Materials (Ending with capital L, using setKX methods)
        Material lakeMaterial = new Material().setKD(0.2).setKS(0.2).setShininess(100).setKR(0.35);
        Material crystalMaterial = new Material().setKD(0.3).setKS(0.6).setShininess(60).setKT(0.5).setKR(0.4);
        Material solidGlossyMaterial = new Material().setKD(0.4).setKS(0.5).setShininess(30).setKR(0.1);
        Material orbMaterial = new Material().setKD(0.1).setKS(0.8).setShininess(80).setKT(0.8);
        
        // --- Multi-Level Hierarchy Containers Architecture ---
        // Root components
        Geometries rootContainer = scene.geometries;
        
        // Tier 1 Containers (Sub-roots)
        Geometries buildingsRoot = useManualHierarchy ? new Geometries() : rootContainer;
        Geometries celestialRoot = useManualHierarchy ? new Geometries() : rootContainer;
        
        // Tier 2 Containers (Spatially divided sectors for buildings hierarchy)
        Geometries leftDistrict = useManualHierarchy ? new Geometries() : buildingsRoot;
        Geometries centerDistrict = useManualHierarchy ? new Geometries() : buildingsRoot;
        Geometries rightDistrict = useManualHierarchy ? new Geometries() : buildingsRoot;
        
        // Base/Background Elements (Always added directly to root)
        rootContainer.add(new Plane(
                new Point(0, -100, 0),
                new Vector(0, 1, 0))
                .setMaterial(lakeMaterial)
                .setEmission(new Color(5, 10, 20)));
        
        // --- Group A: Pyramids from Triangles (50 pyramids * 12 triangles = 600 bodies) ---
        int pyramidCount = 50;
        for (int i = 0; i < pyramidCount; i++) {
            double xCenter = -380 + (i % 10) * 85 + (i * 9.5) % 30;
            double zCenter = -180 - (i / 10) * 130 - (i * 17) % 50;
            double width = 35 + (i % 4) * 12;
            double height = 70 + (i % 6) * 25;
            
            Point base1 = new Point(xCenter - width / 2, -100, zCenter - width / 2);
            Point base2 = new Point(xCenter + width / 2, -100, zCenter - width / 2);
            Point base3 = new Point(xCenter + width / 2, -100, zCenter + width / 2);
            Point base4 = new Point(xCenter - width / 2, -100, zCenter + width / 2);
            Point apex = new Point(xCenter, -100 + height, zCenter);
            
            Color buildingEmission = new Color(15 + i * 2, 10 + (i % 3) * 15, 40 + (i % 5) * 12);
            Material mat = (i % 3 == 0) ? crystalMaterial : solidGlossyMaterial;
            
            // Determine which spatial branch container to target based on X coordinate
            Geometries sectorTarget;
            if (xCenter < -120) {
                sectorTarget = leftDistrict;
            } else if (xCenter > 120) {
                sectorTarget = rightDistrict;
            } else {
                sectorTarget = centerDistrict;
            }
            
            // Outer Layer (Tier 3 structures)
            sectorTarget.add(new Triangle(base1, base2, base3).setMaterial(mat).setEmission(buildingEmission));
            sectorTarget.add(new Triangle(base1, base3, base4).setMaterial(mat).setEmission(buildingEmission));
            sectorTarget.add(new Triangle(base1, base2, apex).setMaterial(mat).setEmission(buildingEmission));
            sectorTarget.add(new Triangle(base2, base3, apex).setMaterial(mat).setEmission(buildingEmission));
            sectorTarget.add(new Triangle(base3, base4, apex).setMaterial(mat).setEmission(buildingEmission));
            sectorTarget.add(new Triangle(base4, base1, apex).setMaterial(mat).setEmission(buildingEmission));
            
            // Inner Core Layer (Tier 3 structures)
            double innerW = width * 0.75;
            double innerH = height * 1.15;
            Point innerApex = new Point(xCenter, -100 + innerH, zCenter);
            Point ib1 = new Point(xCenter - innerW / 2, -100, zCenter - innerW / 2);
            Point ib2 = new Point(xCenter + innerW / 2, -100, zCenter - innerW / 2);
            Point ib3 = new Point(xCenter + innerW / 2, -100, zCenter + width / 2);
            Point ib4 = new Point(xCenter - innerW / 2, -100, zCenter + width / 2);
            
            sectorTarget.add(new Triangle(ib1, ib2, ib3).setMaterial(crystalMaterial)
                                                        .setEmission(buildingEmission.reduce(2)));
            sectorTarget.add(new Triangle(ib1, ib3, ib4).setMaterial(crystalMaterial)
                                                        .setEmission(buildingEmission.reduce(2)));
            sectorTarget.add(new Triangle(ib1, ib2, innerApex).setMaterial(crystalMaterial)
                                                              .setEmission(buildingEmission.reduce(2)));
            sectorTarget.add(new Triangle(ib2, ib3, innerApex).setMaterial(crystalMaterial)
                                                              .setEmission(buildingEmission.reduce(2)));
            sectorTarget.add(new Triangle(ib3, ib4, innerApex).setMaterial(crystalMaterial)
                                                              .setEmission(buildingEmission.reduce(2)));
            sectorTarget.add(new Triangle(ib4, ib1, innerApex).setMaterial(crystalMaterial)
                                                              .setEmission(buildingEmission.reduce(2)));
        }
        
        // --- Group B: Celestial Field of Spheres (250 Spheres) ---
        int ballsCount = 250;
        for (int j = 0; j < ballsCount; j++) {
            double radius = 4 + (j % 5) * 3;
            double x = -550 + (j * 13) % 1100;
            double y = 40 + (j * 19) % 350;
            double z = -150 - (j * 7) % 750;
            
            Color sphereEmission = new Color((j * 4) % 120, 30 + (j * 5) % 90, 90 + (j * 3) % 140);
            
            Material mat;
            if (j % 6 == 0) {
                mat = new Material().setKD(0.2).setKS(0.4).setShininess(50).setKT(0.6).setKR(0.5);
            } else if (j % 2 == 0) {
                mat = crystalMaterial;
            } else {
                mat = orbMaterial;
            }
            
            celestialRoot.add(new Sphere(new Point(x, y, z), radius)
                    .setMaterial(mat)
                    .setEmission(sphereEmission));
        }
        
        // Assemble the Multi-Level Composite Tree structure if hierarchy is active
        if (useManualHierarchy) {
            // Bind Tier 2 (Districts) into Tier 1 (Buildings Root)
            buildingsRoot.add(leftDistrict);
            buildingsRoot.add(centerDistrict);
            buildingsRoot.add(rightDistrict);
            
            // Bind Tier 1 roots into the main Scene geometries container
            rootContainer.add(buildingsRoot);
            rootContainer.add(celestialRoot);
        }
        
        // 3. Add 5 Randomized Cloud Lights directly above the city
        scene.lights.add(new DirectionalLight(new Color(30, 60, 90), new Vector(0.15, -1, -0.15)));
        scene.lights.add(new PointLight(new Color(400, 250, 100), new Point(245, 290, -480)).setKl(0.0005)
                                                                                            .setKq(0.0002));
        scene.lights.add(new SpotLight(new Color(500, 100, 300), new Point(-315, 360, -195), new Vector(0.05, -1,
                -0.05)).setKl(0.001)
                       .setKq(0.0005));
        scene.lights.add(new PointLight(new Color(50, 400, 150), new Point(-110, 240, -530)).setKl(0.002).setKq(0.001));
        scene.lights.add(new SpotLight(new Color(100, 200, 600), new Point(135, 410, -215), new Vector(-0.05, -1,
                0.05)).setKl(0.0005)
                      .setKq(0.0001));

        if (useCBR) {
            Intersectable.enableCbr(); // Ensure CBR flag is enabled in the engine
            scene.geometries.buildTree(); // Automatically sort the scene into an optimized tree structure
        }
        
        // 4. Camera Configuration Builder
        Camera.Builder cameraBuilder = new Camera.Builder()
                .setLocation(new Point(0, 450, 900))
                .setDirection(new Vector(0, -0.5, -1), new Vector(0, 1, -0.5))
                .setVpSize(200, 200)
                .setVpDistance(400)
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setDebugPrint(1);
        
        if (useMultithreading) {
            cameraBuilder.setMultithreading(-1);
        }
        if (useCBR) {
            cameraBuilder.enableCbr();
        }
        
        Camera camera = cameraBuilder.build();
        
        // 5. Execution and Time Measurement Benchmark
        long startTime = System.currentTimeMillis();
        camera.renderImage();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        camera.writeToImage("Minip2Test");
        
        // 6. Write multi-level performance results to log file
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(String.format("Configuration: %-55s | Render Time: %d ms\n", testName, duration));
        } catch (IOException e) {
            System.err.println("Error writing performance logs to file: " + e.getMessage());
        }
    }
}