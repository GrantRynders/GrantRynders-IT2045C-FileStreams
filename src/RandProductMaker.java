import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import static java.nio.file.StandardOpenOption.CREATE;

public class RandProductMaker extends JFrame
{
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
    private JTextField descriptionField;
    private JTextField idField;
    private JTextField costField;
    private JScrollPane outputFileScroller;
    private JTextArea outputFileField;
    public static void main(String[] args)
    {
        RandProductMaker rpm = new RandProductMaker();
    }
    public RandProductMaker()
    {
        optionPane = new JOptionPane();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,1));

        controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(6,1));
        nameField = new JTextField(20);
        nameField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Create the product's name", TitledBorder.LEFT, TitledBorder.TOP));
        nameField.addActionListener(e ->
        {
            if (nameField.getText().length() > 35)
            {
                optionPane.showInternalMessageDialog(null, "Input too long. Limit: 35 Characters",
                        "Input Error", JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
            }
        });
        descriptionField = new JTextField(20);
        descriptionField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Create the product's description", TitledBorder.LEFT, TitledBorder.TOP));
        descriptionField.addActionListener(e ->
        {
            if (descriptionField.getText().length() > 75)
            {
                optionPane.showInternalMessageDialog(null, "Input too long. Limit: 75 Characters",
                        "Input Error", JOptionPane.INFORMATION_MESSAGE);
                descriptionField.setText("");
            }
        });
        idField = new JTextField(20);
        idField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Choose the product's 6 digit id", TitledBorder.LEFT, TitledBorder.TOP));
        idField.addActionListener(e ->
        {
            if (idField.getText().length() > 6)
            {
                optionPane.showInternalMessageDialog(null, "Input too long. Limit: 6 Characters",
                        "Input Error", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
            }
        });
        costField = new JTextField(20);
        costField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Choose the product's cost", TitledBorder.LEFT, TitledBorder.TOP));
        inputFileButton = new JButton("Choose Output File");
        inputFileButton.addActionListener(e ->
        {
            inputFile = ChooseFile();
            try
            {
                raFile = new RandomAccessFile(inputFile, "rw");
                optionPane.showInternalMessageDialog(null, "Created RAF: " + raFile.toString(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (IOException f)
            {
                System.out.println("Random Access File Creation Failed");
                f.printStackTrace();
            }
        });
        runButton = new JButton("Finalize Product");
        runButton.addActionListener(e ->
        {
            Product product = CreateProductFromInput();
            if (product != null)
            {
                WriteProductToDisplay(product);
                SaveProductData((count * 124), product);
                count++;
                ClearFields();
            }

        });

        controlsPanel.add(nameField);
        controlsPanel.add(descriptionField);
        controlsPanel.add(idField);
        controlsPanel.add(costField);
        controlsPanel.add(inputFileButton);
        controlsPanel.add(runButton);


        textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new GridLayout(2,1));
        outputFileField = new JTextArea(30, 20);
        outputFileScroller = new JScrollPane(outputFileField);
        outputFileScroller.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Product Data Sample", TitledBorder.LEFT, TitledBorder.TOP));
        textAreaPanel.add(outputFileScroller);
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e ->
        {
            System.exit(0);
        });
        textAreaPanel.add(quitButton);
        mainPanel.add(controlsPanel);
        mainPanel.add(textAreaPanel);
        BuildWindow();
    }
    public void BuildWindow()
    {
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
    private File ChooseFile()
    {

        File chosenFile = null;
        JFileChooser chooser = new JFileChooser();
        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);
            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                chosenFile = chooser.getSelectedFile();
                Path file = chosenFile.toPath();
                InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                optionPane.showInternalMessageDialog(null, "File selected: " + chosenFile.toString(),
                        "File info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (FileNotFoundException e)
        {
            optionPane.showInternalMessageDialog(null, "File not found!",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return chosenFile;
    }
    private Product CreateProductFromInput()
    {
        Product product = new Product();
        product.setName(nameField.getText().trim());
        if (product.getName() != null)
        {
            System.out.println("Valid Product");
            product.setDescription(descriptionField.getText().trim());
            if (product.getDescription() != null)
            {
                System.out.println("Valid Description");
                product.setId(idField.getText().trim());
                if (product.getId() != null)
                {
                    System.out.println("Valid Id");
                    String costString = costField.getText();
                    try
                    {
                        double costDouble = Double.parseDouble(costString);
                        product.setCost(costDouble);
                        System.out.println("Valid Double");
                        return product;
                    }
                    catch (NumberFormatException a)
                    {
                        a.printStackTrace();
                        optionPane.showInternalMessageDialog(null, "Invalid Cost",
                                "Invalid Cost", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else
                {
                    optionPane.showInternalMessageDialog(null, "Invalid Id",
                            "Invalid Id", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else
            {
                optionPane.showInternalMessageDialog(null, "Invalid Description",
                        "Invalid Description", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
        {
            optionPane.showInternalMessageDialog(null, "Invalid name",
                    "Invalid name", JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("FAIL");
        return null;
    }
    public void WriteProductToDisplay(Product product)
    {
        System.out.println("WRITING TO FILE:");
        outputFileField.append("\nProduct Count: " + (count + 1) + "\n");
        outputFileField.append(product.getId() + "\n");
        outputFileField.append(product.getName() + "\n");
        outputFileField.append(product.getDescription() + "\n");
        outputFileField.append(String.valueOf(product.getCost()) + "\n");
    }
    private void SaveProductData(int seek, Product product)
    {
        try {
            raFile.seek(seek);
            String formattedName = String.format("%35s", product.getName()); // pad the name
            raFile.write(formattedName.getBytes()); // write it to the file
            raFile.write(String.format("%75s", product.getDescription()).getBytes(StandardCharsets.UTF_8));
            raFile.write(String.format("%6s", product.getId()).getBytes(StandardCharsets.UTF_8));
            raFile.writeDouble(product.getCost());
        }
        catch (IOException e)
        {
            optionPane.showInternalMessageDialog(null, "Failed to Save Product Data to File",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
    }
    public void ClearFields()
    {
        nameField.setText("");
        descriptionField.setText("");
        idField.setText("");
        costField.setText("");
    }
}
