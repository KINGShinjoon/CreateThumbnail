import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ThumbnailGenerator extends JFrame {

    private JButton representativeThumbnailButton, productThumbnailButton;

    public ThumbnailGenerator() {
        setTitle("Thumbnail Generator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        representativeThumbnailButton = new JButton("대표썸네일");
        productThumbnailButton = new JButton("상품썸네일");

        representativeThumbnailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRepresentativeThumbnail();
            }
        });

        productThumbnailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateProductThumbnail();
            }
        });

        JPanel panel = new JPanel();
        panel.add(representativeThumbnailButton);
        panel.add(productThumbnailButton);

        add(panel);
    }

    private void generateRepresentativeThumbnail() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File folder = fileChooser.getSelectedFile();
            try {
                BufferedImage backgroundImage = ImageIO.read(new File(folder, "배경.png"));
                BufferedImage edgeOutlineImage = ImageIO.read(new File(folder, "edge_outline.png"));
                BufferedImage titleImage = ImageIO.read(new File(folder, "타이틀.png"));

                BufferedImage resultImage = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resultImage.createGraphics();

                g2d.drawImage(backgroundImage, 0, 0, null);
                g2d.drawImage(edgeOutlineImage, 0, 0, null);

                int x = (backgroundImage.getWidth() - titleImage.getWidth()) / 2;
                int y = (backgroundImage.getHeight() - titleImage.getHeight()) / 2;
                g2d.drawImage(titleImage, x, y, null);

                g2d.dispose();

                File outputFile = new File(folder, "대표썸네일.png");
                ImageIO.write(resultImage, "PNG", outputFile);

                JOptionPane.showMessageDialog(null, "대표썸네일이 생성되었습니다!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "이미지 파일을 읽는 데 실패했습니다.");
            }
        }
    }

    private void generateProductThumbnail() {
        // Folder selection
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int folderSelection = folderChooser.showOpenDialog(null);

        if (folderSelection == JFileChooser.APPROVE_OPTION) {
            File folder = folderChooser.getSelectedFile();

            // Product image selection
            JFileChooser imageChooser = new JFileChooser();
            imageChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
            int productImageResult = imageChooser.showOpenDialog(null);

            if (productImageResult == JFileChooser.APPROVE_OPTION) {
                File productImageFile = imageChooser.getSelectedFile();

                // Title image selection
                int titleImageResult = imageChooser.showOpenDialog(null);
                if (titleImageResult == JFileChooser.APPROVE_OPTION) {
                    File titleImageFile = imageChooser.getSelectedFile();

                    try {
                        // Load images
                        BufferedImage backgroundImage = ImageIO.read(new File(folder, "배경.png"));
                        BufferedImage edgeOutlineImage = ImageIO.read(new File(folder, "edge_outline.png"));
                        BufferedImage productImage = ImageIO.read(productImageFile);
                        BufferedImage titleImage = ImageIO.read(titleImageFile);

                        // Create result image
                        BufferedImage resultImage = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = resultImage.createGraphics();

                        // Draw images
                        g2d.drawImage(backgroundImage, 0, 0, null);
                        g2d.drawImage(edgeOutlineImage, 0, 0, null);

                        // Convert cm to pixels (approx. 37.8 pixels per cm)
                        int titleOffsetFromTop = (int) (14 * 37.8);
                        int productOffsetFromBottom = (int) (40 * 37.8);

                        // Center title image with an offset from top
                        int titleX = (backgroundImage.getWidth() - titleImage.getWidth()) / 2;
                        int titleY = titleOffsetFromTop;
                        g2d.drawImage(titleImage, titleX, titleY, null);

                        // Center product image with an offset from bottom
                        int productX = (backgroundImage.getWidth() - productImage.getWidth()) / 2;
                        int productY = backgroundImage.getHeight() - productImage.getHeight() - productOffsetFromBottom;
                        g2d.drawImage(productImage, productX, productY, null);

                        g2d.dispose();

                        // Save result image
                        File outputFile = new File(folder, "상품썸네일.png");
                        ImageIO.write(resultImage, "PNG", outputFile);

                        JOptionPane.showMessageDialog(null, "상품썸네일이 생성되었습니다!");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "이미지 파일을 읽는 데 실패했습니다.");
                    }
                }
            }
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ThumbnailGenerator generator = new ThumbnailGenerator();
            generator.setVisible(true);
        });
    }
}
