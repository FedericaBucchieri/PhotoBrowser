import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


public class PhotoBrowser extends JFrame {
    // JLabel representing the status bar
    public JLabel statusLabel;
    // the list of names to be assigned to the ToggleButtons in the ToolBar
    public String[] toggleButtons = {"People", "Places", "School"};
    // Panel that contains the Photo Component
    private JPanel photoContainerPanel;
    // The current Photo being displayed
    private PhotoComponent currentPhoto;
    // The JMenu components added in order to display extra features for modifying annotation on PhotoComponents
    private JMenu colorMenu;
    private JMenu strokeMenu;
    private JMenu fontMenu;

    /**
     * This constructor takes care of adding window listeners to correctly handling Saving procedures
     * for the current PhotoComponent displayed in the application
     */
    public PhotoBrowser(){
        super("Photo Browser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                File f = new File(Utils.storageFileName);
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    if(f.exists())
                        f.delete();

                    f.createNewFile();

                    if(currentPhoto != null) {
                        fos = new FileOutputStream(f);
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(getPhotoState());

                        oos.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                setupUI();

                File f = new File(Utils.storageFileName);
                if(f.length() != 0) {
                    FileInputStream fis = null;
                    ObjectInputStream ois = null;
                    try {
                        fis = new FileInputStream(f);
                        ois = new ObjectInputStream(fis);

                        PhotoState photoState = (PhotoState) ois.readObject();
                        File imgFile = new File(photoState.getImgUrl());

                        addNewPhotoComponent(imgFile);
                        currentPhoto.getModel().setAnnotationList(photoState.getAnnotationList());
                        repaint();

                        ois.close();
                    } catch (IOException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * This method returns the current photoState of the current PhotoComponent
     * @return the PhotoState instance created taking the imageUrl and the Annotations of the currentPhotoComponent
     */
    private PhotoState getPhotoState(){
        String imgUrl = currentPhoto.getImgUrl();
        List<Annotation> annotationList = currentPhoto.getModel().getAnnotationList();

        return new PhotoState(imgUrl, annotationList);
    }

    /**
     * This method sets up all the UI elements.
     * In particular, sets all the JFrame related size settings and then calls all the single UI element set up methods.
     */
    private void setupUI(){
        // JFrame Size Setting - 16:9
        setPreferredSize(new Dimension(Utils.PREF_WIDTH,Utils.PREF_HEIGHT));
        setMinimumSize(new Dimension(Utils.MIN_WIDTH,Utils.MIN_HEIGHT));
        setMaximumSize(new Dimension(Utils.MAX_WIDTH,Utils.MAX_HEIGHT));

        // UI setup
        setupMainPanel();
        setupStatusBar();
        setupToolBar();
        setupMenu();

        pack();
    }

    /**
     * This method builds the MenuBar with the relative Menus associated
     */
    private void setupMenu(){
        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);

        // JMenus setup
        JMenu fileMenu = setupFileMenu();
        JMenu viewMenu = setupViewMenu();
        colorMenu = setupColorMenu();
        colorMenu.setEnabled(false);
        strokeMenu = setupStrokeMenu();
        strokeMenu.setEnabled(false);
        fontMenu = setupFontMenu();
        fontMenu.setEnabled(false);

        // Adding JMenus to the JMenuBar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(colorMenu);
        menuBar.add(strokeMenu);
        menuBar.add(fontMenu);
    }

    /**
     * This method sets up the Menu "File" with all the associated MenuItems
     * @return a JMenu representing the "File" menu
     */
    private JMenu setupFileMenu(){
        JMenu fileMenu = new JMenu("File", true);
        JMenuItem importMenuItem = setupImportMenuItem();
        fileMenu.add(importMenuItem);

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                photoContainerPanel.remove(currentPhoto);
                currentPhoto = null;
                deactivateAnnotationMenu();
                repaint();
                changeStatusMessage("Photo Deleted");
            }
        });
        fileMenu.add(deleteMenuItem);

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(event -> System.exit(0));
        fileMenu.add(quitMenuItem);

        return fileMenu;
    }


    /**
     * This method sets up the MenuItem "Import" that opens a fileChoser
     * @return a JMenuItem representing the "Import" MenuItem
     */
    private JMenuItem setupImportMenuItem(){
        JMenuItem importMenuItem = new JMenuItem("Import");

        importMenuItem.addActionListener(event -> {
            JFileChooser chooser = new JFileChooser();

            // Adding filter images
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            chooser.setFileFilter(imageFilter);
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();

                // remove previous photo in this version of the application
                if(currentPhoto != null)
                    removeCurrentPhotoComponent();
                //add new photo
                addNewPhotoComponent(selectedFile);

                changeStatusMessage(Utils.fileSelected + selectedFile.getAbsolutePath());
            }
        });

        return importMenuItem;
    }

    /**
     * This method remoces the current PhotoComponent from the photoContainerPanel
     */
    private void removeCurrentPhotoComponent(){
        photoContainerPanel.remove(currentPhoto);
        currentPhoto = null;
    }

    /**
     * This method creates a new PhotoComponent and adds it to the photoContainerPanel
     */
    private void addNewPhotoComponent(File image){
        PhotoComponent photoComponent = new PhotoComponent(image);
        photoContainerPanel.add(photoComponent);
        currentPhoto = photoComponent;
        activateAnnotationMenu();
    }

    /**
     * This method activates the ColorMenu and the StrokeMenu
     */
    private void activateAnnotationMenu(){
        colorMenu.setEnabled(true);
        strokeMenu.setEnabled(true);
        fontMenu.setEnabled(true);
    }

    /**
     * This method deactivate the ColorMenu and the StrokeMenu
     */
    private void deactivateAnnotationMenu(){
        colorMenu.setEnabled(false);
        strokeMenu.setEnabled(false);
        fontMenu.setEnabled(false);
    }

    /**
     * This method sets up the Menu "View" with all the associated JRadioButtonMenuItems
     * @return a JMenu representing the "View" menu
     */
    private JMenu setupViewMenu(){
        JMenu viewMenu = new JMenu("View", true);
        ButtonGroup radioButtonGroup = new ButtonGroup();

        JRadioButtonMenuItem photoViewerMenuItem = new JRadioButtonMenuItem("Photo Viewer", true);
        photoViewerMenuItem.addActionListener(event -> changeStatusMessage(Utils.viewSelected));
        radioButtonGroup.add(photoViewerMenuItem);
        viewMenu.add(photoViewerMenuItem);

        JRadioButtonMenuItem browserMenuItem = new JRadioButtonMenuItem("Browser Viewer");
        radioButtonGroup.add(browserMenuItem);
        browserMenuItem.addActionListener(event -> changeStatusMessage(Utils.browserSelected));
        viewMenu.add(browserMenuItem);

        return viewMenu;
    }

    /**
     * This method builds the ColorMenu item with all the options related to colors
     * @return the JMenu for choosing colors
     */
    private JMenu setupColorMenu(){
        HashMap<Color,String> hm = new HashMap<Color,String>();
        hm.put(Color.BLACK,"Black");
        hm.put(Color.red, "Red");
        hm.put(Color.blue, "Blue");
        hm.put(Color.green, "Green");
        hm.put(Color.yellow, "Yellow");

        JMenu colorMenu = new JMenu("Color", true);
        ButtonGroup radioButtonGroup = new ButtonGroup();

        for (Color color: hm.keySet()) {
            JRadioButtonMenuItem colorOption;

            if(hm.get(color).equals("Black")) {
                colorOption = new JRadioButtonMenuItem(hm.get(color), true);
            }
            else {
                colorOption = new JRadioButtonMenuItem(hm.get(color));
            }

            colorOption.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    changeStatusMessage(Utils.colorSelected + hm.get(color));
                    if(currentPhoto != null)
                        currentPhoto.setAnnotationColor(color);
                }
            });
            radioButtonGroup.add(colorOption);
            colorMenu.add(colorOption);
        }
        
        return colorMenu;
    }

    /**
     * This method builds the StrokeMenu item with all the options related to stroke width
     * @return the JMenu for choosing stroke width
     */
    private JMenu setupStrokeMenu(){
        int[] stokeValues = new int[]{1,3,5,10,15};

        JMenu strokeMenu = new JMenu("Strokes", true);
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JRadioButtonMenuItem strokeMenuItem = null;
        for(int stroke: stokeValues){
            if(stroke == 1) {
                 strokeMenuItem = new JRadioButtonMenuItem(String.valueOf(stroke), true);
            }
            else {
                 strokeMenuItem = new JRadioButtonMenuItem(String.valueOf(stroke));
            }

            strokeMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    changeStatusMessage(Utils.strokeThicknessSelected + String.valueOf(stroke));
                    if(currentPhoto != null)
                        currentPhoto.setAnnotationStroke(stroke);
                }
            });
            radioButtonGroup.add(strokeMenuItem);
            strokeMenu.add(strokeMenuItem);
        }

        return strokeMenu;
    }

    /**
     * This method builds the StrokeMenu item with all the options related to stroke width
     * @return the JMenu for choosing stroke width
     */
    private JMenu setupFontMenu(){
        List<Font> fontList = new ArrayList<>();
        fontList.add(new Font("Serif", Font.PLAIN, 14));
        fontList.add(new Font("Helvetica", Font.PLAIN, 14));
        fontList.add(new Font("Courier", Font.PLAIN, 14));
        fontList.add(new Font("Dialog", Font.PLAIN, 14));


        JMenu fontMenu = new JMenu("Fonts", true);
        ButtonGroup radioButtonGroup = new ButtonGroup();

        for(Font font : fontList){
            JRadioButtonMenuItem fontMenuItem = null;

            if(Objects.equals(font.getFontName(), "Serif"))
                fontMenuItem = new JRadioButtonMenuItem(font.getFontName(), true);
            else
                fontMenuItem = new JRadioButtonMenuItem(font.getFontName());

            fontMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    changeStatusMessage(Utils.fontSelected + font.getFontName());
                    if(currentPhoto != null)
                        currentPhoto.setAnnotationFont(font);
                }
            });
            radioButtonGroup.add(fontMenuItem);
            fontMenu.add(fontMenuItem);
        }

        return fontMenu;
    }

    /**
     * This method sets up the Main Panel that for the moment is empty
     */
    private void setupMainPanel(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.GRAY);
        add(mainPanel, BorderLayout.CENTER);

        photoContainerPanel = new JPanel();
        photoContainerPanel.setBackground(Color.decode("#0d254d"));
        JScrollPane scrollPane = new JScrollPane(photoContainerPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * This method build the status bar, with a border and a default status message
     */
    private void setupStatusBar(){
        statusLabel = new JLabel(Utils.statusMessage);
        EmptyBorder emptyBorder = new EmptyBorder(10, 5, 10, 5);
        statusLabel.setBorder(emptyBorder);
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * This method changes the status message displayed in the status bar
     * @param newMessage The String to be set as a text of the status bar
     */
    private void changeStatusMessage(String newMessage) {
        statusLabel.setText(newMessage);
    }

    /**
     * This method builds the ToolBar, creating a list of ToggleButtons accordingly to the "toggleButtons" list.
     */
    private void setupToolBar(){
        JToolBar toolBar = new JToolBar(null, JToolBar.VERTICAL);
        toolBar.setBackground(Color.white);
        add(toolBar, BorderLayout.WEST);

        // Generating buttons from a list
        for ( String button : toggleButtons) {
                JToggleButton newButton = new JToggleButton(button);
                newButton.addActionListener(event -> {
                        if(newButton.isSelected())
                            changeStatusMessage(Utils.buttonSelected + button + " : " + Utils.filterApplied);
                        else
                            changeStatusMessage(Utils.buttonSelected + button + " : " + Utils.filterRemoved);
                });
                toolBar.add(newButton);
        }
    }


}
