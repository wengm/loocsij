/*
 * @#ValidateCodeImage.java
 */
package org.loocsij.img;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.loocsij.img.ImageUtil;

/**
 * Generate verify image - verify if user or application is using our product
 * especilly in signing up as new user. <br>
 * Verify image are generated by image draw technology.
 * 
 * @author wengm
 * @version 1.0
 * 
 */
public class VerifyImageServlet extends HttpServlet {
    Logger log = LogManager.getLogger(VerifyImageServlet.class);

    private static final long serialVersionUID = 1L;

    /**
     * attribute name which used to store the random value and check if user
     * input the correct value.
     */
    private static String name;

    /**
     * get name
     * 
     * @return the name
     */
    public static String getName() {
        return name;
    }

    /**
     * image with
     */
    private int width;

    /**
     * image height
     */
    private int height;

    /**
     * random number string length
     */
    private int length;

    /**
     * inter line number, how many random lines in the image to disturb the
     * image readability.
     */
    private int interlinenum;

    /**
     * maximum font size
     */
    private int maxfontsize;

    /**
     * minimum font size
     */
    private int minfontsize;

    /**
     * randome string
     */
    private String randomCode;

    /**
     * process get request
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        // set necessary response infomation
        res.setContentType("image/jpeg");
        res.setHeader("Pragma", "No-cache");
        res.setHeader("Cache-Control", "No-cache");
        res.setDateHeader("Expires", 0L);

        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics graph = image.getGraphics();
        // set background color and fill the rectangle in specified color
        graph.setColor(ImageUtil.getRandomColor(200, 55));
        graph.fillRect(0, 0, width, height);
        // set interrupt line's color and draw random line in specifed color
        graph.setColor(ImageUtil.getRandomColor(100, 27));
        for (int i = 0; i < interlinenum; i++) {
            ImageUtil.drawRandomLine(graph, width, height);
        }
        // set the random code color and draw random code
        graph.setColor(ImageUtil.getRandomColor(60, 3));
        drawRandomCode(graph, width, height, length);
        // set session value
        HttpSession session = req.getSession();
        session.setAttribute(name, this.randomCode);
        // release the resource of the image
        graph.dispose();
        // output the image to the page
        try {
            ImageIO.write(image, "JPEG", res.getOutputStream());
        } catch (IOException e) {
            log.error("fail to drow image:", e);
        }
    }

    /**
     * process post request
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        doGet(req, res);
    }

    /**
     * draw random number on given graph. number, font, and position are all
     * random.
     * 
     * @param graph
     *            graphics context
     * @param width
     *            width of the range of the graphics context
     * @param height
     *            height of the range of the graphics context
     * @param length
     *            length of the random code
     */
    private void drawRandomCode(Graphics graph, int width, int height,
            int length) {
        StringBuffer sb = new StringBuffer();
        String eachCode = null;
        int fontsize = minfontsize;
        int y = 0;
        Random r = new Random(new Date().getTime());
        for (int index = 0; index < length; index++) {
            // random code
            eachCode = String.valueOf(r.nextInt(10));
            // constrct complete random code
            sb.append(eachCode);
            // random fontsize
            fontsize = minfontsize + r.nextInt(maxfontsize - minfontsize);
            // random y axes
            y = height / 2 + r.nextInt(height / 2);
            // set font and draw random code
            graph.setFont(new Font("Times New Roman", Font.PLAIN, fontsize));
            graph.drawString(eachCode,
                    (int) (index * ((double) width / (double) length)), y);
        }
        this.randomCode = sb.toString();
    }

    /**
     * initialize - read initial parameter from web descriptor(web.xml)
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        name = config.getInitParameter("name");
        width = Integer.parseInt(config.getInitParameter("width"));
        height = Integer.parseInt(config.getInitParameter("height"));
        length = Integer.parseInt(config.getInitParameter("length"));
        maxfontsize = Integer.parseInt(config.getInitParameter("maxfontsize"));
        minfontsize = Integer.parseInt(config.getInitParameter("minfontsize"));
        interlinenum = Integer
                .parseInt(config.getInitParameter("interlinenum"));
    }
}
