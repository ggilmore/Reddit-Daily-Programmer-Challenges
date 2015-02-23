package getMyPrioritiesRight;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import static org.junit.Assert.*;

public class SingleHeap {
    /**
     * This is a heap implementation for elements with one attribute
     */

    private final BiMap<Integer, Element> elementIndexMapping; // need to ensure
                                                               // that the
                                                               // element at
                                                               // index 0 is the
                                                               // greatest
                                                               // element in the
                                                               // heap

    public SingleHeap() {
        this.elementIndexMapping = HashBiMap.create();
        checkRep();
    }

    private void checkRep() {
        if (this.size() > 1) {
            Element greatestElement = this.elementIndexMapping.get(0);
            double greatestElementValue = greatestElement.getValue();
            for (int i = 2; i < this.size(); i++) {
                Element elementToCompare = this.elementIndexMapping.get(i);
                assert greatestElementValue >= elementToCompare.getValue();
            }
        }
    }

    /**
     * Adds all the elements in "elementsToAdd" to the heap, in sequential order
     * as given in the list
     * 
     * @param elementsToAdd
     *            list of elements to Add
     */
    public void enqueue(List<Element> elementsToAdd) {
        for (int i = 0; i < elementsToAdd.size(); i++) {
            Element elementToAdd = elementsToAdd.get(i);
            this.enqueue(elementToAdd);
        }
    }

    /**
     * Adds element "elementToAdd" to the heap
     * 
     * @param elementToAdd
     *            the element instance to add to the heap
     * 
     */
    public void enqueue(Element elementToAdd) {

        int currentSize = elementIndexMapping.size();
        elementIndexMapping.put(currentSize, elementToAdd);
        Double indexOfParentAsDouble = Math.floor(currentSize / 2.0);
        int indexOfParentAsInt = indexOfParentAsDouble.intValue();
        heapify(indexOfParentAsInt);
        checkRep();
        System.out.println(this.elementIndexMapping);
    }

    public Element dequeue() {
        assert this.size() > 0;
        Element greatestElement = this.elementIndexMapping.get(0);
        this.elementIndexMapping.remove(0);
        this.handleRemovedElement(0);
        System.out.println(this.elementIndexMapping);
        return greatestElement;
    }

    private void handleRemovedElement(int indexOfRemovedElement) {
        if (!this.elementIndexMapping.isEmpty()) {
            Element elementToSwitchPlace = this.elementIndexMapping.get(this
                    .size());
            if (this.size() == 1) {
                this.elementIndexMapping.inverse().forcePut(
                        elementToSwitchPlace, 0);
            } else {
                this.elementIndexMapping.put(indexOfRemovedElement,
                        elementToSwitchPlace);
            }
        }
    }

    public void delete(Element elementToRemove) {
        boolean heapContainsThisElement = this.elementIndexMapping.inverse()
                .containsKey(elementToRemove);

        if (heapContainsThisElement) {
            int indexOfRemovedElement = this.elementIndexMapping.inverse()
                    .remove(elementToRemove);
            this.handleRemovedElement(indexOfRemovedElement);
        }
    }

    public void delete(int indexToRemoveElementFrom) {
        boolean heapContainsThisElement = this.elementIndexMapping
                .containsKey(indexToRemoveElementFrom);

        if (heapContainsThisElement) {
            this.elementIndexMapping.remove(indexToRemoveElementFrom);
            this.handleRemovedElement(indexToRemoveElementFrom);
        }
    }

    /**
     * removes all elements from this heap
     */
    public void clear() {
        this.elementIndexMapping.clear();
    }

    private boolean validBounds(int indexToCheck) {
        return ((indexToCheck < this.size()) && (indexToCheck >= 0));
    }

    /**
     * fixes a local flaw in this heap. Replaces the parent node with the
     * largest of its two children iff any of its two children are larger than
     * it.
     * 
     * @param indexToStart
     *            the index to start the heapify process on
     */
    private void heapify(int indexToStart) {
        assert validBounds(indexToStart); // fail fast
        int rightChildIndex = indexToStart * 2 + 1;
        int leftChildIndex = indexToStart * 2;
        boolean rightChildExists = validBounds(rightChildIndex);
        boolean leftChildExists = validBounds(leftChildIndex);

        if (rightChildExists && leftChildExists) {
            heapifyHelperRightAndLeft(indexToStart, rightChildIndex,
                    leftChildIndex);
        } else if (leftChildExists) {
            heapifyHelperOnlyLeftChild(indexToStart, leftChildIndex);
        }
        // otherwise this is a leaf, do nothing
    }

    private void heapifyHelperRightAndLeft(int indexOfParent,
            int indexOfRightChild, int indexOfLeftChild) {
        
       

        Element parent = this.elementIndexMapping.get(indexOfParent);
        Element rightChild = this.elementIndexMapping.get(indexOfRightChild);
        Element leftChild = this.elementIndexMapping.get(indexOfLeftChild);

        double parentValue = parent.getValue();
        double rightChildValue = rightChild.getValue();
        double leftChildValue = leftChild.getValue();

        if ((leftChildValue >= rightChildValue)
                && (leftChildValue > parentValue)) {
            // swapping parent with left child
            this.elementIndexMapping.forcePut(indexOfLeftChild, parent);
            this.elementIndexMapping.forcePut(indexOfParent, leftChild);
            heapify(indexOfLeftChild); // now we call heapify on the new left
                                       // child to check for further errors
        }

        else if ((rightChildValue > leftChildValue)
                && (rightChildValue > parentValue)) {
            // swapping parent with right child
            this.elementIndexMapping.forcePut(indexOfRightChild, parent);
            this.elementIndexMapping.forcePut(indexOfParent, rightChild);
            heapify(indexOfRightChild); // now we call heapify on on the new
                                        // right child to check for further
                                        // errors
        }

        // otherwise do nothing

    }

    private void heapifyHelperOnlyLeftChild(int indexOfParent,
            int indexOfLeftChild) {
        System.out.println("leftchild called");

        Element parent = this.elementIndexMapping.get(indexOfParent);
        Element leftChild = this.elementIndexMapping.get(indexOfLeftChild);

        double parentValue = parent.getValue();
        double leftChildValue = leftChild.getValue();
        if (leftChildValue > parentValue) {
            this.elementIndexMapping.forcePut(indexOfLeftChild, parent);
            this.elementIndexMapping.forcePut(indexOfParent, leftChild);
            heapify(indexOfLeftChild);
        }
    }

    /**
     * 
     * @return the # of elements that are in this heap
     */
    public int size() {
        return this.elementIndexMapping.size();
    }

    public static void main(String[] args) {
        SingleHeap heap = new SingleHeap();

        Element a = new Element("a", 9);

        Element b = new Element("b", 8);

        Element c = new Element("c", 7);

        Element d = new Element("d", 6);

        Element e = new Element("c", 5);

        Element f = new Element("f", 4);

        Element g = new Element("g", 3);

        List<Element> listOfElements = Arrays.asList(g, f, e, d, c, b, a);

        heap.enqueue(listOfElements);

        Element returnedElement = heap.dequeue();
        System.out.println(returnedElement.getName());
    }
}
