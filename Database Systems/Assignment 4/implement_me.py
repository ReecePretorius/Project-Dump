# Implementation of B+-tree functionality.

# A4
# implement_me.py

from index import *


def isLeaf(node):
    if(node.pointers.pointers[0] == 0 and node.pointers.pointers[1] == 0):
        return True
    else:
        return False

def isFull(node):
    if(node.keys.keys[0] == -1 or node.keys.keys[1] == -1):
        return False
    else:
        return True

# You should implement all of the static functions declared
# in the ImplementMe class and submit this (and only this!) file.
class ImplementMe:

    # Returns a B+-tree obtained by inserting a key into a pre-existing
    # B+-tree index if the key is not already there. If it already exists,
    # the return value is equivalent to the original, input tree.
    #
    # Complexity: Guaranteed to be asymptotically linear in the height of the tree
    # Because the tree is balanced, it is also asymptotically logarithmic in the
    # number of keys that already exist in the index.
    @staticmethod
    def InsertIntoIndex( index, key ):
        if(index.nodes == []):
            height = 0
            index = Index([Node()]*1)
            root = Node(KeySet((key, -1)), PointerSet((0,0,0)))
            index.nodes[0] = root
        else:
            height = 1
            # initialize cursor to root.
            cursor = index.nodes[0]
            parent = cursor
            direction = None

            # Root only case to check if key is already inserted.
            if(key == cursor.keys.keys[0] or key == cursor.keys.keys[1]):
                return index

            # Traversing to leaf node level.
            while(not isLeaf(cursor)):
                height += 1
                # Go Left.
                if(key < cursor.keys.keys[0]):
                    cursor = index.nodes[cursor.pointers.pointers[0]]
                    direction = 0

                # Go Middle.
                elif(key >= cursor.keys.keys[0] and key < cursor.keys.keys[1]):
                    cursor = index.nodes[cursor.pointers.pointers[1]]
                    direction = 1

                # Go Right.
                elif(key >= cursor.keys.keys[1]):
                    cursor = index.nodes[cursor.pointers.pointers[2]]
                    direction = 2
                else:
                    continue

            # Inserting key.
            if(isFull(cursor)): #If leaf is full.
                # Create larger index.
                newSize = int((1 - 3**(height+1)) / -2)
                newIndex = Index([Node()] * newSize)
                # Copy data to resized index.
                for i in range(len(index.nodes)):
                    newIndex.nodes[i] = index.nodes[i]
                
                if(key < cursor.keys.keys[0]):
                    newParent = Node(KeySet((cursor.keys.keys[0], -1)), PointerSet((1,2,0)))
                    newIndex.nodes[0] = newParent

                    # Create new node with key to be inserted.
                    newNode = Node(KeySet((key, -1)), PointerSet((0,0,2)))
                    newIndex.nodes[newParent.pointers.pointers[0]] = newNode # Left case.

                    oldParent = Node(cursor.keys, cursor.pointers)
                    newIndex.nodes[newParent.pointers.pointers[1]] = oldParent # Middle case.
                    index = newIndex
                
                elif(key > cursor.keys.keys[0] and key < cursor.keys.keys[1]):
                    newParent = Node(KeySet((cursor.keys.keys[1], -1)), PointerSet((1,2,0)))
                    newIndex.nodes[0] = newParent

                    keys = (cursor.keys.keys[0], key)
                    keyset = KeySet(tuple(sorted(keys)))
                    newNode = Node(keyset, PointerSet((0,0,2)))
                    newIndex.nodes[newParent.pointers.pointers[0]] = newNode # Left case.

                    oldParent = Node(newParent.keys, cursor.pointers)
                    newIndex.nodes[newParent.pointers.pointers[1]] = oldParent # Middle case.

                    index = newIndex
                
                else: #key > cursor.keys.keys[1]
                    newParent = Node(KeySet((cursor.keys.keys[0], -1)), PointerSet((1,2,0)))
                    newIndex.nodes[0] = newParent

                    keys = (cursor.keys.keys[0], key)
                    keyset = KeySet(tuple(sorted(keys)))
                    newNode = Node(keyset, PointerSet((0,0,2)))
                    newIndex.nodes[newParent.pointers.pointers[0]] = newNode # Left case.

                    oldParent = Node(newParent.keys, cursor.pointers)
                    newIndex.nodes[newParent.pointers.pointers[1]] = oldParent # Middle case.

                    index = newIndex

            else:
                keys = (cursor.keys.keys[0], key)
                keyset = KeySet(tuple(sorted(keys)))
                newLeaf = Node(keyset, cursor.pointers)

                if(direction == None):
                    # insert into root
                    index.nodes[0] = newLeaf
                else:
                    # inserting into leaf
                    index.nodes[parent.pointers.pointers[direction]] = newLeaf
        
        return index

    # Returns a boolean that indicates whether a given key
    # is found among the leaves of a B+-tree index.
    #
    # Complexity: Guaranteed not to touch more nodes than the
    # height of the tree
    @staticmethod
    def LookupKeyInIndex( index, key ):
        cursor = index.nodes[0]

        while(not isLeaf(cursor)):
            # Go Left.
            if(key < cursor.keys.keys[0]):
                cursor = index.nodes[cursor.pointers.pointers[0]]

            # Go Middle.
            elif(key >= cursor.keys.keys[0] and (key < cursor.keys.keys[1] or cursor.keys.keys[1] == -1)):
                cursor = index.nodes[cursor.pointers.pointers[1]]

            # Go Right.
            elif(key > cursor.keys.keys[1]):
                cursor = index.nodes[cursor.pointers.pointers[2]]
            else:
                return True

        for k in cursor.keys.keys:
            if(k == key):
                return True
        
        return False

    # Returns a list of keys in a B+-tree index within the half-open
    # interval [lower_bound, upper_bound)
    #
    # Complexity: Guaranteed not to touch more nodes than the height
    # of the tree and the number of leaves overlapping the interval.
    @staticmethod
    def RangeSearchInIndex( index, lower_bound, upper_bound ):
        cursor = index.nodes[0]
        result = []

        while(not isLeaf(cursor)):
            # Go Left.
            if(lower_bound < cursor.keys.keys[0]):
                cursor = index.nodes[cursor.pointers.pointers[0]]

            # Go Middle.
            elif(lower_bound >= cursor.keys.keys[0] and (lower_bound < cursor.keys.keys[1] or cursor.keys.keys[1] == -1)):
                cursor = index.nodes[cursor.pointers.pointers[1]]

            # Go Right.
            elif(lower_bound >= cursor.keys.keys[1]):
                cursor = index.nodes[cursor.pointers.pointers[2]]
            else:
                continue

        val = cursor.keys.keys[0]
        while(val != upper_bound):
            for key in cursor.keys.keys:
                if(key == -1):
                    continue
                if(key >= lower_bound and key < upper_bound):
                    result.append(key)
                    val = key
                if(key == upper_bound):
                    return result
            if(cursor.pointers.pointers[2] != 0):
                cursor = index.nodes[cursor.pointers.pointers[2]]
            else:
                break
        
        return result

    def insertRecursive(index, cursor, key):
        if(index.nodes == []):
            height = 0
            index = Index([Node()]*1)
            root = Node(KeySet((key, -1)), PointerSet((0,0,0)))
            index.nodes[0] = root
            return index

        if(index == Index([Node()*1])):
            root = Node(KeySet((key, -1)), PointerSet((0,0,0)))
            index.nodes[0] = root
            return index
        
        if(key < cursor.keys.keys[0]):
            index.nodes[cursor.pointers.pointers[0] + 1] = insertRecursive(index, index.nodes[cursor.pointers.pointers[0] + 1], key)
            print("Left recursive")
                
        elif(key > cursor.keys.keys[0] and key < cursor.keys.keys[1]):
            print("Middle recursive")
                
        else: #key > cursor.keys.keys[1]
            print("Right recursive")

        return index
