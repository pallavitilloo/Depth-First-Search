/**********************************************************************
*
*  The class ArrayStack implements the Stack using an array to
*   hold the items.
*
*  @author Pallavi Tilloo
*************************************************************************/

class ArrayStack<T> implements StackInterface<T>
{
  private T[] stackItems;
  private int topIndex, stackSize;
  
  public ArrayStack(int capacity)
  {
    @SuppressWarnings("unchecked")
    T[] temp = (T[]) new Object[capacity];
    stackItems = temp;    
    
    topIndex = -1;
    stackSize = 0;
  }
  
  // Adds item to stack
  public void push(T item)
  {
    topIndex++;
    if(topIndex == stackItems.length) topIndex = 0;
    stackItems[topIndex] = item;
    stackSize++;
  }
  
  // Removes item from stack
  public T pop()
  {
    T returnValue = stackItems[topIndex];
    topIndex--;
    if (topIndex == stackItems.length) topIndex = 0;
    stackSize--;
    return returnValue;
  }
  
  // Returns item at top
  public T peek()
  {
    return stackItems[topIndex];
  }
  
  // Returns true iff the stack is empty
  public boolean isEmpty()
  {
    return stackSize == 0;
  }
  
  // Removes all items from stack
  public void clear()
  {
    for (int i = 0; i < stackItems.length; i++)
      stackItems[i] = null;    
    topIndex = -1;
    stackSize = 0;
  }
}
  
    