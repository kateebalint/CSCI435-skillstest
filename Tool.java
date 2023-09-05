import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import java.io.*;
import java.util.Scanner;

public class Tool {
    private static ArrayList<Point2D> leafCoords;

    public Tool() {
        leafCoords = new ArrayList<>();
    }

    private static void findLeaves(String fileName) {
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(fileName));
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();
            NodeList nodeList = document.getElementsByTagName("node");

            if(nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Element node = (Element) nodeList.item(i);
                    if(isClickable(node)) {
                     getCoords(node);
                    }
                }
            }
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch(SAXException se) {
            se.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void drawBoxes(String screenshotFileName) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(screenshotFileName));
        } catch (IOException e) {
        }

        Graphics2D graphics = img.createGraphics();
        graphics.setColor(Color.YELLOW);
        BasicStroke stroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        graphics.setStroke(stroke);

        for(int i = 0; i < leafCoords.size() - 1; i+=2) {
            Point2D topLeft = leafCoords.get(i);
            Point2D bottomRight = leafCoords.get(i+1);
            double width = bottomRight.getX() - topLeft.getX();
            double height = bottomRight.getY() - topLeft.getY();
            Rectangle2D.Double rect = new Rectangle2D.Double(topLeft.getX(), topLeft.getY(), width, height);
            graphics.draw(rect);
        }

        try {
            File output = new File(screenshotFileName.substring(0, screenshotFileName.length() - 4) + "_new.png");
            ImageIO.write(img, "png", output);
        } catch (IOException e) {

        }
    }

    private static boolean isClickable(Element node) {
        if(node.getAttribute("clickable").equals("true")) {
            return true;
        }
        return false;
    }

    private static void getCoords(Element node) {
        String coordString = node.getAttribute("bounds");
        String tempstring1 = coordString.replace("][", ",");
        String tempstring2 = tempstring1.replace("[", "");
        String tempstring3 = tempstring2.replace("]", "");

        ArrayList<String> tempList = new ArrayList<>();
        Scanner scanner = new Scanner(tempstring3);
        scanner.useDelimiter(",");
        while (scanner.hasNext()) {
            tempList.add(scanner.next());
        }

        int x1 = Integer.parseInt(tempList.get(0));
        int y1 = Integer.parseInt(tempList.get(1));
        int x2 = Integer.parseInt(tempList.get(2));
        int y2 = Integer.parseInt(tempList.get(3));

        Point2D point1 = new Point2D.Float(x1, y1);
        Point2D point2 = new Point2D.Float(x2, y2);

        leafCoords.add(point1);
        leafCoords.add(point2);
    }

//    private static String getTextValue(Element node, String tagName) {
//        String textVal = null;
//        NodeList nodes = node.getElementsByTagName(tagName);
//        if(nodes != null && nodes.getLength() > 0) {
//            Element el = (Element) nodes.item(0);
//            textVal = el.getFirstChild().getNodeValue();
//        }
//        return textVal;
//    }

    public static void main(String[] args) {
        if (args[0] == null || args[1] == null) {
            System.out.println("Invalid arguments: need paths for both XML file and screenshot");
            return;
        }
        else {
            leafCoords = new ArrayList<>();
            findLeaves(args[0]);
            drawBoxes(args[1]);
            System.out.println("All leaf-level nodes highlighted. Program finished.");
        }
        return;
    }
}
