package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;

/**
 * Node in a Bounding Volume Hierarchy (BVH) tree for accelerating
 * ray–geometry intersection tests.
 * <p>
 * Each node is either a leaf (holding a small list of geometries)
 * or an internal node with two children and a bounding AABB.
 * <p>
 * Built recursively using SAH (Surface Area Heuristic) to choose
 * the optimal axis and split point for minimal traversal cost.
 * <p>
 * Immutable after construction — safe for multithreaded rendering.
 *
 * @author Eli and David
 */
public class BVHNode extends Intersectable {

    /**
     * The bounding box of this BVHNode, which is used for quick rejection of rays that do not intersect the node.
     */
    private AABB box;

    /**
     * The left child of this BVHNode. If this node is a leaf, this will be null.
     * If this node is an internal node, it will contain another BVHNode.
     */
    private BVHNode leftChild;

    /**
     * The right child of this BVHNode. If this node is a leaf, this will be null.
     * If this node is an internal node, it will contain another BVHNode.
     */
    private BVHNode rightChild;

    /**
     * A list of intersectable geometries contained in this BVHNode if it is a leaf node.
     * This is used when the number of geometries is less than or equal to MAX_LEAF_SIZE.
     */
    private List<Intersectable> leafGeometries;

    /**
     * A flag indicating whether this BVHNode is a leaf node or an internal node.
     * If true, this node contains geometries; if false, it has child nodes.
     */
    private final boolean isLeaf;

    /**
     * The maximum number of geometries that can be contained in a leaf node.
     * If the number of geometries exceeds this limit, the node will be split into child nodes.
     */
    private static final int MAX_LEAF_SIZE = 6;

    /**
     * Precomputed comparators for sorting by each axis.
     */
    private static final List<Comparator<Intersectable>> AXIS_COMPARATORS = Arrays.asList(
            Comparator.<Intersectable>comparingDouble(g -> g.getBoundingBox().getCenter().getX()),
            Comparator.<Intersectable>comparingDouble(g -> g.getBoundingBox().getCenter().getY()),
            Comparator.<Intersectable>comparingDouble(g -> g.getBoundingBox().getCenter().getZ())
    );

//    private static final List<Comparator<Intersectable>> AXIS_COMPARATORS = Arrays.asList(
//            Comparator.<Intersectable>comparingDouble(g -> {
//                AABB box = g.getBoundingBox();
//                return box != null ? box.getCenter().getX() : Double.NEGATIVE_INFINITY;
//            }),
//            Comparator.<Intersectable>comparingDouble(g -> {
//                AABB box = g.getBoundingBox();
//                return box != null ? box.getCenter().getY() : Double.NEGATIVE_INFINITY;
//            }),
//            Comparator.<Intersectable>comparingDouble(g -> {
//                AABB box = g.getBoundingBox();
//                return box != null ? box.getCenter().getZ() : Double.NEGATIVE_INFINITY;
//            })
//    );

    /**
     * Constructs a BVHNode as a leaf node containing a list of intersectable geometries.
     * This constructor is used when the number of geometries is less than or equal to MAX_LEAF_SIZE.
     *
     * @param geometries the list of intersectable geometries to be contained in this leaf node
     */
    public BVHNode(List<Intersectable> geometries) {
        this.leafGeometries = geometries;
        this.isLeaf = true;
        setBoundingBox();
    }

    /**
     * Constructs a BVHNode as an internal node with two child nodes.
     * This constructor is used when the node is not a leaf and has two children.
     *
     * @param left  the left child node
     * @param right the right child node
     */
    public BVHNode(BVHNode left, BVHNode right) {
        this.leftChild = left;
        this.rightChild = right;
        this.isLeaf = false;
        this.box = AABB.combine(left.getBoundingBox(), right.getBoundingBox());
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        if (leftChild != null && rightChild != null) {
            List<Intersection> result = null;

            boolean leftHit = leftChild.box != null && leftChild.box.intersect(ray);
            boolean rightHit = rightChild.box != null && rightChild.box.intersect(ray);

            if (leftHit) {
                result = leftChild.calculateIntersectionsHelper(ray);
            }

            if (rightHit) {
                List<Intersection> rightResult = rightChild.calculateIntersectionsHelper(ray);
                if (rightResult != null) {
                    if (result == null) result = new ArrayList<>(rightResult);
                    else result.addAll(rightResult);
                }
            }

            return result;
        }

        // Leaf node – brute-force intersection with all geometries
        List<Intersection> result = null;
        for (Intersectable geo : leafGeometries) {
            // Safe to call calculateIntersectionsHelper since geometries.clear() prevents nested BVH structures
            List<Intersection> temp = geo.calculateIntersectionsHelper(ray);
            if (temp != null) {
                if (result == null)
                    result = new ArrayList<>();
                result.addAll(temp);
            }
        }
        return result;
    }

    /**
     * Builds a BVHNode from a list of intersectable geometries.
     * If the number of geometries is less than or equal to MAX_LEAF_SIZE, it creates a leaf node.
     * Otherwise, it finds the best split using surface area heuristic and recursively builds child nodes.
     *
     * @param geometries the list of intersectable geometries to build the BVH from
     * @return a BVHNode representing the bounding volume hierarchy of the geometries
     */
    public static BVHNode buildFrom(List<Intersectable> geometries) {
        if (geometries.size() <= MAX_LEAF_SIZE)
            return new BVHNode(geometries); // Leaf node

        geometries.removeIf(g -> g.getBoundingBox() == null);

        SplitResult split = findBestSplit(geometries);
        if (split == null || split.index <= 0 || split.index >= geometries.size())
            return new BVHNode(geometries); // Fallback: make it a leaf

        // Sort by the chosen axis
        geometries.sort(AXIS_COMPARATORS.get(split.axis));

        List<Intersectable> leftList = new ArrayList<>(geometries.subList(0, split.index));
        List<Intersectable> rightList = new ArrayList<>(geometries.subList(split.index, geometries.size()));

        BVHNode left = buildFrom(leftList);
        BVHNode right = buildFrom(rightList);

        return new BVHNode(left, right);
    }

    /**
     * Computes the bounding box that contains all geometries in the list.
     * This is used to initialize the bounding box for the BVH.
     *
     * @param list the list of intersectable geometries
     * @return a bounding box that encompasses all geometries in the list
     */
    private static AABB computeBoundingBox(List<Intersectable> list) {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : list) {
            AABB b = geo.getBoundingBox();
            if (b == null) continue;

            Point min = b.getMin();
            Point max = b.getMax();

            minX = Math.min(minX, min.getX());
            minY = Math.min(minY, min.getY());
            minZ = Math.min(minZ, min.getZ());

            maxX = Math.max(maxX, max.getX());
            maxY = Math.max(maxY, max.getY());
            maxZ = Math.max(maxZ, max.getZ());
        }

        return new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Represents the result of a split operation.
     * Contains the axis of the split, the index where the split occurs,
     * and the cost associated with that split.
     */
    private static class SplitResult {

        /**
         * The axis along which the split occurs (0 = x, 1 = y, 2 = z).
         */
        final int axis;

        /**
         * The index in the sorted list of objects where the split occurs.
         */
        final int index;

        /**
         * Constructs a SplitResult with the specified axis, index, and cost.
         *
         * @param axis  the axis along which the split occurs (0 = x, 1 = y, 2 = z)
         * @param index the index where the split occurs in the sorted list of objects
         */
        public SplitResult(int axis, int index) {
            this.axis = axis;
            this.index = index;
        }
    }

    /**
     * Finds the best split for the given list of intersectable objects using surface area heuristic.
     * It evaluates splits along each axis (x, y, z) and chooses the one with the lowest cost.
     * Optimized to sort only once per axis instead of repeatedly sorting the same list.
     *
     * @param objects the list of intersectable objects to be split
     * @return a SplitResult containing the best axis, index, and cost for the split
     */
    private static SplitResult findBestSplit(List<Intersectable> objects) {
        SplitResult best = null;
        double bestCost = Double.POSITIVE_INFINITY;

        AABB globalBox = computeBoundingBox(objects);
        double totalArea = globalBox.surfaceArea();

        // Pre-sort the list for each axis to avoid repeated sorting
        List<List<Intersectable>> sortedLists = new ArrayList<>(3);
        for (int axis = 0; axis < 3; axis++) {
            List<Intersectable> sorted = new ArrayList<>(objects);
            sorted.sort(AXIS_COMPARATORS.get(axis));
            sortedLists.add(sorted);
        }

        for (int axis = 0; axis < 3; axis++) {
            List<Intersectable> sortedObjects = sortedLists.get(axis);
            int n = sortedObjects.size();

            // Precompute left and right bounding boxes
            AABB[] leftBoxes = new AABB[n];
            AABB[] rightBoxes = new AABB[n];

            AABB left = null;
            for (int i = 0; i < n; i++) {
                AABB b = sortedObjects.get(i).getBoundingBox();
                left = (left == null) ? new AABB(b) : left.combine(b);
                leftBoxes[i] = left;
            }

            AABB right = null;
            for (int i = n - 1; i >= 0; i--) {
                AABB b = sortedObjects.get(i).getBoundingBox();
                right = (right == null) ? new AABB(b) : right.combine(b);
                rightBoxes[i] = right;
            }

            // Try all split points
            for (int i = 1; i < n; i++) {
                AABB leftBox = leftBoxes[i - 1];
                AABB rightBox = rightBoxes[i];

                int leftCount = i;
                int rightCount = n - i;

                double leftArea = leftBox.surfaceArea();
                double rightArea = rightBox.surfaceArea();

                double cost = 1.0 + (leftCount * leftArea + rightCount * rightArea) / totalArea;

                if (cost < bestCost) {
                    bestCost = cost;
                    best = new SplitResult(axis, i);
                }
            }
        }

        return best;
    }

    @Override
    public AABB getBoundingBox() {
        return box;
    }

    @Override
    public void setBoundingBox() {
        if (isLeaf) this.box = computeBoundingBox(leafGeometries);
    }
}