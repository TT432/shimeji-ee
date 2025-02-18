package com.group_finity.mascot.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Original Author: Yuki Yamada of Group Finity (http://www.group-finity.com/Shimeji/)
 * Currently developed by Shimeji-ee Group.
 */

public class ImagePairLoader {
    /**
     *
     */
    public static void load(final String name, final String rightName, final Point center, final int scaling) throws IOException {
        if (ImagePairs.contains(name + (rightName == null ? "" : rightName)))
            return;

        final BufferedImage leftImage = premultiply(ImageIO.read(new FileInputStream("img/" + name)));
        final BufferedImage rightImage;
        if (rightName == null)
            rightImage = flip(leftImage);
        else
            rightImage = premultiply(ImageIO.read(new FileInputStream("img/" + rightName)));

        ImagePair ip = new ImagePair(new MascotImage(leftImage, new Point(center.x * scaling, center.y * scaling)),
                new MascotImage(rightImage, new Point((rightImage.getWidth() - center.x) * scaling, center.y * scaling)));
        ImagePairs.load(name + (rightName == null ? "" : rightName), ip);
    }

    /**
     *
     */
    private static BufferedImage flip(final BufferedImage src) {

        final BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(),
                src.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB : src.getType());

        for (int y = 0; y < src.getHeight(); ++y) {
            for (int x = 0; x < src.getWidth(); ++x) {
                copy.setRGB(copy.getWidth() - x - 1, y, src.getRGB(x, y));
            }
        }
        return copy;
    }

    private static BufferedImage premultiply(final BufferedImage source) {
        //int scaling = Integer.parseInt( Main.getInstance( ).getProperties( ).getProperty( "Scaling", "1" ) );
//        final BufferedImage returnImage = new BufferedImage( source.getWidth( ) * scaling, source.getHeight( ) * scaling,
//                                                             source.getType( ) == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB_PRE : source.getType( ) );
//        Color colour;
//        float[ ] components;
//        
//        for( int y = 0; y < returnImage.getHeight( ); ++y )
//        {
//            for( int x = 0; x < returnImage.getWidth( ); ++x )
//            {
//                colour = new Color( source.getRGB( x / scaling, y / scaling ), true );
//                components = colour.getComponents( null );
//                components[ 0 ] = components[ 3 ] * components[ 0 ];
//                components[ 1 ] = components[ 3 ] * components[ 1 ];
//                components[ 2 ] = components[ 3 ] * components[ 2 ];
//                colour = new Color( components[ 0 ], components[ 1 ], components[ 2 ], components[ 3 ] );
//                returnImage.setRGB( x, y, colour.getRGB( ) );
//            }
//        }
//        
//        return returnImage;

        final BufferedImage returnImage = new BufferedImage(source.getWidth(), source.getHeight(),
                source.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB_PRE : source.getType());
        Color colour;
        float[] components;

        for (int y = 0; y < returnImage.getHeight(); ++y) {
            for (int x = 0; x < returnImage.getWidth(); ++x) {
                colour = new Color(source.getRGB(x, y), true);
                components = colour.getComponents(null);
                components[0] = components[3] * components[0];
                components[1] = components[3] * components[1];
                components[2] = components[3] * components[2];
                colour = new Color(components[0], components[1], components[2], components[3]);
                returnImage.setRGB(x, y, colour.getRGB());
            }
        }

        return returnImage;
    }
}
