import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.List;

public class FileUpload {
    public static void main(String[] args) {
        JFrame frame = new JFrame("File Upload");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.LIGHT_GRAY);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panel.setBackground(Color.WHITE);
        frame.add(panel, BorderLayout.CENTER);

        // Create a label to display the file path
        JLabel filePathLabel = new JLabel("Selected File:");
        filePathLabel.setForeground(Color.BLUE);
        panel.add(filePathLabel);

        // Create a text field to display the selected file path
        JTextField filePathTextField = new JTextField(20);
        filePathTextField.setEditable(false);
        panel.add(filePathTextField);

        // Create a button for file selection
        JButton browseButton = new JButton("Browse");
        browseButton.setBackground(Color.ORANGE);
        panel.add(browseButton);

        // Create a drop target to support drag and drop for the entire frame
        new DropTarget(frame, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable transferable = dtde.getTransferable();
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        if (!fileList.isEmpty()) {
                            File selectedFile = fileList.get(0);
                            filePathTextField.setText(selectedFile.getAbsolutePath());

                            // Copy the file to the current directory
                            String currentDirectory = System.getProperty("user.dir");
                            File destinationFile = new File(currentDirectory, selectedFile.getName());
                            Files.copy(selectedFile.toPath(), destinationFile.toPath());
                        }
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (IOException | UnsupportedFlavorException e) {
                    e.printStackTrace();
                }
            }
        });

        // Action listener for the "Browse" button
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathTextField.setText(selectedFile.getAbsolutePath());

                // Copy the file to the current directory
                String currentDirectory = System.getProperty("user.dir");
                File destinationFile = new File(currentDirectory, selectedFile.getName());
                try {
                    Files.copy(selectedFile.toPath(), destinationFile.toPath());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }
}

