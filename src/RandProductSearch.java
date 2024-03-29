import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.CREATE;

public class RandProductSearch extends JFrame
{
    private ArrayList<Product> productsArrayList;
    private int count = 0;
    private File inputFile;
    private RandomAccessFile raFile;
    private JOptionPane optionPane;
    private JPanel mainPanel;
    private JPanel textAreaPanel;
    private JPanel controlsPanel;
    private JButton inputFileButton;
    private JButton runButton;
    private JButton quitButton;
    private JTextField nameField;
    private JScrollPane outputFileScroller;
    private JTextArea outputFileField;
    private JScrollPane inputFileScroller;
    private JTextArea inputFileField;

    public static void main(String[] args) {
        RandProductSearch rpm = new RandProductSearch();
    }

    public RandProductSearch() {
        optionPane = new JOptionPane();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(4, 1));
        nameField = new JTextField(20);
        nameField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Choose a search term", TitledBorder.LEFT, TitledBorder.TOP));
        nameField.addActionListener(e ->
        {
            if (nameField.getText().length() > 35) {
                optionPane.showInternalMessageDialog(null, "Input too long. Limit: 35 Characters",
                        "Input Error", JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
            }
        });
        inputFileButton = new JButton("Choose Input File");
        inputFileButton.addActionListener(e ->
        {
            inputFile = ChooseFile();
        });
        runButton = new JButton("Search the file");
        runButton.addActionListener(e ->
        {
            if (inputFile != null) {
                outputFileField.setText("");
                inputFileField.setText("");
                if (productsArrayList != null) {
                    productsArrayList.clear();
                }
                productsArrayList = GenerateProducts();
                String searchTerm = nameField.getText().trim().toLowerCase();
                WriteProductToDisplay(productsArrayList, searchTerm);
            }
            else
            {
                optionPane.showInternalMessageDialog(null, "NO FILE SELECTED",
                        "Input Error", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        controlsPanel.add(nameField);
        controlsPanel.add(inputFileButton);
        controlsPanel.add(runButton);
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e ->
        {
            System.exit(0);
        });
        controlsPanel.add(quitButton);

        textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new GridLayout(1, 2));
        outputFileField = new JTextArea(30, 20);
        outputFileScroller = new JScrollPane(outputFileField);
        outputFileScroller.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Filtered products", TitledBorder.LEFT, TitledBorder.TOP));

        inputFileField = new JTextArea(30, 20);
        inputFileScroller = new JScrollPane(inputFileField);
        inputFileScroller.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "All products from file", TitledBorder.LEFT, TitledBorder.TOP));
        textAreaPanel.add(inputFileScroller);
        textAreaPanel.add(outputFileScroller);
        mainPanel.add(controlsPanel);
        mainPanel.add(textAreaPanel);
        BuildWindow();
    }

    public void BuildWindow() {
        add(mainPanel);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize((screenWidth / 4) * 3, screenHeight);
        setLocation(screenWidth / 8, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private File ChooseFile() {
        File chosenFile = null;
        JFileChooser chooser = new JFileChooser();
        try {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                chosenFile = chooser.getSelectedFile();
                Path file = chosenFile.toPath();
                InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                optionPane.showInternalMessageDialog(null, "File selected: " + chosenFile.toString(),
                        "File info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (FileNotFoundException e) {
            optionPane.showInternalMessageDialog(null, "File not found!",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chosenFile;
    }
    public void WriteProductToDisplay(ArrayList<Product> products, String searchTerm)
    {
        for (Product product : products)
        {
            inputFileField.append("\nProduct Count: " + (count + 1) + "\n");
            inputFileField.append(product.getId() + "\n");
            inputFileField.append(product.getName() + "\n");
            inputFileField.append(product.getDescription() + "\n");
            inputFileField.append(String.valueOf(product.getCost()) + "\n");
            if (product.getName().trim().toLowerCase().contains(searchTerm))
            {
                outputFileField.append("\nProduct Count: " + (count + 1) + "\n");
                outputFileField.append(product.getId() + "\n");
                outputFileField.append(product.getName() + "\n");
                outputFileField.append(product.getDescription() + "\n");
                outputFileField.append(String.valueOf(product.getCost()) + "\n");
            }
            count++;
        }

    }

    public ArrayList<Product> GenerateProducts()
    {
        ArrayList<Product> products = new ArrayList<Product>();
        try
        {
            raFile = new RandomAccessFile(inputFile, "rw");
            long totalLength = raFile.length();
            long numProducts = totalLength / 124;
            for (int i = 0; i < numProducts; i++)
            {
                int startingPoint = i *124;
                raFile.seek(startingPoint);

                byte[] nameBytes = new byte[35];
                raFile.read(nameBytes);
                String name = new String(nameBytes).trim();

                raFile.seek(startingPoint + 35);
                byte[] descriptionBytes = new byte[75];
                raFile.read(descriptionBytes);
                String description = new String(descriptionBytes).trim();

                raFile.seek(startingPoint + 110);
                byte[] idBytes = new byte[6];
                raFile.read(idBytes);
                String id = new String(idBytes).trim();

                raFile.seek(startingPoint + 116);
                byte[] costBytes = new byte[8];
                double cost = raFile.readDouble();
                System.out.println(new String(costBytes));
                System.out.println(cost);
                products.add(new Product(name, description, id, cost));
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return products;
    }
}



