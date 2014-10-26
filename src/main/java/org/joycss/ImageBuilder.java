/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.joycss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

/**
 * @author eward.song
 * @version $Id: ImageBuilder.java, v 0.1 14/10/26 上午10:30 eward.song Exp $$
 */
public class ImageBuilder {

    /**
     * json config for how to build sprite
     */
    Map<String, Object> config;

    /**
     * base path of image location
     */
    String base;

    /**
     * save image to position destination
     */
    String destination;

    public static void main(String[] args) throws IOException {
        if (args[0] != null && args.length == 3) {
            long start = System.currentTimeMillis();
            Map<String, Object> config = JSON.parseObject(args[0], Map.class);
            String base = args[1];
            ImageBuilder builder = new ImageBuilder(config, base, args[2]);

            builder.buidImage();
            long coast = System.currentTimeMillis() - start;
            System.out.printf("success: %d%n", coast);
        }
        System.exit(0);
    }

    public ImageBuilder(Map<String, Object> config, String base, String destination) {
        this.config = config;
        if (!base.endsWith("/")) {
            base = base + "/";
        }
        this.base = base;
        this.destination = destination;
    }

    /**
     * read image to BufferImage
     *
     * @param imagePath image file path
     * @return BufferImage
     * @throws IOException
     */
    BufferedImage readImg(String imagePath) throws IOException {
        InputStream img = new FileInputStream(imagePath);
        BufferedImage buffer = ImageIO.read(img);

        img.close();
        return buffer;
    }

    void buidImage() throws IOException {
        Integer width = (Integer) config.get("width");
        Integer height = (Integer) config.get("height");
        BufferedImage base = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        JSONArray images = (JSONArray) config.get("images");

        for (Object image : images) {
            Map<String, Object> img = (Map<String, Object>) image;
            String fullPath = getFullPath(img.get("file_location"));
            BufferedImage buffer = readImg(fullPath);

            Integer x = (Integer) img.get("spritepos_left");
            Integer y = (Integer) img.get("spritepos_top");

            drawImage(buffer, base, x, y, img.get("repeat"));
        }

        ByteArrayOutputStream imageByteSteam = new ByteArrayOutputStream();
        ImageIO.write(base, "PNG", imageByteSteam);
        byte[] imageBytes = imageByteSteam.toByteArray();

        OutputStream out = new FileOutputStream(destination);
        out.write(imageBytes);

        imageByteSteam.close();
        out.close();
    }

    String getFullPath(Object path) {
        String name = (String) path;
        if (name.startsWith("./")) {
            name = name.substring(1);
        }

        return (base + name).replace("//", "/");
    }

    /**
     * Draws <code>image<code> on the <code>canvas</code> placing the top left corner of
     * <code>image</code> at <code>x</code> / <code>y</code> offset from the top left
     * corner of <code>canvas</code>.
     */
    public static void drawImage(BufferedImage image, BufferedImage canvas, int x, int y, Object repeat) {
        int w = image.getWidth();
        int h = image.getHeight();
        int[] imgRGB = image.getRGB(0, 0, w, h, null, 0, w);
        canvas.setRGB(x, y, w, h, imgRGB, 0, w);

        String repeatType = (String) repeat;
        // repeat-x support
        if (repeatType.equals("repeat-x")) {
            int width = canvas.getWidth();
            while(x + w <= width) {
                x = x + w;
                // 最后一次不能出界
                w = x + w > width ? width - x : w;
                if (w == 0) break;
                canvas.setRGB(x, y, w, h, imgRGB, 0, w);
            }
        }

        if (repeatType.equals("repeat-y")) {
            int heigt = canvas.getHeight();
            while(y + h <= heigt) {
                y = y + h;
                // 最后一次不能出界
                h = y + h > heigt ? heigt - y : h;
                if (h == 0) break;
                canvas.setRGB(x, y, w, h, imgRGB, 0, w);
            }
        }
    }

}
