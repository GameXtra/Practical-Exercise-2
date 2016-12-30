/**
 * FibonacciHeap
 * An implementation of fibonacci heap over non-negative integers.
 */
public class FibonacciHeap {
	static private int TotalLinks = 0;
	static private int TotalCuts = 0;
	public int NumberOfTrees = 0;
	public int NumberOfMarkedNodes = 0;
	private HeapNode HeapNode_Min;
	private int size;
	
   /**
    * public boolean empty()
    * @complexity O(1).
    * @return true if and only if the heap is empty.
    * @pre none.
    */
    public boolean empty(){
    	return NumberOfTrees==0;
    }
		
   /**
    * public HeapNode insert(int key)
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    * @complexity O(1) - add new node as a tree regardless of heap size.
    * @pre key>=0.
    * @post the heap now contain a new node with this key.
    * @return the new heap node created.
    */
    public HeapNode insert(int key){
    	HeapNode heapNode = new HeapNode(key);
    	size ++;
    	NumberOfTrees ++;
    	if(empty()){
    		HeapNode_Min=heapNode;
    	}else if(NumberOfTrees==1){
    		heapNode.nextNode = HeapNode_Min;
    		heapNode.prevNode = HeapNode_Min;
    		HeapNode_Min.nextNode = heapNode;
    		HeapNode_Min.prevNode = heapNode;
    		
    	}else{
    		heapNode.nextNode = HeapNode_Min;
    		heapNode.prevNode = HeapNode_Min.prevNode;
    		HeapNode_Min.prevNode.nextNode = heapNode;
    		HeapNode_Min.prevNode = heapNode;
    	}
    	
		if(HeapNode_Min.key > heapNode.key){
			HeapNode_Min = heapNode;
		}
    	return heapNode;    		
    }

   /**
    * public void deleteMin()
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	if(empty()){
    		return; //nothing to do.
    	}
    	if(HeapNode_Min.isMarked){
    		NumberOfMarkedNodes -= 1;
    	}
		HeapNode firstNode ,tempNode;
    	if(NumberOfTrees == 1){
    		if(HeapNode_Min.rank == 0){ // no children, nothing to do.
    			NumberOfTrees = 0;
    			HeapNode_Min = null;
    			return;
    		}else if(HeapNode_Min.rank == 1){ // replace with child.
    			HeapNode_Min = HeapNode_Min.child;
    			HeapNode_Min.parentNode = null;
    			return;
    		}else{ //add children instead.
    			NumberOfTrees = HeapNode_Min.rank;
    			HeapNode_Min = HeapNode_Min.child;
    			firstNode = HeapNode_Min;
    			tempNode = HeapNode_Min;
    			do{
    				tempNode.parentNode = null;
    				if(tempNode.key<HeapNode_Min.key){
    					HeapNode_Min = tempNode;
    				}
    				tempNode = tempNode.nextNode;
    			}while(tempNode!=firstNode);
    			consolidate();
    			return;
    		}
    	}else{
    		if(HeapNode_Min.rank == 0){ // no children, just delete it.
    			if(NumberOfTrees == 2){
    				HeapNode_Min = HeapNode_Min.nextNode;
    				HeapNode_Min.nextNode = null;
    				HeapNode_Min.prevNode = null;
    				NumberOfTrees = 1;
    				return;
    			}else{
    				HeapNode_Min.nextNode.prevNode = HeapNode_Min.prevNode;
    				HeapNode_Min.prevNode.nextNode = HeapNode_Min.nextNode;
    				HeapNode_Min = HeapNode_Min.nextNode;
    				NumberOfTrees -= 1;
    			}
    		}else if(HeapNode_Min.rank == 1){ // add child instead.
    			HeapNode_Min.nextNode.prevNode = HeapNode_Min.child;
    			HeapNode_Min.prevNode.nextNode = HeapNode_Min.child;
    			HeapNode_Min.child.nextNode = HeapNode_Min.nextNode;
    			HeapNode_Min.child.prevNode = HeapNode_Min.prevNode;
    			HeapNode_Min = HeapNode_Min.child;
    			HeapNode_Min.parentNode = null;
    		}else{ //add children instead.
    			firstNode = HeapNode_Min.child;
    			tempNode = HeapNode_Min.child;
    			NumberOfTrees += HeapNode_Min.rank - 1;
    			do{ // delete parent pointers.
    				tempNode.parentNode = null;
    				tempNode = tempNode.nextNode;
    			}while(tempNode!=firstNode);
    			HeapNode_Min.prevNode.nextNode = HeapNode_Min.child;
    			HeapNode_Min.nextNode.prevNode = HeapNode_Min.child.prevNode;
    			HeapNode_Min.child.prevNode = HeapNode_Min.prevNode;
    			HeapNode_Min.child.prevNode.nextNode = HeapNode_Min.nextNode;
    			HeapNode_Min = HeapNode_Min.child;
    		}
    		consolidate();
    		firstNode = HeapNode_Min;
    		tempNode = HeapNode_Min;
    		do{ // find new min;
    			if(tempNode.key<HeapNode_Min.key){
    				HeapNode_Min = tempNode;
    			}
    			tempNode = tempNode.nextNode;
    		}while(tempNode!=HeapNode_Min);
    	}
     	if(NumberOfTrees==0){
     		HeapNode_Min=null;
     		return; //nothing to do now.
     	}
    }
   /**
    * public HeapNode findMin()
    * @return the node of the heap whose key is minimal. 
    * @complexity O(1)
    * @pre - min exists 
    */
    public HeapNode findMin()
    {
    	return HeapNode_Min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    * Meld the heap with heap2 - make the two tree double link trees list connected.
    * @complexity O(1) - connect the ends of two double link lists.
    * @pre none.
    * @post the two heaps are now combined.
    */
    public void meld(FibonacciHeap heap2)
    {
    	if(heap2 == null || heap2.empty()){ // if heap2 empty, nothing to do.
    		return;
    	}
    	
    	if(empty()){ //if this heap is empty, replace this heap with heap 2.
        	NumberOfTrees = heap2.NumberOfTrees;
        	NumberOfMarkedNodes = heap2.NumberOfMarkedNodes;
			HeapNode_Min=heap2.findMin();
			size = heap2.size();
        	return;
    	}
    	
    	//need to merge:
    	HeapNode Heap2Node_Min= heap2.findMin();
    	if(NumberOfTrees==1){
        	if(heap2.NumberOfTrees==1){
	    		HeapNode_Min.nextNode = Heap2Node_Min;
	    		HeapNode_Min.prevNode = Heap2Node_Min;
	    		Heap2Node_Min.nextNode = HeapNode_Min;
	    		Heap2Node_Min.prevNode = HeapNode_Min;
        	}else{
        		HeapNode_Min.nextNode = Heap2Node_Min;
        		HeapNode_Min.prevNode = Heap2Node_Min.prevNode;
        		Heap2Node_Min.prevNode.nextNode = HeapNode_Min;        		
        		Heap2Node_Min.prevNode = HeapNode_Min;
        	}
    	}else{
        	if(heap2.NumberOfTrees==1){
        		Heap2Node_Min.nextNode = HeapNode_Min;
        		Heap2Node_Min.prevNode = HeapNode_Min.prevNode;
        		HeapNode_Min.prevNode.nextNode = Heap2Node_Min;        		
        		HeapNode_Min.prevNode = Heap2Node_Min;
        	}else{
		    	HeapNode HeapNode_Min_next = HeapNode_Min.nextNode;
		    	HeapNode Heap2Node_Min_prev = Heap2Node_Min.prevNode;
		    	
		    	HeapNode_Min.nextNode = Heap2Node_Min;
		    	Heap2Node_Min.prevNode = HeapNode_Min;
		    	
		    	HeapNode_Min_next.prevNode = Heap2Node_Min_prev;
		    	Heap2Node_Min_prev.nextNode = HeapNode_Min_next;
        	}
    	}
    	
    	if(Heap2Node_Min.key < HeapNode_Min.key){ // check if there's a new min in the heap.
    		HeapNode_Min = Heap2Node_Min;
    	}
    	
		size += heap2.size();
    	NumberOfTrees += heap2.NumberOfTrees;
    	NumberOfMarkedNodes += heap2.NumberOfMarkedNodes;
    }

   /**
    * public int size()
    * Return the number of elements in the heap
    * @complexity O(1) - return a value which update regularly.
    * @pre none.
    * @return number of nodes in tree.
    */
    public int size()
    {
    	return size;
    }
    	
    /**
    * public int[] countersRep()
    * @return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * @dependencies getMaxRank.
    * @complexity O(n) - worst case run over all 0-rank tree nodes in the heap.
    * @pre none.
    */
    public int[] countersRep()
    {
    	if(empty()){
    		return new int[0];
		}
    	int[] arr = new int[getMaxRank() + 1];
    	
    	arr[HeapNode_Min.rank]=1; 
    	if(NumberOfTrees==1){ // arr would contains zeros except for the rank of the tree of the min node
    		return arr;
    	}
    	
    	HeapNode TempNode = HeapNode_Min.nextNode;
    	while(TempNode!= HeapNode_Min){ // looping over all of the trees until we return to the min node.
        	arr[TempNode.rank] += 1; // updating ranks
        	TempNode = TempNode.nextNode;
    	}
    	return arr;
    }
    
    /**
    * private int getMaxRank()
    * @return the maximum rank in heap. 
    * @pre heap is not empty.
    * @complexity O(n) - worst case run over all 0-rank tree nodes in the heap.
    */
    private int getMaxRank(){
    	if(NumberOfTrees==1){ 
    		return HeapNode_Min.rank;
		}
    	int max=0;
    	HeapNode temp_heap = HeapNode_Min.nextNode;
    	while(temp_heap!= HeapNode_Min){
        	if(max<temp_heap.rank){
        		max=temp_heap.rank;
        	}
    	}
    	return max;
    }
    
   /**
    * public void arrayToHeap(int[] array)
    * Insert the array to the heap. Delete previous elements in the heap.
    * @complexity O(n) - see details in arrayToBinomialHeap.
    * @dependencies nodesArrayToHeap - O(n), arrayToBinomialHeap - O(n).
    * @pre none.
    * @post heap now contain all items in array.
    */
    public void arrayToHeap(int[] array)
    {
    	if(array==null || array.length==0){
    		size = array.length;
    		NumberOfMarkedNodes = 0;
    		NumberOfTrees = 0;
    		HeapNode_Min = null;
    		return;
    	}
    	
		size = array.length;
    	NumberOfMarkedNodes = 0;
    	nodesArrayToHeap(
    			arrayToBinomialHeap(array)
    				);
    }
    /**
     * public void arrayToBinomialHeap(int[] array)
     * Insert the array to the Binomial heap.
     * @dependencies getBitLength - O(log(n)).
     * @complexity O(n) - go over each item of the array with amortized time of insert action to binomial heap of O(1).
     * @pre array != empty or null.
     * @return binomial heap with the items in the array.
     */
    private HeapNode[] arrayToBinomialHeap(int[] array){
        HeapNode[] Heaps_Arr = new HeapNode[getBitLength(array.length)];
        HeapNode newNode;
        int tempRank;
        for(int i:array){
        	newNode = new HeapNode(i);
        	tempRank = 0;
        	while (Heaps_Arr[tempRank] != null){
        		if(newNode.key<=Heaps_Arr[tempRank].key){
        			newNode.addChild(Heaps_Arr[tempRank]);
        		}else{
        			Heaps_Arr[tempRank].addChild(newNode);
        			newNode = Heaps_Arr[tempRank];
        		}
        		Heaps_Arr[tempRank] = null;
        		tempRank++;
        	}
        	Heaps_Arr[tempRank] = newNode;
        }
        return Heaps_Arr;
    }
    /**
     * private void nodesArrayToHeap(HeapNode[] Heaps_Arr)
     * Insert the array to the heap. Delete previous elements in the heap.
     * @complexity O(n) - go over the array and connect each item to a double linked list.
     * @pre Heaps_Arr != empty or null.
     * @post heap contain all the tree that are in the array. integer "size" doesnt change.
     */    
    private void nodesArrayToHeap(HeapNode[] Heaps_Arr){    	
        int i = 0;
        NumberOfTrees = 0;
        while(Heaps_Arr[i] == null){ 			// find first node.
        	i++;
        	if(i == Heaps_Arr.length){return;}
        }
        HeapNode_Min = Heaps_Arr[i];
        NumberOfTrees++;
        i++;
        
        while(Heaps_Arr[i] == null){ 			// find second node.
        	i++;
        	if(i == Heaps_Arr.length){return;}
        }
        HeapNode_Min.nextNode = Heaps_Arr[i];
        HeapNode_Min.prevNode = Heaps_Arr[i];
        Heaps_Arr[i].nextNode = HeapNode_Min;
        Heaps_Arr[i].prevNode = HeapNode_Min;
        if(Heaps_Arr[i].key<HeapNode_Min.key){
        	HeapNode_Min=Heaps_Arr[i];
        }
        NumberOfTrees++;
        i++;

        while(i<Heaps_Arr.length){ 				// find the rest.
        	if(Heaps_Arr[i] != null){
	        	NumberOfTrees++;
	        	Heaps_Arr[i].nextNode = HeapNode_Min;
	        	Heaps_Arr[i].prevNode = HeapNode_Min.prevNode;
	            HeapNode_Min.prevNode.nextNode = Heaps_Arr[i];
	            HeapNode_Min.prevNode = Heaps_Arr[i];
	            if(Heaps_Arr[i].key<HeapNode_Min.key){
	            	HeapNode_Min=Heaps_Arr[i];
	            }
            }
            i++;
        }    
    }
    
    /**
     * public void getBitLength(int k)
     * @return the number of bit takes to represent k. 
     * @complexity O(log(k)) - how many character it takes to represent the number.
     * @pre none.
     */
    private int getBitLength(int k){
    	int i = 0;
    	while(k!=0){
    		k = k >>> 1;
    		i++;
    	}
    	return i;
    }
   /**
    * public void delete(HeapNode x)
    * Deletes the node x from the heap. 
    * @param - HeapNode x an HeapNode.
    * @complexity -
    * @dependencies - 
    * @pre HeapNode x exists
    * @post HeapNode doesn't exist in the heap anymore and the heap is valid.
    */
    public void delete(HeapNode x) 
    {    
    	if(x==HeapNode_Min){
    		deleteMin();
    		return;} //Just do deleteMin. same action.
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    * @dependencies cut - O(1) , cascadingCut - O(log(n)) ????
    * @complexity O(log(n))
    * @pre HeapNode x exists
    * @post the heap is now updated according to the new key.
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.key -= delta;
    	HeapNode parentNode = x.parentNode;
    	if (parentNode != null && x.key < parentNode.key){ // x isn't root and key became smaller the parent's key 
			cut(x, parentNode);
			cascadingCut(parentNode);
    	}
		if(x.key < HeapNode_Min.key){ // x is now the min node.
			HeapNode_Min = x;
		}
    }
    
    /**
     * public void cut(HeapNode node, HeapNode parentNode)
     * The function cuts the node node from its location.
     * @dependencies removeNodeFromNodesList - O(1), mergeNodesList - O(1)
     * @complexity O(1) 
     * @pre none.
     * @post the node is no longer linked to its parent
     */
    private void cut(HeapNode node, HeapNode parentNode){
    	TotalCuts ++;
    	removeNodeFromNodesList(node);
    	parentNode.rank --;
    	mergeNodesList(node, HeapNode_Min);
    }
    
    /**
     * public void cascadingCut(HeapNode node)
     * The function performs a cascading cut.
     * @dependencies cut - O(1) , cascadingCut - O(log(n)) ????
     * @complexity ??/ 
     * @pre HeapNode node exists
     * @post cascading cut was performed, if needed.
     */
    
    private void cascadingCut(HeapNode node){
    	HeapNode parentNode = node.parentNode;
    	if(parentNode!= null){ // node isn't root
    		if(node.isMarked){
    			cut(node, parentNode);
    			cascadingCut(parentNode);
    		}else{
    			node.isMarked = true;
    		}
    	}
    }
    
    /**
     * public void mergeNodesList(HeapNode node, HeapNode node2)
     * The function links the node node to another node, node2.
     * @dependencies - none
     * @complexity - O(1)
     * @pre - HeapNode node and node2 exists.
     * @post - nodes are now connected.
     */
    
    public void mergeNodesList(HeapNode node, HeapNode node2){
    	// fixing pointers of the new siblings
    	HeapNode tempNode = node.nextNode;
    	node.nextNode = node2.nextNode;
    	node.nextNode.prevNode = node;
    	node2.nextNode = tempNode;
    	node2.nextNode.prevNode = node2;
    }
    
    /**
     * public void removeNodeFromNodesList(HeapNode node)
     * The function removes the node from siblings linked list.
     * @dependencies 
     * @complexity - O(1)
     * @pre HeapNode node exists
     * @post HeapNode node is no longer linked to its siblings.
     */
    
    public void removeNodeFromNodesList(HeapNode node){
    	// fixing pointers of siblings
    	HeapNode nodePrev = node.prevNode;
    	HeapNode nodeNext = node.nextNode;
    	nodePrev.nextNode = nodeNext;
    	nodeNext.prevNode = nodePrev;
		// link the node to itself
    	node.nextNode = node;
    	node.prevNode = node;
    }
    
    /**
     * public void consolidate()
     * Combine every tree of the same size.
     * @dependencies nodesArrayToHeap - O(n), getMaxRank - O(n).
     * @complexity O(n) - worst case go over all 0-rank trees in the heap.
     * @pre none.
     * @post the heap now doesn't contain 2 tree of the same rank.
     */    
    public void consolidate()
    {    
    	if(NumberOfTrees < 2){
    		return;
		} //nothing to do.
    	HeapNode[] Heaps_Arr = 
    			new HeapNode[getMaxRank() + 
    			             getBitLength(NumberOfTrees)];
    						// if every tree is of rank max rank, and we combine them all, this is the max rank.
        HeapNode currentNode = HeapNode_Min, nextNode = HeapNode_Min.nextNode;
        int tempRank;
        do{
        	tempRank = currentNode.rank;
        	while (Heaps_Arr[tempRank] != null){
        		if(currentNode.key<=Heaps_Arr[tempRank].key){
        			currentNode.addChild(Heaps_Arr[tempRank]);
        		}else{
        			Heaps_Arr[tempRank].addChild(currentNode);
        			currentNode = Heaps_Arr[tempRank];
        		}
        		Heaps_Arr[tempRank] = null;
        		tempRank++;
        	}
        	Heaps_Arr[tempRank] = currentNode;
        	currentNode = nextNode;
        	nextNode = nextNode.nextNode;
        }while(currentNode!=HeapNode_Min);
    	nodesArrayToHeap(Heaps_Arr);
    }
    
    
   /**
    * public int potential() 
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    * @returns the current potential of the heap, which is: * Potential = #trees + 2*#marked
    * @complexity O(1) - use value which are regularly being updated.
    * @pre none.
    */
    public int potential() 
    {    
    	return NumberOfTrees + 2 * NumberOfMarkedNodes;
    }

   /**
    * public static int totalLinks() 
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    * @returns the total number of link operations made during the run-time of the program.
    * @complexity O(1) - use value which are regularly being updated.
    * @pre none.
    */
    public static int totalLinks()
    {    
    	return TotalLinks;
    }

   /**
    * public static int totalCuts() 
    * A cut operation is the operation which disconnects a subtree from its parent (during decreaseKey/delete methods). 
    * @returns the total number of cut operations made during the run-time of the program.
    * @complexity O(1) - use value which are regularly being updated.
    * @pre none.
    */
    public static int totalCuts()
    {    
    	return TotalCuts;
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	public int key;
    	public boolean isMarked = false;
    	public HeapNode nextNode = null;
    	public HeapNode prevNode = null;
    	public HeapNode parentNode = null;
    	public HeapNode child = null;
    	private int rank = 0; // Number Of children.
    	
	   /**
	    * public HeapNode() 
	    * Constructor for a HeapNode object.
	    * @param k - key of the node.
	    * @complexity O(1) 
	    * @pre none.
	    */
    	public HeapNode(int k){
    		key=k;
    	}
    	
 	   /**
 	    * public void addChild(HeapNode newChild)
 	    * Adds a new child for an HeapNode object.
 	    * @param HeapNode newChild - the new child.
 	    * @complexity O(1) 
 	    * @pre none.
 	    * @post the new child is added now to the father node.
 	    */
    	public void addChild(HeapNode newChild){
    		TotalLinks++;
    		rank++;
    		newChild.parentNode = this;
    		if(child == null){
    			newChild.nextNode = null;
    			newChild.prevNode = null;
    		}else if(child.nextNode==null){
    			newChild.prevNode = child;
    			newChild.nextNode = child;
    			child.prevNode = newChild;
    			child.nextNode = newChild;
    		}else{
    			newChild.prevNode = child.prevNode;
    			newChild.nextNode = child;
    			child.prevNode.nextNode = newChild;
    			child.prevNode = newChild;
    		}
    		child = newChild;
    	}
	    
  	   /**
  	    * public void cutOff()
  	    * TODO - delete?
  	    * Adds a new child for an HeapNode object.
  	    * @param HeapNode newChild - the new child.
  	    * @complexity O(1) 
  	    * @pre none.
  	    * @post the new child is added now to the father node.
  	    */
    	public void cutOff(){
    		if(parentNode != null){
    			parentNode.rank--;
    			if(parentNode.child == this){
					parentNode.child = nextNode;
    			}
    		}
    		if(nextNode != null){
    			if(nextNode == prevNode){
    				nextNode.nextNode = null;
    				nextNode.prevNode = null;
    			}else{
    				nextNode.prevNode = prevNode;
    				prevNode.nextNode = nextNode;
    			}
    			nextNode = null;
    			prevNode = null;
    		}
    	}
    }
}
