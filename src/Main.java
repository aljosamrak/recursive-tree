import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Aljoša
 * @version 1.0
 */

public class Main {
	
	public static void main(String[] args) {
        TreeFrame frm = new TreeFrame();
        frm.setSize(800,600);
        frm.setVisible(true);
	}
}

/**
 * This code creates GUI
 */
class TreeFrame extends JFrame {

    private JSlider branchesSlider, angleSlider, levelsSlider, strokeSlider;
    private JCheckBox random;
    private TreePanel tree;


    public TreeFrame() {
        this("Tree");
    }
    public TreeFrame(String title) {
        super(title);
        initComponents();


        setTreeParameters();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                redraw();
            }
        });

        tree = new TreePanel();

        branchesSlider = new JSlider();
        strokeSlider = new JSlider();
        levelsSlider = new JSlider();
        angleSlider = new JSlider();
        random = new JCheckBox();

        JPanel jPanel1 = new JPanel();          jPanel1.setLayout(new GridLayout(5, 1));
        JPanel jPanel2 = new JPanel();          jPanel2.setLayout(new BorderLayout());
        JPanel branc = new JPanel();            branc.setLayout(new BorderLayout());
        JPanel str = new JPanel();              str.setLayout(new BorderLayout());
        JPanel niv = new JPanel();              niv.setLayout(new BorderLayout());
        JPanel ang = new JPanel();              ang.setLayout(new BorderLayout());

        JLabel jLabel4 = new JLabel();
        JLabel jLabel5 = new JLabel();
        JLabel jLabel7 = new JLabel();
        JLabel jLabel6 = new JLabel();

        JButton jButton1 = new JButton();
        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu jMenu1 = new JMenu();
        JMenuItem jMenuItem1 = new JMenuItem();
        JMenu jMenu2 = new JMenu();
        JMenuItem jMenuItem2 = new JMenuItem();

        branchesSlider.setPaintLabels(true);
        angleSlider.setPaintLabels(true);
        strokeSlider.setPaintLabels(true);
        levelsSlider.setPaintLabels(true);


        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel4.setText("Number of branches");
        branc.add(jLabel4, BorderLayout.NORTH);
        jPanel1.add(branc);
        branchesSlider.setMaximum(20);
        branchesSlider.setPaintLabels(true);
        branchesSlider.setValue(3);
        branchesSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                redraw();
            }
        });
        branc.add(branchesSlider, BorderLayout.CENTER);


        jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel5.setText("Thickness");
        str.add(jLabel5, BorderLayout.NORTH);
        jPanel1.add(str);
        strokeSlider.setMaximum(50);
        strokeSlider.setPaintLabels(true);
        strokeSlider.setValue(8);
        strokeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                redraw();
            }
        });
        str.add(strokeSlider, BorderLayout.CENTER);


        jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel7.setText("Levels");
        niv.add(jLabel7, BorderLayout.NORTH);
        jPanel1.add(niv);
        levelsSlider.setMaximum(10);
        levelsSlider.setPaintLabels(true);
        levelsSlider.setValue(3);
        levelsSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                redraw();
            }
        });
        niv.add(levelsSlider, BorderLayout.CENTER);


        jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel6.setText("Angle");
        ang.add(jLabel6, BorderLayout.NORTH);
        jPanel1.add(ang);
        angleSlider.setMaximum(180);
        angleSlider.setPaintLabels(true);
        angleSlider.setValue(30);
        angleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                redraw();
            }
        });
        ang.add(angleSlider, BorderLayout.CENTER);



        random.setText("random");
        random.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                redraw();
            }
        });
        jPanel2.add(random, BorderLayout.CENTER);

        jButton1.setFont(new Font("Ubuntu", 1, 18));
        jButton1.setText("REDRAW");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tree.repaint();
            }
        });
        jPanel2.add(jButton1, BorderLayout.EAST);
        jPanel1.add(jPanel2);


        getContentPane().add(jPanel1, BorderLayout.EAST);



        GroupLayout treeLayout = new GroupLayout(tree);
        tree.setLayout(treeLayout);
        treeLayout.setHorizontalGroup(
                treeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 426, Short.MAX_VALUE)
        );
        treeLayout.setVerticalGroup(
                treeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 532, Short.MAX_VALUE)
        );

        getContentPane().add(tree, BorderLayout.CENTER);

        jMenu1.setText("File");

        jMenuItem1.setText("Save");
        jMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                save();
            }
        });
        jMenu1.add(jMenuItem1);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Color");

        jMenuItem2.setText("Select color");
        jMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tree.setColor(JColorChooser.showDialog(TreeFrame.this, "Choose color", Color.BLACK));
            }
        });
        jMenu2.add(jMenuItem2);
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);

        pack();
    }

    private void setTreeParameters() {
        tree.setParams(branchesSlider.getValue(), strokeSlider.getValue(), Math.toRadians(angleSlider.getValue()),
                random.isSelected(), levelsSlider.getValue());
    }

    private void save() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Picture JPG (.jpg, .jpeg)", ".jpg", ".jpeg", ".JPG", ".JPEG"));
        if(fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
            tree.save(fileChooser.getSelectedFile());
        }
    }

    private void redraw() {
        setTreeParameters();
        tree.repaint();
    }
}
