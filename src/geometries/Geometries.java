package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Geometries} class represents a collection of {@link Intersectable} objects,
 * such as spheres, planes, triangles, etc. It serves as a composite design pattern,
 * allowing the management and intersection calculations of a group of geometries.
 * <p>
 * This class provides methods to add geometries to the collection and to find
 * intersection points of a given {@link Ray} with all the geometries in the collection.
 * </p>
 */
public class Geometries extends Intersectable {

    /**
     * The list of geometries contained in this collection.
     * <p>
     * This list holds all the {@link Intersectable} objects that are part of this collection.
     * </p>
     */
    // private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * The infinite geometries witch has not had bounding box.
     */
    private final List<Intersectable> infinite = new ArrayList<>();
    /**
     * The bounding box of the geometries.
     */
    private Intersectable accelerationStructure = null;
    /**
     * List of intersectable geometries.
     */
    private List<Intersectable> geometries = new ArrayList<Intersectable>();
    /**
     * Constructs an empty {@code Geometries} object.
     * <p>
     * Initializes the internal list of geometries to an empty list, allowing geometries
     * to be added later using the {@link #add(Intersectable...)} method.
     * </p>
     */
    public Geometries() {
    }

    /**
     * Constructs a {@code Geometries} object and adds the given {@link Intersectable} geometries to it.
     * <p>
     * This constructor allows initializing the collection with one or more geometries.
     * </p>
     *
     * @param geometries one or more {@link Intersectable} objects to add to the collection.
     *                   If no geometries are provided, the collection will remain empty.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more {@link Intersectable} geometries to the current collection.
     * <p>
     * This method allows adding multiple geometries at once using varargs. Each geometry
     * passed as an argument will be added to the internal list of geometries managed by this object.
     * </p>
     *
     * @param geometries one or more {@link Intersectable} objects to be added.
     *                   Can pass zero or more geometries.
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds the intersections of a ray with all geometries in the collection.
     * <p>
     * This method iterates through all the geometries in the collection and calculates
     * the intersection points of the given {@link Ray} with each geometry. If there are
     * no intersections, the method returns {@code null}.
     * </p>
     *
     * @param ray the {@link Ray} to find intersections with.
     * @return a list of intersection points, or {@code null} if no intersections are found.
     */
//    @Override
//    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
//        List<Intersection> intersections = null;
//        for (Intersectable geometry : geometries) {
//            List<Intersection> geoIntersections = geometry.calculateIntersectionsHelper(ray);
//            if (geoIntersections != null) {
//                if (intersections == null) {
//                    intersections = new LinkedList<>();
//                }
//                intersections.addAll(geoIntersections);
//            }
//        }
//        return intersections;
//    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> list = null;
        if (accelerationStructure != null) {
            for (Intersectable item : infinite) {
                List<Intersection> tmp = item.calculateIntersections(ray);
                if (tmp != null) {
                    if (list == null)
                        list = new ArrayList<>(tmp);
                    else
                        list.addAll(tmp);
                }
            }
            List<Intersection> listBVH = accelerationStructure.calculateIntersections(ray);

            if (listBVH == null)
                return list;

            if (list == null)
                return listBVH;

            list.addAll(listBVH);
            return list;
        }

        // if (accelerationStructure != null) return accelerationStructure.calculateIntersections(ray, maxDistance);

        //List<Intersection> list = null;
        for (Intersectable item : geometries) {
            List<Intersection> found = item.calculateIntersections(ray);
            if (found != null) {
                if (list == null)
                    list = new LinkedList<>(found);
                else
                    list.addAll(found);
            }
        }
        return list;
    }

    @Override
    public void setBoundingBox() {
        if (geometries.isEmpty()) {
            this.box = null;
            return;
        }

        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : geometries) {
            geo.setBoundingBox();
            AABB b = geo.getBoundingBox();
            if (b == null) continue;

            Point min = b.getMin(), max = b.getMax();
            if (min.getX() < minX) minX = min.getX();
            if (min.getY() < minY) minY = min.getY();
            if (min.getZ() < minZ) minZ = min.getZ();

            if (max.getX() > maxX) maxX = max.getX();
            if (max.getY() > maxY) maxY = max.getY();
            if (max.getZ() > maxZ) maxZ = max.getZ();
        }

        this.box = new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Builds a BVH acceleration structure over the current geometries.
     */
    public void buildBVH() {
        setBoundingBox();
        for (Intersectable g : geometries) {
            if (g.getBoundingBox() == null)
                infinite.add(g);
        }
        this.accelerationStructure = BVHNode.buildFrom(geometries);
        this.box = AABB.createInfiniteBoundingBox();
        geometries.clear(); // optional, to free memory or mark as transferred
    }

}