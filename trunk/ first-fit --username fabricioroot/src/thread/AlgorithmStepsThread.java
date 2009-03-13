package thread;

import gui.MainScreen;
import bean.Process;
import bean.MemoryCell;
import manager.MemoryGenerator;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import javax.swing.JDialog;
import manager.FirstFitAlgorithm;

/**
 *
 * @author Fabrício Reis
 */
public class AlgorithmStepsThread implements Runnable {

    JButton jButtonAlgorithmSteps;
    Vector<MemoryCell> finalMainMemory;
    MemoryGenerator memoryGenerator = new MemoryGenerator();
    Vector<Process> processesQueue;
    JPanel jPanelAnimation;
    JLabel jLabelNextStep;
    MainScreen mainScreen;
    boolean isJButtonOkClicked = false;
    JDialog jDialogNextStep;
    JButton jButtonOkNextStep;
    JLabel jLabelAtDialogNextStep;
    
    public AlgorithmStepsThread(MainScreen mainScreen, JButton jButtonAlgorithmSteps, Vector<MemoryCell> finalMainMemory, Vector<Process> processesQueue, JPanel jPanelAnimation) {
        this.mainScreen = mainScreen ;
        this.jButtonAlgorithmSteps = jButtonAlgorithmSteps;
        this.finalMainMemory = finalMainMemory;
        this.processesQueue = processesQueue;
        this.jPanelAnimation = jPanelAnimation;
    }
    
    public Vector<MemoryCell> getFinalMainMemory() {
        return finalMainMemory;
    }
    
    public JDialog getJDialogNextStep() {
        return jDialogNextStep;
    }

    public void setJDialogNextStep(JDialog jDialogNextStep) {
        this.jDialogNextStep = jDialogNextStep;
    }
    
    public void run() {
        this.jDialogNextStep = new JDialog();
        this.jDialogNextStep.setModalityType(ModalityType.MODELESS);
        this.jDialogNextStep.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        //this.jDialogNextStep.setAlwaysOnTop(true);
        this.jDialogNextStep.setResizable(false);
        this.jDialogNextStep.setBounds(750, 520, 231, 118);
        this.jDialogNextStep.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.jDialogNextStep.setLayout(null);

        this.jButtonOkNextStep = new JButton("OK");
        this.jButtonOkNextStep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        this.jButtonOkNextStep.setBorderPainted(true);
        this.jButtonOkNextStep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.jButtonOkNextStep.setBounds(80, 35, 60, 30);

        this.jButtonOkNextStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isJButtonOkClicked = true;
            }
        });
        
        this.jLabelAtDialogNextStep = new JLabel("Clique em 'OK' para o próximo passo");
        this.jLabelAtDialogNextStep.setBounds(5, 3, 500, 30);
        
        this.jDialogNextStep.add(this.jLabelAtDialogNextStep);
        this.jDialogNextStep.add(this.jButtonOkNextStep);

        this.jButtonAlgorithmSteps.setEnabled(false);
        
        this.finalMainMemory = this.memoryGenerator.decreaseProcessLifeTime(this.finalMainMemory);
        this.mainScreen.paintMainMemory(this.finalMainMemory);
        
        Process process = new Process();
        process.setSize(this.processesQueue.firstElement().getSize());
        process.setLifeTime(this.processesQueue.firstElement().getLifeTime());
        process.setId(this.processesQueue.firstElement().getId());
        this.processesQueue.remove(0);
        this.mainScreen.paintProcessesQueue(this.processesQueue);
        
        this.jDialogNextStep.setTitle("INSERÇÃO DE P" + String.valueOf(process.getId()) + " ...");
        
        FirstFitAlgorithm algorithm = new FirstFitAlgorithm();
        
        //Semantically this object 'algorithmResult' determines if the algorithm found a solution
        //If the solution is found this object has the position where the process goes in and the leftover (Cell's size - Process's size)
        //See the corresponding method at the 'FirstFitAlgorithm' class for more information.
        Vector<Integer> algorithmResult = algorithm.toExecute_A(this.finalMainMemory, process);
        
        if(algorithmResult != null) {

            Vector<MemoryCell> newMemory = algorithm.toExecute_B(this.finalMainMemory, process);
            int steps = 0; //This variable stores how many steps (blocks) the process (represented like one block) has "to jump"
            int orientationAxisY = 25;

            //It finds the 'steps' and walk until it reachs the position to go in
            for(int i = 0; i <= algorithmResult.get(0); i++){
                steps = steps + this.finalMainMemory.elementAt(i).getSize();
            }

            JTextField block = new JTextField();
            block.setText(String.valueOf(process.getSize()));
            block.setBackground(new java.awt.Color(51, 255, 255));
            block.setForeground(new java.awt.Color(0, 0, 0));
            block.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            block.setEditable(false);
            block.setToolTipText("Tamanho de P" + String.valueOf(process.getId()) + " = " +  String.valueOf(process.getSize()));
            this.jPanelAnimation.add(block);

            int j = 0;
            if(steps <= 15) {
                this.jDialogNextStep.setVisible(true);
                block.setBounds(20, orientationAxisY, 30, 30);
                while (j <= (steps - 1)) {
                    if (this.isJButtonOkClicked) {
                        this.isJButtonOkClicked = false;
                        j++;
                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    }
                }
                this.jDialogNextStep.setVisible(false);
            }
            else {
                if((steps > 15) && (steps <= 30)) {
                    this.jDialogNextStep.setVisible(true);

                    // First row
                    block.setBounds(20, orientationAxisY, 30, 30);    
                    j = 0;
                    while (j <= 14) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                    }
                        
                    // Second row
                    block.setBounds(20, (orientationAxisY + 60), 30, 30);
                    j = 0;
                    while (j <= (steps - 16)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                    }
                    this.jDialogNextStep.setVisible(false);
                }
                else {
                    if((steps > 30) && (steps <= 45)){
                        this.jDialogNextStep.setVisible(true);

                        // First row
                        block.setBounds(20, orientationAxisY, 30, 30);
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }

                        // Second row
                        block.setBounds(20, (orientationAxisY + 60), 30, 30);
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        }

                        // third row
                        block.setBounds(20, orientationAxisY + 120, 30, 30);
                        j = 0;
                        while (j <= (steps - 31)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20 + (j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                    }
                }
            }
            this.finalMainMemory = newMemory;
            this.mainScreen.paintMainMemory(this.finalMainMemory);
            
            if(this.processesQueue.size() > 0) {
                this.jButtonAlgorithmSteps.setEnabled(true);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Não há espaço contínuo grande suficiente na memória para armazenar o processo!\n" +
                            "Por isso, ele será inserido novamente na fila (última posição).", "ATENÇÃO", JOptionPane.WARNING_MESSAGE);
            this.processesQueue.add(process);
            this.mainScreen.paintProcessesQueue(this.processesQueue);
            this.jButtonAlgorithmSteps.setEnabled(true);
        }
    }
}