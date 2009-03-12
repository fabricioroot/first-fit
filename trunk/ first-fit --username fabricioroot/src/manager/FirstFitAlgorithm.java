package manager;

import java.util.Vector;
import bean.MemoryCell;
import bean.Process;


/**
 *
 * @author Fabricio Reis
 */
public class FirstFitAlgorithm {

    public FirstFitAlgorithm() {
    }

    /* This method implements the algorithm FIRST-FIT of memory management, which
     * looks for the first big enough free space to put the process.
     * It receives two parameters: memory (Vector<MemoryCell>) and process (Process).
     * Its out is a vector object that contains the position (starts counting from 0) where the process can be put
     * and a number (leftover) that is how much space rested.
     * If the out is null, means none big enough free space has been found in the memory to put the process.
     */
    
     public Vector<Integer> toExecute_A(Vector<MemoryCell> memory, Process process) {
        int leftover = -1; //This variable stores the leftover (Cell's size - Process's size).
        int positionFirstFit = 0;
        Vector<Integer> out = null; //This is the method's return. A vector where its first position stores the position where the process was put
                                    // and its second position stores the leftover (Cell's size - Process's size).
        MemoryCell cell = new MemoryCell();
        boolean found = false;
        
        int i = 0;
        while(!found){
            cell = memory.elementAt(i);
            if((process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                leftover = cell.getSize() - process.getSize();
                positionFirstFit = i;
                found = true;
            }
            i++;
            if((i == memory.size()) && (!found)){  //Brazilian joke = "Gambiarra"
                found = true;
                positionFirstFit = -1;
            }
        }

        if(positionFirstFit != -1) {
            out = new Vector<Integer>();
            out.add(positionFirstFit);
            out.add(leftover);
        }                
        
        return out;
    }
     
    /* This method implements the algorithm FIRST-FIT of memory management, which
     * looks for the first big enough free space to put the process.
     * It receives two parameters: memory (Vector<MemoryCell>) and process (Process).
     * Its out is the modified memory (includes the process in the memory).
     */
     
     public Vector<MemoryCell> toExecute_B(Vector<MemoryCell> memory, Process process) {
        int leftover = -1; //This variable stores the leftover (Cell's size - Process's size).
        int positionFirstFit = 0;
        Vector<MemoryCell> out = memory; //This is the method's return. The new memory!
        MemoryCell cell = new MemoryCell();
        boolean found = false;
        
        int i = 0;
        while(!found){
            cell = memory.elementAt(i);
            if((process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                leftover = cell.getSize() - process.getSize();
                positionFirstFit = i;
                found = true;
            }
            i++;
            if((i == memory.size()) && (!found)){  //Brazilian joke = "Gambiarra"
                found = true;
                positionFirstFit = -1;
            }
        }
        
        if(positionFirstFit != -1) {
            MemoryCell auxCell = new MemoryCell(false, process, process.getSize());
            out.set(positionFirstFit, auxCell);
            if(leftover > 0){
                auxCell = new MemoryCell(true, null, leftover);
                out.add(positionFirstFit + 1, auxCell);
            }
        }
        return out;
    }
}